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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.GameState;
import com.dab.resume.TerminalGame;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.SoundFX;
import com.dab.resume.events.Observer;
import com.dab.resume.input.InputEvent;

import java.util.ArrayList;

public class MainMenu implements Observer {
	private BitmapFont font;
	private Sprite overlay, title;
	private Sound acceptSound;

	private int selectedItem = 0;
	private ArrayList<String> items;
	private final float itemsStartY = -5.0f;
	private final float itemsIncrementY = -20.0f;

	public MainMenu(BitmapFont font) {
		this. font = font;
		Assets.getInstance().load("colors/overlay.png", Texture.class);
		Assets.getInstance().load("game/hud/title.png", Texture.class);
		Assets.getInstance().load("game/sounds/dialog-accept.ogg", Sound.class);

		items = new ArrayList<String>();
		items.add("START GAME");
		items.add("CONTROLS");
		items.add("CREDITS");
		items.add("EXIT GAME");
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

		acceptSound = Assets.getInstance().get("game/sounds/dialog-accept.ogg");
	}

	private void acceptPressed() {
		acceptSound.play(SoundFX.VOLUME_MODIFIER);
		switch (selectedItem) {
			case 0: break;
			case 1: break;
			case 2: break;
			case 3:
				Gdx.app.exit();
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		// Overlay
		overlay.draw(spriteBatch);

		// Title/logo
		title.draw(spriteBatch);

		// Avoid clutter by hiding the menu items if the pause overlay is showing.
		if (!GameState.isGameStateSet(GameState.State.PAUSED)) {
			float itemY = itemsStartY;
			for (int item = 0; item < items.size(); ++item) {
				font.setColor(0.15f, 0.85f, 0.4f, 1.0f); // Unselected color
				BitmapFont.TextBounds textBounds;

				// If this is the selected item, change the color and draw the selection indicator
				if (selectedItem == item) {
					font.setColor(0.18f, 1.0f, 0.51f, 1.0f); // Selected color
					String menuItem = "~                ~";
					textBounds = font.getBounds(menuItem);
					font.draw(spriteBatch, menuItem, 0.0f - textBounds.width / 2.0f,
							0.0f + textBounds.height / 2.0f + itemY);
				}

				// Draw the item
				String itemText = items.get(item);
				textBounds = font.getBounds(itemText);
				font.draw(spriteBatch, itemText, 0.0f - textBounds.width / 2.0f,
						0.0f + textBounds.height / 2.0f + itemY);

				itemY += itemsIncrementY; // Update Y position
			}
		}
	}

	@Override
	public void eventTriggered(Object data) {
		// If the event was an input event and we aren't paused.
		// Pausing in the main menu can only happen if the window loses focus, and in that
		// case, we will want to shift input focus to the pause overlay, not the main menu.
		if (data instanceof InputEvent
				&& !GameState.isGameStateSet(GameState.State.PAUSED)) {
			switch ((InputEvent) data) {
				case PRESS_DOWN:
					selectedItem++;
					// If they pressed down when they were at the bottom, put them at the top
					if (selectedItem >= items.size()) {
						selectedItem = 0;
					}
					break;
				case PRESS_UP:
					selectedItem--;
					// If they pressed up when they were at the top, put them at the bottom
					if (selectedItem < 0) {
						selectedItem = items.size()-1;
					}
					break;
				case PRESS_ACCEPT:
					acceptPressed();
					break;
			}
		}
	}
}
