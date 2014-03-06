/********************************************************************************************************
 * Project:     Résumé
 * File:        HUD.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles loading, initialization, and drawing of Sprites related to the heads up display
 ********************************************************************************************************/

package com.dab.resume.hud;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.SoundFX;

public class HUD {
	private static final float SPACE_BETWEEN_HEARTS = 18.0f;
	private static final float INIT_X = -165.0f, INIT_Y = 130.0f;

	private TextureRegion heart_empty, heart_filled;
	private Sound heartsLowWarningSound;
	private boolean warningSoundPlayed = false;
	private int heartsMax = 5, heartsFilled = heartsMax;

	public HUD(int heartsMax) {
		Assets.getInstance().load("sounds/heart-warning.ogg", Sound.class);

		this.heartsMax = heartsMax;
		heartsFilled = heartsMax;
	}

	public void initAssets() {
		TextureAtlas atlas = Assets.getInstance().get("spritesheets/common.pack");
		heart_empty = atlas.findRegion("heart-empty");
		heart_filled = atlas.findRegion("heart-filled");

		heartsLowWarningSound = Assets.getInstance().get("sounds/heart-warning.ogg");
	}

	public void setFilledHearts(int numFilledHearts) {
		heartsFilled = Math.min(Math.abs(numFilledHearts), heartsMax);

		if (heartsFilled == 1 && !warningSoundPlayed) {
			heartsLowWarningSound.play(SoundFX.VOLUME_MODIFIER);
			warningSoundPlayed = true;
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		float posX = INIT_X, posY = INIT_Y;

		// Render filled hearts
		for (int heartNum = 0; heartNum < heartsFilled; ++heartNum) {
			Sprite filledHeart = new Sprite(heart_filled);
			filledHeart.setPosition(posX, posY);
			filledHeart.draw(spriteBatch);

			posX += SPACE_BETWEEN_HEARTS;
		}

		// Render empty hearts
		for (int heartNum = heartsFilled; heartNum < heartsMax; ++heartNum) {
			Sprite emptyHeart = new Sprite(heart_empty);
			emptyHeart.setPosition(posX, posY);
			emptyHeart.draw(spriteBatch);

			posX += SPACE_BETWEEN_HEARTS;
		}
	}
}
