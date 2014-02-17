/********************************************************************************************************
 * Project:     Résumé
 * File:        Xbox360Pad.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines button and axes' constants for the Xbox360 controller in order to test
 *      for input conditions.
 ********************************************************************************************************/

package com.dab.resume.input;

import com.badlogic.gdx.controllers.PovDirection;

public class Xbox360Pad {
	/*********************************************************************************
	 * The XBox360 controller is known to have numerous possible ID's. It's best to
	 * test for the presence of "Xbox" in the ID rather than an exact match of the ID.
	 * Nevertheless, these are some possible ID's:
	 *      Controller (Gamepad for Xbox 360)
	 *      Controller (XBOX 360 For Windows)
	 *      Controller (Xbox 360 Wireless Receiver for Windows)
	 *      Controller (Xbox wireless receiver for windows)
	 *      XBOX 360 For Windows (Controller)
	 *      Xbox 360 Wireless Receiver
	 *      Xbox Receiver for Windows (Wireless Controller)
	 *      Xbox wireless receiver for windows (Controller)
	 **********************************************************************************/
	// Buttons
	public static final int BUTTON_A = 0; // PS3 Cross
	public static final int BUTTON_B = 1; // PS3 Circle
	public static final int BUTTON_X = 2; // PS3 Square
	public static final int BUTTON_Y = 3; // PS3 Triangle
	// Start, Back
	public static final int BUTTON_START = 7;
	public static final int BUTTON_BACK = 6;  // PS3 Select
	// DPad
	public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
	public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
	public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
	public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
	// Shoulder buttons
	public static final int BUTTON_LB = 4; // PS3 L1
	public static final int BUTTON_RB = 5; // PS3 R1
	public static final int AXIS_LEFT_TRIGGER = 4; // PS3 L2 (range: 0.0f to 1.0f)
	public static final int AXIS_RIGHT_TRIGGER = 4; // PS3 R2 (range: 0.0f to 1.0f)
	/* Analog sticks
		Input range visualization:
				      -1.0f
			        OOOOOOOOO
			       OOOOOOOOOOO
			-1.0f  OO STICK OO  +1.0f
			       OOOOOOOOOOO
			        OOOOOOOOO
				      +1.0f

		(Negative: up/left, Positive: down/right)
	 */
	// Left stick
	public static final int AXIS_LEFT_X = 1; // (range: -1.0f to 1.0f)
	public static final int AXIS_LEFT_Y = 0; // (range: -1.0f to 1.0f)
	// Right stick
	public static final int AXIS_RIGHT_X = 3; // (range: -1.0f to 1.0f)
	public static final int AXIS_RIGHT_Y = 2; // (range: -1.0f to 1.0f)
	public static final int BUTTON_L3 = 8;
	public static final int BUTTON_R3 = 9;
}