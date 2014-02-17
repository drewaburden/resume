/********************************************************************************************************
 * Project:     Résumé
 * File:        CollisionEvent.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines the different types of collisions so that the CollidableObject may handle
 *      the collision event properly.
 ********************************************************************************************************/

package com.dab.resume.collision;

public enum CollisionEvent {
	PLAYER, FRIENDLY, ENEMY, ATTACK, BLOCKING, UNDEF
}
