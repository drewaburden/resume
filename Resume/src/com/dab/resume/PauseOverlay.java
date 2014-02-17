/********************************************************************************************************
 * Project:     Résumé
 * File:        PauseOverlay.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles drawing the overlay of the game screen when the GameState is set to paused
 ********************************************************************************************************/

package com.dab.resume;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PauseOverlay {
	private BitmapFont font;

	public PauseOverlay(BitmapFont font) {
		this. font = font;
	}

	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.end();

		// Black overlay
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0.0f, 0.0f, 0.0f, 0.25f);
		shapeRenderer.rect(0.0f - TerminalGame.VIRTUAL_WIDTH, 0.0f - TerminalGame.VIRTUAL_HEIGHT,
				TerminalGame.VIRTUAL_WIDTH*2.0f, TerminalGame.VIRTUAL_HEIGHT*2.0f);
		shapeRenderer.end();

		// Paused text
		spriteBatch.begin();
		font.setColor(0.15f, 0.85f, 0.4f, 1.0f);
		String pauseText = "PAUSED";
		BitmapFont.TextBounds textBounds = font.getBounds(pauseText);
		font.draw(spriteBatch, pauseText, 0.0f - textBounds.width/2.0f,
				0.0f + textBounds.height/2.0f + 30.0f);

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
}
