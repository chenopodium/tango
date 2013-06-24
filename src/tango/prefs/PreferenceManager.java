/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.prefs;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Chantal
 */
public class PreferenceManager {
    
    private static Preferences prefs ;             
    private static PreferenceManager manager;
    private ArrayList<UserPref> userprefs;
    
    
    public UserPref nr_angles;
    public UserPref outputFolder;
    public UserPref model;
    public UserPref times;
    public UserPref seed;
    
    public UserPref extanglesa;
    public UserPref extanglesb;
    
    public UserPref exteff;
    public UserPref exteffargs;
    
    public UserPref extmeas;
    public UserPref extmeasargs;
    
    static {
        manager  = new PreferenceManager();
        if (manager != null) manager.createPrefs();
    }
     private void createPrefs() {
        nr_angles = add("NR_ANGLES", "Number of angles", "8", "The number of angles to use in this simulation");
        times = add("TIMES", "Number of pairs", "1000", "The number of pairs to generate per simulation");
        model = add("MODEL", "Experiment model", "default", "Experiment model to use");
        outputFolder = add("OUTPUT_FOLDER", "Output folder", ".", "Output folder for results");
        seed = add("RANDOM_SEED", "Random seed", "1234", "Default random seed to use to initialize random number generator");
        exteff= add("EXTERNAL_EFFICIENCY_PROG", "External efficiency prog", "externalprogs/sampleeff.bat", "Program that computes the detector efficiency for one particle and detector");
        exteffargs= add("EXTERNAL_EFFICIENCY_PROG_ARGS", "External efficiency prog", "#particle #detector", "Program arguments");
        
        extanglesa= add("EXTERNAL_ANGLES_A", "External angles", "externalprogs/anglesa.csv", "File containing external angles in .csv format");
        extanglesb= add("EXTERNAL_ANGLES_B", "External angles", "externalprogs/anglesb.csv", "File containing external angles in .csv format");
        extmeas = add("EXTERNAL_MEASUREMENT", "External measurement program", "extermalprogs/samplemeasure.bat", "Program to compute the spin for one particle and detector");
        extmeasargs = add("EXTERNAL_MEASUREMENT_ARGS", "External measurement args", "#particle #detector", "Program arguments");
        // add the user prefs
    }
    
    private PreferenceManager() {    
        init();
    }
    public static Preferences getPreferences() {
        return manager.prefs;
    }
    
    private void init() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        userprefs = new ArrayList<UserPref>();
       
    }
    public String getOutputFolder() {
        return outputFolder.getValue();
    }
    public int getSeed() {
        return seed.getInt();
    }
    public void setSeed(int nr) {
        seed.setValue(nr);
    }
    
    public int getNrAngles() {
        return nr_angles.getInt();
    }
    /** the angle between measurements */
    public double getAngleDelta() {
        return 360.0/(double)getNrAngles();
    }
    private UserPref add(String key, String name, String defaultValue, String description ) {
       
        UserPref userpref = new UserPref(key, name, defaultValue, description);
        userprefs.add(userpref);
        // get the value from the user preferences file
        userpref.setValue(prefs.get(key, defaultValue));
        return userpref;
    }
    public static PreferenceManager getManager() {
        return manager;
    }
    
    public void set(String key, String val) {
        prefs.put(key, val);
        try {
            prefs.sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(PreferenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String get(String key, String def){
        return prefs.get(key, def);
    }
    public void set(String key, double val) {
        prefs.putDouble(key, val);
        try {
            prefs.sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(PreferenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     private static void p(String msg) {
        System.out.println("PreferenceManager: " + msg);
        Logger.getLogger(PreferenceManager.class.getName()).log(Level.INFO, msg);
    }

   
}
