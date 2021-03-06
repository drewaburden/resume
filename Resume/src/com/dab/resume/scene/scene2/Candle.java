/********************************************************************************************************
 * Project:     Résumé
 * File:        Candle.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles load, initialization, animation, and drawing of the candle holders
 *      and candle flames in Scene 2
 ********************************************************************************************************/

package com.dab.resume.scene.scene2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.dab.resume.GameState;
import com.dab.resume.assets.Assets;

import static com.dab.resume.GameState.State.PAUSED;

public class Candle {
	private final float ANIM_RATE = 0.15f;

	private float animTime = 0.0f; // How long an animation has been playing. Determines which frame to display.
	private Sprite holder;
	private Animation flame;
	private float posX = 0.0f, posY = 0.0f;

	public Candle() { }
	public void initAssets() {
		TextureAtlas atlas = Assets.getInstance().get("spritesheets/scene2.pack");
		holder = atlas.createSprite("candle");
		flame = Assets.getInstance().getAnimation(atlas.findRegion("candle-flame"), 4, ANIM_RATE*1.5f);
		flame.setPlayMode(Animation.LOOP);

		setPosition(posX, posY);
	}

	public void setPosition(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
		holder.setPosition(posX - holder.getWidth()/2.0f + 2.0f, posY - 14.0f);
	}
	public void translate(float offsetX, float offsetY) {
		posX += offsetX;
		posY += offsetY;
		holder.translate(offsetX, offsetY);
	}

	public void draw(SpriteBatch spriteBatch) {
		if (!GameState.isGameStateSet(PAUSED)) {
			float delta = Gdx.graphics.getDeltaTime();
			animTime += delta;
		}
		holder.draw(spriteBatch);

		// Determine which frame to draw
		TextureRegion currentFrame = flame.getKeyFrame(animTime);
		// Draw the frame (and align it correctly)
		spriteBatch.draw(currentFrame, posX, posY);
	}
}
