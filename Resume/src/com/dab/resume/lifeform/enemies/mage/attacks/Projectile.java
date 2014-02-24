/********************************************************************************************************
 * Project:     Résumé
 * File:        Projectile.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *
 ********************************************************************************************************/

package com.dab.resume.lifeform.enemies.mage.attacks;

public abstract class Projectile {
	protected int attackPower;
	protected float posX, posY;

	public Projectile(int attackPower, float posX, float posY) {
		this.attackPower = attackPower;
		this.posX = posX;
		this.posY = posY;
	}

	public int getAttackPower() { return attackPower; }
	public float getPosX() { return posX; }
	public float getPosY() { return posY; }

	public void setPosition(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}
	public void translate(float offsetX, float offsetY) {
		posX += offsetX;
		posY += offsetY;
	}
}
