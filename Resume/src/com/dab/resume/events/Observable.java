/********************************************************************************************************
 * Project:     Résumé
 * File:        Observable.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *
 ********************************************************************************************************/

package com.dab.resume.events;

import java.util.ArrayList;

public class Observable {
	ArrayList<Observer> observers;

	public Observable() {
		observers = new ArrayList<Observer>();
	}

	public void registerObserver(Observer observer) { observers.add(observer); }
	public void notifyObservers(Object data) {
		for (Observer observer : observers) {
			observer.eventTriggered(data);
		}
	}
}
