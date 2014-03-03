/********************************************************************************************************
 * Project:     Résumé
 * File:        OldWoman.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines the simple OldWoman NPC
 ********************************************************************************************************/

package com.dab.resume.lifeform.friendlies.oldwoman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.debug.Log;
import com.dab.resume.lifeform.*;
import com.dab.resume.lifeform.enemies.mage.MageAnimationFactory;

import static com.dab.resume.lifeform.AnimationFactory.AnimationType.IDLE;

public class OldWoman extends Lifeform {
	private final OldWomanAnimationFactory oldWomanAnimationFactory = new OldWomanAnimationFactory();

	public OldWoman(float posX) {
		super(LifeformType.OLDWOMAN);
		direction = Direction.LEFT;
		lifeformMovement.setPosX(posX);
		animationFactory.setOldWomanAnimationFactory(oldWomanAnimationFactory);
	}

	public void initAssets() {
		Log.log();
		oldWomanAnimationFactory.initAssets();
		animationManager = new LifeformAnimationManager(animationFactory.getAnimation(this.lifeformType, IDLE));
	}

	@Override public void hurt(int damage, Direction damagedSide) { /* OldWoman can't be hurt */ }
	@Override public void move(Direction direction) { /* OldWoman can't move */ }

	public void draw(SpriteBatch spriteBatch) {
		// Draw the lifeform's animation
		animationManager.draw(spriteBatch, lifeformMovement.getPosX(), lifeformMovement.getPosY());
	}
}
