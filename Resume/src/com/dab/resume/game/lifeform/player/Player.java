/********************************************************************************************************
 * Project:     Résumé
 * File:        Player.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines the Player's game avatar and his or her graphics, sounds, and movement constituents.
 ********************************************************************************************************/

package com.dab.resume.game.lifeform.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.debug.Log;
import com.dab.resume.events.Observer;
import com.dab.resume.game.collision.BoundingBox;
import com.dab.resume.game.input.InputBridge;
import com.dab.resume.game.input.InputEvent;
import com.dab.resume.game.lifeform.*;

import static com.badlogic.gdx.graphics.g2d.Animation.NORMAL;
import static com.dab.resume.game.lifeform.AnimationFactory.AnimationType.ATTACK;
import static com.dab.resume.game.lifeform.AnimationFactory.AnimationType.IDLE;

public class Player extends Lifeform implements Observer {
	private final PlayerAnimationFactory playerAnimationFactory = new PlayerAnimationFactory();
	private final PlayerSoundFX lifeformSoundFX = new PlayerSoundFX();

	public Player() {
		super(LifeformType.PLAYER);
		animationFactory.setPlayerAnimationFactory(playerAnimationFactory);
		lifeformMovement.registerObserver(this);
	}

	public void initAssets() {
		Log.log();
		playerAnimationFactory.initAssets();
		lifeformGraphics = new LifeformGraphics(animationFactory.getAnimation(this.lifeformType, IDLE));
		lifeformSoundFX.initAssets();
	}
	public void jump() {
		if (canChangeStates() && lifeformMovement.isOnGround()) {
			Log.log();
			lifeformMovement.jump();
			if (!isAttacking()) {
				lifeformGraphics.playAnimation(playerAnimationFactory.getAnimation(IDLE));
			}
			lifeformSoundFX.playJumpSound();
			state = State.MOVING;
		}
	}
	public void attack() {
		if (canAttack()) {
			Log.log();
			state = State.ATTACKING;
			deltaAttackTime = 0.0f;
			lifeformGraphics.playAnimation(playerAnimationFactory.getAnimation(ATTACK), NORMAL);
			lifeformSoundFX.playSwordSwing();
		}
	}

	public BoundingBox getAttackBoundingBox() {
		BoundingBox collAttack = new BoundingBox(lifeformMovement.getPosX(), lifeformMovement.getPosY()+40.0f-16.0f, 16.0f, 16.0f);
		if (direction == Direction.RIGHT) {
			collAttack.setX(lifeformMovement.getPosX() + 72.0f - collAttack.width);
		}

		return collAttack;
	}

	public void draw(SpriteBatch spriteBatch) {
		final float delta = Gdx.graphics.getDeltaTime();
		deltaHurtTime += delta;

		updateMovement(delta);

		/*if (y_movement) {
			if (y_force) {
				elapJumpTime += delta;
				if (elapJumpTime >= jumpTimeLimit) {
					elapJumpTime = 0.0f;
					y_force = false;
				}
				velocityY += jumpAcceleration * delta;
			}

			this.translate(0.0f, velocityY * delta); // Apply force
			velocityY -= World.GRAVITY * delta; // Apply deceleration

			this.setPosY(Math.max(posY - (World.GRAVITY * delta), World.FLOOR)); // Apply gravity
			if (posY == World.FLOOR && !y_force) {
				y_movement = false;
				y_force = false;
				lifeformSoundFX.playJumpLandSound();
				recheckInput();
			}
		}
		if (x_movement) {
			// Step sound
			deltaStepTime += delta;
			if (!y_movement && deltaStepTime >= stepDelay && !isAttacking()) {
				lifeformSoundFX.playStepSound();
				// We set this to zero instead of subtracting STEP_TIME, because we don't want to
				// end up playing a ton of step sounds overlapped if there's a framerate drop.
				deltaStepTime = 0.0f;
			}

			// Acceleration
			if (x_force) {
				if (direction == Direction.RIGHT) {
					velocityX = Math.min(velocityX + moveAcceleration * delta, maxSpeedX);
				}
				else {
					velocityX = Math.max(velocityX - moveAcceleration * delta, -maxSpeedX);
				}
			}
			// Deceleration. Make the player slide very slightly when he/she stops
			else if (!y_movement && !x_force) {
				if (direction == Direction.RIGHT) {
					velocityX = Math.max(velocityX - decelerationX * delta, 0.0f);
				}
				else {
					velocityX = Math.min(velocityX + decelerationX * delta, 0.0f);
				}
			}

			if (velocityX == 0.0f) {
				x_movement = false;
				x_force = false;
				recheckInput();
			}
			this.translate(velocityX * delta, 0.0f);
		}*/
		if (isAttacking()) {
			if (lifeformGraphics.isCurrentAnimationDone()) {
				stopAllActions();
				recheckInput();
			}
			else {
				// If the player is running on the ground and attacking, slow them down a lot (but not completely)
				lifeformMovement.attackSlowDown(delta);
			}
		}
		else {
			deltaAttackTime += delta;
		}

		// Draw the lifeform's animation
		lifeformGraphics.draw(spriteBatch, lifeformMovement.getPosX(), lifeformMovement.getPosY());
	}

	public void recheckInput() {
		// Check if the user is still holding a direction after an action finishes.
		// We have to do this because no new key events would be fired if the user
		// is holding a key that they had pressed before the the action finished.
		if (canChangeStates()) {
			if (InputBridge.isMovementLeftPressed()) { move(Direction.LEFT); }
			else if (InputBridge.isMovementRightPressed()) { move(Direction.RIGHT); }
			else { idle(); }
		}
	}

	public void eventTriggered(Object data) {
		if (data instanceof MovementEvent) {
			Log.log();
			MovementEvent event = (MovementEvent) data;
			switch (event) {
				case LANDED: lifeformSoundFX.playJumpLandSound(); recheckInput(); break;
				case DONE_HURTING: if (isAlive()) recheckInput();
				default: assert(false);
			}
		}
		else if (data instanceof InputEvent) {
			InputEvent event = (InputEvent) data;
			switch (event) {
				case PRESS_MOVE_RIGHT: move(Direction.RIGHT); break;
				case PRESS_MOVE_LEFT: move(Direction.LEFT); break;
				case RELEASE_MOVE: stopXForce(); recheckInput(); break;
				case PRESS_JUMP: jump(); break;
				case RELEASE_JUMP: stopYForce(); break;
				case PRESS_ATTACK: attack(); break;
			}
		}
	}
}
