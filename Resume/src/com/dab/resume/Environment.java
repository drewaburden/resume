/********************************************************************************************************
 * Project:     Résumé
 * File:        Environment.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing of the environmental assets not directly related to gameplay
 *      and any post processing effects
 ********************************************************************************************************/

package com.dab.resume;

import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.assets.Assets;

public class Environment {
	public static final float SCENE_WIDTH = TerminalGame.VIRTUAL_WIDTH, SCENE_HEIGHT = TerminalGame.VIRTUAL_HEIGHT;
	public static final float SCENE_X = 0.0f - SCENE_WIDTH /2.0f, SCENE_Y = 0.0f - SCENE_HEIGHT /2.0f;

	Sprite environment, colorgrade, monitor_glare, monitor_glow;

	public Environment() {
		// Load assets and generate mipmaps
		TextureLoader.TextureParameter texparams = new TextureLoader.TextureParameter();
		texparams.genMipMaps = true;
		Assets.getInstance().load("scene/environment.png", Texture.class, texparams);
		Assets.getInstance().load("colors/colorgrade.png", Texture.class, texparams);
		Assets.getInstance().load("monitor/glare.png", Texture.class, texparams);
		Assets.getInstance().load("monitor/glow.png", Texture.class, texparams);
	}

	public void initAssets() {
		// Fetch textures and create sprites
		Texture texture = Assets.getInstance().get("scene/environment.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		environment = new Sprite(texture);
		texture = Assets.getInstance().get("colors/colorgrade.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		colorgrade = new Sprite(texture);
		texture = Assets.getInstance().get("monitor/glare.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		monitor_glare = new Sprite(texture);
		texture = Assets.getInstance().get("monitor/glow.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		monitor_glow = new Sprite(texture);

		// Set sprite sizes and positions
		environment.setBounds(SCENE_X, SCENE_Y, SCENE_WIDTH, SCENE_HEIGHT);
		colorgrade.setBounds(SCENE_X, SCENE_Y, SCENE_WIDTH, SCENE_HEIGHT);
		monitor_glow.setBounds(SCENE_X, SCENE_Y, SCENE_WIDTH, SCENE_HEIGHT);
		monitor_glare.setBounds(SCENE_X, SCENE_Y, SCENE_WIDTH, SCENE_HEIGHT);

		// Set sprite alphas
		colorgrade.setAlpha(0.1f);
		monitor_glare.setAlpha(0.7f);
		monitor_glow.setAlpha(1.0f);
	}

	public void drawBackground(SpriteBatch spriteBatch) {
		environment.draw(spriteBatch);
		monitor_glare.draw(spriteBatch);
		monitor_glow.draw(spriteBatch);
	}
	public void drawColorGrade(SpriteBatch spriteBatch) {
		colorgrade.draw(spriteBatch);
	}
}
