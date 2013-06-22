package tango.experiment;

/**
 * @author Chantal Roth 
 */
public class Particle {

    private String name;
    
    private HiddenVariablesIF hiddenVars;
    
    public Particle(String name) {
        this.name = name;
    }

    public boolean isA() {
        return name.equalsIgnoreCase("A");
    }

    public boolean isB() {
        return !(isA());
    }

    @Override
    public String toString() {
        return "Particle " + name+", hidden vars: "+getHiddenVars();
    }

    /**
     * @return the hiddenVars
     */
    public HiddenVariablesIF getHiddenVars() {
        return hiddenVars;
    }

    /**
     * @param hiddenVars the hiddenVars to set
     */
    public void setHiddenVars(HiddenVariablesIF hiddenVars) {
        this.hiddenVars = hiddenVars;
    }

   
}
