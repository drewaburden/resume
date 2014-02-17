/********************************************************************************************************
 * Project:     Resume
 * File:        GameScreen.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Creates, loads, and initializes assets and any instances of classes we need for the game
 *      to run. This class also kind of acts as the MVC controller.
 ********************************************************************************************************/

package com.dab.resume.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.TerminalGame;
import com.dab.resume.game.audio.Music;
import com.dab.resume.game.hud.HUD;
import com.dab.resume.game.input.GamePadInput;
import com.dab.resume.game.input.InputBridge;
import com.dab.resume.game.input.KeyboardInput;
import com.dab.resume.game.lifeform.player.Player;
import com.dab.resume.game.scene.Scene;

public class GameScreen implements Screen {
	private OrthographicCamera camera;
	private OrthographicCamera staticCamera;
	private SpriteBatch spriteBatch;
	private PauseOverlay pauseOverlay;

	private InputBridge inputBridge;
	private KeyboardInput keyboardInput;
	private GamePadInput gamePadInput;
	private BitmapFont commonFont;
	private Scene scene;

	private Music gameMusic;
	private HUD gameHud;
	private Player player;

	public GameScreen() {
		camera = new OrthographicCamera(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		staticCamera = new OrthographicCamera(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		spriteBatch = new SpriteBatch();

		/**************
		 * Load commonly used font
		 **************/
		commonFont = new BitmapFont(Gdx.files.internal("fonts/fixedsys.fnt"));
		commonFont.setScale(1.0f);

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

		// Pause overlay
		pauseOverlay = new PauseOverlay(commonFont);
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

		if (GameState.getGameState() == GameState.State.PAUSED) {
			pauseOverlay.draw(spriteBatch);
		}
		else {
			// HUD
			gameHud.setFilledHearts(player.getHealth());
			gameHud.draw(spriteBatch);
		}

		spriteBatch.end();
	}

	@Override public void resize(int width, int height) {}
	@Override public void show() {}
	@Override public void hide() {}
	@Override public void pause() {
		GameState.setGameState(GameState.State.PAUSED);
	}
	@Override public void resume() {}
	@Override public void dispose() {}
}
