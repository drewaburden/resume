/********************************************************************************************************
 * Project:     Résumé
 * File:        Direction.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines the possible directions that an entity can be facing
 ********************************************************************************************************/

package com.dab.resume.game.lifeform;

public enum Direction {
	LEFT (false),
	RIGHT (true);

	boolean isDirectionRight;
	Direction(boolean isDirectionRight) {
		this.isDirectionRight = isDirectionRight;
	}

	public Direction invert() {
		if (isDirectionRight) return LEFT;
		else return RIGHT;
	}
}
