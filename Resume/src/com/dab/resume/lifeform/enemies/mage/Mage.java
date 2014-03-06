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
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.collision.CollisionEvent;
import com.dab.resume.debug.Log;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.Lifeform;
import com.dab.resume.lifeform.LifeformAnimationManager;
import com.dab.resume.lifeform.State;
import com.dab.resume.lifeform.enemies.mage.attacks.AttackType;
import com.dab.resume.lifeform.enemies.mage.attacks.Lightning;
import com.dab.resume.lifeform.enemies.mage.attacks.Projectile;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.g2d.Animation.NORMAL;
import static com.dab.resume.GameState.State.CINEMATIC;
import static com.dab.resume.GameState.State.PAUSED;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.ATTACK_LIGHTNING;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.IDLE;

public class Mage extends Lifeform {
	private final MageAnimationFactory mageAnimationFactory = new MageAnimationFactory();
	private final MageSoundFactory mageSoundFactory = new MageSoundFactory();
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	// Times it takes for the mage to actually attack after initiating the attack action
	private final float powerUpTime_lightning = 1.15f;
	private final float powerUpTime_fireball = 1.15f;
	private AttackType poweringUpAttack = null;
	private float elapPowerUpTime = 0.0f; // How long the mage has been powering up

	public Mage(float posX) {
		super(LifeformType.MAGE);
		jumpTimeLimit = 0.5f;
		lifeformMovement.moveAccelerationX = 300.0f;
		lifeformMovement.moveDecelerationX = 350.0f;
		lifeformMovement.moveMaxSpeedX = 40.0f;
		hurtDelay = 0.5f;
		direction = Direction.LEFT;
		lifeformMovement.setPosX(posX);
		boundingBox = new BoundingBox(lifeformMovement.getPosX()+16.0f, lifeformMovement.getPosY(), 24.0f, 50.0f, CollisionEvent.ENEMY);
		healthMax = 4;
		healthCurrent = healthMax;
		animationFactory.setMageAnimationFactory(mageAnimationFactory);
		soundFactory.setMageSoundFactory(mageSoundFactory);

		// Load assets for attacks
		Assets.getInstance().load("game/chars/mage-lightning.png", Texture.class);
	}

	public void initAssets() {
		if (isAlive()) {
			Log.log();
			mageAnimationFactory.initAssets();
			mageSoundFactory.initAssets();
			animationManager = new LifeformAnimationManager(animationFactory.getAnimation(this.lifeformType, IDLE));
		}
	}

	public void attack(AttackType type) {
		if (canAttack()) {
			Log.log();
			state = State.ATTACKING;
			deltaAttackTime = 0.0f;
			poweringUpAttack = type;
			switch (type) {
				case LIGHTNING: animationManager.playAnimation(mageAnimationFactory.getAnimation(ATTACK_LIGHTNING), NORMAL); break;
				case FIREBALL: break;
			}
		}
	}
	private void attack_lightning() {
		Log.log();
		//soundManager.playLightning();
		projectiles.add(new Lightning(getPosX()+4.0f, getPosY()+10.0f));
	}
	public ArrayList<Projectile> getActiveProjectiles() { return projectiles; }
	public void destroyProjectile(Projectile projectile) {
		projectile.dispose();
		projectiles.remove(projectile);
	}

	@Override
	public void die() {
		super.die();
		// I would do a foreach, but that would give a ConcurrentModificationException
		int numProjectiles = getActiveProjectiles().size();
		for (int projectileIndex = 0; projectileIndex < numProjectiles; ++projectileIndex) {
			Projectile projectile = getActiveProjectiles().get(projectileIndex);
			destroyProjectile(projectile);
			numProjectiles--;
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		if (!GameState.areAnyGameStatesSet(PAUSED, CINEMATIC)) {
			final float delta = Gdx.graphics.getDeltaTime();
			deltaHurtTime += delta;

			// If the mage is powering up an attack
			if (poweringUpAttack != null && isAlive()) {
				elapPowerUpTime += delta;
				switch (poweringUpAttack) {
					case LIGHTNING:
						if (elapPowerUpTime >= powerUpTime_lightning) {
							elapPowerUpTime = 0.0f;
							poweringUpAttack = null;
							attack_lightning();
						}
						break;
					case FIREBALL:
						if (elapPowerUpTime >= powerUpTime_fireball) {
							elapPowerUpTime = 0.0f;
							poweringUpAttack = null;
							//attack_fireball();
						}
						break;
				}
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
