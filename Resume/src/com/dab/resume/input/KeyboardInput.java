/********************************************************************************************************
 * Project:     Résumé
 * File:        KeyboardInput.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles input from the keyboard (and mouse if needed)
 ********************************************************************************************************/

package com.dab.resume.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import static com.badlogic.gdx.Input.Keys.*;

public class KeyboardInput implements InputProcessor {
	private InputBridge inputBridge;

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
			inputBridge.leftPressed();
		}
		else if (keycode == RIGHT) {
			inputBridge.rightPressed();
		}
		else if (keycode == UP) {
			inputBridge.upPressed();
		}
		else if (keycode == DOWN) {
			inputBridge.downPressed();
		}
		else if (keycode == ENTER) {
			inputBridge.acceptPressed();
		}
		else if (keycode == X) {
			inputBridge.attackPressed();
		}
		else if (keycode == Z) {
			inputBridge.jumpPressed();
		}
		else if (keycode == ESCAPE) {
			inputBridge.pauseButtonPressed();
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
