/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import tango.guiutils.GuiUtils;

/**
 *
 * @author Chantal
 */
public class ErrorHandler {
    
	private static Throwable exception;

	public static void log(Exception e) {
		log("Error", e);
	}

	public static void log(Object obj, Exception e) {
		String trace = getString(e);
		String name = obj.getClass().getName();
		int dot = Math.max(0, name.lastIndexOf("."));
		name = name.substring(dot);
		err(name + ": " + trace);
	}

        public static void showError(String err) {
            showError(err, null);
        }
        public static void showError(String err, Exception e) {
            String msg = err;
            if (e != null) msg +="<br>"+ getString(e);
            GuiUtils.showNonModalDialog(msg, "Problem");
            
        }
	public static void log(String name, Exception e) {
		String trace = getString(e);
		err(name + ": " + trace);
	}

	
	// *****************************************************************
	// OUTPUT STUFF
	// *****************************************************************

	public static Throwable getLastException() {
		return exception;
	}

	// *****************************************************************
	// HELPER
	// *****************************************************************

	public static String getString(Throwable e) {
		exception = e;
		if (e == null) return "No exception";
		StringWriter w = new StringWriter();
		PrintWriter p = new PrintWriter(w, true);
		e.printStackTrace(p);
		String res = w.getBuffer().toString();
		return res;
	}

	// *****************************************************************
	// LOG
	// *****************************************************************
	private void err(String msg, Exception ex) {
		Logger.getLogger(ErrorHandler.class.getName()).log(Level.SEVERE, msg, ex);
	}

	private static void err(String msg) {
		Logger.getLogger(ErrorHandler.class.getName()).log(Level.SEVERE, msg);
	}

	private void warn(String msg) {
		Logger.getLogger(ErrorHandler.class.getName()).log(Level.WARNING, msg);
	}

	private static void p(String msg) {
		System.out.println("ErrorHandler: " + msg);
		Logger.getLogger( ErrorHandler.class.getName()).log(Level.INFO, msg);
	}
}
