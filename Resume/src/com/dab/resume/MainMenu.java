/********************************************************************************************************
 * Project:     Résumé
 * File:        MainMenu.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing the main menu when the GameState is set to MAINMENU
 ********************************************************************************************************/

package com.dab.resume;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.assets.Assets;

public class MainMenu {
	private BitmapFont font;
	private Sprite overlay, title;

	public MainMenu(BitmapFont font) {
		this. font = font;
		Assets.getInstance().load("colors/overlay.png", Texture.class);
		Assets.getInstance().load("game/hud/title.png", Texture.class);
	}

	public void initAssets() {
		Texture texture = Assets.getInstance().get("colors/overlay.png");
		overlay = new Sprite(texture);
		overlay.setPosition(0.0f - TerminalGame.VIRTUAL_WIDTH/2.0f, 0.0f - TerminalGame.VIRTUAL_HEIGHT/2.0f);
		overlay.setSize(TerminalGame.VIRTUAL_WIDTH, TerminalGame.VIRTUAL_HEIGHT);
		overlay.setAlpha(0.25f);

		texture = Assets.getInstance().get("game/hud/title.png");
		title = new Sprite(texture);
		title.setPosition(0.0f - title.getWidth()/2.0f, 55.0f);
	}

	public void draw(SpriteBatch spriteBatch) {
		// Overlay
		overlay.draw(spriteBatch);

		// Title/logo
		title.draw(spriteBatch);

		// Text
		font.setColor(0.18f, 1.0f, 0.51f, 1.0f); // Selected color
		String menuItem = "~                ~";
		BitmapFont.TextBounds textBounds = font.getBounds(menuItem);
		font.draw(spriteBatch, menuItem, 0.0f - textBounds.width / 2.0f,
				0.0f + textBounds.height / 2.0f + -5.0f);

		menuItem = "START GAME";
		textBounds = font.getBounds(menuItem);
		font.draw(spriteBatch, menuItem, 0.0f - textBounds.width / 2.0f,
				0.0f + textBounds.height / 2.0f + -5.0f);

		font.setColor(0.15f, 0.85f, 0.4f, 1.0f); // Unselected color
		menuItem = "CONTROLS";
		textBounds = font.getBounds(menuItem);
		font.draw(spriteBatch, menuItem, 0.0f - textBounds.width / 2.0f,
				0.0f + textBounds.height / 2.0f + -25.0f);

		menuItem = "CREDITS";
		textBounds = font.getBounds(menuItem);
		font.draw(spriteBatch, menuItem, 0.0f - textBounds.width / 2.0f,
				0.0f + textBounds.height / 2.0f + -45.0f);

		menuItem = "EXIT GAME";
		textBounds = font.getBounds(menuItem);
		font.draw(spriteBatch, menuItem, 0.0f - textBounds.width / 2.0f,
				0.0f + textBounds.height / 2.0f + -65.0f);
	}
}
