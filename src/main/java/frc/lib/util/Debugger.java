/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frc.lib.util;

import java.util.ArrayList;

/**
 * Based on Team 1114 code from 2015
 * 
 * You can turn off all debug messages easily. You can also set different Flag levels.
 */
public class Debugger {

	public static final int verbose1 = 1;
	public static final int debug2 = 2;
	public static final int info3 = 3;
	public static final int warning4 = 4;
	public static final int error5 = 5;
	public static final int fatal6 = 6;
	public static final int silent7 = 7; //Nothing is printed
	
	private static ArrayList<String> currFlags; 
    private static boolean defaultOn;
    private static int currLevel;
    
    static {
        currFlags = new ArrayList<>();
        currLevel = 7;
        defaultOn = false;
    }
    
    public static void println(String msg) {
        if(defaultOn) {
            System.out.println("[DEBUG] " + msg);
        }
    }
    
    /**
     * Check if we should print the message based on flag and level
     * Print the level and flag as well
     * If the level is error or above also send it to DS output
     * @param msg
     * @param flag
     * @param level
     */
    public static void println(String msg, String flag, int level) {
        if(meetsCurrRequirements(flag, level)) {
            System.out.println(level + ": [" + flag + "] " + msg);
        }
        /*
         * Disabled until We can figure out where this method lives now
        if (level >= error5){
        	writeToDS(msg);
        }
        */
    }
    
    public static void println(String msg, String flag) {
        println(msg, flag, 0);
    }
    
    public static void println(int msg) {
        println("" + msg);
    }
    
    public static void println(int msg, String flag) {
        println("" + msg, flag);
    }
    
    public static void println(int msg, String flag, int level) {
        println("" + msg, flag, level);
    }
    
    public static void println(double msg) {
        println("" + msg);
    }
    
    public static void println(double msg, String flag) {
        println("" + msg, flag);
    }
    
    public static void println(double msg, String flag, int level) {
        println("" + msg, flag, level);
    }
    
    public static void println(float msg) {
        println("" + msg);
    }
    
    public static void println(float msg, String flag) {
        println("" + msg, flag);
    }
    
    public static void println(float msg, String flag, int level) {
        println("" + msg, flag, level);
    }
    
    public static void println(long msg) {
        println("" + msg);
    }
    
    public static void println(long msg, String flag) {
        println("" + msg, flag);
    }
    
    public static void println(long msg, String flag, int level) {
        println("" + msg, flag, level);
    }
    
    public static void println(boolean msg) {
        println("" + msg);
    }
    
    public static void println(boolean msg, String flag) {
        println("" + msg, flag);
    }
    
    public static void println(boolean msg, String flag, int level) {
        println("" + msg, flag, level);
    }
    
    public static void println(Object msg) {
        println(msg.toString());
    }
    
    public static void println(Object msg, String flag) {
        println(msg.toString(), flag);
    }
    
    public static void println(Object msg, String flag, int level) {
        println(msg.toString(), flag, level);
    }
    
    public static void println(byte msg) {
        println("" + msg);
    }
    
    public static void println(byte msg, String flag) {
        println("" + msg, flag);
    }
    
    public static void println(byte msg, String flag, int level) {
        println("" + msg, flag, level);
    }
    
    public static void println(char msg) {
        println("" + msg);
    }
    
    public static void println(char msg, String flag) {
        println("" + msg, flag);
    }
    
    public static void println(char msg, String flag, int level) {
        println("" + msg, flag, level);
    }
    
    public static void println(char[] msg) {
        println(new String(msg));
    }
    
    public static void println(char[] msg, String flag) {
        println(new String(msg), flag);
    }
    
    public static void println(char[] msg, String flag, int level) {
        println(new String(msg), flag, level);
    }
    
    public static void flagOn(String flag) {
        if(!currFlags.contains(flag)) {
            currFlags.add(flag);
        }
    }
    
    public static void flagOff(String flag) {
        currFlags.remove(flag);
    }
    
    public static void allFlagsOff() {
        currFlags.clear();
    }
    
    public static void defaultOn() {
        defaultOn = true;
    }
    
    public static void defaultOff() {
        defaultOn = false;
    }
    
    public static void setLevel(int level) {
        currLevel = level;
    }  
    
    //Check if the flag is set and if the level is high enough to print
    private static boolean meetsCurrRequirements(String flag, int level) {
        for(int i = 0; i < currFlags.size(); i++) {
            if(((String) currFlags.get(i)).equals(flag) && level >= currLevel) {
                return true;
            }
        }
        return false;
    }
    
    /*
    //written by 1554
    public static final void writeToDS(String message) {
		final ControlWord controlWord = new ControlWord();
		if (controlWord.getDSAttached()) {
			FRCNetworkCommunicationsLibrary.HALSetErrorData(message);
		}
	}
    
    //written by 1554
    public static final void exceptionToDS(Throwable t) {
		final StackTraceElement[] stackTrace = t.getStackTrace();
		final StringBuilder message = new StringBuilder();
		final String separator = "===\n";
		final Throwable cause = t.getCause();

		message.append("Exception of type ").append(t.getClass().getName()).append('\n');
		message.append("Message: ").append(t.getMessage()).append('\n');
		message.append(separator);
		message.append("   ").append(stackTrace[0]).append('\n');

		for (int i = 1; i < stackTrace.length; i++) {
			message.append(" \t").append(stackTrace[i]).append('\n');
		}

		if (cause != null) {
			final StackTraceElement[] causeTrace = cause.getStackTrace();
			message.append(" \t\t").append("Caused by ").append(cause.getClass().getName()).append('\n');
			message.append(" \t\t").append("Because: ").append(cause.getMessage()).append('\n');
			message.append(" \t\t   ").append(causeTrace[0]).append('\n');
			message.append(" \t\t \t").append(causeTrace[2]).append('\n');
			message.append(" \t\t \t").append(causeTrace[3]);
		}

		writeToDS(message.toString());
	}
	*/
}
