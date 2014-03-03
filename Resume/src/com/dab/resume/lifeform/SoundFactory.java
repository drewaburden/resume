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
import com.dab.resume.lifeform.friendlies.oldwoman.OldWomanSoundFactory;
import com.dab.resume.lifeform.player.PlayerSoundFactory;

public class SoundFactory {
	public static enum SoundType {
		MOVE, JUMP, LANDED, BLOCK, HURT, ATTACK_SWORD, ATTACK_LIGHTNING, FIREBALL, DEATH
	}

	MageSoundFactory mageSoundFactory = new MageSoundFactory();
	PlayerSoundFactory playerSoundFactory = new PlayerSoundFactory();
	OldWomanSoundFactory oldWomanSoundFactory = new OldWomanSoundFactory();
	public void setMageSoundFactory(MageSoundFactory factory) { mageSoundFactory = factory; }
	public void setPlayerSoundFactory(PlayerSoundFactory factory) { playerSoundFactory = factory; }
	public void setOldWomanSoundFactory(OldWomanSoundFactory factory) { oldWomanSoundFactory = factory; }

	public LifeformSound getSound(Lifeform.LifeformType type, SoundType sound) {
		switch (type) {
			case PLAYER: return playerSoundFactory.getSound(sound);
			case MAGE: return mageSoundFactory.getSound(sound);
			case OLDWOMAN: return oldWomanSoundFactory.getSound(sound);
			default: throw new IllegalArgumentException("That lifeform type does not exist");
		}
	}
}
