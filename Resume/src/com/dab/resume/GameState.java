/********************************************************************************************************
 * Project:     Résumé
 * File:        GameState.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *
 ********************************************************************************************************/

package com.dab.resume;

import com.dab.resume.debug.Log;

public class GameState {
	private static State currentState = State.LOADING;

	public static enum State {
		PLAYING,
		LOADING,
		MAINMENU,
		GAMEOVER,
		CREDITS,
		OPTIONS,
		PAUSED
	}


	public static void setGameState(State state) {
		Log.log("State set to " + state.toString());
		currentState = state;
	}
	public static State getGameState() { return currentState; }
}
