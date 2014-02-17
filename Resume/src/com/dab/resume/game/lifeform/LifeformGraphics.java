/********************************************************************************************************
 * Project:     Résumé
 * File:        LifeformGraphics.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles changing and drawing of animations for any Lifeform
 ********************************************************************************************************/

package com.dab.resume.game.lifeform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dab.resume.GameState;

import static com.badlogic.gdx.graphics.g2d.Animation.LOOP;

public class LifeformGraphics {
	protected float animTime = 0.0f; // How long an animation has been playing. Determines which frame to display.
	protected Animation animCurrent;

	protected boolean isFlashing = false;
	public float flashDelay = 0.1f; // Time between hurt flashes
	protected float deltaFlashTime = flashDelay; // Time since last hurt flash
	public float flashOnTime = 0.1f; // Time for the flash to stay rendered
	protected float elapFlashTime = 0.0f; // Elapsed time of the hurt flash
	public float totalTimeToFlash = 1.5f; // Total time to hurt flash

	private TextureRegion currentFrame;

	public LifeformGraphics(Animation animInit) {
		animCurrent = animInit;
		playAnimation(animCurrent);
	}

	public void playAnimation(Animation animation) { playAnimation(animation, getCurrentFacing(), LOOP); }
	public void playAnimation(Animation animation, Direction direction) { playAnimation(animation, direction, LOOP); }
	public void playAnimation(Animation animation, final int playMode) { playAnimation(animation, getCurrentFacing(), playMode); }
	public void playAnimation(Animation animation, Direction direction, final int playMode) {
		animTime = 0.0f;
		// Make sure the animation is facing the correct direction
		if (animation.getDirection() != direction) animation.flipHorizontal();
		animCurrent = animation;
		animCurrent.setPlayMode(playMode);
	}
	public boolean isCurrentAnimationDone() { return animCurrent.isAnimationFinished(animTime); }
	public Direction getCurrentFacing() { return animCurrent.getDirection(); }

	public void startFlashing() { isFlashing = true; }
	public void stopFlashing() {
		elapFlashTime = 0.0f;
		deltaFlashTime = flashDelay;
		isFlashing = false;
	}

	public void draw(SpriteBatch spriteBatch, float posX, float posY) {
		if (GameState.getGameState() == GameState.State.PLAYING) {
			float delta = Gdx.graphics.getDeltaTime();

			animTime += delta;

			currentFrame = animCurrent.getKeyFrame(animTime);
			if (isFlashing) {
				elapFlashTime += delta;
				deltaFlashTime += delta;
				if (deltaFlashTime >= flashDelay) {
					spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
					if (deltaFlashTime >= flashDelay+flashOnTime) {
						deltaFlashTime = 0.0f;
						spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
					}
				}
				if (elapFlashTime >= totalTimeToFlash) {
					stopFlashing();
					spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				}
			}
		}

		// Draw the frame
		spriteBatch.draw(currentFrame, posX, posY);
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
