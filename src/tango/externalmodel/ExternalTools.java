/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.externalmodel;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import tango.prefs.UserPref;
import tango.utils.ErrorHandler;
import tango.utils.Executor;
import tango.utils.FileTools;
import tango.utils.StringTools;

/**
 *
 * @author Chantal
 */
public class ExternalTools {

    public static int getIntFromCommand(String commandLine) {
        String result = Executor.execute(commandLine);
        p("Got result: " + result + " from " + commandLine);
        
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {
            ErrorHandler.showError("Could read int from "+result+", command was " + commandLine, e);
        }
        return -1;
    }
     public static double getDoubleFromCommand(String commandLine) {
        String result = Executor.execute(commandLine);
        p("Got result: " + result + " from " + commandLine);
        try {
            return Double.parseDouble(result);
        } catch (Exception e) {
            ErrorHandler.showError("Could read double from "+result+", command was " + commandLine, e);
        }
        return -1;
    }

    public static ArrayList<Double> getListOfValuesFromFile(String file) {
        String content = FileTools.getFileAsString(file, false, true);

        return StringTools.parseListToDouble(content, ",");

    }

    public static String getCsvFile(UserPref pref) {
        return FileTools.getFile(pref.getName(), ".csv, .txt", pref.getValue());
    }

    public static ArrayList<Double> getListOfValues(UserPref pref, String name) {
        
       
        PickFilePanel fpanel= new PickFilePanel(pref);
        
        
        JOptionPane.showMessageDialog(null, fpanel, name, JOptionPane.QUESTION_MESSAGE);
        String input = fpanel.getContent();
        if (input == null || input.length() < 0) {
            return null;
        }

        input = input.replace(";", ",");
        input = input.replace("\n", ",");
        input = input.replace(",,", ",");

        return StringTools.parseListToDouble(input, ",");
    }

    private static void p(String msg) {
        System.out.println("ExternalTools: " + msg);
        Logger.getLogger(ExternalTools.class.getName()).log(Level.INFO, msg);
    }
}
