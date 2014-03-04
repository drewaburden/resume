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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.Music;
import com.dab.resume.events.Observer;
import com.dab.resume.hud.*;
import com.dab.resume.input.GamePadInput;
import com.dab.resume.input.InputBridge;
import com.dab.resume.input.InputEvent;
import com.dab.resume.input.KeyboardInput;
import com.dab.resume.lifeform.player.Player;
import com.dab.resume.scene.SceneEvent;
import com.dab.resume.scene.scene1.Scene1;
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

	private Fadeable fadeOverlay; // Used to fade the entire terminal screen
	private Fadeable fadeUnderlay; // Used to fade only between the menus and the scene

	private MainMenu mainMenu;
	private PauseOverlay pauseOverlay;
	private GameoverOverlay gameoverOverlay;
	private ControlsOverlay controlsOverlay;
	private CreditsOverlay creditsOverlay;
	private Scene1 scene1;
	private Scene2 scene2;
	private Music music;
	private HUD hud;
	private Player player;

	public GameScreen(BitmapFont commonFont) {
		this.commonFont = commonFont;
		camera = new OrthographicCamera(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		staticCamera = new OrthographicCamera(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		spriteBatch = new SpriteBatch();

		/**************
		 * Menus and overlays
		 **************/
		Assets.getInstance().load("colors/overlay.png", Texture.class);
		Assets.getInstance().finishLoading();
		Texture texture = Assets.getInstance().get("colors/overlay.png");
		fadeOverlay = new Fadeable(texture);
		fadeOverlay.setPosition(0.0f - TerminalGame.VIRTUAL_WIDTH/2.0f, 0.0f - TerminalGame.VIRTUAL_HEIGHT/2.0f);
		fadeOverlay.setSize(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		fadeOverlay.setAlpha(1.0f);
		fadeUnderlay = new Fadeable(texture);
		fadeUnderlay.setPosition(0.0f - TerminalGame.VIRTUAL_WIDTH/2.0f, 0.0f - TerminalGame.VIRTUAL_HEIGHT/2.0f);
		fadeUnderlay.setSize(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		fadeUnderlay.setAlpha(0.25f);
		controlsOverlay = new ControlsOverlay();
		creditsOverlay = new CreditsOverlay(fadeUnderlay);
		mainMenu = new MainMenu(commonFont, fadeUnderlay, controlsOverlay, creditsOverlay);
		pauseOverlay = new PauseOverlay(commonFont);
		gameoverOverlay = new GameoverOverlay(commonFont);

		/**************
		 * Load general textures and player
		 **************/
		player = new Player();
		hud = new HUD(player.getMaxHealth());

		/**************
		 * Load scene assets
		 **************/
		scene1 = new Scene1(camera, player, fadeUnderlay);
		scene1.registerObserver(this);

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
	}

	// Initialization
	public void initialize() {
		/**************
		 * Create sprites
		 ***************/
		// HUD
		hud.initAssets();
		// Characters
		player.initAssets();
		// Start preloading the next scene
		GameState.addGameState(PRELOADING);
		scene2 = new Scene2(camera, player, fadeUnderlay);
		scene2.registerObserver(this);
		// Show Scene1
		scene1.initAssets();
		scene1.show();

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
		inputBridge.registerObserver(mainMenu);
		inputBridge.registerObserver(controlsOverlay);
		inputBridge.registerObserver(creditsOverlay);

		/**************
		 * Overlays
		 **************/
		controlsOverlay.initAssets();
		creditsOverlay.initAssets();
		pauseOverlay.initAssets();
		gameoverOverlay.initAssets();
		mainMenu.initAssets();
		fadeOverlay.fadeToAlpha(0.0f, 0.25f); // Fade out the overlay, thereby "fading in" the game.
	}

	@Override
	public void render(float delta) {
		spriteBatch.begin();
		/************
		 * Scenes
		 ************/
		spriteBatch.setProjectionMatrix(camera.combined);
		scene1.draw(spriteBatch);
		scene2.draw(spriteBatch);

		/*************
		 * Static assets
		 *************/
		spriteBatch.setProjectionMatrix(staticCamera.combined);
		fadeUnderlay.draw(spriteBatch);
		// Menus and Overlays
		if (GameState.isGameStateSet(MAINMENU)) {
			mainMenu.draw(spriteBatch);
		}
		else if (GameState.isGameStateSet(CONTROLS)) {
			controlsOverlay.draw(spriteBatch);
		}
		else if (GameState.isGameStateSet(CREDITS)) {
			creditsOverlay.draw(spriteBatch);
		}
		else if (GameState.isGameStateSet(GameState.State.GAMEOVER)) {
			gameoverOverlay.draw(spriteBatch);
		}
		else {
			// HUD
			hud.setFilledHearts(player.getHealth());
			hud.draw(spriteBatch);
		}

		fadeOverlay.draw(spriteBatch);

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
	public boolean eventTriggered(Object data) {
		if (data instanceof InputEvent) {
			switch ((InputEvent) data) {
				// If the user pressed pause, either pause or resume and handle the pause overlay accordingly.
				case PRESS_PAUSE:
					if (GameState.isGameStateSet(PLAYING) && !GameState.isGameStateSet(PAUSED)) {
						GameState.addGameState(PAUSED);
						pauseOverlay.show();
					}
					else if (GameState.isGameStateSet(PAUSED)) {
						GameState.removeGameState(PAUSED);
						pauseOverlay.hide();
					}
					return true;
			}
		}
		else if (data instanceof SceneEvent) {
			switch ((SceneEvent) data) {
				case TRANSITION_TO_SCENE2:
					scene1.hide();
					if (GameState.isGameStateSet(PRELOADING)) {
						GameState.removeGameState(PRELOADING);
						GameState.addGameState(LOADING);
						Assets.getInstance().finishLoading();
					}
					scene2.show(true);
					GameState.removeGameState(TRANSITIONING);
					return true;
				case TRANSITION_TO_SCENE1:
					scene2.hide();
					scene1.show(true);
					GameState.removeGameState(TRANSITIONING);
					return true;
			}
		}
		return false;
	}
}
