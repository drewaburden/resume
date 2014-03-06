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
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.collision.CollisionEvent;
import com.dab.resume.debug.Log;
import com.dab.resume.events.Observer;
import com.dab.resume.hud.Dialog;
import com.dab.resume.input.InputBridge;
import com.dab.resume.input.InputEvent;
import com.dab.resume.lifeform.*;

import static com.badlogic.gdx.graphics.g2d.Animation.NORMAL;
import static com.dab.resume.GameState.State.CINEMATIC;
import static com.dab.resume.GameState.State.GAMEOVER;
import static com.dab.resume.GameState.State.PAUSED;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.ATTACK_SWORD;
import static com.dab.resume.lifeform.AnimationFactory.AnimationType.IDLE;
import static com.dab.resume.lifeform.SoundFactory.SoundType;

public class Player extends Lifeform implements Observer {
	private final PlayerAnimationFactory playerAnimationFactory = new PlayerAnimationFactory();
	private final PlayerSoundFactory playerSoundFactory = new PlayerSoundFactory();

	public Player() {
		super(LifeformType.PLAYER);
		lifeformMovement.registerObserver(this);
		boundingBox = new BoundingBox(lifeformMovement.getPosX()+26.0f, lifeformMovement.getPosY(), 20.0f, 56.0f, CollisionEvent.PLAYER);
		animationFactory.setPlayerAnimationFactory(playerAnimationFactory);
		soundFactory.setPlayerSoundFactory(playerSoundFactory);
	}

	public void initAssets() {
		Log.log();
		playerAnimationFactory.initAssets();
		playerSoundFactory.initAssets();
		animationManager = new LifeformAnimationManager(animationFactory.getAnimation(this.lifeformType, IDLE));
	}
	public void jump() {
		if (canChangeStates() && lifeformMovement.isOnGround()) {
			Log.log();
			lifeformMovement.jump();
			if (!isAttacking()) {
				animationManager.playAnimation(playerAnimationFactory.getAnimation(IDLE));
			}
			soundManager.playSound(playerSoundFactory.getSound(SoundType.JUMP));
			state = State.MOVING;
		}
	}
	public void attack() {
		if (canAttack()) {
			Log.log();
			state = State.ATTACKING;
			deltaAttackTime = 0.0f;
			animationManager.playAnimation(playerAnimationFactory.getAnimation(ATTACK_SWORD), NORMAL);
			soundManager.playSound(playerSoundFactory.getSound(SoundType.ATTACK_SWORD));
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

	@Override
	public void die() {
		super.die();
		GameState.setGameState(GAMEOVER);
		GameState.addGameState(CINEMATIC);
	}

	public void draw(SpriteBatch spriteBatch) {
		if (!GameState.areAnyGameStatesSet(PAUSED, CINEMATIC)) {
			final float delta = Gdx.graphics.getDeltaTime();
			deltaHurtTime += delta;

			updateMovement(delta);

			if (isAttacking()) {
				if (animationManager.isCurrentAnimationDone()) {
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
		animationManager.draw(spriteBatch, lifeformMovement.getPosX(), lifeformMovement.getPosY());
	}

	public void recheckInput() {
		// Check if the user is still holding a direction after an action finishes.
		// We have to do this because no new key events would be fired if the user
		// is holding a key that they had pressed before the the action finished.
		if (canChangeStates() && Dialog.NUM_DIALOGS_SHOWING == 0) {
			if (InputBridge.isMovementLeftPressed()) { move(Direction.LEFT); }
			else if (InputBridge.isMovementRightPressed()) { move(Direction.RIGHT); }
			else { idle(); }
		}
	}

	@Override
	public boolean eventTriggered(Object data) {
		if (data instanceof MovementEvent) {
			Log.verboseLog();
			MovementEvent event = (MovementEvent) data;
			switch (event) {
				case STEPPED: soundManager.playSound(playerSoundFactory.getSound(SoundType.MOVE)); return true;
				case LANDED:
					soundManager.playSound(playerSoundFactory.getSound(SoundType.LANDED));
					recheckInput();
					return true;
				case DONE_HURTING:
					if (isAlive()) {
						recheckInput();
					}
					return true;
			}
		}
		else if (data instanceof InputEvent) {
			InputEvent event = (InputEvent) data;
			switch (event) {
				case PRESS_RIGHT: move(Direction.RIGHT); return true;
				case PRESS_LEFT: move(Direction.LEFT); return true;
				case RELEASE_MOVE: stopXForce(); recheckInput(); return true;
				case PRESS_JUMP: jump(); return true;
				case RELEASE_JUMP: stopYForce(); return true;
				case PRESS_ATTACK: attack(); return true;
			}
		}
		else if (data instanceof com.dab.resume.collision.CollisionEvent) {

		}
		return false;
	}
}
