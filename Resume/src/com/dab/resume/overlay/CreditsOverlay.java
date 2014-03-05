/********************************************************************************************************
 * Project:     Résumé
 * File:        CreditsOverlay.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing the credits overlay when the GameState is set to CREDITS
 ********************************************************************************************************/

package com.dab.resume.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.SoundFX;
import com.dab.resume.events.Observer;
import com.dab.resume.input.InputEvent;

public class CreditsOverlay implements Observer {
	private Fadeable underlay;
	private Sprite title, credits;
	private Sound acceptSound;

	private final float velocityY = 30.0f;
	private final float titleStopY = 55.0f;
	private boolean showing = false;

	public CreditsOverlay(Fadeable underlay) {
		this.underlay = underlay;
		Assets.getInstance().load("game/hud/title.png", Texture.class);
		Assets.getInstance().load("game/hud/credits.png", Texture.class);
		Assets.getInstance().load("game/sounds/dialog-accept.ogg", Sound.class);
	}

	public void initAssets() {
		Texture texture = Assets.getInstance().get("game/hud/credits.png");
		credits = new Sprite(texture);
		credits.setPosition(0.0f - credits.getWidth()/2.0f, 0.0f - credits.getHeight()*1.35f);

		texture = Assets.getInstance().get("game/hud/title.png");
		title = new Sprite(texture);
		title.setPosition(0.0f - title.getWidth()/2.0f, credits.getY() - title.getHeight() - 55.0f);

		acceptSound = Assets.getInstance().get("game/sounds/dialog-accept.ogg");
	}

	private void acceptPressed() {
		acceptSound.play(SoundFX.VOLUME_MODIFIER);
		hide();
	}

	public void show() {
		initAssets();
		showing = true;
	}
	public void hide() {
		showing = false;
		underlay.fadeToAlpha(0.25f, 2.5f); // Fade back to the main menu's underlay alpha
		GameState.addGameState(GameState.State.MAINMENU);
		GameState.removeGameState(GameState.State.CREDITS);
	}

	public void draw(SpriteBatch spriteBatch) {
		if (!GameState.isGameStateSet(GameState.State.PAUSED)) {
			float delta = Gdx.graphics.getDeltaTime();
			if (title.getY() < titleStopY) {
				// Credits
				credits.translateY(velocityY * delta);
				credits.draw(spriteBatch);
				// Title/logo
				title.translateY(velocityY*delta);
				// Fade screen brighter when we're close to the end of the credits
				if (title.getY() >= 0.0f && !underlay.isFading()) {
					underlay.fadeToAlpha(0.25f, 0.25f);
				}
			}
			else {
				// If the credits are done, hide the credits overlay
				hide();
			}
			// Title/logo
			title.draw(spriteBatch);
		}
	}

	@Override
	public boolean eventTriggered(Object data) {
		// If the event was an input event and we aren't paused.
		// Pausing in the credits can only happen if the window loses focus, and in that
		// case, we will want to shift input focus to the pause overlay, not the credits.
		if (data instanceof InputEvent
				&& showing
				&& !GameState.isGameStateSet(GameState.State.PAUSED)) {
			switch ((InputEvent) data) {
				case PRESS_ACCEPT:
					acceptPressed();
					return true;
			}
		}
		return false;
	}
}
