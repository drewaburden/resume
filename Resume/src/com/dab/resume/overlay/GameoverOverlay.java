/********************************************************************************************************
 * Project:     Résumé
 * File:        GameoverOverlay.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing the overlay of the game over screen when the GameState is set to GAMEOVER
 ********************************************************************************************************/

package com.dab.resume.overlay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.TerminalGame;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.Music;

public class GameoverOverlay {
	private BitmapFont font;
	private Sprite overlay;
	private Music music;
	private boolean showing = false;

	public GameoverOverlay(BitmapFont font, Music music) {
		this. font = font;
		this.music = music;
		Assets.getInstance().load("colors/overlay.png", Texture.class);
	}

	public void initAssets() {
		Texture texture = Assets.getInstance().get("colors/overlay.png");
		overlay = new Sprite(texture);
		overlay.setPosition(0.0f - TerminalGame.VIRTUAL_WIDTH/2.0f, 0.0f - TerminalGame.VIRTUAL_HEIGHT/2.0f);
		overlay.setSize(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		overlay.setAlpha(0.25f);
	}


	public void show() {
		if (!showing) {
			showing = true;
			music.stopAllMusic();
			music.playGameOverMusic();
		}
	}
	public void hide() {
		if (showing) {
			showing = false;
		}
	}
	public boolean isShowing() { return showing; }

	public void draw(SpriteBatch spriteBatch) {
		if (showing) {
			// Overlay
			overlay.draw(spriteBatch);

			// Text
			font.setColor(0.15f, 0.85f, 0.4f, 1.0f);
			String gameoverText = "GAME OVER";
			BitmapFont.TextBounds textBounds = font.getBounds(gameoverText);
			font.draw(spriteBatch, gameoverText, 0.0f - textBounds.width / 2.0f,
					0.0f + textBounds.height / 2.0f + 30.0f);
		}
	}
}
