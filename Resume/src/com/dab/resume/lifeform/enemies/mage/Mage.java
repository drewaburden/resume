/********************************************************************************************************
 * Project:     Résumé
 * File:        Mage.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines the Mage enemy and it's graphics, sounds, movement, and state machine constituents.
 ********************************************************************************************************/

package com.dab.resume.lifeform.enemies.mage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.debug.Log;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.collision.CollisionEvent;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.Lifeform;
import com.dab.resume.lifeform.LifeformGraphics;
import com.dab.resume.lifeform.State;

import static com.badlogic.gdx.graphics.g2d.Animation.NORMAL;
import static com.dab.resume.GameState.State.PLAYING;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.ATTACK_LIGHTNING;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.IDLE;

public class Mage extends Lifeform {
	private final MageAnimationFactory mageAnimationFactory = new MageAnimationFactory();

	public Mage() {
		super(LifeformType.MAGE);
		jumpTimeLimit = 0.5f;
		lifeformMovement.moveAccelerationX = 300.0f;
		lifeformMovement.moveDecelerationX = 350.0f;
		lifeformMovement.moveMaxSpeedX = 40.0f;
		hurtDelay = 0.5f;
		direction = Direction.LEFT;
		lifeformMovement.setPosX(125.0f);
		boundingBox = new BoundingBox(lifeformMovement.getPosX()+16.0f, lifeformMovement.getPosY(), 24.0f, 50.0f, CollisionEvent.ENEMY);
		healthMax = 2;
		healthCurrent = healthMax;
		animationFactory.setMageAnimationFactory(mageAnimationFactory);
	}

	public void initAssets() {
		Log.log();
		mageAnimationFactory.initAssets();
		lifeformGraphics = new LifeformGraphics(animationFactory.getAnimation(this.lifeformType, IDLE));
	}

	public void attack() {
		if (canAttack()) {
			Log.log();
			state = State.ATTACKING;
			deltaAttackTime = 0.0f;
			lifeformGraphics.playAnimation(mageAnimationFactory.getAnimation(ATTACK_LIGHTNING), NORMAL);
			//lifeformSoundFX.playLightning();
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		if (GameState.getGameState() == PLAYING) {
			final float delta = Gdx.graphics.getDeltaTime();
			deltaHurtTime += delta;

			if (deltaHurtTime >= 6.0f) {
				deltaHurtTime = 0.0f;
				attack();
			}

			updateMovement(delta);

			if (isAttacking() && lifeformGraphics.isCurrentAnimationDone()) {
				stopAllActions();
			}
			else {
				deltaAttackTime += delta;
			}
		}

		// Draw the lifeform's animation
		lifeformGraphics.draw(spriteBatch, lifeformMovement.getPosX(), lifeformMovement.getPosY());
	}
}
