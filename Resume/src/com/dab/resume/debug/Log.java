/********************************************************************************************************
 * Project:     Résumé
 * File:        Log.java
 * Authors:     Drew Burden
 *
 * Copyright © 2014 Drew Burden
 * All rights reserved.
 *
 * Description:
 *      A global tool for logging general debug information, errors, and warnings.
 ********************************************************************************************************/

package com.dab.resume.debug;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
	public static void verboseLog(String message) {	if (DebugFlags.DEV_LOG_VERBOSE) log(message); }
	public static void verboseLog() { if (DebugFlags.DEV_LOG_VERBOSE) log(); }
	public static void log() { if (DebugFlags.DEV_LOG) log(""); }
	public static void log(String message) {
		if (DebugFlags.DEV_LOG) {
			printGeneralDebugInfo();
			System.out.println(message);
		}
	}
	// Always log errors
	public static void error(String message) {
		System.err.print("Error: ");
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (DebugFlags.DEV_LOG_TIME) System.out.print(getTime() + " ");
		if (DebugFlags.DEV_LOG_CALLER_FILE) System.out.print(getCallingFileInfo(stackTraceElements[3]) + " ");
		if (DebugFlags.DEV_LOG_CALLER_CLASS) System.out.print(getCallingClassInfo(stackTraceElements[3]) + " ");
		System.err.println(message);
	}
	public static void warn(String message) {
		if (DebugFlags.DEV_LOG) {
			System.err.print("Warning: ");
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			if (DebugFlags.DEV_LOG_TIME) System.out.print(getTime() + " ");
			if (DebugFlags.DEV_LOG_CALLER_FILE) System.out.print(getCallingFileInfo(stackTraceElements[3]) + " ");
			if (DebugFlags.DEV_LOG_CALLER_CLASS) System.out.print(getCallingClassInfo(stackTraceElements[3]) + " ");
			System.err.println(message);
		}
	}

	private static void printGeneralDebugInfo() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (DebugFlags.DEV_LOG_TIME) System.out.print(getTime() + " ");
		if (DebugFlags.DEV_LOG_CALLER_FILE) System.out.print(getCallingFileInfo(stackTraceElements[4]) + " ");
		if (DebugFlags.DEV_LOG_CALLER_CLASS) System.out.print(getCallingClassInfo(stackTraceElements[4]) + " ");
	}

	private static String getTime() {
		return "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "]";
	}
	private static String getCallingFileInfo(StackTraceElement stackTraceElement) {
		return "<" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ">";
	}
	private static String getCallingClassInfo(StackTraceElement stackTraceElement) {
		String className = stackTraceElement.getClassName();
		// Strip off the first few packages from the class name to save some space
		for (int numPackagesStripped = 0; numPackagesStripped < 3; ++numPackagesStripped) {
			className = className.substring(className.indexOf('.')+1, className.length());
		}
		return "(" + className + ") " + stackTraceElement.getMethodName() + "()";
	}

	public static void printStackTrace() { Thread.dumpStack(); }
}
