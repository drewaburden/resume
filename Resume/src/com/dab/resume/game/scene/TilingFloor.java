/********************************************************************************************************
 * Project:     Résumé
 * File:        TilingFloor.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles the drawing and tiling for the floor in a Scene
 ********************************************************************************************************/

package com.dab.resume.game.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TilingFloor extends Sprite {
	private Sprite mainTile, continuationTile;

	private float offsetFromLastRetile = 0.0f;

	public TilingFloor(Texture mainTile) {
		this.mainTile = new Sprite(mainTile);
		this.continuationTile = new Sprite(mainTile);
	}

	public void update() {
		// If the camera is about to go beyond the main tile, put the continuation tile after it
		if (offsetFromLastRetile > mainTile.getX() + mainTile.getWidth()/2.0f) {
			continuationTile.setPosition(mainTile.getX() + mainTile.getWidth(), mainTile.getY());
		}
		else if (offsetFromLastRetile < mainTile.getX() + mainTile.getWidth()/2.0f) {
			continuationTile.setPosition(mainTile.getX() - mainTile.getWidth(), mainTile.getY());
		}

		// Switch the references of the main tile and the continuation tile if the camera is
		// showing more of the continuation tile
		if (offsetFromLastRetile > continuationTile.getX() + continuationTile.getWidth()/2.0f
				|| offsetFromLastRetile < mainTile.getX() - mainTile.getWidth()/2.0f) {
			Sprite tmp = mainTile;
			mainTile = continuationTile;
			continuationTile = tmp;
		}
	}
	public void addOffset(float offset) { this.offsetFromLastRetile += offset; }

	@Override
	public void setPosition(float posX, float posY) {
		offsetFromLastRetile = 0.0f;
		mainTile.setPosition(posX, posY);
		continuationTile.setPosition(posX + mainTile.getWidth(), posY);
	}
	@Override
	public void translate(float amountX, float amountY) {
		offsetFromLastRetile += amountX;
		setPosition(mainTile.getX() + amountX, mainTile.getY() + amountY);
		update();
	}
	public float getTileWidth() { return mainTile.getWidth(); }
	public float getTileHeight() { return mainTile.getHeight(); }

	@Override
	public void draw(Batch batch) {
		mainTile.draw(batch);
		continuationTile.draw(batch);
	}
}
