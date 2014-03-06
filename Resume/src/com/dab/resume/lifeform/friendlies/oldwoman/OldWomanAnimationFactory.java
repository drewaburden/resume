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

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.LifeformAnimation;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType;

public class OldWomanAnimationFactory {
	private final float ANIM_RATE = 0.15f;
	private LifeformAnimation animTalking, animDeath;

	public OldWomanAnimationFactory() { }
	public void initAssets() {
		TextureAtlas atlas = Assets.getInstance().get("spritesheets/scene2.pack");
		animTalking = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("oldwoman-talking"), 2, ANIM_RATE*1.25f), Direction.LEFT);
		animDeath = new LifeformAnimation(
				Assets.getInstance().getAnimation(atlas.findRegion("oldwoman-death"), 5, ANIM_RATE*0.75f), Direction.LEFT);
	}

	public LifeformAnimation getAnimation(AnimationType animation) {
		switch (animation) {
			case TALKING: return animTalking;
			case DEATH: return animDeath;
			default: throw new IllegalArgumentException("That animation does not exist");
		}
	}
}
