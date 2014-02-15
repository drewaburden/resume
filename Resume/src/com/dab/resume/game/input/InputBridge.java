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

package com.dab.resume.game.input;

import com.dab.resume.debug.DebugFlags;
import com.dab.resume.debug.Log;
import com.dab.resume.events.Observable;
import com.dab.resume.game.hud.Dialog;

public class InputBridge extends Observable {
	public void debugOptionsPressed() {
		Log.log();
		DebugFlags.DEV_SHOW_BOUNDINGBOXES = !DebugFlags.DEV_SHOW_BOUNDINGBOXES;
		notifyObservers(InputEvent.PRESS_DEBUG_OPTIONS);
	}
	public void movementRightPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0) {
			notifyObservers(InputEvent.PRESS_MOVE_RIGHT);
		}
	}
	public void movementLeftPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0) {
			notifyObservers(InputEvent.PRESS_MOVE_LEFT);
		}
	}
	public void movementReleased() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0) {
			notifyObservers(InputEvent.RELEASE_MOVE);
		}
	}
	public void jumpPressed() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0) {
			notifyObservers(InputEvent.PRESS_JUMP);
		}
	}
	public void jumpReleased() {
		if (Dialog.NUM_DIALOGS_SHOWING == 0) {
			notifyObservers(InputEvent.RELEASE_JUMP);
		}
	}
	public void attackPressed() {
		notifyObservers(InputEvent.PRESS_ATTACK);
	}

	public static boolean isMovementRightPressed() { return KeyboardInput.isMovementRightPressed() || GamePadInput.isMovementRightPressed(); }
	public static boolean isMovementLeftPressed() { return KeyboardInput.isMovementLeftPressed() || GamePadInput.isMovementLeftPressed(); }
}
