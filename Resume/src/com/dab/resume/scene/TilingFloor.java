/********************************************************************************************************
 * Project:     Résumé
 * File:        TilingFloor.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles the drawing and tiling for a horizontal floor in a Scene
 ********************************************************************************************************/

package com.dab.resume.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dab.resume.debug.Log;

import java.util.ArrayList;
import java.util.LinkedList;

public class TilingFloor extends Sprite {
	private LinkedList<Sprite> tiles;
	private float offsetSinceLastRetile = 0.0f;

	public TilingFloor(Texture mainTile, int numTilesOnScreen) {
		if (numTilesOnScreen < 2) {
			throw new IllegalArgumentException("You must have at least 2 tiles");
		}

		// Create all the tiles
		tiles = new LinkedList<Sprite>();
		Sprite tile = new Sprite(mainTile);
		tiles.add(tile);
		for (int tileNumber = 1; tileNumber < numTilesOnScreen; ++tileNumber) {
			tile = new Sprite(mainTile);
			tiles.addLast(tile);
		}
		setPosition(tiles.getFirst().getX(), tiles.getFirst().getY()); // Initialize all the tiles positions relative to the first tile
		offsetSinceLastRetile = 0.0f;
	}

	public void update() {
		Sprite centerTile = tiles.get(tiles.size()/2 + 1);

		// If the camera has advanced to the right of the center tile
		//if (camera.position.x > centerTile.getX()+centerTile.getWidth()) {
		if (offsetSinceLastRetile > getTileWidth()) {
			Log.log();
			// Take the first tile and put it after the last tile
			// (both its position and its index in the list)
			Sprite firstTile = tiles.pollFirst();
			Sprite lastTile = tiles.getLast();
			firstTile.setX(lastTile.getX()+lastTile.getWidth());
			tiles.addLast(firstTile);
			offsetSinceLastRetile = 0.0f;
		}
		// If the camera has advanced to the left of the center tile
		else if (offsetSinceLastRetile < -getTileWidth()) {
			// Take the last tile and put it before the first tile
			// (both its position and its index in the list)
			Sprite lastTile = tiles.pollLast();
			Sprite firstTile = tiles.getFirst();
			lastTile.setX(firstTile.getX() - firstTile.getWidth());
			tiles.add(0, lastTile);
			offsetSinceLastRetile = 0.0f;
		}
	}
	public void addOffset(float offsetX) {
		offsetSinceLastRetile += offsetX;
	}

	@Override
	public void setPosition(float posX, float posY) {
		offsetSinceLastRetile -= (posX - tiles.getFirst().getX());
		tiles.getFirst().setPosition(posX, posY);
		for (int tileIndex = 1; tileIndex < tiles.size(); ++tileIndex) {
			Sprite prevTile = tiles.get(tileIndex-1);
			float calculatedX = prevTile.getX() + prevTile.getWidth();
			tiles.get(tileIndex).setPosition(calculatedX, posY);
		}
	}
	@Override
	public void translate(float amountX, float amountY) {
		setPosition(tiles.getFirst().getX() + amountX, tiles.getFirst().getY() + amountY);
		//update();
	}
	public float getTileWidth() { return tiles.getFirst().getWidth(); }
	public float getTileHeight() { return tiles.getFirst().getHeight(); }

	@Override
	public void setAlpha(float alpha) {
		for (Sprite tile : tiles) {
			tile.setAlpha(alpha);
		}
	}

	@Override
	public void draw(Batch batch) {
		for (Sprite tile : tiles) {
			tile.draw(batch);
		}
	}
}
