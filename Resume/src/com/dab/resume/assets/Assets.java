/********************************************************************************************************
 * Project:     Resume
 * File:        Assets.java
 * Authors:     Drew Burden
 * 
 * Copyright © 2013 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Singleton used to manage assets. Loaded assets can be accessed globally and persist until the
 *      application exits or are explicitly unloaded.
 ********************************************************************************************************/

package com.dab.resume.assets;

import com.badlogic.gdx.assets.AssetManager;

public class Assets extends AssetManager {
	private static Assets instance;

	private Assets() { } // This class doesn't need initialization. It's a singleton.

	public static Assets getInstance() {
		if (instance == null) instance = new Assets();
		return instance;
	}

	@Override
	public synchronized void dispose() {
		super.dispose();
		instance = null;
	}
}