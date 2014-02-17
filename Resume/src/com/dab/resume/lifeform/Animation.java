/********************************************************************************************************
 * Project:     Résumé
 * File:        Animation.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Extension of the LibGDX Animation class that adds helper methods to flip the Animation
 *      based on its Direction
 ********************************************************************************************************/

package com.dab.resume.lifeform;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation extends com.badlogic.gdx.graphics.g2d.Animation {
	Direction facing = Direction.RIGHT;

	public Animation(float frameDuration, TextureRegion[] keyFrames, Direction facing) {
		super(frameDuration, keyFrames);

		this.facing = facing;
	}

	public Direction getDirection() { return facing; }

	public void flipHorizontal() {
		for (TextureRegion frame : this.getKeyFrames()) {
			frame.flip(true, false);
		}
		if (facing == Direction.LEFT) {
			facing = Direction.RIGHT;
		}
		else {
			facing = Direction.LEFT;
		}
	}
}
