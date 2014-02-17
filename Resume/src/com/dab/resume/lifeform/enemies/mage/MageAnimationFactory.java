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
		animIdle = new LifeformAnimation(ANIM_RATE*1.25f, frames, Direction.LEFT);

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
		animMove = new LifeformAnimation(ANIM_RATE*1.25f, frames, Direction.LEFT);

		texture = Assets.getInstance().get("game/chars/mage-attack-lightning.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		tmp = TextureRegion.split(texture, texture.getWidth() / 12, texture.getHeight());
		frames = new TextureRegion[12];
		index = 0;
		for (TextureRegion[] rows : tmp) {
			for (TextureRegion cols : rows) {
				frames[index] = cols;
				index++;
			}
		}
		animAttack_lightning = new LifeformAnimation(ANIM_RATE*1.0f, frames, Direction.LEFT);

		texture = Assets.getInstance().get("game/chars/mage-death.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		tmp = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight());
		frames = new TextureRegion[5];
		index = 0;
		for (TextureRegion[] rows : tmp) {
			for (TextureRegion cols : rows) {
				frames[index] = cols;
				index++;
			}
		}
		animDeath = new LifeformAnimation(ANIM_RATE*1.0f, frames, Direction.LEFT);
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
