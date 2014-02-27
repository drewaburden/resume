/********************************************************************************************************
 * Project:     Resume
 * File:        TerminalGame.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      LibGDX entry point
 *      Controls the overall flow of the application and handles major events
 *      Sets up the game environment
 ********************************************************************************************************/

package com.dab.resume;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.assets.Assets;
import com.dab.resume.debug.DebugFlags;

import java.io.File;
import java.util.HashMap;

public class TerminalGame extends Game {
	// Developer variable. This stores modification times for assets so that they can be reloaded when they've been modified.
	// This is only used when the DebugFlags.DEV_ASSET_MONITORING bool is set to true
	private HashMap<String, Long> asset_modification_times;

	// 4:3 aspect ratio, because old-school. Shut up, you're not my mom.
	public static final float VIRTUAL_WIDTH = 720.0f;
	public static final float VIRTUAL_HEIGHT = 540.0f;

	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private BitmapFont commonFont;
	private Environment environment;
	private GameScreen gameScreen;

	private boolean windowHasFocus = true;

	@Override
	public void create() {
		Texture.setAssetManager(Assets.getInstance());

		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

		spriteBatch = new SpriteBatch();

		/**************
		 * Load commonly used font
		 **************/
		commonFont = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		commonFont.setScale(1.0f);
		Assets.getInstance().finishLoading();

		/**************
		 * Load textures
		 **************/
		Texture.setEnforcePotImages(false);
		environment = new Environment();

		/**************
		 * Load game
		 ***************/
		gameScreen = new GameScreen();
	}

	// Initialization
	private void initialize() {
		/**************
		 * Create sprites
		 ***************/
		// Environment
		environment.initAssets();

		/**************
		 * Initialize game
		 ***************/
		gameScreen.initialize();

		/**************
		 * Begin first render
		 ***************/
		this.setScreen(gameScreen);
		renderAssets();
	}

	@Override
	public void render() {
		if (GameState.getGameState() != GameState.State.LOADING) {
			/*********
			 * Asset monitoring
			 *********/
			// Monitor for changes in assets and reload them if they've changed.
			if (DebugFlags.DEV_ASSET_MONITORING) {
				if (asset_modification_times == null) {
					asset_modification_times = new HashMap<String, Long>();
					for (String filename : Assets.getInstance().getAssetNames()) {
						File file = new File(filename);
						asset_modification_times.put(filename, file.lastModified());
					}
				}
				else {
					for (String filename : Assets.getInstance().getAssetNames()) {
						File file = new File(filename);
						// If the file has been modified
						if (asset_modification_times.get(filename) < file.lastModified()) {
							System.out.println("Asset modified. Reloading asset.\t(" + filename + ")");

							Class asset_type = Assets.getInstance().getAssetType(filename);
							if (asset_type != Texture.class) {
								// We can't be nearly as nice when the asset isn't a texture.
								// We have to reinitialize everything, and it'll probably mess some things up, (like
								// sprite positions) but reloading non-texture assets is not nearly as common of an
								// occurrence. The init methods could probably all be made to play nicer with this
								// feature but it's not really worth the time.
								Assets.getInstance().unload(filename);
								Assets.getInstance().load(filename, asset_type);
								GameState.setGameState(GameState.State.LOADING);
							}
							else {
								asset_modification_times.remove(filename);
								asset_modification_times.put(filename, file.lastModified());
								// Cause all textures to be reloaded nicely and cleanly. No re-init necessary.
								Texture.invalidateAllTextures(Gdx.app);
								Assets.getInstance().finishLoading();
								// I could probably code up something that would only reload the necessary textures,
								// but it's really not worth the time since this feature is just a hack-ish dev tool.
							}
						}
					}
					if (GameState.getGameState() == GameState.State.LOADING) {
						return;
					}
				}
			}
			/**********
			 * Render
			 **********/
			renderAssets();
		}
		else {
			// If the assets aren't loaded yet, don't start rendering assets yet.
			if (Assets.getInstance().getProgress() < 1) {
				Assets.getInstance().update();

				// Display loading text
				spriteBatch.begin();
				spriteBatch.setProjectionMatrix(camera.combined);
				commonFont.setColor(0.15f, 0.85f, 0.4f, 1.0f);
				String loadingText = "LOADING";
				BitmapFont.TextBounds textBounds = commonFont.getBounds(loadingText);
				commonFont.draw(spriteBatch, loadingText, 0.0f - textBounds.width/2.0f,
						0.0f + textBounds.height/2.0f + 30.0f);
				spriteBatch.end();
			}
			// If the assets have finally loaded, initialize the assets, and eventually start rendering.
			else {
				initialize();

				GameState.setGameState(GameState.State.MAINMENU);
				if (!windowHasFocus) {
					GameState.addGameState(GameState.State.PAUSED);
					gameScreen.pause();
				}
			}
		}
	}

	protected void renderAssets() {
		// Clear the screen
		GLCommon gl = Gdx.gl20;
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/**************
		 * GameScreen
		 **************/
		gameScreen.render(Gdx.graphics.getDeltaTime());

		/**************
		 * Environment
		 **************/
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		// Environment
		if (!DebugFlags.DEV_SHOW_BOUNDINGBOXES)
		environment.drawBackground(spriteBatch);
		// Color grading
		environment.drawColorGrade(spriteBatch);

		spriteBatch.end();
	}

	@Override
	public void pause() {
		super.pause();
		windowHasFocus = false;
	}

	@Override
	public void resume() {
		super.resume();
		windowHasFocus = true;
	}
}
