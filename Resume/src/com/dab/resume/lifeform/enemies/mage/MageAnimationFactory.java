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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.LifeformAnimation;
import com.dab.resume.lifeform.Direction;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType;

public class MageAnimationFactory {
	private final float ANIM_RATE = 0.15f;
	private LifeformAnimation animIdle, animMove, animAttack_lightning, animDeath;

	public MageAnimationFactory() {
		Assets.getInstance().load("game/chars/mage-idle.png", Texture.class);
		Assets.getInstance().load("game/chars/mage-move.png", Texture.class);
		Assets.getInstance().load("game/chars/mage-attack-lightning.png", Texture.class);
		Assets.getInstance().load("game/chars/mage-death.png", Texture.class);
	}
	public void initAssets() {
		animIdle = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/mage-idle.png", 4, ANIM_RATE*1.25f), Direction.LEFT);
		animMove = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/mage-move.png", 4, ANIM_RATE*1.25f), Direction.LEFT);
		animAttack_lightning = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/mage-attack-lightning.png", 12, ANIM_RATE), Direction.LEFT);
		animDeath = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/mage-death.png", 5, ANIM_RATE), Direction.LEFT);
	}

	public LifeformAnimation getAnimation(AnimationType animation) {
		switch (animation) {
			case IDLE: return animIdle;
			case MOVE: return animMove;
			case BLOCK: return animIdle;
			case ATTACK_LIGHTNING: return animAttack_lightning;
			case DEATH: return animDeath;
			default: return animIdle;
		}
	}
}
