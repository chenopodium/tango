/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.utils;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Chantal Roth
 */
public class FileTools {

    public static String getFile(String title, String ext, String val) {
        return getFile(title, ext, val, false);
    }

    public static String getFile(String title, String ext, String val, boolean toSave) {
        JFileChooser cc = new JFileChooser();
        cc.setDialogTitle(title);


        if (val != null) {
            File f = new File(val);
            // if (!dir.isDirectory()) dir 
            cc.setSelectedFile(f);
            if (f.isDirectory()) {
                cc.setCurrentDirectory(f);
            } else if (f.getParentFile() != null) {
                cc.setCurrentDirectory(f.getParentFile());
            }
        }
        cc.setVisible(true);
        String[] Ext = new String[]{ext};
        if (ext.indexOf(",") > 0) {
            Ext = StringTools.parseList(ext, ",").toArray(Ext);

        }
        System.out.println("Ext: "+ Arrays.toString(Ext));
        ExtensionFileFilter filter1 = new ExtensionFileFilter(Arrays.toString(Ext) + " files", Ext);
        cc.setFileFilter(filter1);

        String res = val;
        int ans = 0;
        if (!toSave) {
            ans = cc.showOpenDialog(null);
        } else {
            ans = cc.showSaveDialog(null);
        }
        if (ans == JOptionPane.OK_OPTION) {
            File f = cc.getSelectedFile();
            if (toSave && f != null && ext != null && ext.length() > 0 && !ext.endsWith("*")) {
                String file = f.getAbsolutePath();
                if (ext.startsWith("*")) {
                    ext = ext.substring(1);
                }
                if (!file.endsWith(ext)) {
                    if (!ext.startsWith(".")) {
                        ext += ".";
                    }
                    p("Attaching " + ext + " to " + file);
                    file = file + ext;
                    f = new File(file);
                }
            }

            if (!f.exists()) {
                if (!toSave) {
                    JOptionPane.showMessageDialog(new JFrame(), f + " does not exist - please select an existing file");
                }
            } else {
                if (toSave) {

                    int ok = JOptionPane.showConfirmDialog(new JFrame(), "Would you want to overwrite file " + f + "?", "File exists", JOptionPane.YES_NO_OPTION);
                    if (ok != JOptionPane.YES_OPTION) {
                        return null;
                    }
                }
            }
            if (f.isDirectory()) {
                JOptionPane.showMessageDialog(null, f + " is a directory - please select a file");
                //f.get
            } else {
                res = f.getAbsolutePath();

            }

        } else {
            res = null;
        }
        return res;

    }
     /**
     * Returns the content of a file as a string.    
     * @param ignorefirst if true, ignores the first line
     * @param ignoreempt if true, skips empty lines
     * @return
     */
    public static String getFileAsString(String filename, boolean ignorefirst, boolean ignoreempty) {
        //	p("getFileAsString: File is "+filename);
        if (filename == null) {
           
            return null;
        }
        BufferedReader in = null;
        StringBuilder res = new StringBuilder();
        int count = 0;
        try {
            in = new BufferedReader(new FileReader(filename));
            while (in.ready()) {
                // res.append((char)in.read());
            	String line = in.readLine();
            	if (!ignorefirst || count >  0) {
	            	if (!ignoreempty || (line != null && line.trim().length()>0)) {
	            		res.append(line);
	            		res.append("\n");
	            	}
            	}
            	count++;
            }
            in.close();
            //		p("getFileAsString: Result is:\n"+res);
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
           
        }
        finally {
            if (in != null) try {
                in.close();
            } catch (IOException ex) {
             
            }
        }
        
        return res.toString();
    }

    public static boolean writeStringToFile(File f, String content, boolean append) {
        PrintWriter fout = null;
        try {
            fout = new PrintWriter(new BufferedWriter(new FileWriter(f, append)));
            fout.print(content);
            fout.flush();
            fout.close();
            return true;
        } catch (FileNotFoundException e) {
            err("File " + f + " not found");
        } catch (IOException e) {
            err("IO Exception");
        } finally {
            if (fout != null) {
                fout.flush();
                fout.close();
            }
        }
        return false;
    }

    private static void err(String msg, Exception ex) {
        Logger.getLogger(FileTools.class.getName()).log(Level.SEVERE, msg, ex);
    }

    private static void err(String msg) {
        Logger.getLogger(FileTools.class.getName()).log(Level.SEVERE, msg);
    }
    
    private static void p(String msg) {
        Logger.getLogger(FileTools.class.getName()).log(Level.INFO, msg);
    }
}
