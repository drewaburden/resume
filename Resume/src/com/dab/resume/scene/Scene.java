/********************************************************************************************************
 * Project:     Résumé
 * File:        Scene.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Needs refactoring. Will update description later.
 ********************************************************************************************************/

package com.dab.resume.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.dab.resume.assets.Assets;
import com.dab.resume.debug.DebugFlags;
import com.dab.resume.debug.Log;
import com.dab.resume.events.Observer;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.hud.Dialog;
import com.dab.resume.input.InputEvent;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.enemies.mage.Mage;
import com.dab.resume.lifeform.player.Player;

import static com.dab.resume.collision.CollisionEvent.BLOCKING;

public class Scene implements Observer {
	// The ultimate boundaries of the scene where the player cannot walk beyond
	private BoundingBox playerBounds = new BoundingBox(-200.0f, -1000.0f, 2000.0f, 2000.0f, BLOCKING);
	// The bounds that the camera cannot pan beyond.
	private BoundingBox cameraBounds = new BoundingBox(0.0f, -1000.0f, 1675.0f, 2000.0f, BLOCKING);

	private OrthographicCamera camera, staticCamera; // Static camera is not for panning
	private CameraPanner cameraPanner;
	private Player player;
	private Mage mage;
	private TilingFloor floor, back_grass, back_grass2;
	private ParallaxBackground background;
	private Dialog dialog1;

	public Scene(OrthographicCamera camera, Player player) {
		this.camera = camera;
		this.player = player;
		staticCamera = new OrthographicCamera(camera.viewportWidth, camera.viewportHeight);
		cameraPanner = new CameraPanner(camera, player, playerBounds, cameraBounds);
		mage = new Mage();
		float dialogWidth = 250.0f, dialogHeight = 176.0f;
		dialog1 = new Dialog(0.0f - dialogWidth/2.0f, 25.0f - dialogHeight/2.0f, dialogWidth, dialogHeight);
		Assets.getInstance().load("game/environments/scene1-floor.png", Texture.class);
		Assets.getInstance().load("game/environments/scene1-backgrass.png", Texture.class);
		Assets.getInstance().load("game/environments/scene1-backgrass2.png", Texture.class);
		Assets.getInstance().load("game/environments/scene1-fog.png", Texture.class);
		Assets.getInstance().load("game/environments/scene1-mountain-back.png", Texture.class);
		Assets.getInstance().load("game/environments/scene1-mountain-mid.png", Texture.class);
		Assets.getInstance().load("game/environments/scene1-mountain-front.png", Texture.class);
	}

	public void initAssets() {
		Log.log();
		dialog1.initAssets();

		mage.initAssets();

		Texture texture = Assets.getInstance().get("game/environments/scene1-floor.png");
		floor = new TilingFloor(texture);
		floor.setPosition(camera.position.x - floor.getTileWidth()/2.0f, camera.position.y - floor.getTileHeight()/2.0f - 90.0f);

		background = new ParallaxBackground();

		texture = Assets.getInstance().get("game/environments/scene1-mountain-back.png");
		Sprite mountain = new Sprite(texture);
		mountain.setPosition(camera.position.x - mountain.getWidth()/2.0f, camera.position.y - mountain.getHeight()/2.0f + 20.0f);
		ParallaxLayer layer = new ParallaxLayer(-10.0f);
		layer.addSprite(mountain);
		background.addLayer(layer);

		texture = Assets.getInstance().get("game/environments/scene1-mountain-mid.png");
		mountain = new Sprite(texture);
		mountain.setPosition(camera.position.x - mountain.getWidth()/2.0f + 100.0f, camera.position.y - mountain.getHeight()/2.0f + 0.0f);
		layer = new ParallaxLayer(-7.5f);
		layer.addSprite(mountain);
		background.addLayer(layer);

		texture = Assets.getInstance().get("game/environments/scene1-mountain-front.png");
		mountain = new Sprite(texture);
		mountain.setPosition(camera.position.x - mountain.getWidth()/2.0f - 50.0f, camera.position.y - mountain.getHeight()/2.0f - 25.0f);
		layer = new ParallaxLayer(-5.75f);
		layer.addSprite(mountain);
		background.addLayer(layer);

		texture = Assets.getInstance().get("game/environments/scene1-fog.png");
		Sprite fog = new Sprite(texture);
		fog.setPosition(camera.position.x - fog.getWidth()/2.0f, camera.position.y - fog.getHeight()/2.0f - 15.0f);
		layer = new ParallaxLayer(0.0f);
		layer.addSprite(fog);
		background.addLayer(layer);

		texture = Assets.getInstance().get("game/environments/scene1-backgrass2.png");
		back_grass2 = new TilingFloor(texture);
		back_grass2.setPosition(camera.position.x - back_grass2.getTileWidth()/2.0f, camera.position.y - back_grass2.getTileHeight()/2.0f - 42.0f);
		layer = new ParallaxLayer(-2.25f);
		layer.addSprite(back_grass2);
		background.addLayer(layer);

		texture = Assets.getInstance().get("game/environments/scene1-backgrass.png");
		back_grass = new TilingFloor(texture);
		back_grass.setPosition(camera.position.x - back_grass.getTileWidth()/2.0f, camera.position.y - back_grass.getTileHeight()/2.0f - 34.0f);
		layer = new ParallaxLayer(-1.5f);
		layer.addSprite(back_grass);
		background.addLayer(layer);
	}

