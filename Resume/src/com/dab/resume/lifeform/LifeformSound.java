/********************************************************************************************************
 * Project:     Résumé
 * File:        LifeformSound.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Simply serves as a container for a Sound and associates a volume with it.
 *      This was done because LibGDX Sounds' volumes can only be altered after they've started
 *      playing, and they don't store any volume information.
 ********************************************************************************************************/

package com.dab.resume.lifeform;

public class LifeformSound {
	public com.badlogic.gdx.audio.Sound stream;
	public float volume;
	public LifeformSound(com.badlogic.gdx.audio.Sound stream, float volume) {
		this.stream = stream;
		this.volume = volume;
	}
}
