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

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.audio.Music;

public class GameoverOverlay {
	private BitmapFont font;
	private Music music;
	private Fadeable overlay;
	private boolean hasPlayedMusic;
	private boolean showing = false;

	public GameoverOverlay(BitmapFont font, Music music, Fadeable overlay) {
		this.overlay = overlay;
		this. font = font;
		this.music = music;
	}

	public void initAssets() {
		hasPlayedMusic = false;
	}

	public void show() {
		if (!showing) {
			showing = true;
			music.stopAllMusic();
			overlay.fadeToAlpha(1.0f, 0.65f);
		}
	}
	public void hide() {
		if (showing) {
			showing = false;
		}
	}
	public boolean isShowing() { return showing; }

	public void draw(SpriteBatch spriteBatch) {
		if (showing && !GameState.isGameStateSet(GameState.State.PAUSED)) {
			if (!overlay.isFading()) {
				if (!hasPlayedMusic) {
					music.playGameOverMusic();
					hasPlayedMusic = true;
				}
				// Text
				font.setColor(0.15f, 0.85f, 0.4f, 1.0f);
				String gameoverText = "GAME OVER";
				BitmapFont.TextBounds textBounds = font.getBounds(gameoverText);
				font.draw(spriteBatch, gameoverText, 0.0f - textBounds.width / 2.0f,
						0.0f + textBounds.height / 2.0f + 30.0f);
			}
		}
	}
}
