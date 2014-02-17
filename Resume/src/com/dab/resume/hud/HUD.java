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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.assets.Assets;

public class HUD {
	private static final float SPACE_BETWEEN_HEARTS = 18.0f;
	private static final float INIT_X = -165.0f, INIT_Y = 130.0f;

	private Texture heart_empty, heart_filled;
	private int heartsMax = 5, heartsFilled = heartsMax;

	public HUD(int heartsMax) {
		Assets.getInstance().load("game/hud/heart-empty.png", Texture.class);
		Assets.getInstance().load("game/hud/heart-filled.png", Texture.class);

		this.heartsMax = heartsMax;
		heartsFilled = heartsMax;
	}

	public void initAssets() {
		heart_empty = Assets.getInstance().get("game/hud/heart-empty.png");
		heart_empty.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		heart_filled = Assets.getInstance().get("game/hud/heart-filled.png");
		heart_filled.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
	}

	public void setFilledHearts(int numFilledHearts) {
		heartsFilled = Math.min(Math.abs(numFilledHearts), heartsMax);
	}
	public void incHearts() {
		heartsFilled = Math.min(heartsFilled +1, heartsMax);
	}
	public void decHearts() {
		heartsFilled = Math.max(heartsFilled -1, heartsMax);
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
