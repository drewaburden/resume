/********************************************************************************************************
 * Project:     Résumé
 * File:        PlayerAnimationFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Loads, initializes, and serves Player Animations
 ********************************************************************************************************/

package com.dab.resume.lifeform.player;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.LifeformAnimation;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType;

public class PlayerAnimationFactory {
	private final float ANIM_RATE = 0.15f;
	private LifeformAnimation animIdle, animMove, animAttack;

	public PlayerAnimationFactory() { }
	public void initAssets() {
		TextureAtlas atlas = Assets.getInstance().get("spritesheets/common.pack");
		animIdle = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("player-idle"), 3, ANIM_RATE), Direction.RIGHT);
		animMove = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("player-move"), 12, ANIM_RATE), Direction.RIGHT);
		animAttack = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("player-attack"), 5, ANIM_RATE*0.4f), Direction.RIGHT);
	}

	public LifeformAnimation getAnimation(AnimationType animation) {
		switch (animation) {
			case IDLE: return animIdle;
			case MOVE: return animMove;
			case JUMP: return animIdle;
			case BLOCK: return animIdle;
			case ATTACK_SWORD: return animAttack;
			case DEATH: return animIdle;
			default: throw new IllegalArgumentException("That animtion does not exist");
		}
	}
}
