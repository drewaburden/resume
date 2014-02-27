/********************************************************************************************************
 * Project:     Résumé
 * File:        MageStateMachine.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Mage AI. This class determines what actions the Mage should perform based on
 *      environmental criteria and probabilistic decision-making.
 ********************************************************************************************************/

package com.dab.resume.lifeform.enemies.mage;

import com.dab.resume.GameState;
import com.dab.resume.debug.Log;
import com.dab.resume.lifeform.enemies.mage.attacks.AttackType;
import com.dab.resume.lifeform.player.Player;

import java.util.Random;

import static com.dab.resume.GameState.State.PAUSED;

public class MageStateMachine {
	private final float wakeDistance = 200.0f; // How close the player needs to be before the Mage starts making decisions
	private boolean awake = false;
	private final float closenessTriggerDistance = 70.0f; // Distance player must be from the mage before the closeness trigger fires
	private final float actionDelay = 2.5f; // Amount of time (in seconds) that must pass between each action
	private final float quickActionDelay = 1.0f; // Amount of time (in seconds) that must have passed in order to perform a split-second action (like if the player gets dangerously close)
	private float deltaAction = actionDelay; // Time that has passed since the last action was performed.
	private Mage mage;
	private Player player;

	public MageStateMachine(Mage mage, Player player) {
		this.mage = mage;
		this.player = player;
	}

	private void closenessTrigger() {
		Log.log("Mage closeness trigger activated");
		// 60% chance to sword attack, 40% chance to block
		int actionDecision = new Random().nextInt(100)+1; // 1 - 100
		if (actionDecision <= 60) {
			mage.attack(AttackType.LIGHTNING);
			Log.log("Mage attack swords");
		}
		else {
			mage.attack(AttackType.LIGHTNING);
			Log.log("Mage block");
		}
		deltaAction = 0.0f;
	}

	private void makeDecision() {
		Log.log("Mage making decision");
		// 50% chance to lightning attack, 50% chance to fireball attack
		int actionDecision = new Random().nextInt(100)+1; // 1 - 100
		if (actionDecision <= 50) {
			mage.attack(AttackType.LIGHTNING);
			Log.log("Mage attack lightning");
		}
		else {
			mage.attack(AttackType.LIGHTNING);
			Log.log("Mage attack fireball");
		}
		deltaAction = 0.0f;
	}

	private boolean isAwake() {
		if (!awake && Math.abs(mage.getPosX() - player.getPosX()) <= wakeDistance) {
			awake = true;
		}
		return awake;
	}
	private boolean canPerformAction() {
		return (mage.isAlive() && isAwake() && deltaAction >= actionDelay);
	}
	private boolean canPerformQuickAction() {
		return (mage.isAlive() && mage.isHurtBouncing() && isAwake() && deltaAction >= quickActionDelay);
	}

	public void update(float delta) {
		if (!GameState.isGameStateSet(PAUSED)) {
			deltaAction += delta;

			// Calculate distance using the usual distance formula
			float distance = (float) Math.abs(Math.sqrt(
					Math.pow(player.getPosX()-mage.getPosX(), 2)
							+ Math.pow(player.getPosY()-mage.getPosY(), 2)));
			// If the player is close enough, do something based on that
			if (distance <= closenessTriggerDistance && canPerformQuickAction()) {
				closenessTrigger();
			}
			else if (canPerformAction()) {
				makeDecision();
			}
			if (!isAwake()) {
				Log.verboseLog("Can't perform action");
			}
		}
	}
}
