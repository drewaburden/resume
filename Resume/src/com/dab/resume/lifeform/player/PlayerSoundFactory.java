/********************************************************************************************************
 * Project:     Résumé
 * File:        PlayerSoundFactory.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Loads, initializes, and serves Player Sounds
 ********************************************************************************************************/

package com.dab.resume.lifeform.player;

import com.badlogic.gdx.audio.Sound;
import com.dab.resume.assets.Assets;
import com.dab.resume.lifeform.LifeformSound;
import com.dab.resume.lifeform.SoundFactory;

import java.util.Random;

public class PlayerSoundFactory {
	private Sound[] sword_swing;
	private Sound[] step;
	private Sound jump;
	private Sound[] jump_land;
	private Sound hurt;
	private Sound death;

	private int lastSwordSwingSound = -1;
	private int lastStepSound = -1;
	private int lastJumpLandSound = -1;

	public PlayerSoundFactory() {
		Assets.getInstance().load("sounds/sword-swing0.ogg", Sound.class);
		Assets.getInstance().load("sounds/sword-swing1.ogg", Sound.class);
		Assets.getInstance().load("sounds/sword-swing2.ogg", Sound.class);
		sword_swing = new Sound[3];


		Assets.getInstance().load("sounds/step0.ogg", Sound.class);
		Assets.getInstance().load("sounds/step1.ogg", Sound.class);
		Assets.getInstance().load("sounds/step2.ogg", Sound.class);
		Assets.getInstance().load("sounds/step3.ogg", Sound.class);
		Assets.getInstance().load("sounds/step4.ogg", Sound.class);
		Assets.getInstance().load("sounds/step5.ogg", Sound.class);
		Assets.getInstance().load("sounds/step6.ogg", Sound.class);
		Assets.getInstance().load("sounds/step7.ogg", Sound.class);
		step = new Sound[8];

		Assets.getInstance().load("sounds/jump0.ogg", Sound.class);
		Assets.getInstance().load("sounds/jump-land0.ogg", Sound.class);
		Assets.getInstance().load("sounds/jump-land1.ogg", Sound.class);
		jump_land = new Sound[2];

		Assets.getInstance().load("sounds/player-hurt.ogg", Sound.class);
		Assets.getInstance().load("sounds/player-death.ogg", Sound.class);
	}

	public void initAssets() {
		sword_swing[0] = Assets.getInstance().get("sounds/sword-swing0.ogg");
		sword_swing[1] = Assets.getInstance().get("sounds/sword-swing1.ogg");
		sword_swing[2] = Assets.getInstance().get("sounds/sword-swing2.ogg");

		step[0] = Assets.getInstance().get("sounds/step0.ogg");
		step[1] = Assets.getInstance().get("sounds/step1.ogg");
		step[2] = Assets.getInstance().get("sounds/step2.ogg");
		step[3] = Assets.getInstance().get("sounds/step3.ogg");
		step[4] = Assets.getInstance().get("sounds/step4.ogg");
		step[5] = Assets.getInstance().get("sounds/step5.ogg");
		step[6] = Assets.getInstance().get("sounds/step6.ogg");
		step[7] = Assets.getInstance().get("sounds/step7.ogg");

		jump = Assets.getInstance().get("sounds/jump0.ogg");
		jump_land[0] = Assets.getInstance().get("sounds/jump-land0.ogg");
		jump_land[1] = Assets.getInstance().get("sounds/jump-land1.ogg");

		hurt = Assets.getInstance().get("sounds/player-hurt.ogg");
		death = Assets.getInstance().get("sounds/player-death.ogg");
	}

	public LifeformSound getSound(SoundFactory.SoundType sound) {
		switch (sound) {
			case ATTACK_SWORD: return getSwordSwingSound();
			case MOVE: return getStepSound();
			case JUMP: return getJumpSound();
			case LANDED: return getJumpLandSound();
			case HURT: return getHurtSound();
			case DEATH: return getDeathSound();
			default: throw new IllegalArgumentException("That sound does not exist");
		}
	}

	public LifeformSound getSwordSwingSound() {
		int soundIndex = new Random().nextInt(3);
		while (soundIndex == lastSwordSwingSound) {
			soundIndex = new Random().nextInt(3);
		}
		lastSwordSwingSound = soundIndex;
		return new LifeformSound(sword_swing[soundIndex], 1.0f);
	}

	public LifeformSound getStepSound() {
		int soundIndex = new Random().nextInt(8);
		while (soundIndex == lastStepSound) {
			soundIndex = new Random().nextInt(8);
		}
		lastStepSound = soundIndex;
		return new LifeformSound(step[soundIndex], 0.8f);
	}

	public LifeformSound getJumpSound() {
		return new LifeformSound(jump, 0.5f);
	}
	public LifeformSound getJumpLandSound() {
		int soundIndex = new Random().nextInt(2);
		while (soundIndex == lastJumpLandSound) {
			soundIndex = new Random().nextInt(2);
		}
		lastJumpLandSound = soundIndex;
		return new LifeformSound(jump_land[soundIndex], 0.5f);
	}

	public LifeformSound getHurtSound() {
		return new LifeformSound(hurt, 0.5f);
	}
	public LifeformSound getDeathSound() {
		return new LifeformSound(death, 0.5f);
	}
}
