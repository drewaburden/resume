/********************************************************************************************************
 * Project:     Résumé
 * File:        CollidableObject.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines an object that has a bounding box and that can be notified of collision events
 *      with that bounding box
 ********************************************************************************************************/

package com.dab.resume.game.collision;

import com.dab.resume.events.Observer;

public interface CollidableObject extends Observer {
	public BoundingBox getBoundingBox();
}
