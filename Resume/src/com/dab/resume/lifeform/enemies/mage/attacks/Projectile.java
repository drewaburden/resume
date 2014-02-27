/********************************************************************************************************
 * Project:     Résumé
 * File:        Projectile.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines an abstract projectile that can collide with other objects and damage them
 *      and move across the screen.
 ********************************************************************************************************/

package com.dab.resume.lifeform.enemies.mage.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.dab.resume.collision.BoundingBox;

public abstract class Projectile implements Disposable {
	private final int attackPower;
	private float posX, posY;
	private final float velocityX;
	private final BoundingBox boundingBox;

	public Projectile(int attackPower, BoundingBox boundingBox, float posX, float posY, float velocityX) {
		this.attackPower = attackPower;
		this.boundingBox = boundingBox;
		this.posX = posX;
		this.posY = posY;
		this.velocityX = velocityX;
	}

	public BoundingBox getBoundingBox() { return boundingBox; }
	public int getAttackPower() { return attackPower; }

	public float getPosX() { return posX; }
	public float getPosY() { return posY; }
	public void translate(float offsetX, float offsetY) { setPosition(posX+offsetX, posY+offsetY); }
	public void setPosition(float newPosX, float newPosY) {
		// Calculate new bounding box position based on the offset from the actual position
		float boundingBoxOffsetX = getBoundingBox().getLeft() - getPosX();
		float boundingBoxOffsetY = getPosY() - getBoundingBox().getBottom();
		// Set the new position and bounding box position
		this.posX = newPosX;
		this.posY = newPosY;
		getBoundingBox().setX(boundingBoxOffsetX + newPosX);
		getBoundingBox().setY(boundingBoxOffsetY + newPosY);
	}

	protected void update(float delta) {
		this.translate(velocityX*delta, 0.0f);
	}
	public abstract void draw(SpriteBatch spriteBatch);
}
