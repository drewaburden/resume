/********************************************************************************************************
 * Project:     Résumé
 * File:        CameraPanner.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles the panning of the camera by detecting when the player has reached
 *      a particular point in the frustum, and also constrains the player and camera
 *      locations to pre-defined bounding boxes.
 ********************************************************************************************************/

package com.dab.resume.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.dab.resume.TerminalGame;
import com.dab.resume.collision.BoundingBox;
import com.dab.resume.debug.Log;
import com.dab.resume.lifeform.player.Player;

public class CameraPanner {
	private static final float PAN_TRIGGER_LEFT = TerminalGame.VIRTUAL_WIDTH/2.0f - 25.0f;
	private static final float PAN_TRIGGER_RIGHT = TerminalGame.VIRTUAL_WIDTH/2.0f + 25.0f;

	OrthographicCamera camera;
	Player player;
	BoundingBox playerBounds, cameraBounds;

	float lastTranslateAmount = 0.0f;

	public CameraPanner(OrthographicCamera camera, Player player, BoundingBox playerBounds, BoundingBox cameraBounds) {
		this.camera = camera;
		this.playerBounds = playerBounds;
		this.cameraBounds = cameraBounds;
		this.player = player;
	}

	public void update() {
		float delta = Gdx.graphics.getDeltaTime();

		BoundingBox playerCollision = player.getBoundingBox();
		Vector3 panRightTrigger = new Vector3(PAN_TRIGGER_RIGHT, 0.0f, 0.0f);
		Vector3 panLeftTrigger = new Vector3(PAN_TRIGGER_LEFT, 0.0f, 0.0f);
		camera.unproject(panRightTrigger);
		camera.unproject(panLeftTrigger);

		// If the player has gone outside of bounds
		if (!playerBounds.contains(player.getPosX(), player.getPosY())) {
			Log.verboseLog("Player out of bounds");
			// If the player was traveling in the positive direction, they must've hit the right bound
			if (player.getVelocityX() > 0.0f) { player.setPosX(playerBounds.getRight()); }
			// If the player was traveling in the negative direction, they must've hit the left bound
			else if (player.getVelocityX() < 0.0f) { player.setPosX(playerBounds.getLeft()); }

			player.stopXMovement();
			lastTranslateAmount = 0.0f; // If we constrained the player, we didn't pan the camera
		}
		// If the player has entered one of the areas on the screen meant to trigger a camera pan
		else if ((playerCollision.getRight() > panRightTrigger.x && player.getVelocityX() > 0.0f)
				|| (playerCollision.getLeft() < panLeftTrigger.x && player.getVelocityX() < 0.0f)) {
			// Pan the camera and update all trigger and bounding areas
			float boundsUpdate = delta * (player.getVelocityX() + delta*player.getAccelerationX()/2.0f); // Velocity Verlet method
			lastTranslateAmount = boundsUpdate;
			camera.translate(boundsUpdate, 0.0f);

			// If the camera is now outside of the camera bounds
			if (!cameraBounds.contains(camera.position.x, camera.position.y)) {
				Log.verboseLog("Camera out of bounds");
				// If we translated to the right, we must've hit the right bound
				if (boundsUpdate > 0.0f) {
					// Clamp the camera and calculate the updated translation for the trigger and bounding areas
					boundsUpdate = 0.0f - camera.position.x - cameraBounds.getX()+cameraBounds.getWidth();
					camera.position.x = cameraBounds.getX()+cameraBounds.getWidth();
				}
				// If we translated to the left, we must've hit the left bound
				else if (boundsUpdate < 0.0f) {
					// Clamp the camera and calculate the updated translation for the trigger and bounding areas
					boundsUpdate = cameraBounds.getX() - camera.position.x;
					camera.position.x = cameraBounds.getX();
				}
				lastTranslateAmount += boundsUpdate;
			}
			camera.update(true);
		}
		else {
			lastTranslateAmount = 0.0f; // Didn't translate
		}
	}

	public float getLastTranslateAmount() { return lastTranslateAmount; }
}
