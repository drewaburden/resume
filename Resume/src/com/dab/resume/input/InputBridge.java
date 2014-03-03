/********************************************************************************************************
 * Project:     Résumé
 * File:        InputBridge.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Receives interpreted input from any of the valid sources and then handles them accordingly
 ********************************************************************************************************/

package com.dab.resume.input;

import com.dab.resume.GameState;
import com.dab.resume.debug.DebugFlags;
import com.dab.resume.debug.Log;
import com.dab.resume.events.Observable;
import com.dab.resume.hud.Dialog;

import static com.dab.resume.GameState.State.*;

public class InputBridge extends Observable {
	public void debugOptionsPressed() {
		Log.log();
		DebugFlags.DEV_DEBUG_VIEW = !DebugFlags.DEV_DEBUG_VIEW;
		notifyObservers(InputEvent.PRESS_DEBUG_OPTIONS);
	}
	public void pauseButtonPressed() {
		Log.log();
		notifyObservers(InputEvent.PRESS_PAUSE);
		// If we're in-game and the game isn't paused after notifying the observers,
		// the player must've just unpaused, so we need to recheck/resend movement input.
		if (GameState.isGameStateSet(PLAYING) && !GameState.isGameStateSet(PAUSED)) {
			if (isMovementRightPressed()) rightPressed();
			else if (isMovementLeftPressed()) leftPressed();
			else movementReleased();
		}
	}
	public void rightPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0
				&& GameState.isGameStateSet(PLAYING)
				&& !GameState.areAnyGameStatesSet(PAUSED, TRANSITIONING)) {
			Log.log();
			notifyObservers(InputEvent.PRESS_RIGHT);
		}
	}
	public void leftPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0
				&& GameState.isGameStateSet(PLAYING)
				&& !GameState.areAnyGameStatesSet(PAUSED, TRANSITIONING)) {
			Log.log();
			notifyObservers(InputEvent.PRESS_LEFT);
		}
	}
	public void upPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0
				&& GameState.isGameStateSet(MAINMENU)) {
			Log.log();
			notifyObservers(InputEvent.PRESS_UP);
		}
	}
	public void downPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0
				&& GameState.isGameStateSet(MAINMENU)) {
			Log.log();
			notifyObservers(InputEvent.PRESS_DOWN);
		}
	}
	public void movementReleased() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0
				&& GameState.isGameStateSet(PLAYING)
				&& !GameState.areAnyGameStatesSet(PAUSED, TRANSITIONING)) {
			Log.log();
			notifyObservers(InputEvent.RELEASE_MOVE);
		}
	}
	public void jumpPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0
				&& GameState.isGameStateSet(PLAYING)
				&& !GameState.areAnyGameStatesSet(PAUSED, TRANSITIONING)) {
			Log.log();
			notifyObservers(InputEvent.PRESS_JUMP);
		}
		// If any of the above conditions fail, the player must be looking at a menu
		else {
			acceptPressed();
		}
	}
	public void jumpReleased() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0
				&& GameState.isGameStateSet(PLAYING)
				&& !GameState.areAnyGameStatesSet(PAUSED, TRANSITIONING)) {
			Log.log();
			notifyObservers(InputEvent.RELEASE_JUMP);
		}
	}
	public void attackPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0
				&& GameState.isGameStateSet(PLAYING)
				&& !GameState.areAnyGameStatesSet(PAUSED, TRANSITIONING)) {
			Log.log();
			notifyObservers(InputEvent.PRESS_ATTACK);
		}
	}

	public void acceptPressed() {
		Log.log();
		// Accept button pressed
		notifyObservers(InputEvent.PRESS_ACCEPT);
	}

	public static boolean isMovementRightPressed() { return KeyboardInput.isMovementRightPressed() || GamePadInput.isMovementRightPressed(); }
	public static boolean isMovementLeftPressed() { return KeyboardInput.isMovementLeftPressed() || GamePadInput.isMovementLeftPressed(); }
}
