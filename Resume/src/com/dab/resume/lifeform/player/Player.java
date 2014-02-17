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

package com.dab.resume.lifeform.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.debug.Log;
import com.dab.resume.events.Observer;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.collision.CollisionEvent;
import com.dab.resume.input.InputBridge;
import com.dab.resume.input.InputEvent;
import com.dab.resume.lifeform.*;

import static com.badlogic.gdx.graphics.g2d.Animation.NORMAL;
import static com.dab.resume.GameState.State.PLAYING;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.ATTACK;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.IDLE;

public class Player extends Lifeform implements Observer {
	private final PlayerAnimationFactory playerAnimationFactory = new PlayerAnimationFactory();
	private final PlayerSoundFX lifeformSoundFX = new PlayerSoundFX();

	public Player() {
		super(LifeformType.PLAYER);
		animationFactory.setPlayerAnimationFactory(playerAnimationFactory);
		lifeformMovement.registerObserver(this);
		boundingBox = new BoundingBox(lifeformMovement.getPosX()+26.0f, lifeformMovement.getPosY(), 20.0f, 56.0f, CollisionEvent.PLAYER);
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
		BoundingBox collAttack = new BoundingBox(lifeformMovement.getPosX(), lifeformMovement.getPosY()+40.0f-16.0f,
				16.0f, 16.0f, CollisionEvent.ATTACK);
		if (direction == Direction.RIGHT) {
			collAttack.setX(lifeformMovement.getPosX() + 72.0f - collAttack.width);
		}

		return collAttack;
	}

	public void draw(SpriteBatch spriteBatch) {
		if (GameState.getGameState() == PLAYING) {
			final float delta = Gdx.graphics.getDeltaTime();
			deltaHurtTime += delta;

			updateMovement(delta);

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
				case STEPPED: lifeformSoundFX.playStepSound(); break;
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
		else if (data instanceof com.dab.resume.collision.CollisionEvent) {

		}
	}
}