	public void draw(SpriteBatch spriteBatch) {
		/************
		 * Static camera
		 ************/
		spriteBatch.setProjectionMatrix(staticCamera.combined);
		// Background mountains and grass
		background.draw(spriteBatch);

		/************
		 * Foreground camera
		 ************/
		spriteBatch.setProjectionMatrix(camera.combined);
		// Foreground floor
		floor.draw(spriteBatch);
		// Enemies
		mage.draw(spriteBatch);
		// Player
		player.draw(spriteBatch);

		cameraPanner.update();

		// Update parallax
		float lastTranslateAmount = cameraPanner.getLastTranslateAmount();
		floor.addOffset(lastTranslateAmount); // Don't move the floor, but tell it how far the foreground camera has panned
		background.translate(lastTranslateAmount, 0.0f);
		back_grass.update();
		back_grass2.update();
		floor.update();

		// Debug
		renderCollisionDebug(spriteBatch);

		/************
		 * Static camera
		 ************/
		spriteBatch.setProjectionMatrix(staticCamera.combined);
		dialog1.draw(spriteBatch);

		checkCollision();
	}

	/***********
	 * TODO: Move to own class
	 ***********/
	public void checkCollision() {
		if (player.getBoundingBox().overlaps(mage.getBoundingBox()) && mage.isAlive()) {
			Direction damagedSide = Direction.RIGHT;
			if (player.getBoundingBox().getX() > mage.getBoundingBox().getX()) {
				damagedSide = Direction.LEFT;
			}
			player.hurt(mage.getAttackPower(), damagedSide);
		}
		if (player.isAttacking() && player.getAttackBoundingBox().overlaps(mage.getBoundingBox()) && mage.isAlive()) {
			Direction damagedSide = Direction.RIGHT;
			if (mage.getBoundingBox().getX() > player.getBoundingBox().getX()) {
				damagedSide = Direction.LEFT;
			}
			mage.hurt(player.getAttackPower(), damagedSide);
		}
	}
	/***********
	 * TODO: Move to own class
	 ***********/
	public void renderCollisionDebug(SpriteBatch spriteBatch) {
		if (DebugFlags.DEV_SHOW_BOUNDINGBOXES) {
			spriteBatch.end();
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
			// Player bounds
			shapeRenderer.rect(playerBounds.getX() + player.getBoundingBox().getX() - player.getPosX(),
					playerBounds.y, playerBounds.width, playerBounds.height);

			// Hurt collision
			Rectangle rect = player.getBoundingBox();
			shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
			rect = mage.getBoundingBox();
			shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);

			// Player attack collision
			if (player.isAttacking()) {
				rect = player.getAttackBoundingBox();
				shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
			}

			// Camera bounds
			shapeRenderer.setColor(1.0f, 1.0f, 0.0f, 0.0f);
			shapeRenderer.rect(cameraBounds.x, cameraBounds.y, cameraBounds.width, cameraBounds.height);
			shapeRenderer.end();
			shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
			shapeRenderer.point(camera.position.x, camera.position.y, 0.0f);

			shapeRenderer.end();
			spriteBatch.begin();
		}
	}

	@Override
	public void eventTriggered(Object data) {
		if (data instanceof InputEvent) {
			if ((InputEvent) data == InputEvent.PRESS_DEBUG_OPTIONS) {
				if (!dialog1.isShowing()) {
					dialog1.show();
				}
				else {
					dialog1.hide();
				}
			}
		}
	}
}
