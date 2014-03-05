/********************************************************************************************************
 * Project:     Résumé
 * File:        ControlsOverlay.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing the controls overlay when the GameState is set to CONTROLS
 ********************************************************************************************************/

package com.dab.resume.overlay;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.TerminalGame;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.SoundFX;
import com.dab.resume.events.Observer;
import com.dab.resume.input.InputEvent;

public class ControlsOverlay implements Observer {
	private Sprite controls;
	private Sound acceptSound;

	private boolean showing = false;

	public ControlsOverlay() {
		Assets.getInstance().load("game/hud/title.png", Texture.class);
		Assets.getInstance().load("game/hud/controls.png", Texture.class);
		Assets.getInstance().load("game/sounds/dialog-accept.ogg", Sound.class);
	}

	public void initAssets() {
		Texture texture = Assets.getInstance().get("game/hud/controls.png");
		controls = new Sprite(texture);
		controls.setPosition(0.0f - controls.getWidth()/2.0f, 0.0f - controls.getHeight()/2.0f + 30.0f);

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
		GameState.addGameState(GameState.State.MAINMENU);
		GameState.removeGameState(GameState.State.CONTROLS);
	}

	public void draw(SpriteBatch spriteBatch) {
		if (!GameState.isGameStateSet(GameState.State.PAUSED)) {
			// Controls
			controls.draw(spriteBatch);
		}
	}

	@Override
	public boolean eventTriggered(Object data) {
		// If the event was an input event and we aren't paused.
		// Pausing in the controls can only happen if the window loses focus, and in that
		// case, we will want to shift input focus to the pause overlay, not the controls.
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
