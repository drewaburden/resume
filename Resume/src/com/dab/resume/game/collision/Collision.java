/********************************************************************************************************
 * Project:     Résumé
 * File:        Collision.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Checks for collision between registered objects
 ********************************************************************************************************/

package com.dab.resume.game.collision;

import java.util.ArrayList;

public class Collision {
	private static final ArrayList<CollidableObject> collidableObjects = new ArrayList<CollidableObject>();

	public static void registerCollidable(CollidableObject collidableObject) {
		collidableObjects.add(collidableObject);
	}
	public static void unregisterCollidable(CollidableObject collidableObject) {
		collidableObjects.remove(collidableObject);
	}

	public static void updateCollision() {
		for(CollidableObject object1 : collidableObjects) {
			for(CollidableObject object2 : collidableObjects) {
				if (object1 != object2) {
					// If the objects overlap, notify each with the other's collision type
					if (object1.getBoundingBox().overlaps(object2.getBoundingBox())) {
						object1.eventTriggered(object2.getBoundingBox().getCollisionType());
						object2.eventTriggered(object1.getBoundingBox().getCollisionType());
					}
				}
			}
		}
	}
}
