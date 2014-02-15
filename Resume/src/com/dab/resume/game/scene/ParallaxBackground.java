/********************************************************************************************************
 * Project:     Résumé
 * File:        ParallaxBackground.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing, compositing, and translation of the parallax for a Scene
 ********************************************************************************************************/

package com.dab.resume.screens.game.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class ParallaxBackground {
	private ArrayList<ParallaxLayer> layers;

	public ParallaxBackground() {
		layers = new ArrayList<ParallaxLayer>();
	}

	public void addLayer(ParallaxLayer layer) {
		layers.add(layer);
	}

	public void translate(float offsetX, float offsetY) {
		for (ParallaxLayer layer : layers) {
			// Negative depth (background) moves slower
			// Positive depth (foreground) moves faster
			// 0.0f depth means the layer is static and will never translate (relative to the camera)

			float depth = layer.getDepth();
			if (depth < 0.0f) {
				depth = 1.0f/depth;
			}
			layer.translate(offsetX * depth, offsetY * depth);
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		for (ParallaxLayer layer : layers) {
			layer.draw(spriteBatch);
		}
	}
}
