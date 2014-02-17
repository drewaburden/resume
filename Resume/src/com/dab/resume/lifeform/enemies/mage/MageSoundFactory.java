/********************************************************************************************************
 * Project:     Résumé
 * File:        MageSoundFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Loads, initializes, and serves Mage Sounds
 ********************************************************************************************************/

package com.dab.resume.lifeform.enemies.mage;

import com.badlogic.gdx.audio.Sound;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.LifeformSound;
import com.dab.resume.lifeform.SoundFactory;

public class MageSoundFactory {
	Sound death;

	public MageSoundFactory() {
		Assets.getInstance().load("game/sounds/mage-death.ogg", Sound.class);
	}

	public void initAssets() {
		death = Assets.getInstance().get("game/sounds/mage-death.ogg");
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
