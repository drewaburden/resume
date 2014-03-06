/********************************************************************************************************
 * Project:     Résumé
 * File:        MageAnimationFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Loads, initializes, and serves Mage Animations
 ********************************************************************************************************/

package com.dab.resume.lifeform.enemies.mage;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.LifeformAnimation;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType;

public class MageAnimationFactory {
	private final float ANIM_RATE = 0.15f;
	private LifeformAnimation animIdle, animMove, animAttack_lightning, animDeath;

	public MageAnimationFactory() { }
	public void initAssets() {
		TextureAtlas atlas = Assets.getInstance().get("spritesheets/scene2.pack");
		animIdle = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("mage-idle"), 4, ANIM_RATE*1.25f), Direction.LEFT);
		animMove = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("mage-move"), 4, ANIM_RATE*1.25f), Direction.LEFT);
		animAttack_lightning = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("mage-attack-lightning"), 12, ANIM_RATE), Direction.LEFT);
		animDeath = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("mage-death"), 6, ANIM_RATE), Direction.LEFT);
	}

	public LifeformAnimation getAnimation(AnimationType animation) {
		switch (animation) {
			case IDLE: return animIdle;
			case MOVE: return animMove;
			case BLOCK: return animIdle;
			case ATTACK_LIGHTNING: return animAttack_lightning;
			case DEATH: return animDeath;
			default: throw new IllegalArgumentException("That animation does not exist");
		}
	}
}
