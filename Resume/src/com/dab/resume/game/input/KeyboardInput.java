/********************************************************************************************************
 * Project:     Résumé
 * File:        KeyboardInput.java
 * Authors:     Drew Burden
 *
 * Copyright © 2013 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles input from the keyboard
 ********************************************************************************************************/

package com.dab.resume.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import static com.badlogic.gdx.Input.Keys.*;

public class KeyboardInput implements InputProcessor {
	InputBridge inputBridge;

	public KeyboardInput(InputBridge inputBridge) {
		this.inputBridge = inputBridge;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int mouseButton) {
		return false;
	}
	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int mouseButton) {
		return false;
	}
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == LEFT) {
			inputBridge.movementLeftPressed();
		}
		else if (keycode == RIGHT) {
			inputBridge.movementRightPressed();
		}
		else if (keycode == X) {
			inputBridge.attackPressed();
		}
		else if (keycode == Z) {
			inputBridge.jumpPressed();
		}
		else if (keycode == ESCAPE) {
			Gdx.app.exit();
		}
		else if (keycode == GRAVE) {
			inputBridge.debugOptionsPressed();
		}

		return true;
	}
	@Override
	public boolean keyUp(int keycode) {
		// Only stop player movement if the player released a movement-related key
		// and they aren't pressing another movement-related key
		if ((keycode == LEFT || keycode == RIGHT) // X movement
				&& (!Gdx.input.isKeyPressed(LEFT) && !Gdx.input.isKeyPressed(LEFT))) {
			inputBridge.movementReleased();
		}
		else if (keycode == Z) { // Y movement
			inputBridge.jumpReleased();
		}

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public static boolean isMovementRightPressed() { return Gdx.input.isKeyPressed(RIGHT); }
	public static boolean isMovementLeftPressed() { return Gdx.input.isKeyPressed(LEFT); }
}
