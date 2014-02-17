/********************************************************************************************************
 * Project:     Résumé
 * File:        AnimationFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Serves an animation based on the type of Lifeform and the type of animation requested
 ********************************************************************************************************/

package com.dab.resume.lifeform;

import com.dab.resume.lifeform.enemies.mage.MageAnimationFactory;
import com.dab.resume.lifeform.player.PlayerAnimationFactory;

public class AnimationFactory {
	public static enum AnimationType {
		IDLE, MOVE, JUMP, BLOCK, HURT, ATTACK_SWORD, ATTACK_LIGHTNING, FIREBALL, DEATH
	}

	PlayerAnimationFactory playerAnimationFactory = new PlayerAnimationFactory();
	MageAnimationFactory mageAnimationFactory = new MageAnimationFactory();

	public Animation getAnimation(Lifeform.LifeformType type, AnimationType animation) {
		switch (type) {
			case PLAYER: return playerAnimationFactory.getAnimation(animation);
			case MAGE: return mageAnimationFactory.getAnimation(animation);
			default: assert(false); return null;
		}
	}

	public void setPlayerAnimationFactory(PlayerAnimationFactory factory) { playerAnimationFactory = factory; }
	public void setMageAnimationFactory(MageAnimationFactory factory) { mageAnimationFactory = factory; }
}
