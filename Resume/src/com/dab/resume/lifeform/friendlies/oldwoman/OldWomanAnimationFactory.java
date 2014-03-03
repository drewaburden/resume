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
	private LifeformAnimation animIdle, animDeath;

	public OldWomanAnimationFactory() {
		Assets.getInstance().load("game/chars/oldwoman-idle.png", Texture.class);
		Assets.getInstance().load("game/chars/oldwoman-death.png", Texture.class);
	}
	public void initAssets() {
		Texture texture = Assets.getInstance().get("game/chars/oldwoman-idle.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight());
		TextureRegion[] frames = new TextureRegion[4];
		int index = 0;
		for (TextureRegion[] rows : tmp) {
			for (TextureRegion cols : rows) {
				frames[index] = cols;
				index++;
			}
		}
		animIdle = new LifeformAnimation(ANIM_RATE*1.25f, frames, Direction.LEFT);

		texture = Assets.getInstance().get("game/chars/oldwoman-death.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		tmp = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight());
		frames = new TextureRegion[4];
		index = 0;
		for (TextureRegion[] rows : tmp) {
			for (TextureRegion cols : rows) {
				frames[index] = cols;
				index++;
			}
		}
		animDeath = new LifeformAnimation(ANIM_RATE*1.25f, frames, Direction.LEFT);
	}

	public LifeformAnimation getAnimation(AnimationType animation) {
		switch (animation) {
			case IDLE: return animIdle;
			case DEATH: return animDeath;
			default: return animIdle;
		}
	}
}
