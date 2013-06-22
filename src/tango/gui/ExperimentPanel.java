/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tango.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import tango.defaultmodel.DefaultExperimentModel;
import tango.experiment.AngleGeneratorIF;
import tango.experiment.CorrelationResults;
import tango.experiment.Detector;
import tango.experiment.DetectorEfficiencyIF;
import tango.experiment.Experiment;
import tango.experiment.ExperimentModel;
import tango.experiment.MeasurementFormulaIF;
import tango.experiment.ModelFactory;
import tango.experiment.ModelItemIF;
import tango.experiment.Setup;
import tango.guiutils.GuiUtils;
import tango.prefs.PreferenceManager;
import tango.utils.ErrorHandler;
import tango.utils.FileTools;
import tango.utils.RandomUtils;

/**
 *
 * @author Chantal
 */
public class ExperimentPanel extends javax.swing.JPanel {

    private ExperimentModel model;
    private Experiment experiment;
    private Detector detectorA;
    private Detector detectorB;
    private int times;
    private Setup setup = Setup.getSetup();
    private PreferenceManager prefs = PreferenceManager.getManager();
    private CorrelationResults result;
    private DecimalFormat f = new DecimalFormat("#.##");
    private JPanel detailPanel;

    /**
     * Creates new form ExperimentPanel
     */
    public ExperimentPanel(JPanel detailPanel) {
        this.detailPanel = detailPanel;

        initComponents();
        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        this.rabContainer.setLayout(new BorderLayout());
        times = prefs.times.getInt();
        
        restart();

    }

    public void restart() {
        model = new DefaultExperimentModel();
        experiment = new Experiment(model);
        detectorA = experiment.getDetectorA();

        detectorB = experiment.getDetectorB();

        times = prefs.times.getInt();

        updateModel();
        updateGUI();
    }

    private void updateModel() {
        detectorA.setModel(model);
        detectorB.setModel(model);
    }

    private void updateGUI() {
        if (result != null) {
            this.lblCHSH.setText("CHSH: " + f.format(result.getCHSH()));
        } else {
            this.lblCHSH.setText("CHSH: no result yet");
        }

        this.lblLhvModel.setToolTipText(model.getDescription());
        this.btnLHVModel.setToolTipText(model.getDescription());
        this.lblEffA.setText(f.format(detectorA.getPercentDetected()) + "%");
        this.lblEffB.setText(f.format(detectorB.getPercentDetected()) + "%");
        this.btnSeed.setToolTipText("Current seed: " + prefs.getSeed());

        lblEffA.setToolTipText(detectorA.getDetectorEfficiency().getDescription());
        lblEffB.setToolTipText(detectorB.getDetectorEfficiency().getDescription());
        this.btnDetectorA.setToolTipText(detectorA.getDetectorEfficiency().getDescription());
        this.btnDetectorB.setToolTipText(detectorB.getDetectorEfficiency().getDescription());

        this.lblCountA.setText("#" + detectorA.getNrDetected());
        this.lblCountB.setText("#" + detectorB.getNrDetected());
        lblCountA.setToolTipText(model.getMeasurementFormula().getDescription());
        lblCountB.setToolTipText(model.getMeasurementFormula().getDescription());


        this.lblLhvModel.setText(model.getName());
        this.btnTimes.setToolTipText("Nr particle pairs to generate: " + times);
        //  this.

        lblAnglesA.setText(this.detectorA.getAngleGenerator().toString());
        lblAnglesB.setText(this.detectorB.getAngleGenerator().toString());
        btnAnglesB.setToolTipText(this.detectorB.getAngleGenerator().getDescription());
        btnAnglesA.setToolTipText(this.detectorA.getAngleGenerator().getDescription());

        this.lblNrPairs.setText("" + this.times);
        this.lblFormulaA.setText(this.detectorA.getMeasurementFormula().getName());
        this.lblFormulaB.setText(this.detectorB.getMeasurementFormula().getName());
        this.btnMeasurementA.setToolTipText(this.detectorA.getMeasurementFormula().getDescription());
        this.btnMeasurementB.setToolTipText(this.detectorB.getMeasurementFormula().getDescription());
        this.lblSeed1.setText("" + prefs.getSeed());
        this.lblSeed2.setText("" + prefs.getSeed());
        this.btnSeed.setToolTipText("" + prefs.getSeed());
        this.btnSeed1.setToolTipText("" + prefs.getSeed());
        this.lblCountA.setToolTipText("Number of detected particles");
        this.lblCountB.setToolTipText("Number of detected particles");
        if (result != null) {

            this.lblCHSH.setText("CHSH: " + f.format(result.getCHSH()));
            RabPanel chart = new RabPanel(result);
            detailPanel.removeAll();
            detailPanel.add("Center", chart);

            rabContainer.removeAll();

            RabPanel tiny = new RabPanel(result, true);
            rabContainer.add("Center", tiny);

            rabContainer.invalidate();
            rabContainer.revalidate();
            rabContainer.repaint();

        } else {
            this.btnResultsA.setToolTipText("No data yet - run the experiment first");
            this.btnResultsDB.setToolTipText("No data yet - run the experiment first");
            this.lblCHSH.setText("CHSH: No data yet");
        }
    }

