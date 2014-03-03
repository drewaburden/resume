/********************************************************************************************************
 * Project:     Résumé
 * File:        Scene2.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Defines all of Scene2
 *      TODO: This probably needs a bit of refactoring and abstraction
 ********************************************************************************************************/

package com.dab.resume.scene.scene2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.dab.resume.GameState;
import com.dab.resume.TerminalGame;
import com.dab.resume.assets.Assets;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.debug.DebugFlags;
import com.dab.resume.debug.Log;
import com.dab.resume.events.Observable;
import com.dab.resume.hud.Dialog;
import com.dab.resume.hud.Fadeable;
import com.dab.resume.input.InputEvent;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.enemies.mage.Mage;
import com.dab.resume.lifeform.enemies.mage.MageStateMachine;
import com.dab.resume.lifeform.enemies.mage.attacks.Projectile;
import com.dab.resume.lifeform.player.Player;
import com.dab.resume.scene.*;

import java.util.LinkedList;

import static com.dab.resume.GameState.State.TRANSITIONING;
import static com.dab.resume.collision.CollisionEvent.BLOCKING;

public class Scene2 extends Observable implements Observer {
	// The ultimate boundaries of the scene where the player cannot walk beyond
	private BoundingBox playerBounds = new BoundingBox(-500.0f, -1000.0f, 2300.0f, 2000.0f, BLOCKING);
	// The bounds that the camera cannot pan beyond.
	//private BoundingBox cameraBounds = new BoundingBox(0.0f, -1000.0f, 2675.0f, 2000.0f, BLOCKING);
	private BoundingBox cameraBounds = new BoundingBox(0.0f, -1000.0f, 1500.0f, 2000.0f, BLOCKING);
	private float prevSceneTransitionX = -175.0f;

	private boolean showing = false;

	private Fadeable sceneTransitionFade;
	private OrthographicCamera camera, staticCamera; // Static camera is not for panning
	private CameraPanner cameraPanner;
	private Player player;
	private Mage mage;
	private MageStateMachine mageAI;
	private OldWoman oldWoman;
	private TilingFloor floor, dirt;
	private Dialog dialog1;
	private Rain rain;
	private Sprite fog;
	private LinkedList<Sprite> wall;
	private LinkedList<Candle> candles;
	private LinkedList<Sprite> crates;
	private LinkedList<Sprite> logs_background, logs_foreground;

	public Scene2(OrthographicCamera camera, Player player, Fadeable sceneFadeOut) {
		this.camera = camera;
		this.player = player;
		this.sceneTransitionFade = sceneFadeOut;
		staticCamera = new OrthographicCamera(camera.viewportWidth, camera.viewportHeight);
		cameraPanner = new CameraPanner(camera, player, playerBounds, cameraBounds);
		mage = new Mage(1250.0f);
		oldWoman = new OldWoman(150.0f);
		float dialogWidth = 340.0f, dialogHeight = 120.0f;
		dialog1 = new Dialog("Dying old woman", "Please... You must defeat the foe that lies ahead of you. " +
				"You are our only hope now. Avenge us...", 0.0f - dialogWidth/2.0f, 50.0f - dialogHeight/2.0f,
				dialogWidth, dialogHeight);
		rain = new Rain(true);
		wall = new LinkedList<Sprite>();
		crates = new LinkedList<Sprite>();
		logs_background = new LinkedList<Sprite>();
		logs_foreground = new LinkedList<Sprite>();
		// Create candles
		candles = new LinkedList<Candle>();
		for(int candleNum = 0; candleNum < 6; ++candleNum) {
			candles.add(new Candle());
		}
		Assets.getInstance().load("game/environments/castle/floor.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/wall.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/dirt-corner.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/crate.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/crate-stacked.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/candle.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/candle-flame.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/log-short.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/log-long.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/window-three.png", Texture.class);
		Assets.getInstance().load("game/environments/castle/fog-top.png", Texture.class);
	}

