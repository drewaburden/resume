/********************************************************************************************************
 * Project:     Résumé
 * File:        SoundFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Serves a sound based on the type of Lifeform and the type of sound requested
 ********************************************************************************************************/

package com.dab.resume.lifeform;

import com.dab.resume.lifeform.enemies.mage.MageSoundFactory;
import com.dab.resume.lifeform.player.PlayerSoundFactory;

public class SoundFactory {
	public static enum SoundType {
		MOVE, JUMP, LANDED, BLOCK, HURT, ATTACK_SWORD, ATTACK_LIGHTNING, FIREBALL, DEATH
	}

	MageSoundFactory mageSoundFactory = new MageSoundFactory();
	PlayerSoundFactory playerSoundFactory = new PlayerSoundFactory();

	public LifeformSound getSound(Lifeform.LifeformType type, SoundType sound) {
		switch (type) {
			case PLAYER: return playerSoundFactory.getSound(sound);
			case MAGE: return mageSoundFactory.getSound(sound);
			default: assert(false); return null;
		}
	}

	public void setMageSoundFactory(MageSoundFactory factory) { mageSoundFactory = factory; }
	public void setPlayerSoundFactory(PlayerSoundFactory factory) { playerSoundFactory = factory; }
}
