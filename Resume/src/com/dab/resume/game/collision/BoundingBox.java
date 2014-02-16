/********************************************************************************************************
 * Project:     Résumé
 * File:        BoundingBox.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines an extension of the Rectangle class to be used specifically for collision detection
 ********************************************************************************************************/

package com.dab.resume.game.collision;

import com.badlogic.gdx.math.Rectangle;

public class BoundingBox extends Rectangle {
	CollisionEvent type;

	public BoundingBox(float x, float y, float width, float height, CollisionEvent type) {
		super(x, y, width, height);
		this.type = type;
	}

	public float getRight() { return this.getX() + this.getWidth(); }
	public float getLeft() { return this.getX(); }
	public float getTop() { return this.getY() + this.getHeight(); }
	public float getBottom() { return this.getY(); }

	public void translate(float offsetX, float offsetY) {
		this.setPosition(this.getX() + offsetX, this.getY() + offsetY);
	}

	public CollisionEvent getCollisionType() { return type; }
}