	public void initAssets() {
		Log.log();

		mage.initAssets();
		oldWoman.initAssets();

		Texture texture = Assets.getInstance().get("game/environments/castle/fog-top.png");
		fog = new Sprite(texture);
		fog.setPosition(0.0f - TerminalGame.VIRTUAL_WIDTH / 2.0f, camera.position.y - fog.getHeight() / 2.0f + 135.0f);
		fog.setSize(TerminalGame.VIRTUAL_WIDTH, fog.getHeight());

		texture = Assets.getInstance().get("game/environments/castle/wall.png");
		Sprite sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth() * 1.5f, camera.position.y - sprite.getHeight() / 2.0f + 70.0f);
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/window-three.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 15.0f, wall.getLast().getY());
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/wall.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 13.0f, wall.getLast().getY());
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/wall.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 10.0f, wall.getLast().getY());
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/window-three.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 15.0f, wall.getLast().getY());
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/window-three.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 15.0f, wall.getLast().getY());
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/wall.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 13.0f, wall.getLast().getY());
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/wall.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 10.0f, wall.getLast().getY());
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/wall.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 10.0f, wall.getLast().getY());
		wall.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/window-three.png");
		sprite = new Sprite(texture);
		sprite.setPosition(wall.getLast().getX() + wall.getLast().getWidth() - 15.0f, wall.getLast().getY());
		wall.add(sprite);

		for (Candle candle : candles) {
			candle.initAssets();
		}
		candles.get(0).setPosition(35.0f, 30.0f);
		candles.get(1).setPosition(135.0f, 30.0f);
		candles.get(2).setPosition(345.0f, 30.0f);
		candles.get(3).setPosition(425.0f, 30.0f);
		candles.get(4).setPosition(845.0f, 30.0f);
		candles.get(5).setPosition(1400.0f, 30.0f);

		texture = Assets.getInstance().get("game/environments/castle/log-short.png");
		sprite = new Sprite(texture);
		sprite.setPosition(0.0f - sprite.getWidth(), -40.0f);
		logs_background.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/log-long.png");
		sprite = new Sprite(texture);
		sprite.setPosition(0.0f - sprite.getWidth() - 12.0f, -8.0f);
		sprite.rotate(-60.0f);
		logs_background.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/log-long.png");
		sprite = new Sprite(texture);
		sprite.setPosition(380.0f, -28.0f);
		sprite.rotate(35.0f);
		logs_foreground.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/log-short.png");
		sprite = new Sprite(texture);
		sprite.setPosition(1175, -35.0f);
		logs_background.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/log-short.png");
		sprite = new Sprite(texture);
		sprite.setPosition(1185, -25.0f);
		logs_background.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/log-long.png");
		sprite = new Sprite(texture);
		sprite.setPosition(1189, -37.0f);
		sprite.setColor(0.95f, 0.95f, 0.95f, 1.0f);
		logs_foreground.add(sprite);

		texture = Assets.getInstance().get("game/environments/castle/crate-stacked.png");
		sprite = new Sprite(texture);
		sprite.setPosition(0.0f - sprite.getWidth() - 65.0f, -55.0f);
		crates.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/crate.png");
		sprite = new Sprite(texture);
		sprite.setPosition(0.0f - sprite.getWidth() + 125.0f, -55.0f);
		crates.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/crate.png");
		sprite = new Sprite(texture);
		sprite.setPosition(460.0f, -55.0f);
		crates.add(sprite);
		texture = Assets.getInstance().get("game/environments/castle/crate-stacked.png");
		sprite = new Sprite(texture);
		sprite.setPosition(1500.0f, -55.0f);
		crates.add(sprite);

		texture = Assets.getInstance().get("game/environments/castle/floor.png");
		floor = new TilingFloor(texture, 6);
		floor.setPosition(camera.position.x - floor.getTileWidth()*4.0f, camera.position.y - floor.getTileHeight()/2.0f - 70.0f);

		texture = Assets.getInstance().get("game/environments/castle/dirt-corner.png");
		dirt = new TilingFloor(texture, 4);
		dirt.setPosition(camera.position.x - dirt.getTileWidth() * 3.0f, camera.position.y - dirt.getTileHeight() / 2.0f - 28.0f);
		dirt.setAlpha(0.75f);

		rain.initAssets();
		dialog1.initAssets();

		mageAI = new MageStateMachine(mage, player);
	}

	public void show() { show(false); }
	public void show(boolean fadeIn) {
		if (fadeIn) {
			sceneTransitionFade.fadeToAlpha(0.0f, 1.0f);
		}
		player.stopXMovement();
		player.stopYForce();
		showing = true;
		player.setPosX(-150.0f);
		player.setPosY(World.FLOOR);
		camera.position.set(0.0f, 0.0f, 0.0f);
		initAssets();
	}
	public void hide() {
		showing = false;
		stopSounds();
	}

	public void stopSounds() {
		rain.stopSound();
	}

	public void draw(SpriteBatch spriteBatch) {
		if (showing) {
			// If the player hit the scene transition, set the transition state and start fading out.
			if (player.getBoundingBox().getLeft() <= prevSceneTransitionX && !GameState.isGameStateSet(TRANSITIONING)) {
				GameState.addGameState(GameState.State.TRANSITIONING);
				sceneTransitionFade.fadeToAlpha(1.0f, 1.0f);
			}
			// If we're done fading out, notify the observers to actually switch scenes now
			else if (GameState.isGameStateSet(TRANSITIONING) && !sceneTransitionFade.isFading()) {
				notifyObservers(SceneEvent.TRANSITION_TO_SCENE1);
			}

			mageAI.update(Gdx.graphics.getDeltaTime());

			// If the player triggered the dialogue with the OldWoman, stop the player and show the dialog.
			if (player.getBoundingBox().getRight() >= oldWoman.getPosX()-100.0f
					&& !dialog1.isShowing() && !dialog1.hasBeenDisplayed()) {
				player.stopXForce();
				player.stopYForce();
				dialog1.show();
			}

			/************
			 * Foreground camera
			 ************/
			spriteBatch.setProjectionMatrix(camera.combined);
			// Rain
			rain.draw(spriteBatch);
			// Wall
			for (Sprite sprite : wall) {
				sprite.draw(spriteBatch);
			}
			// Floor
			floor.draw(spriteBatch);
			// Candles
			for (Candle candle : candles) {
				candle.draw(spriteBatch);
			}
			// Corner dirt
			dirt.draw(spriteBatch);
			// Behind Crate Logs
			for (Sprite sprite : logs_background) {
				sprite.draw(spriteBatch);
			}
			// Crates
			for (Sprite sprite : crates) {
				sprite.draw(spriteBatch);
			}
			// Old woman
			oldWoman.draw(spriteBatch);
			// In front Crate Logs
			for (Sprite sprite : logs_foreground) {
				sprite.draw(spriteBatch);
			}
			// Enemies
			mage.draw(spriteBatch);
			// Player
			player.draw(spriteBatch);

			cameraPanner.update();

			// Update parallax
			float lastTranslateAmount = cameraPanner.getLastTranslateAmount();
			floor.addOffset(lastTranslateAmount); // Don't move the floor, but tell it how far the foreground camera has panned
			floor.update();
			dirt.addOffset(lastTranslateAmount);
			dirt.update();

			rain.translate(lastTranslateAmount, 0.0f);
			rain.updateSound(Gdx.graphics.getDeltaTime());

			// Debug
			renderCollisionDebug(spriteBatch);

			/************
			 * Static camera
			 ************/
			spriteBatch.setProjectionMatrix(staticCamera.combined);
			fog.draw(spriteBatch);
			dialog1.draw(spriteBatch);

			checkCollision();
		}
	}

	/***********
	 * TODO: Move to own class
	 ***********/
	public void checkCollision() {
		int numProjectiles = mage.getActiveProjectiles().size();
		for (int projectileIndex = 0; projectileIndex < numProjectiles; ++projectileIndex) {
			Projectile projectile = mage.getActiveProjectiles().get(projectileIndex);
			// If the projectile is out of bounds, destroy it
			if (projectile.getBoundingBox().getRight() < playerBounds.getLeft()) {
				mage.destroyProjectile(projectile);
				numProjectiles--;
				projectileIndex--;
				continue;
			}
			// If the projectile hit the player, hurt the player and destroy the projectile
			if (player.getBoundingBox().overlaps(projectile.getBoundingBox())) {
				Direction damagedSide = Direction.RIGHT;
				if (player.getBoundingBox().getX() > projectile.getBoundingBox().getX()) {
					damagedSide = Direction.LEFT;
				}
				player.hurt(projectile.getAttackPower(), damagedSide);
				mage.destroyProjectile(projectile);
				numProjectiles--;
			}
		}
		// If the player ran into the mage, hurt the player
		if (player.getBoundingBox().overlaps(mage.getBoundingBox()) && mage.isAlive()) {
			Direction damagedSide = Direction.RIGHT;
			if (player.getBoundingBox().getX() > mage.getBoundingBox().getX()) {
				damagedSide = Direction.LEFT;
			}
			player.hurt(mage.getAttackPower(), damagedSide);
		}
		// If the player's attack hit the mage, hurt the mage
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
		if (DebugFlags.DEV_DEBUG_VIEW) {
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

			// Projectiles
			for (Projectile projectile : mage.getActiveProjectiles()) {
				rect = projectile.getBoundingBox();
				shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
			}

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
	public boolean eventTriggered(Object data) {
		if (data instanceof InputEvent) {
			switch ((InputEvent) data) {
				case PRESS_ACCEPT:
					if (dialog1.isShowing()) {
						dialog1.accept();

						// If the player didn't just skip the text (the player actually
						// accepted and closed the dialog box)
						if (!dialog1.isShowing()) {
							oldWoman.die();
						}
						return true;
					}
			}
		}
		return false;
	}
}
