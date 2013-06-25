package tango.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import tango.experiment.CorrelationResults;
import tango.results.AbstractResult;

/**
 * simple class that draws the correlation result from the experiment
 */
public class ResultsPanel extends JPanel {

    public ResultsPanel(CorrelationResults measure, AbstractResult res) {
        setLayout(new BorderLayout());
        RabPanel p = new RabPanel(measure, res);

        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout());
        pan.add("Center", new JScrollPane(p));
        pan.add("North", new JLabel(res.getName() + ": " + res.getDescription()));
        pan.add("South", new JLabel("Experiment result: " + res.computeValue()));
        add("Center", pan);
    }
}
