/********************************************************************************************************
 * Project:     Résumé
 * File:        Fadeable.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      An extension of the Sprite class that allows for automation of fading
 *      in and out to a given alpha at a given speed.
 ********************************************************************************************************/

package com.dab.resume.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dab.resume.GameState;

public class Fadeable extends Sprite {
	private boolean fading = false;
	private float alphaInitial, alphaTarget, fadeSpeed;

	public Fadeable(Texture texture) {
		super(texture);
	}

	public void fadeToAlpha(float alpha, float speed) {
		fading = true;
		alphaInitial = this.getColor().a;
		alphaTarget = alpha;
		fadeSpeed = speed;
	}

	public boolean isFading() { return fading; }

	@Override
	public void draw(Batch batch) {
		if (fading && !GameState.isGameStateSet(GameState.State.PAUSED)) {
			float currentAlpha = this.getColor().a;
			float delta = Gdx.graphics.getDeltaTime();

			// Update alpha
			if (alphaTarget - alphaInitial < 0.0f) { // Fading out
				currentAlpha = Math.max(alphaTarget, currentAlpha - fadeSpeed*delta);
			}
			else if (alphaTarget - alphaInitial > 0.0f) { // Fading in
				currentAlpha = Math.min(alphaTarget, currentAlpha + fadeSpeed*delta);
			}
			this.setAlpha(currentAlpha);

			// If we hit the target alpha, we're done
			if (currentAlpha == alphaTarget) fading = false;
		}
		super.draw(batch); // Draw the actual sprite
	}
}
