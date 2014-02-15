/********************************************************************************************************
 * Project:     Résumé
 * File:        PlayerSoundFX.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Load, initializes, and plays the Player's sound effects
 ********************************************************************************************************/

package com.dab.resume.game.lifeform.player;

 import com.badlogic.gdx.audio.Sound;
import com.dab.resume.assets.Assets;
 import com.dab.resume.game.audio.SoundFX;
 import com.dab.resume.game.lifeform.LifeformSoundFX;

 import java.util.Random;

public class PlayerSoundFX extends LifeformSoundFX {
	Sound[] sword_swing;
	Sound[] step;
	Sound jump;
	Sound[] jump_land;

	int lastSwordSwingSound = -1;
	int lastStepSound = -1;
	int lastJumpLandSound = -1;

	public PlayerSoundFX() {
		Assets.getInstance().load("game/sounds/sword-swing0.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/sword-swing1.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/sword-swing2.ogg", Sound.class);
		sword_swing = new Sound[3];


		Assets.getInstance().load("game/sounds/step0.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step1.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step2.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step3.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step4.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step5.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step6.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step7.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step8.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step9.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step10.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step11.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step12.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step13.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/step14.ogg", Sound.class);
		step = new Sound[15];

		Assets.getInstance().load("game/sounds/jump0.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/jump-land0.ogg", Sound.class);
		Assets.getInstance().load("game/sounds/jump-land1.ogg", Sound.class);
		jump_land = new Sound[2];
	}

	public void initAssets() {
		sword_swing[0] = Assets.getInstance().get("game/sounds/sword-swing0.ogg");
		sword_swing[1] = Assets.getInstance().get("game/sounds/sword-swing1.ogg");
		sword_swing[2] = Assets.getInstance().get("game/sounds/sword-swing2.ogg");

		step[0] = Assets.getInstance().get("game/sounds/step0.ogg");
		step[1] = Assets.getInstance().get("game/sounds/step1.ogg");
		step[2] = Assets.getInstance().get("game/sounds/step2.ogg");
		step[3] = Assets.getInstance().get("game/sounds/step3.ogg");
		step[4] = Assets.getInstance().get("game/sounds/step4.ogg");
		step[5] = Assets.getInstance().get("game/sounds/step5.ogg");
		step[6] = Assets.getInstance().get("game/sounds/step6.ogg");
		step[7] = Assets.getInstance().get("game/sounds/step7.ogg");
		step[8] = Assets.getInstance().get("game/sounds/step8.ogg");
		step[9] = Assets.getInstance().get("game/sounds/step9.ogg");
		step[10] = Assets.getInstance().get("game/sounds/step10.ogg");
		step[11] = Assets.getInstance().get("game/sounds/step11.ogg");
		step[12] = Assets.getInstance().get("game/sounds/step12.ogg");
		step[13] = Assets.getInstance().get("game/sounds/step13.ogg");
		step[14] = Assets.getInstance().get("game/sounds/step14.ogg");

		jump = Assets.getInstance().get("game/sounds/jump0.ogg");
		jump_land[0] = Assets.getInstance().get("game/sounds/jump-land0.ogg");
		jump_land[1] = Assets.getInstance().get("game/sounds/jump-land1.ogg");
	}

	public void playSwordSwing() {
		int soundIndex = new Random().nextInt(3);
		while (soundIndex == lastSwordSwingSound) {
			soundIndex = new Random().nextInt(3);
		}
		long id = sword_swing[soundIndex].play();
		sword_swing[soundIndex].setVolume(id, SoundFX.VOLUME_MODIFIER);
		lastSwordSwingSound = soundIndex;
	}

	public void playStepSound() {
		int soundIndex = new Random().nextInt(8);
		while (soundIndex == lastStepSound) {
			soundIndex = new Random().nextInt(8);
		}
		long id = step[soundIndex].play();
		step[soundIndex].setVolume(id, SoundFX.VOLUME_MODIFIER);
		lastStepSound = soundIndex;
	}

	public void playJumpSound() {
		long id = jump.play();
		jump.setVolume(id, SoundFX.VOLUME_MODIFIER * 0.5f);
	}
	public void playJumpLandSound() {
		int soundIndex = new Random().nextInt(2);
		while (soundIndex == lastJumpLandSound) {
			soundIndex = new Random().nextInt(2);
		}
		long id = jump_land[soundIndex].play();
		jump_land[soundIndex].setVolume(id, SoundFX.VOLUME_MODIFIER * 0.5f);
		lastJumpLandSound = soundIndex;
	}
}
