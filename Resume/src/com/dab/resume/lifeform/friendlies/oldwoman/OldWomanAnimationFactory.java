/********************************************************************************************************
 * Project:     Résumé
 * File:        OldWomanAnimationFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Loads, initializes, and serves OldWoman Animations
 ********************************************************************************************************/

package com.dab.resume.lifeform.friendlies.oldwoman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.LifeformAnimation;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType;

public class OldWomanAnimationFactory {
	private final float ANIM_RATE = 0.15f;
	private LifeformAnimation animTalking, animDeath;

	public OldWomanAnimationFactory() {
		Assets.getInstance().load("game/chars/oldwoman-talking.png", Texture.class);
		Assets.getInstance().load("game/chars/oldwoman-death.png", Texture.class);
	}
	public void initAssets() {
		animTalking = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/oldwoman-talking.png", 2, ANIM_RATE*1.25f), Direction.LEFT);
		animDeath = new LifeformAnimation(
				Assets.getInstance().getAnimation("game/chars/oldwoman-death.png", 5, ANIM_RATE*1.5f), Direction.LEFT);
	}

	public LifeformAnimation getAnimation(AnimationType animation) {
		switch (animation) {
			case TALKING: return animTalking;
			case DEATH: return animDeath;
			default: throw new IllegalArgumentException("That animation does not exist");
		}
	}
}
