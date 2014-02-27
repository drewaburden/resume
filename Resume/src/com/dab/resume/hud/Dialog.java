/********************************************************************************************************
 * Project:     Résumé
 * File:        Dialog.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines a class responsible for loading and displaying the graphics required to
 *      create a variably sized dialog with variable text.
 ********************************************************************************************************/

package com.dab.resume.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.dab.resume.GameState;
import com.dab.resume.assets.Assets;
import com.dab.resume.audio.SoundFX;

import static com.dab.resume.GameState.State.PAUSED;

public class Dialog {
	public static int NUM_DIALOGS_SHOWING = 0;
	private final float ANIM_RATE = 0.15f;
	private float animTime = 0.0f; // How long an animation has been playing. Determines which frame to display.

	private float posX, posY, width, height;
	private Sprite background, deco_top, deco_bottom;
	private Sprite edgeLeft, edgeTop, edgeRight, edgeBottom;
	private Sprite cornerTopLeft, cornerTopRight, cornerBottomRight, cornerBottomLeft;
	private Animation advanceArrow;
	private Sound displayCharacterSound, acceptSound, skipSound;
	private final float characterDelay = 0.05f; // Time between displaying a character
	private final float sentenceDelay = 0.5f; // Time to wait between sentences
	private float deltaCharacterDisplay = 0.0f; // Time since last character was displayed.
	private BitmapFont font;
	private String text, speaker, displayText;
	private boolean showing = false;

	public Dialog(String speaker, String text, float posX, float posY, float width, float height) {
		if (width < 150.0f || height < 100.0f) {
			throw new IllegalArgumentException("Dialog width and height must be greater than 200.0f and 64.0f respectively.");
		}

		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;

		Assets.getInstance().load("game/hud/dialog/background.png", Texture.class);
		Assets.getInstance().load("game/hud/dialog/corner.png", Texture.class);
		Assets.getInstance().load("game/hud/dialog/deco_bottom.png", Texture.class);
		Assets.getInstance().load("game/hud/dialog/deco_top.png", Texture.class);
		Assets.getInstance().load("game/hud/dialog/edge.png", Texture.class);
		Assets.getInstance().load("game/hud/dialog/advance-arrow.png", Texture.class);
		Assets.getInstance().load("game/sounds/dialog-display-character.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/dialog-accept.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/dialog-skip.ogg", Sound.class);

		// Set text
		this.speaker = speaker.toUpperCase();
		this.displayText = "";
		this.text = "";
		if (speaker != "") {
			this.displayText = speaker.toUpperCase() + ":\n\n";
			this.text = displayText;
		}
		this.text += text.toUpperCase();
	}

