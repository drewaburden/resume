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

package com.dab.resume;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.audio.Music;
import com.dab.resume.events.Observer;
import com.dab.resume.hud.GameoverOverlay;
import com.dab.resume.hud.HUD;
import com.dab.resume.hud.PauseOverlay;
import com.dab.resume.input.GamePadInput;
import com.dab.resume.input.InputBridge;
import com.dab.resume.input.InputEvent;
import com.dab.resume.input.KeyboardInput;
import com.dab.resume.lifeform.player.Player;
import com.dab.resume.scene.scene1.Scene;
import com.dab.resume.scene.scene2.Scene2;

import static com.dab.resume.GameState.State.*;

public class GameScreen implements Screen, Observer {
	private OrthographicCamera camera;
	private OrthographicCamera staticCamera;
	private SpriteBatch spriteBatch;
	private InputBridge inputBridge;
	private KeyboardInput keyboardInput;
	private GamePadInput gamePadInput;
	private BitmapFont commonFont;

	private MainMenu mainMenu;
	private PauseOverlay pauseOverlay;
	private GameoverOverlay gameoverOverlay;
	private Scene scene;
	private Scene2 scene2;
	private Music music;
	private HUD hud;
	private Player player;

	public GameScreen() {
		camera = new OrthographicCamera(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		staticCamera = new OrthographicCamera(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		spriteBatch = new SpriteBatch();

		/**************
		 * Load commonly used font
		 **************/
		commonFont = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		commonFont.setScale(1.0f);

		/**************
		 * Load general textures and player
		 **************/
		player = new Player();
		hud = new HUD(player.getMaxHealth());

		/**************
		 * Load scene assets
		 **************/
		scene = new Scene(camera, player);
		scene2 = new Scene2(camera, player);

		/**************
		 * Load general audio
		 **************/
		music = new Music();

		/**************
		 * Initialize input
		 **************/
		inputBridge = new InputBridge();
		keyboardInput = new KeyboardInput(inputBridge); // Mouse, keyboard, touch input
		gamePadInput = new GamePadInput(inputBridge); // Controller input

		/**************
		 * Menus and overlays
		 **************/
		mainMenu = new MainMenu(commonFont);
		pauseOverlay = new PauseOverlay(commonFont);
		gameoverOverlay = new GameoverOverlay(commonFont);
	}

	// Initialization
	public void initialize() {
		/**************
		 * Create sprites
		 ***************/
		// Scene
		scene.initAssets();
		scene2.initAssets();
		// HUD
		hud.initAssets();
		// Characters
		player.initAssets();

		/**************
		 * Create audio
		 ***************/
		music.initAssets();
		//music.playBattleMusic();

		/**************
		 * Start receiving input
		 **************/
		Gdx.input.setInputProcessor(keyboardInput);
		Controllers.addListener(gamePadInput);
		inputBridge.registerObserver(this);
		inputBridge.registerObserver(player);
		inputBridge.registerObserver(scene);
		inputBridge.registerObserver(mainMenu);

		/**************
		 * Overlays
		 **************/
		mainMenu.initAssets();
		pauseOverlay.initAssets();
		gameoverOverlay.initAssets();
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
		//scene2.draw(spriteBatch);

		/*************
		 * Static assets
		 *************/
		spriteBatch.setProjectionMatrix(staticCamera.combined);
		// Menus and Overlays
		if (GameState.isGameStateSet(MAINMENU)) {
			mainMenu.draw(spriteBatch);
		}
		else if (GameState.getGameState() == GameState.State.GAMEOVER) {
			gameoverOverlay.draw(spriteBatch);
		}
		else {
			// HUD
			hud.setFilledHearts(player.getHealth());
			hud.draw(spriteBatch);
		}
		pauseOverlay.draw(spriteBatch);

		spriteBatch.end();
	}

	@Override public void resize(int width, int height) {}
	@Override public void show() {}
	@Override public void hide() {}
	@Override public void pause() {
		// Pause if the window loses focus. But only if we aren't loading assets. Loading trumps everything.
		if (!GameState.isGameStateSet(LOADING)) {
			GameState.addGameState(PAUSED);
			pauseOverlay.show();
		}
	}
	@Override public void resume() {}
	@Override public void dispose() {}

	@Override
	public void eventTriggered(Object data) {
		if (data instanceof InputEvent) {
			switch ((InputEvent) data) {
				// If the user pressed pause, either pause or resume and handle the pause overlay accordingly.
				case PRESS_PAUSE:
					if (GameState.isGameStateSet(PLAYING)) {
						GameState.addGameState(PAUSED);
						pauseOverlay.show();
					}
					else if (GameState.isGameStateSet(PAUSED)) {
						GameState.removeGameState(PAUSED);
						pauseOverlay.hide();
					}
					break;
			}
		}
	}
}
