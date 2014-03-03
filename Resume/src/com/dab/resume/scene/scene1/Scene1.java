/********************************************************************************************************
 * Project:     Résumé
 * File:        Scene1.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Needs refactoring. Will update description later.
 ********************************************************************************************************/

package com.dab.resume.scene.scene1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.dab.resume.GameState;
import com.dab.resume.TerminalGame;
import com.dab.resume.assets.Assets;
import com.dab.resume.debug.DebugFlags;
import com.dab.resume.debug.Log;
import com.dab.resume.events.Observable;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.hud.Dialog;
import com.dab.resume.hud.Fadeable;
import com.dab.resume.lifeform.Direction;
import com.dab.resume.lifeform.enemies.mage.Mage;
import com.dab.resume.lifeform.enemies.mage.MageStateMachine;
import com.dab.resume.lifeform.enemies.mage.attacks.Projectile;
import com.dab.resume.lifeform.player.Player;
import com.dab.resume.scene.*;

import static com.dab.resume.GameState.State.TRANSITIONING;
import static com.dab.resume.collision.CollisionEvent.BLOCKING;

public class Scene1 extends Observable {
	// The ultimate boundaries of the scene where the player cannot walk beyond
	private BoundingBox playerBounds = new BoundingBox(-200.0f, -1000.0f, 3200.0f, 2000.0f, BLOCKING);
	// The bounds that the camera cannot pan beyond.
	private BoundingBox cameraBounds = new BoundingBox(0.0f, -1000.0f, 2675.0f, 2000.0f, BLOCKING);
	private float nextSceneTransitionX = 2800.0f;

	private boolean showing = false;

	private Fadeable sceneTransitionFade;
	private OrthographicCamera camera, staticCamera; // Static camera is not for panning
	private CameraPanner cameraPanner;
	private Player player;
	private Mage mage;
	private MageStateMachine mageAI;
	private TilingFloor floor, back_grass, back_grass2;
	private ParallaxBackground background;
	private Dialog dialog1;
	private Rain rain;

	public Scene1(OrthographicCamera camera, Player player, Fadeable sceneFadeOut) {
		this.camera = camera;
		this.player = player;
		this.sceneTransitionFade = sceneFadeOut;
		staticCamera = new OrthographicCamera(camera.viewportWidth, camera.viewportHeight);
		cameraPanner = new CameraPanner(camera, player, playerBounds, cameraBounds);
		mage = new Mage(500.0f);
		float dialogWidth = 250.0f, dialogHeight = 145.0f;
		dialog1 = new Dialog("Dying Man", "Please... You must defeat the foe that lies ahead of you. " +
				"You're our only hope now. Avenge us...", 0.0f - dialogWidth/2.0f, 25.0f - dialogHeight/2.0f,
				dialogWidth, dialogHeight);
		rain = new Rain();
		TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
		params.genMipMaps = true;
		Assets.getInstance().load("game/environments/scene1-floor.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-backgrass.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-backgrass2.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-fog.png", Texture.class);
		Assets.getInstance().load("game/environments/scene1-trees-distant.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-tree1.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-tree2.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-trunk1.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-trunk2.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-forest-fog.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-mountain-back.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-mountain-mid.png", Texture.class, params);
		Assets.getInstance().load("game/environments/scene1-mountain-front.png", Texture.class, params);
	}

