/********************************************************************************************************
 * Project:     Résumé
 * File:        Lifeform.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines an abstract Lifeform and its constituents
 ********************************************************************************************************/

package com.dab.resume.lifeform;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.TerminalGame;
import com.dab.resume.debug.Log;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.collision.CollisionEvent;
import com.dab.resume.events.Observable;

import static com.dab.resume.GameState.State.PLAYING;
import static com.dab.resume.lifeform.AnimationFactory.*;
import static com.dab.resume.lifeform.SoundFactory.*;

public abstract class Lifeform extends Observable {
	public static enum LifeformType {
		PLAYER, MAGE
	}

	protected LifeformType lifeformType;
	protected AnimationFactory animationFactory;
	protected SoundFactory soundFactory;

	protected LifeformAnimationManager animationManager;
	protected LifeformSoundManager soundManager;
	protected LifeformMovement lifeformMovement;
	protected BoundingBox boundingBox;

	protected float jumpTimeLimit = 0.5f; // How long the user holding the jump button will affect the height of the jump
	protected float attackDelay = 0.0f; // Time the user must wait in between attacks
	protected float hurtDelay = 1.5f; // Time that must pass before getting hurt again

	protected Direction direction = Direction.RIGHT;
	protected State state = State.IDLE;
	protected float deltaAttackTime = attackDelay; // Time since last attack
	protected float deltaHurtTime = hurtDelay; // Time since last hurt

	protected int healthMax = 3, healthCurrent = healthMax;
	protected int manaMax = 10, mana = manaMax;
	protected int attack_power = 1;

	public Lifeform(LifeformType lifeformType) {
		this.lifeformType = lifeformType;
		animationFactory = new AnimationFactory();
		soundManager = new LifeformSoundManager();
		soundFactory = new SoundFactory();
		lifeformMovement = new LifeformMovement();
		boundingBox = new BoundingBox(0.0f, 0.0f, 0.0f, 0.0f, CollisionEvent.UNDEF);
	}

	public abstract void initAssets();

	/***********
	 * Movement
	 ***********/
	public void updateMovement(float delta) {
		if (GameState.getGameState() == PLAYING) {
			float originalX = lifeformMovement.getPosX(), originalY = lifeformMovement.getPosY();
			lifeformMovement.updateMovement(delta);
			getBoundingBox().translate(lifeformMovement.getPosX() - originalX, lifeformMovement.getPosY() - originalY);
		}
	}
	public void translate(float offsetX, float offsetY) {
		this.setPosX(this.getPosX() + offsetX);
		this.setPosY(this.getPosY() + offsetY);
	}
	public void setPosX(float newPosX) {
		float boundingBoxOffset = getBoundingBox().getLeft() - lifeformMovement.getPosX();
		lifeformMovement.setPosX(newPosX);
		getBoundingBox().setX(boundingBoxOffset + newPosX);
	}
	public void setPosY(float newPosY) {
		float boundingBoxOffset = lifeformMovement.getPosY() - getBoundingBox().getBottom();
		lifeformMovement.setPosY(newPosY);
		getBoundingBox().setY(boundingBoxOffset + newPosY);
	}
	public float getPosX() { return lifeformMovement.getPosX(); }
	public float getPosY() { return lifeformMovement.getPosY(); }
	public float getVelocityX() { return lifeformMovement.getVelocityX(); }
	public BoundingBox getBoundingBox() { return boundingBox; }
	public void move(Direction direction) {
		if (canChangeStates()) {
			Log.log();
			if (lifeformMovement.isOnGround()) {
				this.direction = direction;
				animationManager.playAnimation(animationFactory.getAnimation(lifeformType, AnimationType.MOVE), direction);
			}
			state = State.MOVING;
			lifeformMovement.move(direction);
			notifyObservers(MovementEvent.MOVE);
		}
	}
	public void stopXMovement() {
		state = State.IDLE; // In case the lifeform is attacking
		idle();
		lifeformMovement.stopXMovement();
	}
	public void stopXForce() { idle(); lifeformMovement.stopXForce(); }
	public void stopYForce() { lifeformMovement.stopYForce(); }

	/***********
	 * States
	 ***********/
	protected boolean canChangeStates() { return (isAlive() && !isAttacking()); }
	public int getMaxHealth() { return healthMax; }
	public int getHealth() { return healthCurrent; }
	public boolean isAlive() { return healthCurrent > 0; }
	public boolean isHurtBouncing() { return lifeformMovement.isHurtBouncing(); }
	public boolean canAttack() { return (deltaAttackTime >= attackDelay && canChangeStates()); }
	public boolean isAttacking() { return state == State.ATTACKING; }
	public int getAttackPower() { return attack_power; }
	public void stopAllActions() {
		state = State.IDLE; // In case the lifeform is attacking, this will force the canChangeStates() call to return true
		idle();
	}
	public void idle() {
		if (canChangeStates()) {
			Log.log();
			state = State.IDLE;
			animationManager.playAnimation(animationFactory.getAnimation(lifeformType, AnimationType.IDLE));
		}
	}
	public void hurt(int damage, Direction damagedSide) {
		if (isAlive() && deltaHurtTime >= hurtDelay) {
			Log.log();
			deltaHurtTime = 0.0f;
			healthCurrent -= damage;
			animationManager.startFlashing();
			lifeformMovement.hurtBounce(damagedSide.invert());
			if (healthCurrent <= 0) {
				die();
			}
			else {
				//animationManager.playAnimation(animationFactory.getAnimation(lifeformType, HURT));
				soundManager.playSound(soundFactory.getSound(lifeformType, SoundType.HURT));
			}
		}
	}
	public void die() {
		Log.log();
		state = State.DEAD;
		animationManager.playAnimation(animationFactory.getAnimation(lifeformType, AnimationType.DEATH), LifeformAnimation.NORMAL);
		animationManager.stopFlashing();
		soundManager.playSound(soundFactory.getSound(lifeformType, SoundType.DEATH));
		healthCurrent = 0;
	}

	/***********
	 * Graphics
	 ***********/
	public abstract void draw(SpriteBatch spriteBatch);
}
