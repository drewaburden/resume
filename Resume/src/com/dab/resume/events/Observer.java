/********************************************************************************************************
 * Project:     Résumé
 * File:        Observer.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *
 ********************************************************************************************************/

package com.dab.resume.events;

public interface Observer {
	public void eventTriggered(Object data);
}
