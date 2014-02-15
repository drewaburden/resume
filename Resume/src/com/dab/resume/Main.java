/********************************************************************************************************
 * Project:     Resume
 * File:        Main.java
 * Authors:     Drew Burden
 * 
 * Copyright © 2013 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      This application is a fun little all-in-one resume/cover letter/references info/demonstration
 *      intended to reduce some of the blandness associated with looking through resumes and cover
 *      letters.
 *      Note: Just about everything in this application (strings, sprites, animations, settings, etc.)
 *      is hard-coded, just for the sake of time. In a real world situation and for work I've done in
 *      the past, I would have coded a subsystem for dynamically loading all of this information from
 *      files, typically XML.
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

public class Main {
	public static void main(String[] args) {
	    // Configure LibGDX desktop application
	    LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	    cfg.resizable = false;
	    cfg.vSyncEnabled = true;
	    //cfg.foregroundFPS=10;
	    cfg.useGL20 = true;
	    cfg.width = 720; cfg.height = 540; // 4:3 aspect ratio, because old-school. Shut up, you're not my mom.
	    cfg.title = "Drew Burden's Terminal Game";

	    // Instantiate LibGDX
	    new LwjglApplication(new TerminalGame(), cfg);
	}
}