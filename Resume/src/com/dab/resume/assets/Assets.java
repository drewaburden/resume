/********************************************************************************************************
 * Project:     Resume
 * File:        Assets.java
 * Authors:     Drew Burden
 * 
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Singleton used to manage assets. Loaded assets can be accessed globally and persist until the
 *      application exits or are explicitly unloaded.
 ********************************************************************************************************/

package com.dab.resume.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Assets extends AssetManager {
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

	public Animation getAnimation(String filename, int numFrames, float frameDuration) {
		Texture texture = Assets.getInstance().get(filename);
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / numFrames, texture.getHeight());
		TextureRegion[] frames = new TextureRegion[numFrames];
		int index = 0;
		for (TextureRegion[] rows : tmp) {
			for (TextureRegion cols : rows) {
				frames[index] = cols;
				index++;
			}
		}
		return new Animation(frameDuration, frames);
	}
}