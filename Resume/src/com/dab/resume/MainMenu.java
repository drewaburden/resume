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
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.SoundFX;
import com.dab.resume.events.Observer;
import com.dab.resume.hud.ControlsOverlay;
import com.dab.resume.hud.CreditsOverlay;
import com.dab.resume.hud.Fadeable;
import com.dab.resume.input.InputEvent;

import java.util.ArrayList;

public class MainMenu implements Observer {
	private BitmapFont font;
	private Fadeable underlay;
	private Sprite title;
	private Sound acceptSound;

	private ControlsOverlay controlsOverlay;
	private CreditsOverlay creditsOverlay;

	private int selectedItem = 0;
	private ArrayList<String> items;
	private final float itemsStartY = -5.0f;
	private final float itemsIncrementY = -20.0f;

	public MainMenu(BitmapFont font, Fadeable underlay, ControlsOverlay controlsOverlay, CreditsOverlay creditsOverlay) {
		this. font = font;
		this.underlay = underlay;
		this.controlsOverlay = controlsOverlay;
		this.creditsOverlay = creditsOverlay;
		Assets.getInstance().load("game/hud/title.png", Texture.class);
		Assets.getInstance().load("game/sounds/dialog-accept.ogg", Sound.class);

		items = new ArrayList<String>();
		items.add("START GAME");
		items.add("CONTROLS");
		items.add("CREDITS");
		items.add("EXIT GAME");
	}

	public void initAssets() {
		Texture texture = Assets.getInstance().get("game/hud/title.png");
		title = new Sprite(texture);
		title.setPosition(0.0f - title.getWidth()/2.0f, 55.0f);

		acceptSound = Assets.getInstance().get("game/sounds/dialog-accept.ogg");
	}

	private void acceptPressed() {
		acceptSound.play(SoundFX.VOLUME_MODIFIER);
		switch (selectedItem) {
			case 0:
				underlay.fadeToAlpha(0.0f, 0.025f);
				GameState.addGameState(GameState.State.PLAYING);
				GameState.removeGameState(GameState.State.MAINMENU);
				break;
			case 1:
				controlsOverlay.show();
				GameState.addGameState(GameState.State.CONTROLS);
				GameState.removeGameState(GameState.State.MAINMENU);
				break;
			case 2:
				underlay.fadeToAlpha(0.75f, 0.5f);
				creditsOverlay.show();
				GameState.addGameState(GameState.State.CREDITS);
				GameState.removeGameState(GameState.State.MAINMENU);
				break;
			case 3:
				// Wait for a fraction of a second and then exit
				// We wait for two reasons:
				// 1. It gives the acceptSound time to play
				// 2. It makes it feel less like the program is just crashing (it's not, but that's what it feels like)
				try { Thread.sleep(200); } catch (Exception e) {}
				Gdx.app.exit();
		}
	}

	public void draw(SpriteBatch spriteBatch) {
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
	public boolean eventTriggered(Object data) {
		// If the event was an input event and we aren't paused.
		// Pausing in the main menu can only happen if the window loses focus, and in that
		// case, we will want to shift input focus to the pause overlay, not the main menu.
		if (data instanceof InputEvent
				&& GameState.isGameStateSet(GameState.State.MAINMENU)
				&& !GameState.isGameStateSet(GameState.State.PAUSED)) {
			switch ((InputEvent) data) {
				case PRESS_DOWN:
					selectedItem++;
					// If they pressed down when they were at the bottom, put them at the top
					if (selectedItem >= items.size()) {
						selectedItem = 0;
					}
					return true;
				case PRESS_UP:
					selectedItem--;
					// If they pressed up when they were at the top, put them at the bottom
					if (selectedItem < 0) {
						selectedItem = items.size()-1;
					}
					return true;
				case PRESS_ACCEPT:
					acceptPressed();
					return true;
			}
		}
		return false;
	}
}