	public void initAssets() {
		/**************
		 * Background
		 **************/
		Texture texture = Assets.getInstance().get("game/hud/dialog/background.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		background = new Sprite(texture);
		background.setPosition(posX+2.0f, posY+2.0f); // We add and subtract here because the corners have a transparent section
		background.setSize(width-4.0f, height-4.0f);

		/**************
		 * Corners
		 **************/
		texture = Assets.getInstance().get("game/hud/dialog/corner.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		cornerBottomLeft = new Sprite(texture);
		cornerBottomLeft.setPosition(posX, posY);

		texture = Assets.getInstance().get("game/hud/dialog/corner.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		cornerTopLeft = new Sprite(texture);
		cornerTopLeft.setOrigin(0.0f, 0.0f);
		cornerTopLeft.rotate(-90.0f);
		cornerTopLeft.setPosition(posX, posY+height);

		texture = Assets.getInstance().get("game/hud/dialog/corner.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		cornerTopRight = new Sprite(texture);
		cornerTopRight.setOrigin(0.0f, 0.0f);
		cornerTopRight.rotate(-180.0f);
		cornerTopRight.setPosition(posX+width, posY+height);

		texture = Assets.getInstance().get("game/hud/dialog/corner.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		cornerBottomRight = new Sprite(texture);
		cornerBottomRight.setOrigin(0.0f, 0.0f);
		cornerBottomRight.rotate(90.0f);
		cornerBottomRight.setPosition(posX+width, posY);


		/**************
		 * Edges
		 **************/
		texture = Assets.getInstance().get("game/hud/dialog/edge.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		edgeLeft = new Sprite(texture);
		edgeLeft.setPosition(posX, posY+cornerBottomLeft.getHeight());
		edgeLeft.setSize(edgeLeft.getWidth(), height-cornerBottomLeft.getHeight()*2.0f);

		texture = Assets.getInstance().get("game/hud/dialog/edge.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		edgeTop = new Sprite(texture);
		edgeTop.setPosition(posX+cornerTopLeft.getWidth(), posY+height);
		edgeTop.setSize(edgeTop.getWidth(), width-cornerTopLeft.getWidth()*2.0f);
		edgeTop.setOrigin(0.0f, 0.0f);
		edgeTop.rotate(-90.0f);

		texture = Assets.getInstance().get("game/hud/dialog/edge.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		edgeRight = new Sprite(texture);
		edgeRight.setPosition(posX+width, posY+height-cornerTopRight.getHeight());
		edgeRight.setSize(edgeLeft.getWidth(), height-cornerTopRight.getHeight() * 2.0f);
		edgeRight.setOrigin(0.0f, 0.0f);
		edgeRight.rotate(-180.0f);
		
		texture = Assets.getInstance().get("game/hud/dialog/edge.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		edgeBottom = new Sprite(texture);
		edgeBottom.setPosition(posX+width-cornerBottomRight.getWidth(), posY);
		edgeBottom.setSize(edgeTop.getWidth(), width-cornerBottomRight.getWidth()*2.0f);
		edgeBottom.setOrigin(0.0f, 0.0f);
		edgeBottom.rotate(90.0f);

		/**************
		 * Deco
		 **************/
		texture = Assets.getInstance().get("game/hud/dialog/deco_top.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		deco_top = new Sprite(texture);
		deco_top.setPosition(posX + width/2.0f - deco_top.getWidth()/2.0f, posY+height - deco_top.getHeight() - edgeTop.getWidth()); // Edgetop's width because it's relative to the texture, not the sprite, and the sprite has been rotated 90 degrees.

		texture = Assets.getInstance().get("game/hud/dialog/deco_bottom.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		deco_bottom = new Sprite(texture);
		deco_bottom.setPosition(posX + width/2.0f - deco_bottom.getWidth()/2.0f, posY + edgeBottom.getWidth()); // Edgebottom's width because it's relative to the texture, not the sprite, and the sprite has been rotated 90 degrees.

		/**************
		 * Advance arrow animation
		 **************/
		texture = Assets.getInstance().get("game/hud/dialog/advance-arrow.png");
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight());
		TextureRegion[] frames = new TextureRegion[4];
		int index = 0;
		for (TextureRegion[] rows : tmp) {
			for (TextureRegion cols : rows) {
				frames[index] = cols;
				index++;
			}
		}
		advanceArrow = new Animation(ANIM_RATE, frames);
		advanceArrow.setPlayMode(Animation.LOOP_PINGPONG);

		/**************
		 * Font
		 **************/
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setScale(1.0f);

		/**************
		 * Sounds
		 **************/
		displayCharacterSound = Assets.getInstance().get("game/sounds/dialog-display-character.ogg");
		acceptSound = Assets.getInstance().get("game/sounds/dialog-accept.ogg");
		skipSound = Assets.getInstance().get("game/sounds/dialog-skip.ogg");
	}

	public void show() {
		NUM_DIALOGS_SHOWING++;
		showing = true;
	}
	public void hide() {
		NUM_DIALOGS_SHOWING = Math.max(0, NUM_DIALOGS_SHOWING-1);
		showing = false;
		displayText = speaker + ":\n\n";
		deltaCharacterDisplay = 0.0f;
	}
	public boolean isShowing() { return showing; }

	public void accept() {
		// If we displayed all the characters, the dialog should close
		if (displayText.length() == text.length()) {
			hide();
			acceptSound.play(SoundFX.VOLUME_MODIFIER);
		}
		// Otherwise advance the text to the end
		else {
			displayText = text;
			displayCharacterSound.stop();
			skipSound.play(SoundFX.VOLUME_MODIFIER);
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		if (showing) {
			background.draw(spriteBatch);

			edgeLeft.draw(spriteBatch);
			edgeTop.draw(spriteBatch);
			edgeRight.draw(spriteBatch);
			edgeBottom.draw(spriteBatch);

			cornerBottomLeft.draw(spriteBatch);
			cornerTopLeft.draw(spriteBatch);
			cornerTopRight.draw(spriteBatch);
			cornerBottomRight.draw(spriteBatch);

			deco_top.draw(spriteBatch);
			deco_bottom.draw(spriteBatch);

			// If the game isn't paused and we haven't displayed all of the text yet
			if (!GameState.isGameStateSet(PAUSED)
					&& displayText.length() != text.length()) {
				deltaCharacterDisplay += Gdx.graphics.getDeltaTime();

				// If we've waited long enough between characters
				if (deltaCharacterDisplay >= characterDelay) {
					deltaCharacterDisplay -= characterDelay;

					char nextChar = text.charAt(displayText.length());
					displayText += nextChar;

					// Only play a sound if the character wasn't whitespace.
					if (font.containsCharacter(nextChar) && nextChar != '\n' && nextChar != ' ') {
						displayCharacterSound.stop();
						displayCharacterSound.play(SoundFX.VOLUME_MODIFIER);
					}

					// If the character was a period, start waiting before the next sentence.
					if (nextChar == '.') {
						deltaCharacterDisplay -= sentenceDelay;
					}
				}
			}
			// Draw the advance arrow when we've displayed all the text
			else {
				animTime += Gdx.graphics.getDeltaTime();
				TextureRegion currentFrame = advanceArrow.getKeyFrame(animTime);
				spriteBatch.draw(currentFrame, posX+width-30.0f, posY+20.0f);
			}

			font.setColor(0.15f, 0.85f, 0.4f, 1.0f);
			font.drawWrapped(spriteBatch, displayText, posX + edgeLeft.getWidth()*2.0f,
					posY+height - edgeTop.getWidth()*2.0f - deco_top.getHeight(), width - edgeLeft.getWidth()*4.0f);
		}
	}
}
