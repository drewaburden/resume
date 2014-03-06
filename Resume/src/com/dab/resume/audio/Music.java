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

package com.dab.resume.audio;

import com.dab.resume.assets.Assets;

public class Music {
	public static float VOLUME_MODIFIER = 0.35f;

	private com.badlogic.gdx.audio.Music battle_intro, battle_loop,
			dialog_intro, dialog_loop, victory_intro, victory_loop, gameover;

	public Music() {
		Assets.getInstance().load("game/music/dialog-intro.ogg", com.badlogic.gdx.audio.Music.class);
		Assets.getInstance().load("game/music/dialog-loop.ogg", com.badlogic.gdx.audio.Music.class);
		Assets.getInstance().load("game/music/battle-intro.ogg", com.badlogic.gdx.audio.Music.class);
		Assets.getInstance().load("game/music/battle-loop.ogg", com.badlogic.gdx.audio.Music.class);
		Assets.getInstance().load("game/music/victory-intro.ogg", com.badlogic.gdx.audio.Music.class);
		Assets.getInstance().load("game/music/victory-loop.ogg", com.badlogic.gdx.audio.Music.class);
		Assets.getInstance().load("game/music/gameover.ogg", com.badlogic.gdx.audio.Music.class);
	}

	public void initAssets() {
		battle_intro = Assets.getInstance().get("game/music/battle-intro.ogg");
		battle_loop = Assets.getInstance().get("game/music/battle-loop.ogg");
		battle_intro.setVolume(VOLUME_MODIFIER);
		battle_loop.setVolume(VOLUME_MODIFIER);
		dialog_intro = Assets.getInstance().get("game/music/dialog-intro.ogg");
		dialog_loop = Assets.getInstance().get("game/music/dialog-loop.ogg");
		dialog_intro.setVolume(VOLUME_MODIFIER);
		dialog_loop.setVolume(VOLUME_MODIFIER);
		victory_intro = Assets.getInstance().get("game/music/victory-intro.ogg");
		victory_loop = Assets.getInstance().get("game/music/victory-loop.ogg");
		victory_intro.setVolume(VOLUME_MODIFIER*1.5f);
		victory_loop.setVolume(VOLUME_MODIFIER*1.5f);
		gameover = Assets.getInstance().get("game/music/gameover.ogg");
		gameover.setVolume(VOLUME_MODIFIER);
	}

	public void playDialogMusic() {
		dialog_intro.play();
		dialog_intro.setOnCompletionListener(new com.badlogic.gdx.audio.Music.OnCompletionListener() {
			@Override
			public void onCompletion(com.badlogic.gdx.audio.Music music) {
				dialog_loop.play();
				dialog_loop.setLooping(true);
			}
		});
	}
	public void stopDialogMusic() {
		dialog_intro.stop();
		dialog_loop.stop();
	}

	public void playBattleMusic() {
		battle_intro.play();
		battle_intro.setOnCompletionListener(new com.badlogic.gdx.audio.Music.OnCompletionListener() {
			@Override
			public void onCompletion(com.badlogic.gdx.audio.Music music) {
				battle_loop.play();
				battle_loop.setLooping(true);
			}
		});
	}
	public void stopBattleMusic() {
		battle_intro.stop();
		battle_loop.stop();
	}

	public void playVictoryMusic() {
		victory_intro.play();
		victory_intro.setOnCompletionListener(new com.badlogic.gdx.audio.Music.OnCompletionListener() {
			@Override
			public void onCompletion(com.badlogic.gdx.audio.Music music) {
				victory_loop.play();
				victory_loop.setLooping(true);
			}
		});
	}
	public void stopVictoryMusic() {
		victory_intro.stop();
		victory_loop.stop();
	}

	public void playGameOverMusic() {
		gameover.play();
	}
	public void stopGameOverMusic() {
		gameover.stop();
	}

	public boolean isMusicPlaying() {
		return (dialog_intro.isPlaying() || dialog_loop.isPlaying()
				|| battle_intro.isPlaying() || battle_loop.isPlaying()
				|| victory_intro.isPlaying() || victory_loop.isPlaying()
				|| gameover.isPlaying());
	}

	public void stopAllMusic() {
		stopDialogMusic();
		stopBattleMusic();
		stopVictoryMusic();
		stopGameOverMusic();
	}
}
