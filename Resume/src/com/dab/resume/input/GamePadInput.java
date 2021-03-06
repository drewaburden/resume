/********************************************************************************************************
 * Project:     Résumé
 * File:        GamePadInput.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles input from a game pad (Tested with a PS3 controller emulating an XBox360 controller)
 ********************************************************************************************************/

package com.dab.resume.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GamePadInput implements ControllerListener {
	private InputBridge inputBridge;
	private static Controller controller = null; // Controller #1, or null if no controller connected

	public GamePadInput(InputBridge inputBridge) {
		this.inputBridge = inputBridge;
		Array<Controller> controllers = Controllers.getControllers();
		if (controllers != null && controllers.size > 0) {
			controller = Controllers.getControllers().first(); // Controller #1
		}
	}

	@Override public void connected(Controller controller) { }
	@Override public void disconnected(Controller controller) { }

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (buttonCode == Xbox360Pad.BUTTON_A) {
			inputBridge.jumpPressed();
		}
		else if (buttonCode == Xbox360Pad.BUTTON_X) {
			inputBridge.attackPressed();
		}
		/*********************
		 * TEMPORARY/DEBUG
		 *********************/
		else if (buttonCode == Xbox360Pad.BUTTON_START) {
			inputBridge.pauseButtonPressed();
		}
		else if (buttonCode == Xbox360Pad.BUTTON_BACK) {
			inputBridge.debugOptionsPressed();
		}

		return true;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (buttonCode == Xbox360Pad.BUTTON_A) {
			inputBridge.jumpReleased();
		}

		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection dpad) {
		// DPad down
		if (dpad == Xbox360Pad.BUTTON_DPAD_LEFT) {
			inputBridge.leftPressed();
		}
		else if (dpad == Xbox360Pad.BUTTON_DPAD_RIGHT) {
			inputBridge.rightPressed();
		}
		else if (dpad == Xbox360Pad.BUTTON_DPAD_UP) {
			inputBridge.upPressed();
		}
		else if (dpad == Xbox360Pad.BUTTON_DPAD_DOWN) {
			inputBridge.downPressed();
		}
		// Stop player's X movement if the DPad state changed and they aren't still holding left or right
		else {
			inputBridge.movementReleased();
		}

		return true;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

	public static boolean isMovementRightPressed() {
		if (controller != null) {
			PovDirection dpad = controller.getPov(0);
			if (dpad == Xbox360Pad.BUTTON_DPAD_RIGHT) return true;
		}
		return false;
	}
	public static boolean isMovementLeftPressed() {
		if (controller != null) {
			PovDirection dpad = controller.getPov(0);
			if (dpad == Xbox360Pad.BUTTON_DPAD_LEFT) return true;
		}
		return false;
	}
}
