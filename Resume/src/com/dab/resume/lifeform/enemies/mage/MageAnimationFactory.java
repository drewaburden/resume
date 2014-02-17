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
import com.dab.resume.lifeform.Animation;
import com.dab.resume.lifeform.Direction;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType;

public class MageAnimationFactory {
	private final float ANIM_RATE = 0.15f;
	private Animation animIdle, animMove, animAttack;

	public MageAnimationFactory() {
		Assets.getInstance().load("game/chars/mage-idle.png", Texture.class);
		Assets.getInstance().load("game/chars/mage-move.png", Texture.class);
		//Assets.getInstance().load("game/chars/mage-attack-lightning.png", Texture.class);
	}
	public void initAssets() {
		Texture texture = Assets.getInstance().get("game/chars/mage-idle.png");
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
		animIdle = new Animation(ANIM_RATE*1.25f, frames, Direction.LEFT);

		texture = Assets.getInstance().get("game/chars/mage-move.png");
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
		animMove = new Animation(ANIM_RATE*1.25f, frames, Direction.LEFT);
	}

	public Animation getAnimation(AnimationType animation) {
		switch (animation) {
			case IDLE: return animIdle;
			case MOVE: return animMove;
			case BLOCK: return animIdle;
			case ATTACK: return animIdle;
			case DEATH: return animIdle;
			default: return animIdle;
		}
	}
}