    public void showChart() {
        if (result == null || result.getTotal() <1) {
            JOptionPane.showMessageDialog(this, "Run the experiment first");
            return;
        }
        RabPanel chart = new RabPanel(result);
        chart.setSize(800, 800);
        chart.setMaximumSize(new Dimension(800, 800));


        ResultsPanel.show(result);

        // save image to file
        String filename = "correlation_chart.png";
        String path = prefs.getOutputFolder();
        BufferedImage img = chart.getImage(1000, 1000);
        try {
            ImageIO.write(img, ".png", new File(path + filename));
        } catch (IOException ex) {
            Logger.getLogger(ExperimentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        p("Wrote image " + path + filename);


    }

    
    public void runExperiment() {

        
        detectorA.resetData();
        detectorB.resetData();
        for (int t = 0; t < times; t++) {
            experiment.runOnce();
            if (t % 100 == 0) {
                updateGUI();
            }
        }

        result = new CorrelationResults();
        result.computeCorrelations(detectorA, detectorB);
        result.saveResults();

        result.getCHSH();
        updateGUI();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLHVModel = new javax.swing.JButton();
        btnResultsDB = new javax.swing.JButton();
        btnResultsA = new javax.swing.JButton();
        btnDetectorB = new javax.swing.JButton();
        btnMeasurementA = new javax.swing.JButton();
        btnRun = new javax.swing.JButton();
        btnSeed = new javax.swing.JButton();
        btnCorrelations = new javax.swing.JButton();
        btnTimes = new javax.swing.JButton();
        btnDetectorA = new javax.swing.JButton();
        btnMeasurementB = new javax.swing.JButton();
        btnSeed1 = new javax.swing.JButton();
        btnAnglesA = new javax.swing.JButton();
        btnAnglesB = new javax.swing.JButton();
        rabContainer = new javax.swing.JPanel();
        btnChart = new javax.swing.JButton();
        lblFormulaB = new javax.swing.JLabel();
        lblSeed1 = new javax.swing.JLabel();
        lblNrPairs = new javax.swing.JLabel();
        lblFormulaA = new javax.swing.JLabel();
        lblSeed2 = new javax.swing.JLabel();
        lblAnglesA = new javax.swing.JLabel();
        lblAnglesB = new javax.swing.JLabel();
        lblCountA = new javax.swing.JLabel();
        lblCountB = new javax.swing.JLabel();
        lblEffA = new javax.swing.JLabel();
        lblEffB = new javax.swing.JLabel();
        lblCHSH = new javax.swing.JLabel();
        lblLhvModel = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        tool = new javax.swing.JToolBar();
        toolNew = new javax.swing.JButton();
        toolModel = new javax.swing.JButton();
        toolRun = new javax.swing.JButton();
        toolChart = new javax.swing.JButton();
        toolChart1 = new javax.swing.JButton();
        toolHelp = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLHVModel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLHVModel.setText("LHV Model");
        btnLHVModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLHVModelActionPerformed(evt);
            }
        });
        add(btnLHVModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, 120, 40));

        btnResultsDB.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnResultsDB.setText("Results B");
        btnResultsDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResultsDBActionPerformed(evt);
            }
        });
        add(btnResultsDB, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 630, -1, 30));

        btnResultsA.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnResultsA.setText("Results A");
        btnResultsA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResultsAActionPerformed(evt);
            }
        });
        add(btnResultsA, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 630, -1, 30));

        btnDetectorB.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDetectorB.setText("Detector B");
        btnDetectorB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetectorBActionPerformed(evt);
            }
        });
        add(btnDetectorB, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 45, -1, -1));

        btnMeasurementA.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnMeasurementA.setText("Formula A");
        btnMeasurementA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMeasurementAActionPerformed(evt);
            }
        });
        add(btnMeasurementA, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 480, -1, 30));

        btnRun.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRun.setText("Run");
        btnRun.setToolTipText("Run the experiment with the given nr of pairs and the specified model");
        btnRun.setContentAreaFilled(false);
        btnRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunActionPerformed(evt);
            }
        });
        add(btnRun, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 300, 100, 80));

        btnSeed.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSeed.setText("Random Seed");
        btnSeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeedActionPerformed(evt);
            }
        });
        add(btnSeed, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 120, -1));

        btnCorrelations.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCorrelations.setText("Correlations");
        btnCorrelations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorrelationsActionPerformed(evt);
            }
        });
        add(btnCorrelations, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 700, -1, 30));

        btnTimes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnTimes.setText("# Pairs");
        btnTimes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimesActionPerformed(evt);
            }
        });
        add(btnTimes, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, 90, -1));

        btnDetectorA.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDetectorA.setText("Detector A");
        btnDetectorA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetectorAActionPerformed(evt);
            }
        });
        add(btnDetectorA, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, -1, -1));

        btnMeasurementB.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnMeasurementB.setText("Formula B");
        btnMeasurementB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMeasurementBActionPerformed(evt);
            }
        });
        add(btnMeasurementB, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 480, -1, 30));

        btnSeed1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSeed1.setText("Random Seed");
        btnSeed1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeed1ActionPerformed(evt);
            }
        });
        add(btnSeed1, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 200, 120, -1));

        btnAnglesA.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAnglesA.setText("Angles A");
        btnAnglesA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnglesAActionPerformed(evt);
            }
        });
        add(btnAnglesA, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, -1));

        btnAnglesB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAnglesB.setText("Angles B");
        btnAnglesB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnglesBActionPerformed(evt);
            }
        });
        add(btnAnglesB, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 100, 90, -1));

        rabContainer.setToolTipText("Click to see a larger chart");
        rabContainer.setOpaque(false);
        rabContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rabContainerMouseClicked(evt);
            }
        });

        btnChart.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnChart.setText("Chart");
        btnChart.setContentAreaFilled(false);
        btnChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChartActionPerformed(evt);
            }
        });
        rabContainer.add(btnChart);

        add(rabContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 470, 180, 110));

        lblFormulaB.setText("formula b");
        add(lblFormulaB, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 510, 170, -1));

        lblSeed1.setText("seed1");
        add(lblSeed1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 60, -1));

        lblNrPairs.setText("nrpairs");
        add(lblNrPairs, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, 90, -1));

        lblFormulaA.setText("formula a");
        add(lblFormulaA, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 510, 180, -1));

        lblSeed2.setText("seed2");
        add(lblSeed2, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 230, 90, -1));

        lblAnglesA.setText("angles A");
        add(lblAnglesA, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 180, -1));

        lblAnglesB.setText("lblAnglesB");
        add(lblAnglesB, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 80, 130, -1));

        lblCountA.setText("6758");
        add(lblCountA, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 380, 40, 20));

        lblCountB.setText("3994");
        add(lblCountB, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 380, 40, 20));

        lblEffA.setText("98%   ");
        add(lblEffA, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, 60, 20));

        lblEffB.setText("98%   ");
        add(lblEffB, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 270, 70, 20));

        lblCHSH.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCHSH.setText("CHSH = 1.987");
        add(lblCHSH, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 680, 110, 20));

        lblLhvModel.setText("tango.hiddenvars.ChantalsModel");
        add(lblLhvModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 190, 180, -1));

        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/experimentnotext1.png"))); // NOI18N
        imageLabel.setText("98%");
        add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 1060, -1));

        tool.setRollover(true);

        toolNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/new1.png"))); // NOI18N
        toolNew.setToolTipText("New");
        toolNew.setFocusable(false);
        toolNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolNewActionPerformed(evt);
            }
        });
        tool.add(toolNew);

        toolModel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/config.png"))); // NOI18N
        toolModel.setToolTipText("Pick model");
        toolModel.setFocusable(false);
        toolModel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolModel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolModelActionPerformed(evt);
            }
        });
        tool.add(toolModel);

        toolRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/arrow-right.png"))); // NOI18N
        toolRun.setToolTipText("Run experiment");
        toolRun.setFocusable(false);
        toolRun.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolRunActionPerformed(evt);
            }
        });
        tool.add(toolRun);

        toolChart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/flowdiagram.png"))); // NOI18N
        toolChart.setToolTipText("Show larger version of the chart");
        toolChart.setFocusable(false);
        toolChart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolChart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolChartActionPerformed(evt);
            }
        });
        tool.add(toolChart);

        toolChart1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/view-list-icons-2.png"))); // NOI18N
        toolChart1.setToolTipText("Show correlations");
        toolChart1.setFocusable(false);
        toolChart1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolChart1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolChart1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolChart1ActionPerformed(evt);
            }
        });
        tool.add(toolChart1);

        toolHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/help-3.png"))); // NOI18N
        toolHelp.setFocusable(false);
        toolHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolHelp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolHelpActionPerformed(evt);
            }
        });
        tool.add(toolHelp);

        add(tool, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void btnLHVModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLHVModelActionPerformed
        pickModel();
    }//GEN-LAST:event_btnLHVModelActionPerformed

    private void btnResultsDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResultsDBActionPerformed
        String msg = experiment.getDetectorB().getResultsAsCsv();
        String filename = "results_detector_a";
        String path = prefs.getOutputFolder();
        FileTools.writeStringToFile(new File(path + filename + ".csv"), msg, false);

        GuiUtils.msg(msg);
    }//GEN-LAST:event_btnResultsDBActionPerformed

    private void btnDetectorBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetectorBActionPerformed
        pickEfficiency(detectorB);
    }//GEN-LAST:event_btnDetectorBActionPerformed

    private void btnResultsAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResultsAActionPerformed
        String msg = detectorA.getResultsAsCsv();
        String filename = "results_detector_a";
        String path = prefs.getOutputFolder();
        FileTools.writeStringToFile(new File(path + filename + ".csv"), msg, false);

        GuiUtils.msg(msg);
    }//GEN-LAST:event_btnResultsAActionPerformed

    private void btnMeasurementAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMeasurementAActionPerformed
        pickMeasurement(detectorA);
    }//GEN-LAST:event_btnMeasurementAActionPerformed

    private void btnCorrelationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorrelationsActionPerformed
        showCorr();
    }//GEN-LAST:event_btnCorrelationsActionPerformed

    private void btnSeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeedActionPerformed
        setSeed();

    }//GEN-LAST:event_btnSeedActionPerformed

    private void btnRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunActionPerformed
        this.runExperiment();
    }//GEN-LAST:event_btnRunActionPerformed

    private void btnTimesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimesActionPerformed
        setTimes();
    }//GEN-LAST:event_btnTimesActionPerformed

    private void btnDetectorAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetectorAActionPerformed
        pickEfficiency(detectorA);
    }//GEN-LAST:event_btnDetectorAActionPerformed

    private void btnMeasurementBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMeasurementBActionPerformed
        pickMeasurement(detectorB);
    }//GEN-LAST:event_btnMeasurementBActionPerformed

    private void btnSeed1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeed1ActionPerformed
        setSeed();
    }//GEN-LAST:event_btnSeed1ActionPerformed

    private void btnAnglesAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnglesAActionPerformed
        AngleGeneratorIF gen = pickAngleGen(detectorA);


    }//GEN-LAST:event_btnAnglesAActionPerformed

    private void btnAnglesBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnglesBActionPerformed
        pickAngleGen(detectorB);
    }//GEN-LAST:event_btnAnglesBActionPerformed

    private void rabContainerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rabContainerMouseClicked
        showChart();
    }//GEN-LAST:event_rabContainerMouseClicked

    private void btnChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChartActionPerformed
        showChart();
    }//GEN-LAST:event_btnChartActionPerformed

    private void toolNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolNewActionPerformed
       restart();
    }//GEN-LAST:event_toolNewActionPerformed

    private void toolHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolHelpActionPerformed
        help();
    }//GEN-LAST:event_toolHelpActionPerformed

    private void toolModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolModelActionPerformed
       pickModel();
    }//GEN-LAST:event_toolModelActionPerformed

    private void toolChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolChartActionPerformed
       showChart();
    }//GEN-LAST:event_toolChartActionPerformed

    private void toolRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolRunActionPerformed
       runExperiment();
    }//GEN-LAST:event_toolRunActionPerformed

    private void toolChart1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolChart1ActionPerformed
       this.showCorr();
    }//GEN-LAST:event_toolChart1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnglesA;
    private javax.swing.JButton btnAnglesB;
    private javax.swing.JButton btnChart;
    private javax.swing.JButton btnCorrelations;
    private javax.swing.JButton btnDetectorA;
    private javax.swing.JButton btnDetectorB;
    private javax.swing.JButton btnLHVModel;
    private javax.swing.JButton btnMeasurementA;
    private javax.swing.JButton btnMeasurementB;
    private javax.swing.JButton btnResultsA;
    private javax.swing.JButton btnResultsDB;
    private javax.swing.JButton btnRun;
    private javax.swing.JButton btnSeed;
    private javax.swing.JButton btnSeed1;
    private javax.swing.JButton btnTimes;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel lblAnglesA;
    private javax.swing.JLabel lblAnglesB;
    private javax.swing.JLabel lblCHSH;
    private javax.swing.JLabel lblCountA;
    private javax.swing.JLabel lblCountB;
    private javax.swing.JLabel lblEffA;
    private javax.swing.JLabel lblEffB;
    private javax.swing.JLabel lblFormulaA;
    private javax.swing.JLabel lblFormulaB;
    private javax.swing.JLabel lblLhvModel;
    private javax.swing.JLabel lblNrPairs;
    private javax.swing.JLabel lblSeed1;
    private javax.swing.JLabel lblSeed2;
    private javax.swing.JPanel rabContainer;
    private javax.swing.JToolBar tool;
    private javax.swing.JButton toolChart;
    private javax.swing.JButton toolChart1;
    private javax.swing.JButton toolHelp;
    private javax.swing.JButton toolModel;
    private javax.swing.JButton toolNew;
    private javax.swing.JButton toolRun;
    // End of variables declaration//GEN-END:variables

    private void setSeed() {
        String ans = JOptionPane.showInputDialog(this, "Enter an integer to set random seed", prefs.seed.getValue());
        try {
            int nr = Integer.parseInt(ans);
            prefs.setSeed(nr);
        } catch (Exception e) {
            ErrorHandler.showError("Could not convert " + ans + " to a seed (integer)");
        }
    }

    private void setTimes() {
        String ans = JOptionPane.showInputDialog(this, "Enter the nr of particle pairs to be generated", prefs.times.getValue());
        try {
            int nr = Integer.parseInt(ans);
            prefs.times.setValue(nr);
            this.times = nr;
            if (times > 10000) {
                JOptionPane.showMessageDialog(this, "<html>It might take a long time to compute " + times + " pairs.</html>");
            }
        } catch (Exception e) {
            ErrorHandler.showError("Could not convert " + ans + " to nr of pairs (integer)");
        }
        this.updateGUI();
    }

    private AngleGeneratorIF pickAngleGen(Detector detector) {
        String msg = " angle generator " + detector.getName();
        AngleGeneratorIF res = (AngleGeneratorIF) pickItem(ModelFactory.getPossibleAngleGenerators(), model.getAngleGenerator(), msg);
        //model.setAngleGenerator(res);
        detector.setAngleGenerator(res);
        res.init();
        this.updateGUI();
        return res;
    }

    private void pickMeasurement(Detector detector) {
        String msg = " the measurement class for " + detector.getName();
        MeasurementFormulaIF res = (MeasurementFormulaIF) pickItem(ModelFactory.getPossibleMeasurementFormulas(this.experiment), model.getMeasurementFormula(), msg);
        //model.setMeasurementFormula(res);        
        detector.setMeasurementFormula(res);
        res.init();
        // init formula
        res.init();
        this.updateGUI();
    }

    private void pickEfficiency(Detector detector) {
        String msg = " an efficiency class for " + detector.getName();
        DetectorEfficiencyIF res = (DetectorEfficiencyIF) pickItem(ModelFactory.getPossibleDetectorEfficencies(experiment), model.getDetectorEfficiency(), msg);
        detector.setDetectorEfficiency(res);
        res.init();
        this.updateGUI();

    }

    public void pickModel() {
        String msg = "Pick a model";
        model = ((ExperimentModel) pickItem(ModelFactory.getPossibleModels(experiment), model, msg));
        experiment.setModel(model);
        detectorA.resetModel();
        detectorB.resetModel();
        model.init();
        this.updateGUI();

    }

    private ModelItemIF pickItem(ArrayList items, Object def, String type) {
        if (items == null) {
            return null;
        }
        JPanel p = new JPanel();
        int rows = items.size();
        p.setLayout(new GridLayout(rows, 2));
        int r = 0;
        ButtonGroup group = new ButtonGroup();
        HashMap<JRadioButton, ModelPick> map = new HashMap<JRadioButton, ModelPick>();
        for (Object obj : items) {
            ModelItemIF it = (ModelItemIF) obj;
            r++;
            JRadioButton box = new JRadioButton(r + " " + it.getName() + ": " + it.getDescription());
            group.add(box);
            box.setToolTipText(it.getDescription());
            if (it.getClass().equals(def.getClass())) {
                box.setSelected(true);
            }
            ModelPick pick = new ModelPick(box, it);
            map.put(box, pick);
            p.add(box);
            //  box.addChangeListener(pick);
        }

        int ans = JOptionPane.showConfirmDialog(this, p, "Pick " + type, JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
        for (JRadioButton box : map.keySet()) {
            if (box.isSelected()) {
                return map.get(box).it;
            }
        }
        return null;
    }

    public void help() {
         String msg = "<html><ul>";
        msg += "<li>Click on the various buttons to change the settings</li>";
        msg += "<li>Click run to run the experiment</li>";
        
        msg += "<li>To see a larger version of the chart, click on the chart</li>";
        msg += "<li>Check the output folder for results</li>";
        msg += "<li>Mouse over the buttons and labels to see tool tip text</li>";
        msg += "</ul></html>";
        JOptionPane.showMessageDialog(this, msg);
    }

    public void showCorr() {
        if (result != null) {
            GuiUtils.msg(result.toString());
        } else {
            GuiUtils.msg("No results yet - run the experiment first");
        }
    }

    private class ModelPick {

        JRadioButton box;
        ModelItemIF it;

        public ModelPick(JRadioButton box, ModelItemIF it) {
            this.box = box;
            this.it = it;
        }
    }

    private static void p(String msg) {
        Logger.getLogger(ExperimentPanel.class.getName()).log(Level.INFO, msg);
    }
}
