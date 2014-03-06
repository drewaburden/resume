/********************************************************************************************************
 * Project:     Résumé
 * File:        TheEndOverlay.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing the overlay of the final "the end" screen
 ********************************************************************************************************/

package com.dab.resume.overlay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.dab.resume.GameState;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.Music;

public class TheEndOverlay {
	private BitmapFont font;
	private Sprite title;

	public TheEndOverlay(BitmapFont font) {
		this. font = font;
	}

	public void initAssets() {
		TextureAtlas atlas = Assets.getInstance().get("spritesheets/menus.pack");
		title = atlas.createSprite("title");
		title.setPosition(0.0f - title.getWidth()/2.0f, 55.0f);
	}

	public void draw(SpriteBatch spriteBatch) {
		// Title/logo
		title.draw(spriteBatch);

		if (!GameState.isGameStateSet(GameState.State.PAUSED)) {
			// Text
			font.setColor(0.15f, 0.85f, 0.4f, 1.0f);
			String text = "THE END";
			BitmapFont.TextBounds textBounds = font.getBounds(text);
			font.draw(spriteBatch, text, 0.0f - textBounds.width / 2.0f,
					0.0f + textBounds.height / 2.0f + 15.0f);

			font.setColor(0.15f, 0.85f, 0.4f, 1.0f);
			text = "THANKS FOR PLAYING!";
			textBounds = font.getBounds(text);
			font.draw(spriteBatch, text, 0.0f - textBounds.width / 2.0f,
					0.0f + textBounds.height / 2.0f + -5.0f);
		}
	}
}
