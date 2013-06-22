/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.prefs;


import java.util.logging.Logger;

/**
 *
 * @author Chantal
 */
public class UserPref {
    private static Logger log = Logger.getLogger("UserPref");
    
    private static PreferenceManager prefs =PreferenceManager.getManager() ;
    
    private String key;
    private String name;
    private String defaultValue;
    private String description;
        
    public UserPref (String key, String name, String defaultValue) {
        this(key, name, defaultValue, name);
    }
    public UserPref (String key, String name, String defaultValue, String description) {
        this.key = key;
        this.name = name;
        this.defaultValue = defaultValue;
        this.description = description;
    }
    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the dvalue
     */
    public boolean  getBoolean() {        
        try {
            return Boolean.parseBoolean(getValue());
        }
        catch (Exception e) {
            log.warning("Could not parse to boolean: "+getValue());
        }
        return false;
    }
    /**
     * @return the dvalue
     */
    public double getDouble() {        
        try {
            return Double.parseDouble(getValue());
        }
        catch (Exception e) {
            log.warning("Could not parse to number: "+getValue());
        }
        return 0;
    }
    public int getInt() {        
        try {
            return Integer.parseInt(getValue());
        }
        catch (Exception e) {
            log.warning("Could not parse to number: "+getValue());
        }
        return 0;
    }

    /**
     * @param dvalue the dvalue to set
     */
    public void setValue(double dvalue) {
        setValue(""+dvalue);
    }
    public void setValue(int value) {
        setValue(""+value);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return prefs.get(key, defaultValue);
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {        
        prefs.set(key, value);
    }
    
}
