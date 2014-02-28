/********************************************************************************************************
 * Project:     Résumé
 * File:        GameState.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines and handles overall states of the game that may or may not affect the flow of the game.
 *      This class also provides helper methods that allow us to set multiple states at one time.
 *      It's up to the user of the class to decide what states should and should not be set at the same
 *      time, and it is that user's responsibility to enforce that.
 ********************************************************************************************************/

package com.dab.resume;

import com.dab.resume.debug.Log;

public class GameState {
	private static int currentState = State.LOADING.getStateCode();

	// Using ints represented as hex allows us to set multiple states at one time by making
	// use of bitwise operators (as long as the values are powers of 2).
	public static enum State {
		PLAYING     (0x00000001),
		LOADING     (0x00000002),
		MAINMENU    (0x00000004),
		GAMEOVER    (0x00000008),
		CREDITS     (0x00000010),
		CONTROLS     (0x00000020),
		PAUSED      (0x00000040),
		PRELOADING  (0x00000080);

		private final int stateCode;
		private State(final int stateCode) { this.stateCode = stateCode; }

		public int getStateCode() { return stateCode; }
	}

	public static boolean isGameStateSet(State state) {
		return (state.getStateCode() & currentState) > 0;
	}
	public static void setGameState(State state) {
		Log.log("State set to " + state.toString());
		currentState = state.getStateCode();
	}
	public static void addGameState(State state) {
		Log.log("Added State " + state.toString());
		currentState |= state.getStateCode();
	}
	public static void removeGameState(State state) {
		Log.log("Removed State " + state.toString());
		currentState ^= state.getStateCode();
	}
}
