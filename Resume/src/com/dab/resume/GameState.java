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
	private static State currentState = State.LOADING;

	// Using ints represented as hex allows us to set multiple states at one time.
	// (as long as the values are powers of 2)
	public static enum State {
		PLAYING     (0x00000001),
		LOADING     (0x00000002),
		MAINMENU    (0x00000004),
		GAMEOVER    (0x00000008),
		CREDITS     (0x00000010),
		OPTIONS     (0x00000020),
		PAUSED      (0x00000040),
		PRELOADING  (0x00000080);

		private int stateCode;
		private State(int stateCode) { this.stateCode = stateCode; }

		public int getStateCode() { return stateCode; }

		public void setStateCode(State state) { this.stateCode = state.getStateCode(); }
		public void addStateCode(State state) { this.stateCode |= state.getStateCode(); } // OR
		public void removeStateCode(State state) { this.stateCode ^= state.getStateCode(); } // Exclusive OR
	}

	public static State getGameState() { return currentState; }
	public static boolean isGameStateSet(State state) {
		return (state.getStateCode() & currentState.getStateCode()) > 0;
	}

	public static void setGameState(State state) {
		Log.log("State set to " + state.toString());
		currentState = state;
	}
	public static void addGameState(State state) {
		Log.log("Added State " + state.toString());
		currentState.addStateCode(state);
	}
	public static void removeGameState(State state) {
		Log.log("Removed State " + state.toString());
		currentState.removeStateCode(state);
	}
}
