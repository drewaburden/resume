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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.assets.Assets;
import com.dab.resume.debug.Log;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.collision.CollisionEvent;
import com.dab.resume.lifeform.*;
import com.dab.resume.lifeform.enemies.mage.attacks.AttackType;
import com.dab.resume.lifeform.enemies.mage.attacks.Lightning;
import com.dab.resume.lifeform.enemies.mage.attacks.Projectile;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.g2d.Animation.NORMAL;
import static com.dab.resume.GameState.State.PAUSED;
import static com.dab.resume.GameState.State.PLAYING;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.ATTACK_LIGHTNING;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.IDLE;

public class Mage extends Lifeform {
	private final MageAnimationFactory mageAnimationFactory = new MageAnimationFactory();
	private final MageSoundFactory mageSoundFactory = new MageSoundFactory();
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

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

		// Load assets for attacks
		Assets.getInstance().load("game/chars/mage-lightning.png", Texture.class);
	}

	public void initAssets() {
		Log.log();
		mageAnimationFactory.initAssets();
		mageSoundFactory.initAssets();
		animationManager = new LifeformAnimationManager(animationFactory.getAnimation(this.lifeformType, IDLE));
	}

	public void attack(AttackType type) {
		if (canAttack()) {
			Log.log();
			state = State.ATTACKING;
			deltaAttackTime = 0.0f;
			switch (type) {
				case LIGHTNING: attack_lightning(); break;
				//case FIREBALL: attack_fireball(); break;
				//case SWORDS: attack_swords(); break;
			}
		}
	}
	private void attack_lightning() {
		animationManager.playAnimation(mageAnimationFactory.getAnimation(ATTACK_LIGHTNING), NORMAL);
		//soundManager.playLightning();
		projectiles.add(new Lightning(getPosX()+4.0f, getPosY()+10.0f));
	}
	public ArrayList<Projectile> getActiveProjectiles() { return projectiles; }
	public void destroyProjectile(Projectile projectile) {
		projectile.dispose();
		projectiles.remove(projectile);
	}

	public void draw(SpriteBatch spriteBatch) {
		if (GameState.getGameState() != PAUSED) {
			final float delta = Gdx.graphics.getDeltaTime();
			deltaHurtTime += delta;

			if (deltaHurtTime >= 2.0f) {
				deltaHurtTime = 0.0f;
				attack(AttackType.LIGHTNING);
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

		// Draw projectiles
		for (Projectile projectile : projectiles) {
			projectile.draw(spriteBatch);
		}
	}
}
