package tango.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import tango.experiment.CorrelationResults;

/**
 * simple class that draws the correlation result from the experiment
 */
public class ResultsPanel extends JPanel {

    private CorrelationResults measure;

    public ResultsPanel(CorrelationResults measure) {
        this.measure = measure;
        setLayout(new BorderLayout());
        add("Center", new RabPanel(measure));
    }

    public static void show(CorrelationResults measure) {

        JFrame f = new JFrame();
        RabPanel p = new RabPanel(measure);

        f.getContentPane().add(new JScrollPane(p));
        f.pack();
        p.setSize(600, 600);
        f.setSize(600, 600);

        f.show();
    }
}
