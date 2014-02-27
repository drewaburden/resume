/********************************************************************************************************
 * Project:     Résumé
 * File:        PauseOverlay.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing the overlay of the game screen when the GameState is set to paused
 ********************************************************************************************************/

package com.dab.resume.hud;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.TerminalGame;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.SoundFX;

public class PauseOverlay {
	private BitmapFont font;
	private Sprite overlay;
	private Sound pauseSound;
	private boolean showing = false;

	public PauseOverlay(BitmapFont font) {
		this. font = font;
		Assets.getInstance().load("colors/overlay.png", Texture.class);
		Assets.getInstance().load("game/sounds/pause.ogg", Sound.class);
	}

	public void initAssets() {
		Texture texture = Assets.getInstance().get("colors/overlay.png");
		overlay = new Sprite(texture);
		overlay.setPosition(0.0f - TerminalGame.VIRTUAL_WIDTH/2.0f, 0.0f - TerminalGame.VIRTUAL_HEIGHT/2.0f);
		overlay.setSize(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		overlay.setAlpha(0.25f);

		pauseSound = Assets.getInstance().get("game/sounds/pause.ogg");
	}

	public void show() {
		if (!showing) {
			showing = true;
			pauseSound.stop();
			pauseSound.play(SoundFX.VOLUME_MODIFIER*0.75f);
		}
	}
	public void hide() {
		if (showing) {
			showing = false;
			pauseSound.stop();
			pauseSound.play(SoundFX.VOLUME_MODIFIER*0.75f);
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		if (showing) {
			// Overlay
			overlay.draw(spriteBatch);

			// Text
			font.setColor(0.15f, 0.85f, 0.4f, 1.0f);
			String pauseText = "PAUSED";
			BitmapFont.TextBounds textBounds = font.getBounds(pauseText);
			font.draw(spriteBatch, pauseText, 0.0f - textBounds.width / 2.0f,
					0.0f + textBounds.height / 2.0f + 30.0f);
		}
	}
}
