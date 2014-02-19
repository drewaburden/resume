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
import com.dab.resume.lifeform.*;

import static com.badlogic.gdx.graphics.g2d.Animation.NORMAL;
import static com.dab.resume.GameState.State.PAUSED;
import static com.dab.resume.GameState.State.PLAYING;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.ATTACK_LIGHTNING;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.IDLE;

public class Mage extends Lifeform {
	private final MageAnimationFactory mageAnimationFactory = new MageAnimationFactory();
	private final MageSoundFactory mageSoundFactory = new MageSoundFactory();

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
		soundFactory.setMageSoundFactory(mageSoundFactory);
	}

	public void initAssets() {
		Log.log();
		mageAnimationFactory.initAssets();
		mageSoundFactory.initAssets();
		animationManager = new LifeformAnimationManager(animationFactory.getAnimation(this.lifeformType, IDLE));
	}

	public void attack() {
		if (canAttack()) {
			Log.log();
			state = State.ATTACKING;
			deltaAttackTime = 0.0f;
			animationManager.playAnimation(mageAnimationFactory.getAnimation(ATTACK_LIGHTNING), NORMAL);
			//soundManager.playLightning();
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		if (GameState.getGameState() != PAUSED) {
			final float delta = Gdx.graphics.getDeltaTime();
			deltaHurtTime += delta;

			if (deltaHurtTime >= 6.0f) {
				deltaHurtTime = 0.0f;
				attack();
			}

			updateMovement(delta);

			if (isAttacking() && animationManager.isCurrentAnimationDone()) {
				stopAllActions();
			}
			else {
				deltaAttackTime += delta;
			}
		}

		// Draw the lifeform's animation
		animationManager.draw(spriteBatch, lifeformMovement.getPosX(), lifeformMovement.getPosY());
	}
}
