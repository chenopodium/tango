/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.models;

/**
 *
 * @author Chantal
 */
public interface ModelItemIF {
    public String getKey() ;
   
    public String getName();
    
    public void check();

    public void init();
   
    public String getDescription();
}
