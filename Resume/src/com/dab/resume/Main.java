/********************************************************************************************************
 * Project:     Resume
 * File:        Main.java
 * Authors:     Drew Burden
 * 
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      This project is meant to be a quick and simple demonstration of some of my abilities in the
 *      form of a game, with the added bonus that it's not super boring to read (like a normal résumé).
 *      The game is in the style of old, 2D DOS-era games. I really love old games, and I always wanted
 *      to make something that could serve as a tribute to the games from that era, and I knew I wanted
 *      to make a less-than-conventional résumé. So, this is my fusion of those two concepts.
 *
 *      Doing this project was also somewhat of an exercise for me. I wanted to understand more about
 *      the other aspects of game creation besides programming. In order to better communicate and
 *      ultimately work much better as a team, I think it's important to understand what it's like to be
 *      in a coworker's position. I believe that this project demonstrates this understanding; therefore,
 *      I think this makes me an excellent candidate for a programming position.
 *
 *      Note: Just about everything in this application (strings, sprites, animations, settings, etc.)
 *      is hard-coded, just for the sake of saving time. In a real world situation and for work I've done
 *      in the past, I would have coded a subsystem for dynamically loading all of this information from
 *      files (typically XML).
 *      Also, I've given myself a deadline on this project, so some things are going to be a bit rushed.
 *      As a side effect, some things may not be implemented in the best way. While I could've sat
 *      around for days or even weeks and thought through every aspect of the implementation in order to
 *      get the best implementations possible, when you're short on time, you have to know when to stop
 *      the planning and just dive into it.
 *
 *      This class defines the entry-point for the desktop LibGDX application.
 ********************************************************************************************************/

package com.dab.resume;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dab.resume.debug.Log;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
	    // Configure LibGDX desktop application
	    LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	    cfg.resizable = false;
	    cfg.vSyncEnabled = true;
	    cfg.useGL20 = true;
	    cfg.width = 720; cfg.height = 540; // 4:3 aspect ratio, because old-school. Shut up, you're not my mom.
	    cfg.title = "Résumé Quest by Drew Burden";

	    // Instantiate LibGDX
	    new LwjglApplication(new TerminalGame(), cfg);

		// Creates an error dialog if an unhandled exception is encountered
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				StackTraceElement[] stackTrack = e.getStackTrace();
				String message = "Error: " + e.toString() + "\n\nin file: " + Log.getCallingFileInfo(stackTrack[1])
						+ "\nin class: " + Log.getCallingClassInfo(stackTrack[1]) + "\n\nMessage: " + e.getLocalizedMessage();
				JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}