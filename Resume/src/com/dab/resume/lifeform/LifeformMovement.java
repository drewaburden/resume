/********************************************************************************************************
 * Project:     Résumé
 * File:        LifeformMovement.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles and performs calculations for (X,Y) movement of Lifeforms.
 *      Upon certain events, the class will notify observers.
 ********************************************************************************************************/

package com.dab.resume.lifeform;

import com.dab.resume.events.Observable;
import com.dab.resume.scene.World;

public class LifeformMovement extends Observable {
	public float jumpTimeLimit = 0.5f; // How long the lifeform holding the jump button will affect the height of the jump
	public float hurtAccelerationX = 1500.0f, hurtDecelerationX = 700.0f, hurtMaxSpeed = 150.0f;
	public float hurtAccelerationY = 400.0f, hurtInitialVelocityY = 850.f;
	public float jumpAccelerationY = 400.0f, jumpInitialVelocityY = 850.0f;
	public float moveAccelerationX = 450.0f, moveDecelerationX = 500.0f, moveMaxSpeedX = 150.0f;
	public Direction direction = Direction.RIGHT;
	public float stepDelay = 0.324324f; // Time between step sounds (~185 bpm)

	protected float deltaFootstepTime; // Time since last footstep
	private boolean y_movement = false, x_movement = false;
	private boolean y_force = false, x_force = false;
	private float accelerationY;
	private float accelerationX, decelerationX, maxSpeedX;
	private float velocityX = 0.0f, velocityY = 0.0f;
	private float posX = -150.0f, posY = World.FLOOR;
	private float elapAirTime = 0.0f; // Time the lifeform has been in the air
	private boolean isHurtBouncing = false;

	/************
	 * General Movement
	 ************/
	public void updateMovement(final float delta) {
		updateYMovement(delta);
		updateXMovement(delta);
	}

	/************
	 * X Movement
	 ************/
	private void updateXMovement(final float delta) {
		if (x_movement) {
			if (isOnGround()) {
				deltaFootstepTime += delta;
				if (deltaFootstepTime >= stepDelay) {
					stepped();
					deltaFootstepTime = 0.0f;
				}
			}

			applyAccelerationX(delta);
			applyDecelerationX(delta);
			if (isStopped()) {
				x_movement = false;
				x_force = false;
			}
			//posX += velocityX * delta;
			posX += delta * (velocityX + delta*accelerationX/2.0f); // Velocity Verlet method
		}
	}
	public void move(Direction direction) {
		deltaFootstepTime += 0.0f;
		accelerationX = moveAccelerationX;
		decelerationX = moveDecelerationX;
		maxSpeedX = moveMaxSpeedX;
		applyXForce();
		this.direction = direction;
	}
	public void hurtBounce(Direction direction) {
		velocityX = 0.0f;
		velocityY = 0.0f;
		elapAirTime = 0.0f;
		accelerationX = hurtAccelerationX;
		decelerationX = hurtDecelerationX;
		maxSpeedX = hurtMaxSpeed;
		accelerationY = hurtAccelerationY;
		velocityY = hurtInitialVelocityY;
		isHurtBouncing = true;
		applyXForce();
		applyYForce();
		this.direction = direction;
	}
	public void stopHurtBounce() {
		stopXForce();
		stopYForce();
	}
	public void attackSlowDown(float delta) {
		if (isOnGround() && !isStopped()) {
			if (direction == Direction.RIGHT) {
				setVelocityX(Math.max(getVelocityX() - decelerationX * 1.5f * delta, 0.0f));
			}
			else {
				setVelocityX(Math.min(getVelocityX() + decelerationX * 1.5f * delta, 0.0f));
			}
		}
	}
	public void setPosX(float newPosX) { this.posX = newPosX; }
	public float getPosX() { return posX; }
	public void setVelocityX(float velocityX) { this.velocityX = velocityX; }
	public float getVelocityX() { return velocityX; }
	public float getAccelerationX() { return accelerationX; }
	public void applyXForce() { x_force = true; x_movement = true; }
	public void stopXForce() {
		// Stop applying the X movement force, and allow the deceleration to bring the lifeform to a stop
		x_force = false;
	}
	private void applyAccelerationX(final float delta) {
		if (x_force) {
			if (direction == Direction.RIGHT) {
				velocityX = Math.min(velocityX + accelerationX * delta, maxSpeedX);
			}
			else {
				velocityX = Math.max(velocityX - accelerationX * delta, -maxSpeedX);
			}
			if (isHurtBouncing && Math.abs(velocityX) >= maxSpeedX) {
				stopHurtBounce();
			}
		}
	}
	private void applyDecelerationX(final float delta) {
		if (!y_movement && !x_force) {
			if (direction == Direction.RIGHT) {
				velocityX = Math.max(velocityX - decelerationX * delta, 0.0f);
			}
			else {
				velocityX = Math.min(velocityX + decelerationX * delta, 0.0f);
			}
		}
	}
	public void stopXMovement() {
		x_movement = false;
		x_force = false;
		velocityX = 0.0f;
	}
	public boolean isStopped() { return (velocityX == 0.0f); }

	/************
	 * Y Movement
	 ************/
	private void updateYMovement(final float delta) {
		if (y_movement) {
			applyAccelerationY(delta);
			applyDecelerationY(delta);
			applyGravity(delta);
			if (isOnGround()) {
				y_movement = false;
				y_force = false;
				velocityY = 0.0f;
				if (isHurtBouncing) {
					isHurtBouncing = false;
					finishedHurtBounce();
				}
				else {
					landed();
				}
			}
		}
	}
	public void setPosY(float newPosY) { this.posY = newPosY; }
	public float getPosY() { return posY; }
	public void setVelocityY(float velocityX) { this.velocityY = velocityY; }
	public float getVelocityY() { return velocityY; }
	public void applyYForce() { y_force = true; y_movement = true; }
	public void stopYForce() {
		// Stop applying the Y movement force, and allow the deceleration to bring the lifeform to a stop
		y_force = false;
	}
	private void applyAccelerationY(final float delta) {
		if (y_force) {
			elapAirTime += delta;
			if (elapAirTime >= jumpTimeLimit) {
				elapAirTime = 0.0f;
				y_force = false;
			}
			velocityY += accelerationY * delta;
		}
		posY += velocityY * delta; // Apply force
	}
	private void applyDecelerationY(final float delta) {
		velocityY -= World.GRAVITY * delta; // Slow down ascent
	}
	private void applyGravity(final float delta) {
		posY = Math.max(posY - (World.GRAVITY * delta), World.FLOOR); // Pull lifeform down
	}
	public void jump() {
		if (!y_movement) {
			deltaFootstepTime += 0.0f;
			accelerationY = jumpAccelerationY;
			velocityY = jumpInitialVelocityY;
			elapAirTime = 0.0f;
			y_movement = true;
			y_force = true;
		}
	}
	public boolean isOnGround() { return (posY == World.FLOOR && !y_force); }
	public boolean isHurtBouncing() { return isHurtBouncing; }
	private void landed() { notifyObservers(MovementEvent.LANDED); }
	private void finishedHurtBounce() { notifyObservers(MovementEvent.DONE_HURTING); }
	private void stepped() { notifyObservers(MovementEvent.STEPPED); }
}
