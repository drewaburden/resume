/********************************************************************************************************
 * Project:     Résumé
 * File:        Observer.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines an object that is capable of receiving event notifications from an Observable
 ********************************************************************************************************/

package com.dab.resume.events;

public interface Observer {
	/***
	 * return  true - event was handled. Don't notify any further observers.
	 *         false - event wasn't handled. Continue notifying.
	 ***/
	public boolean eventTriggered(Object data);
}
