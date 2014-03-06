/********************************************************************************************************
 * Project:     Résumé
 * File:        Lightning.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      This class defines the Mage's lightning projectile and handles its animation.
 ********************************************************************************************************/

package com.dab.resume.lifeform.enemies.mage.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dab.resume.GameState;
import com.dab.resume.assets.Assets;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.collision.CollisionEvent;

import static com.dab.resume.GameState.State.PAUSED;

public class Lightning extends Projectile {
	private final float ANIM_RATE = 0.15f;

	private float animTime = 0.0f; // How long an animation has been playing. Determines which frame to display.
	private Animation lightning;

	private boolean disposing = false;

	public Lightning(float posX, float posY) {
		this(1, posX, posY);
	}
	public Lightning(int attackPower, float posX, float posY) {
		super(attackPower, new BoundingBox(posX, posY, 26.0f, 8.0f, CollisionEvent.ATTACK), posX, posY, -100.0f);
		initAssets();
	}

	public void initAssets() {
		TextureAtlas atlas = Assets.getInstance().get("spritesheets/scene2.pack");
		lightning = Assets.getInstance().getAnimation(atlas.findRegion("mage-lightning"), 2, ANIM_RATE);
		lightning.setPlayMode(Animation.LOOP);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		if (!disposing) {
			if (!GameState.isGameStateSet(PAUSED)) {
				float delta = Gdx.graphics.getDeltaTime();
				animTime += delta;
				update(delta);
			}

			// Determine which frame to draw and draw it
			TextureRegion currentFrame = lightning.getKeyFrame(animTime);
			spriteBatch.draw(currentFrame, getPosX(), getPosY());
		}
	}

	@Override
	public void dispose() {
		disposing = true;
	}
}
