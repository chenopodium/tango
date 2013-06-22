/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author Chantal
 */
public class Executor {
    private static boolean DEBUG = false;
    private String command;
    private StringBuffer out = new StringBuffer();
    private StringBuffer err = new StringBuffer();
    private Process process;
    private int exitval;
    
    String env[] = null;
    //{"PATH=.;C:\\java\\jdk142\\bin",
     //       "CLASSPATH=E:\\Tools\\eclipse3\\workspace\\BioSphere\\classes;C:\\computehost\\lib\\xml.jar;C:\\computehost\\lib\\log4j.jar;"
    //};
    class InputThread extends Thread {

        InputStream is;
        String type;

        InputThread(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        public void run() {
            readOutput(is, type);
        }

    }

    private String readOutput(InputStream is, String type) {
      
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
          //  p("reading..");
            while ((line = br.readLine()) != null) {
               if (type.equalsIgnoreCase("ERR")) err = err.append(line+"\n");
               else out = out.append(line+"\n");
                
           //     p("line is: "+line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (type.equalsIgnoreCase("ERR")) return err.toString();
        else return out.toString();
    }

    public static String execute(String ex) {
        p("Executing: "+ex);
        String[] args = new String[1];
        args[0] = ex;
        Executor e = new Executor(args);
        return ""+e.getExitval();
    }
    
    
//    public String runProcess(String[] cmd) {
//        ProcessBuilder pb = new ProcessBuilder(cmd);
//        pb.redirectErrorStream(true);
//        String res = "";
//        p("Running: "+Arrays.toString(cmd));
//        try {
//            Process proc = pb.start();
//            Reader reader = new InputStreamReader(proc.getInputStream());
//            int ch;
//            while ((ch = reader.read()) != -1) {
//                char c = (char) ch;
//                System.out.print(c);
//                res = res+c;
//            }
//            reader.close();
//            int state = proc.waitFor();
//            p("Process finished with :" + state);
// 
//        } catch (Throwable e) {
//            p("Problem in runProcess: " + ErrorHandler.getString(e));
//            return res;
//        }
//        return res;
//    }
    private String checkCmd(String cmd) {
        String osName = System.getProperty("os.name");
      
//        if ((!cmd.startsWith("java") &&  !cmd.startsWith("echo"))
//                || cmd.startsWith("cmd")) {
//            p("hack: '"+cmd+"' is not java or has a cmd, not adding cmd");
//            return cmd;
//        }
        if (cmd.startsWith("cmd") || cmd.startsWith("hostname")) {
            p("'"+cmd+"' has a cmd/hostname, not adding cmd");
            return cmd;
        }
        if (osName.equals("Windows NT") || osName.equals("Windows 2000")) {
            cmd = "cmd.exe /C "+cmd;
        } else if (osName.indexOf("Windows") > 0) {
            cmd = "command.com /C "+cmd;
        } 
        return cmd;
    }
    public String exec(String[] args) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            b = b.append(args[i]+" ");
        }
        
        return exec(b.toString().trim());
    }
    public String exec(String arg) {
        String res = null;
        try {
            String cmd = checkCmd(arg);
            Runtime rt = Runtime.getRuntime();
           
            System.out.println("executing: \n"+cmd);   
           //  p("env: "+env.toString());
            process = rt.exec(cmd, env);
         
            InputThread errorGobbler = new InputThread(process.getErrorStream(), "ERR");
            InputThread outputGobbler = new  InputThread(process.getInputStream(), "");
            errorGobbler.start();
            outputGobbler.start();

            exitval = process.waitFor();
            p("ExitValue: " + exitval);
         //   p("proc: " + proc);
         //   proc.wait(2000);
           // err = readOutput(proc.getErrorStream());
          //  out = readOutput(proc.getInputStream());

        } catch (Throwable t) {
            t.printStackTrace();
        }
        p("err: "+err);
        p("out: "+out);
        return ""+exitval;
    }

    public Executor(String[] args, String[] env) {
        this.env = env;
        exec(args);
    }

    public Executor(String[] args) {
        exec(args);
    }
    // *****************************************************************
    // MAIN (for testing)
    // *****************************************************************

    public Executor(String cmdline) {
        exec(cmdline);
    }

    public static void main(String args[]) {

        String name = "Executer";
        
        String cmd = "dir";
        if (args.length < 1) {
            System.out.println("USAGE: java Executer <cmd>");
            Executor ex = new Executor(cmd);
        }
        else {
        	Executor ex = new Executor(args);
        }
        System.exit(0);

    }

    // *****************************************************************
    // DEBUG
    // *****************************************************************

    protected static void p(String s) {
        //if (DEBUG) {
       // 	Logger.getLogger("Executor").info(s);
        	System.out.println("Executor:" + s);
        //}
    }

    protected static void err(String s) {
    	Logger.getLogger("Executor").warning(s);
     //   System.out.println("ERROR: Executor:" + s);
    }

    public String getErr() {
        return err.toString();
    }

    public String getOut() {
        if (out == null || out.length()<1) {
            ErrorHandler.showError("Could not execute "+command+", got no result");
        }
        return out.toString().trim();
    }
    public Process getProcess() {
        return process;
    }
    public int getExitval() {
        return exitval;
    } 
}
