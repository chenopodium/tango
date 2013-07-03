/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.utils;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 *
 * @author Chantal Roth
 */
 public abstract class Task extends SwingWorker<Void, Void> implements ProgressListener{
    
    private ProgressListener proglistener;
    private JFrame frame;
    
    
    public Task(ProgressListener proglistener) {
         this.proglistener = proglistener;
   
    }
   
    @Override
   public String toString() {
       String s= this.getClass().getName();
       return s;
   }
  
   
    public abstract boolean isSuccess();
    
    @Override
    public void stop() {
        this.cancel(true);
    }
   
    public ProgressListener getProgListener() {
        return getProglistener();
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        p("Task "+getClass().getName()+"  is done");           
        closeProgressFrame();
        if (getProglistener() != null) getProglistener().stop();
    }
    public JFrame getProgressFrame() {
        return frame;
    }
    public void closeProgressFrame() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
    }
     public void setText(String text) {       
        if (getProglistener() != null) getProglistener().setMessage(text);
    }
    @Override
    public void setProgressValue(int prog) {
        if (prog > 99) prog = 99;
        if (prog <= 0) prog = 1;
        super.setProgress(prog);
        if (getProglistener() != null) getProglistener().setProgressValue(prog);
    }
    /** ================== LOGGING ===================== */
    private void err(String msg, Exception ex) {
        Logger.getLogger(Task.class.getName()).log(Level.SEVERE, msg, ex);
    }
    
    private void err(String msg) {
        Logger.getLogger(Task.class.getName()).log(Level.SEVERE, msg);
    }
    
    private void warn(String msg) {
        Logger.getLogger(Task.class.getName()).log(Level.WARNING, msg);
    }
    
    private void p(String msg) {
        System.out.println("Task: " + msg);
        //Logger.getLogger( Task.class.getName()).log(Level.INFO, msg, ex);
    }
     @Override
    public void setMessage(String msg) {
        setText(msg);
    }

    /**
     * @return the proglistener
     */
    public ProgressListener getProglistener() {
        return proglistener;
    }

    /**
     * @param proglistener the proglistener to set
     */
    public void setProglistener(ProgressListener proglistener) {
        this.proglistener = proglistener;
    }
}