	public void initAssets() {
		Log.log();
		dialog1.initAssets();

		mage.initAssets();

		background = new ParallaxBackground();

		/***************
		 * Mountains
		 ***************/
		Texture texture = Assets.getInstance().get("game/environments/scene1-mountain-back.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		Sprite sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 242.0f, camera.position.y - sprite.getHeight()/2.0f + 40.0f);
		ParallaxLayer layer = new ParallaxLayer(-10.0f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-mountain-mid.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 346.5f, camera.position.y - sprite.getHeight()/2.0f + 30.0f);
		layer = new ParallaxLayer(-7.5f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-mountain-front.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f - 50.0f, camera.position.y - sprite.getHeight()/2.0f - 25.0f);
		layer = new ParallaxLayer(-5.75f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-fog.png");
		Sprite fog = new Sprite(texture);
		fog.setPosition(0.0f - TerminalGame.VIRTUAL_WIDTH/2.0f, camera.position.y - fog.getHeight()/2.0f - 15.0f);
		fog.setSize(TerminalGame.VIRTUAL_WIDTH, fog.getHeight());
		layer = new ParallaxLayer(0.0f);
		layer.addSprite(fog);
		background.addLayer(layer);

		/***************
		 * Trees
		 ***************/
		texture = Assets.getInstance().get("game/environments/scene1-trees-distant.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 575.0f, camera.position.y - sprite.getHeight()/2.0f + -8.0f);
		layer = new ParallaxLayer(-4.75f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-tree1.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 650.0f, camera.position.y - sprite.getHeight()/2.0f + 25.0f);
		layer = new ParallaxLayer(-4.5f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-tree1.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 550.0f, camera.position.y - sprite.getHeight()/2.0f + 40.0f);
		layer = new ParallaxLayer(-4.0f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-tree2.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 700.0f, camera.position.y - sprite.getHeight()/2.0f + 55.0f);
		layer = new ParallaxLayer(-3.5f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-tree2.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 600.0f, camera.position.y - sprite.getHeight()/2.0f + 65.0f);
		layer = new ParallaxLayer(-3.0f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-trunk2.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 800.0f, camera.position.y - sprite.getHeight()/2.0f + 60.0f);
		layer = new ParallaxLayer(-2.75f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-tree1.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 550.0f, camera.position.y - sprite.getHeight()/2.0f + 50.0f);
		layer = new ParallaxLayer(-2.5f);
		layer.addSprite(sprite);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-trunk1.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 1000.0f, camera.position.y - sprite.getHeight()/2.0f + 75.0f);
		layer = new ParallaxLayer(-2.5f);
		layer.addSprite(sprite);
		background.addLayer(layer);

		texture = Assets.getInstance().get("game/environments/scene1-forest-fog.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 1000.0f, camera.position.y - sprite.getHeight()/2.0f + 65.0f);
		layer = new ParallaxLayer(-2.0f);
		layer.addSprite(sprite);
		background.addLayer(layer);

		/***************
		 * Grass
		 ***************/
		texture = Assets.getInstance().get("game/environments/scene1-backgrass2.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		back_grass2 = new TilingFloor(texture, 3);
		back_grass2.setPosition(camera.position.x - back_grass2.getTileWidth()*2.5f, camera.position.y - back_grass2.getTileHeight()/2.0f - 42.0f);
		layer = new ParallaxLayer(-2.25f);
		layer.addSprite(back_grass2);
		background.addLayer(layer);

		texture = Assets.getInstance().get("game/environments/scene1-trunk2.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 700.0f, camera.position.y - sprite.getHeight()/2.0f + 110.0f);
		layer = new ParallaxLayer(-2.4f);
		layer.addSprite(sprite);
		background.addLayer(layer);

		texture = Assets.getInstance().get("game/environments/scene1-trunk1.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		sprite = new Sprite(texture);
		sprite.setPosition(camera.position.x - sprite.getWidth()/2.0f + 700.0f, camera.position.y - sprite.getHeight()/2.0f + 50.0f);
		layer = new ParallaxLayer(-2.0f);
		layer.addSprite(sprite);
		background.addLayer(layer);


		texture = Assets.getInstance().get("game/environments/scene1-backgrass.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		back_grass = new TilingFloor(texture, 3);
		back_grass.setPosition(camera.position.x - back_grass.getTileWidth()*2.5f, camera.position.y - back_grass.getTileHeight()/2.0f - 34.0f);
		layer = new ParallaxLayer(-1.5f);
		layer.addSprite(back_grass);
		background.addLayer(layer);
		texture = Assets.getInstance().get("game/environments/scene1-floor.png");
		texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
		floor = new TilingFloor(texture, 3);
		floor.setPosition(camera.position.x - floor.getTileWidth()*2.55f, camera.position.y - floor.getTileHeight()/2.0f - 90.0f);

		rain.initAssets();

		mageAI = new MageStateMachine(mage, player);
	}

	public void show() { show(false); }
	public void show(boolean fadeIn) {
		player.stopXMovement();
		player.stopYForce();
		showing = true;
		player.setPosX(-150.0f);
		player.setPosY(World.FLOOR);
		camera.position.set(cameraBounds.getLeft(), 0.0f, 0.0f);

		// Must be coming in from the right, so we need different positions
		// TODO: Have a separate argument for the entrance side
		if (fadeIn) {
			sceneTransitionFade.fadeToAlpha(0.0f, 1.0f);
			player.setPosX(nextSceneTransitionX-player.getBoundingBox().getWidth()*3.0f);
			player.setPosY(World.FLOOR);
			camera.position.set(cameraBounds.getRight(), 0.0f, 0.0f);
			rain.initAssets();
		}
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
			if (player.getBoundingBox().getRight() >= nextSceneTransitionX && !GameState.isGameStateSet(TRANSITIONING)) {
				GameState.addGameState(GameState.State.TRANSITIONING);
				sceneTransitionFade.fadeToAlpha(1.0f, 1.0f);
			}
			// If we're done fading out, notify the observers to actually switch scenes now
			else if (GameState.isGameStateSet(TRANSITIONING) && !sceneTransitionFade.isFading()) {
				notifyObservers(SceneEvent.TRANSITION_TO_SCENE2);
			}
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
			// Rain
			rain.draw(spriteBatch);
			// Foreground floor
			floor.draw(spriteBatch);
			// Enemies
			mage.draw(spriteBatch);
			// Player
			camera.update();
			player.draw(spriteBatch);

			cameraPanner.update();

			//camera.translate(150.0f*Gdx.graphics.getDeltaTime(), 0.0f);
			//camera.update();

			// Update parallax
			float lastTranslateAmount = cameraPanner.getLastTranslateAmount();
			floor.addOffset(lastTranslateAmount); // Don't move the floor, but tell it how far the foreground camera has panned
			background.translate(lastTranslateAmount, 0.0f);
			back_grass.update();
			back_grass2.update();
			floor.update();

			rain.translate(lastTranslateAmount, 0.0f);
			rain.updateSound(Gdx.graphics.getDeltaTime());

			// Debug
			renderCollisionDebug(spriteBatch);

			/************
			 * Static camera
			 ************/
			spriteBatch.setProjectionMatrix(staticCamera.combined);
			dialog1.draw(spriteBatch);

			//checkCollision();

			mageAI.update(Gdx.graphics.getDeltaTime());
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
}
