/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.experiment;

/**
 *
 * @author Chantal
 */
public class ModelItem implements ModelItemIF{

    private String key;
    private String name;
    private String description;
    int count;
    private Experiment experiment;

    public ModelItem(String key, String name, String description) {
        this.key = key;
        this.name = name;
        this.description = description;
        
    }
    
    public void init() {
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    public String toString(){
        return name;
    }
    
    public void p(String s) {
        count++;
        if (count % 100 == 0) System.out.println(getClass().getName()+":"+s);
    }

    /**
     * @return the experiment
     */
    public Experiment getExperiment() {
        return experiment;
    }

    /**
     * @param experiment the experiment to set
     */
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }
}
