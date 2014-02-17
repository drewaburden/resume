/********************************************************************************************************
 * Project:     Résumé
 * File:        ParallaxLayer.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines a layer comprised of sprites with a depth variable to be used in the
 *      parallax compositing.
 ********************************************************************************************************/

package com.dab.resume.scene;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class ParallaxLayer {
	private float depth;
	private ArrayList<Sprite> sprites;

	public ParallaxLayer(float depth) {
		this.depth = depth;
		sprites = new ArrayList<Sprite>();
	}

	public void addSprite(Sprite sprite) { sprites.add(sprite); }
	public float getDepth() { return depth; }

	public void translate(float offsetX, float offsetY) {
		for (Sprite sprite : sprites) {
			sprite.translate(offsetX, offsetY);
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		for (Sprite sprite : sprites) {
			sprite.draw(spriteBatch);
		}
	}
}
