/********************************************************************************************************
 * Project:     Résumé
 * File:        Rain.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles graphics and sounds of Scene1's rain
 ********************************************************************************************************/

package com.dab.resume.scene.scene1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.assets.Assets;

public class Rain {
	private Sound rain;
	private long rainPlayingId; // After the sound starts, we can modify its volume using the id assigned to this.
	private final float rainTargetVolume = 0.75f, fadeInTime = 4.0f;
	private float deltaVolumeChange = 0.0f; // Time passed since we started fading in the volume
	private boolean paused = false;

	private Texture rainBackground, rainForeground;
	private final int numRainTileRows = 3;
	private final int numRainTileCols = 3;
	private float rainPosX = 0.0f, rainPosY = 0.0f; // Overall rain system position (this is what compensates for camera panning)
	private float rainRelativeFallingX_back = 0.0f, rainRelativeFallingY_back = 0.0f; // Relative position of the background tiles to the overall position
	private float rainRelativeFallingX_fore = 0.0f, rainRelativeFallingY_fore = 0.0f; // Relative position of the foreground tiles to the overall position
	private final float rainVelocityX_back = -125.0f, rainVelocityY_back = -125.0f;
	private final float rainVelocityX_fore = -200.0f, rainVelocityY_fore = -200.0f;

	public Rain() {
		Assets.getInstance().load("game/environments/scene1-rain-background.png", Texture.class);
		Assets.getInstance().load("game/environments/scene1-rain-foreground.png", Texture.class);
		Assets.getInstance().load("game/sounds/rain-loop.ogg", Sound.class);
	}

	public void initAssets() {
		rainBackground = Assets.getInstance().get("game/environments/scene1-rain-background.png");
		rainForeground = Assets.getInstance().get("game/environments/scene1-rain-foreground.png");

		rain = Assets.getInstance().get("game/sounds/rain-loop.ogg");
		rainPlayingId = rain.loop(0.0f);
	}

	public void translate(float amountX, float amountY) {
		rainPosX += amountX;
		rainPosY += amountY;
	}

	public void updateSound(float delta) {
		/****************************
		 * Fade in sound and handle pausing
         ****************************/
		if (!paused && GameState.getGameState() == GameState.State.PAUSED) {
			rain.pause(rainPlayingId);
			paused = true;
		}
		else if (paused && GameState.getGameState() != GameState.State.PAUSED) {
			rain.resume(rainPlayingId);
			paused = false;
		}

		// Fade in sound, if necessary
		if (!paused && deltaVolumeChange < fadeInTime) {
			// Calculate percentage of total volume to set based upon the time-passed/overall-time ratio.
			deltaVolumeChange += delta;
			rain.setVolume(rainPlayingId, rainTargetVolume * deltaVolumeChange / fadeInTime);
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		float delta = Gdx.graphics.getDeltaTime();

		if (GameState.getGameState() != GameState.State.PAUSED) {
			// Make the background rain fall
			rainRelativeFallingX_back += rainVelocityX_back *delta;
			rainRelativeFallingY_back += rainVelocityY_back *delta;
			// Make the foreground rain fall
			rainRelativeFallingX_fore += rainVelocityX_fore *delta;
			rainRelativeFallingY_fore += rainVelocityY_fore *delta;
		}

		// If the rain tiles have moved one tile position (and hit the "ground"), reset their relative position.
		// This creates the looping.
		if (rainRelativeFallingY_back <= -rainBackground.getHeight()+rainPosY) {
			rainRelativeFallingX_back = 0.0f;
			rainRelativeFallingY_back = 0.0f;
		}
		if (rainRelativeFallingY_fore <= -rainForeground.getHeight()+rainPosY) {
			rainRelativeFallingX_fore = 0.0f;
			rainRelativeFallingY_fore = 0.0f;
		}

		// Create and draw the rain tiles based upon the number of rows/columns, their relative fall position,
		// and the amount the camera has translated.
		// Background
		float currentTileY = rainBackground.getHeight()*(numRainTileRows-2) + rainPosY + rainRelativeFallingY_back;
		for (int tileNumberX = 0; tileNumberX < numRainTileCols; ++tileNumberX) {
			float currentTileX = -rainBackground.getWidth() + rainPosX + rainRelativeFallingX_back;
			for (int tileNumberY = 0; tileNumberY < numRainTileRows; ++tileNumberY) {
				Sprite rainBackgroundSprite = new Sprite(rainBackground);
				rainBackgroundSprite.setPosition(currentTileX, currentTileY);
				rainBackgroundSprite.draw(spriteBatch);
				currentTileX += rainBackground.getWidth();
			}
			currentTileY -= rainBackground.getHeight();
		}
		// Foreground
		currentTileY = rainForeground.getHeight()*(numRainTileRows-2) + rainPosY + rainRelativeFallingY_fore;
		for (int tileNumberX = 0; tileNumberX < numRainTileCols; ++tileNumberX) {
			float currentTileX = -rainForeground.getWidth() + rainPosX + rainRelativeFallingX_fore;
			for (int tileNumberY = 0; tileNumberY < numRainTileRows; ++tileNumberY) {
				Sprite rainForegroundSprite = new Sprite(rainForeground);
				rainForegroundSprite.setPosition(currentTileX, currentTileY);
				rainForegroundSprite.draw(spriteBatch);
				currentTileX += rainForeground.getWidth();
			}
			currentTileY -= rainForeground.getHeight();
		}
	}
}
