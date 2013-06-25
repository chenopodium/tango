package tango.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import tango.experiment.CorrelationResults;
import tango.experiment.Setup;
import tango.results.AbstractResult;

/** simple class that draws the correlation result from the experiment */
public class RabPanel extends JPanel {

    
    double xy[][];
    
    boolean tiny;
    
    AbstractResult res;

    public RabPanel(CorrelationResults measure, AbstractResult res) {
        this(measure, false, res);
    }
    public RabPanel(CorrelationResults measure, boolean tiny, AbstractResult res) {
        this.tiny = tiny;
        xy = measure.getCorrelations();
        this.res = res;
        
    }

    
    public BufferedImage getImage(int w, int h) {
         BufferedImage bimage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
         this.paintComponent(bimage.createGraphics());
         return bimage;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // clear
        int w = this.getWidth();
        int h = this.getHeight();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);

        int BORDER = 40;
        if (tiny) BORDER = 5;
        // x from 0 to 360
        double dx = (double) (w - 2 * BORDER) / 360.0;
        double dy = (double) (h - 2 * BORDER) / 2.0;
        int my = BORDER + (int) ((h - 2 * BORDER) / 2.0);
        g.setColor(Color.black);
        g.drawLine(BORDER, my, w - BORDER, my);
        g.drawLine(BORDER, BORDER, BORDER, h - BORDER);

        g.drawString("Black: cos(alpha)", BORDER, 12);
        g.drawString("Blue:  Rab(alpha)", BORDER, 25);
        //	p("w,h:"+w+", "+h);
        //	p("dx, dy:"+dx+", "+dy);

        DecimalFormat f = new DecimalFormat("0.0");
        // draw angles
        for (double x = 0; x <= 360.0; x += 22.5) {
            int guix = (int) (x * dx) + BORDER;
            int guiy = (int) (my + 15);
            g.drawLine(guix, my - 3, guix, my + 3);
            if(!tiny)  g.drawString("" + f.format(x), guix - 10, guiy);
        }
        // draw y values from -1 to 1
        
        for (double y = -1.0; y <= 1.0; y += 0.1) {
            int guix = BORDER - 20;
            int guiy = (int) (my - y * dy);
            g.drawLine(guix + 10, guiy, BORDER, guiy);
            if(!tiny) g.drawString("" + f.format(y), guix - 10, guiy + 5);

        }
        g.setColor(Color.blue);
        int iold = 0;
        for (int i = 0; i < xy.length; i++) {

            double x = xy[i][0];
            double y = xy[i][1];
            double xold = xy[iold][0];
            double yold = xy[iold][1];
            // scale x from 0 - width
            // scale y from 0 to height
            int guix = (int) (x * dx) + BORDER;
            int guiy = (int) (my - y * dy);
            int guixold = (int) (xold * dx) + BORDER;
            int guiyold = (int) (my - yold * dy);
            g.drawOval(guix, guiy, 5, 5);
            g.drawLine(guix, guiy, guixold, guiyold);
            iold = i;

            //		p("Plotting x/y "+y+"/"+y);
        }
        // now draw cosine!
        
        double xold = 0;
       
        for (double x = 0; x <= 360; x += 0.5) {
            g.setColor(Color.black);
            double y = res.computeValueForAngle(x);
            double yold = res.computeValueForAngle(xold);
            // scale x from 0 - width
            // scale y from 0 to height
            int guix = (int) (x * dx) + BORDER;
            int guiy = (int) (my - y * dy);
            int guixold = (int) (xold * dx) + BORDER;
            int guiyold = (int) (my - yold * dy);
            g.drawOval(guix, guiy, 1, 1);
            g.drawLine(guix, guiy, guixold, guiyold);
            if (res.hasRedAngle(x) ) {
                g.setColor(Color.red);
                g.drawLine(guix, my, guix, guiy);
            }
            else if (res.hasGreenAngle(x)) {
                g.setColor(Color.green);
                g.drawLine(guix, my, guix, guiy);
            }
            xold = x;
            //		p("Plotting x/y "+y+"/"+y);
        }
    }

    private void p(String string) {
        System.out.println("RabPanel:" + string);

    }
}
