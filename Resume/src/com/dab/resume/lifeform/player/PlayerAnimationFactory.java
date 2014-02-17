/********************************************************************************************************
 * Project:     Résumé
 * File:        PlayerAnimationFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Loads, initializes, and serves Mage Animations
 ********************************************************************************************************/

package com.dab.resume.lifeform.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.Animation;
import com.dab.resume.lifeform.Direction;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType;

public class PlayerAnimationFactory {
	private final float ANIM_RATE = 0.15f;
	private Animation animIdle, animMove, animAttack;

	public PlayerAnimationFactory() {
		Assets.getInstance().load("game/chars/player-idle.png", Texture.class);
		Assets.getInstance().load("game/chars/player-move.png", Texture.class);
		Assets.getInstance().load("game/chars/player-attack.png", Texture.class);
	}
	public void initAssets() {
		Texture texture = Assets.getInstance().get("game/chars/player-idle.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 3, texture.getHeight());
		TextureRegion[] frames = new TextureRegion[3];
		int index = 0;
		for (TextureRegion[] rows : tmp) {
			for (TextureRegion cols : rows) {
				frames[index] = cols;
				index++;
			}
		}
		animIdle = new Animation(ANIM_RATE, frames, Direction.RIGHT);
		texture = Assets.getInstance().get("game/chars/player-move.png");
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
		animMove = new Animation(ANIM_RATE, frames, Direction.RIGHT);
		texture = Assets.getInstance().get("game/chars/player-attack.png");
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
		animAttack = new Animation(ANIM_RATE*0.4f, frames, Direction.RIGHT);
	}

	public Animation getAnimation(AnimationType animation) {
		switch (animation) {
			case IDLE: return animIdle;
			case MOVE: return animMove;
			case JUMP: return animIdle;
			case BLOCK: return animIdle;
			case ATTACK: return animAttack;
			case DEATH: return animIdle;
			default: return animIdle;
		}
	}
}
