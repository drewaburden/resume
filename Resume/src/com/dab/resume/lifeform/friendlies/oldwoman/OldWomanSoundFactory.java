/********************************************************************************************************
 * Project:     Résumé
 * File:        OldWomanFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Loads, initializes, and serves OldWoman Sounds
 ********************************************************************************************************/

package com.dab.resume.lifeform.friendlies.oldwoman;

import com.badlogic.gdx.audio.Sound;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.LifeformSound;
import com.dab.resume.lifeform.SoundFactory;

public class OldWomanSoundFactory {
	Sound death;

	public OldWomanSoundFactory() {
		Assets.getInstance().load("game/sounds/oldwoman-death.ogg", Sound.class);
	}

	public void initAssets() {
		death = Assets.getInstance().get("game/sounds/oldwoman-death.ogg");
	}

	public LifeformSound getSound(SoundFactory.SoundType sound) {
		switch (sound) {
			case DEATH: return getDeathSound();
			default: throw new IllegalArgumentException("That sound does not exist");
		}
	}

	public LifeformSound getDeathSound() {
		return new LifeformSound(death, 0.4f);
	}
}
