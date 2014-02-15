/********************************************************************************************************
 * Project:     Résumé
 * File:        Music.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Handles loading, initialization, and playing of background music for the game
 ********************************************************************************************************/

package com.dab.resume.game.audio;

import com.dab.resume.assets.Assets;

public class Music {
	public static float VOLUME_MODIFIER = 0.5f;

	private com.badlogic.gdx.audio.Music battle_intro, battle_loop;

	public Music() {
		Assets.getInstance().load("game/music/battle-intro.ogg", com.badlogic.gdx.audio.Music.class);
		Assets.getInstance().load("game/music/battle-loop.ogg", com.badlogic.gdx.audio.Music.class);
	}

	public void initAssets() {
		battle_intro = Assets.getInstance().get("game/music/battle-intro.ogg");
		battle_loop = Assets.getInstance().get("game/music/battle-loop.ogg");
		battle_intro.setVolume(VOLUME_MODIFIER);
		battle_loop.setVolume(VOLUME_MODIFIER);
	}

	void playBattleMusic() {
		battle_intro.play();
		battle_intro.setOnCompletionListener(new com.badlogic.gdx.audio.Music.OnCompletionListener() {
			@Override
			public void onCompletion(com.badlogic.gdx.audio.Music music) {
				battle_loop.play();
				battle_loop.setLooping(true);
			}
		});
	}
	void stopBattleMusic() {
		battle_intro.stop();
		battle_loop.stop();
	}
}
