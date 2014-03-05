/********************************************************************************************************
 * Project:     Résumé
 * File:        DebugFlags.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      Holds global flags related to debugging and development options
 ********************************************************************************************************/

package com.dab.resume.debug;

public class DebugFlags {
	public static final boolean DEV_LOG = false;
	public static final boolean DEV_LOG_VERBOSE = false;
	public static final boolean DEV_LOG_TIME = true;
	public static final boolean DEV_LOG_CALLER_FILE = true;
	public static final boolean DEV_LOG_CALLER_CLASS = true;

	public static final boolean DEV_ASSET_MONITORING = false; // This determines whether the program should monitor changes in assets and reload the assets if they've been changed

	public static final boolean DEV_ALLOW_DEBUG_VIEW = false; // Lets you view a lot of debug stuff like bounding boxes and removes the environment graphics
	public static boolean DEV_DEBUG_VIEW = false;
}
