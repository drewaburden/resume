/********************************************************************************************************
 * Project:     Résumé
 * File:        LifeformSoundManager.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles changing and playing of sounds for any Lifeform
 ********************************************************************************************************/

package com.dab.resume.lifeform;

import com.dab.resume.audio.SoundFX;

public class LifeformSoundManager {

	public void playSound(LifeformSound sound) {
		sound.stream.play(SoundFX.VOLUME_MODIFIER * sound.volume);
	}
}
