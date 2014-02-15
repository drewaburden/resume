/********************************************************************************************************
 * Project:     Resume
 * File:        GameScreen.java
 * Authors:     Drew Burden
 *
 * Copyright © 2013 Drew Burden
 * All rights reserved.
 *
 * Description:
 *
 ********************************************************************************************************/

package com.dab.resume.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.TerminalGame;
import com.dab.resume.screens.game.hud.HUD;
import com.dab.resume.screens.game.input.GamePadInput;
import com.dab.resume.screens.game.input.InputBridge;
import com.dab.resume.screens.game.input.KeyboardInput;
import com.dab.resume.screens.game.scene.Scene;
import com.dab.resume.screens.game.lifeform.player.Player;
import com.dab.resume.screens.game.audio.Music;

public class GameScreen implements Screen {
	private OrthographicCamera camera;
	private OrthographicCamera staticCamera;
	private SpriteBatch spriteBatch;

	private InputBridge inputBridge;
	private KeyboardInput keyboardInput;
	private GamePadInput gamePadInput;
	private Scene scene;

	private Music gameMusic;
	private HUD gameHud;
	private Player player;

	public GameScreen() {
		camera = new OrthographicCamera(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		staticCamera = new OrthographicCamera(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		spriteBatch = new SpriteBatch();

		/**************
		 * Load general textures and player
		 **************/
		player = new Player();
		gameHud = new HUD(player.getMaxHealth());

		/**************
		 * Load scene assets
		 **************/
		scene = new Scene(camera, player);//, gameBackground);

		/**************
		 * Load general audio
		 **************/
		gameMusic = new Music();

		/**************
		 * Initialize input
		 **************/
		inputBridge = new InputBridge();
		keyboardInput = new KeyboardInput(inputBridge); // Mouse, keyboard, touch input
		gamePadInput = new GamePadInput(inputBridge); // Controller input
	}

	// Initialization
	public void initialize() {
		/**************
		 * Create sprites
		 ***************/
		// Scene
		scene.initAssets();
		// HUD
		gameHud.initAssets();
		// Characters
		player.initAssets();

		/**************
		 * Create audio
		 ***************/
		gameMusic.initAssets();
		//gameMusic.playBattleMusic();

		/**************
		 * Start receiving input
		 **************/
		Gdx.input.setInputProcessor(keyboardInput);
		Controllers.addListener(gamePadInput);
		inputBridge.registerObserver(player);
		inputBridge.registerObserver(scene);
	}

	@Override
	public void render(float delta) {
		spriteBatch.begin();
		/************
		 * Moveable assets
		 ************/
		spriteBatch.setProjectionMatrix(camera.combined);
		// Scene
		scene.draw(spriteBatch);

		/*************
		 * Static assets
		 *************/
		spriteBatch.setProjectionMatrix(staticCamera.combined);
		// HUD
		gameHud.setFilledHearts(player.getHealth());
		gameHud.draw(spriteBatch);

		spriteBatch.end();
	}

	@Override public void resize(int width, int height) {}

	@Override public void show() {}

	@Override public void hide() {}

	@Override public void pause() {}

	@Override public void resume() {}

	@Override public void dispose() {}
}
