/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.utils;

public interface ProgressListener {
    public void setProgressValue(int progress);
    /** In case the situation changes! */
    public void setMessage(String msg);
    public void stop();
}