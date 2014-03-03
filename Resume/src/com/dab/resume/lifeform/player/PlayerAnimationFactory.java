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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.LifeformAnimation;
import com.dab.resume.lifeform.Direction;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType;

public class PlayerAnimationFactory {
	private final float ANIM_RATE = 0.15f;
	private LifeformAnimation animIdle, animMove, animAttack;

	public PlayerAnimationFactory() {
		Assets.getInstance().load("game/chars/player-idle.png", Texture.class);
		Assets.getInstance().load("game/chars/player-move.png", Texture.class);
		Assets.getInstance().load("game/chars/player-attack.png", Texture.class);
	}
	public void initAssets() {
		animIdle = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/player-idle.png", 3, ANIM_RATE), Direction.RIGHT);
		animMove = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/player-move.png", 12, ANIM_RATE), Direction.RIGHT);
		animAttack = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/player-attack.png", 5, ANIM_RATE*0.4f), Direction.RIGHT);
	}

	public LifeformAnimation getAnimation(AnimationType animation) {
		switch (animation) {
			case IDLE: return animIdle;
			case MOVE: return animMove;
			case JUMP: return animIdle;
			case BLOCK: return animIdle;
			case ATTACK_SWORD: return animAttack;
			case DEATH: return animIdle;
			default: return animIdle;
		}
	}
}
