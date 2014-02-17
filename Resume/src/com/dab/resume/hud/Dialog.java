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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dab.resume.assets.Assets;

public class Dialog {
	public static int NUM_DIALOGS_SHOWING = 0;

	private float posX, posY, width, height;
	private Sprite background, deco_top, deco_bottom;
	private Sprite edgeLeft, edgeTop, edgeRight, edgeBottom;
	private Sprite cornerTopLeft, cornerTopRight, cornerBottomRight, cornerBottomLeft;
	private BitmapFont font;
	private String text, displayText;
	private boolean showing = false;

	public Dialog(float posX, float posY, float width, float height) {
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

		displayText = "dfjskdjfklsdjfjd;lskjfk;ljdsfgkljsdf;lgkjksldfjgkl;jsdfg;lkjsdklfgjlksdfjgl;ksdjfg;lkjdsf;gkjs;lkdfjgl;ksdjfg;lkjsdfglk\n\ndfjgjfdsgkhsdfkjghjskdflhgljksfhjlkghlkj";
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
		 * Font
		 **************/
		font = new BitmapFont(Gdx.files.internal("fonts/fixedsys.fnt"));
		font.setScale(1.0f);
	}

	public void show() {
		NUM_DIALOGS_SHOWING++;
		showing = true;
	}
	public void hide() {
		NUM_DIALOGS_SHOWING = Math.max(0, NUM_DIALOGS_SHOWING-1);
		showing = false;
	}
	public boolean isShowing() { return showing; }

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

			font.setColor(0.15f, 0.85f, 0.4f, 1.0f);
			font.drawWrapped(spriteBatch, displayText, posX + edgeLeft.getWidth()*2.0f,
					posY+height - edgeTop.getWidth()*2.0f - deco_top.getHeight(), width - edgeLeft.getWidth()*4.0f);
		}
	}
}
