/*
 *	Copyright (C) 2011 Life Technologies Inc.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  @Author Chantal Roth
 */
package tango.guiutils;

import tango.guiutils.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This is a general class that has a canvas that is zoomable. Zooming in/out
 * can be done by using a slider either on the side or at the bottom, or by
 * drawing a rectangle. The canvas can be moved around by pressing the right
 * mouse button and by moving the mouse at the same time. To use this class,
 * create a subclass, and use the methods: setZoomRange(min, max) for setting
 * the zoom range (example: 100 = scale 1, 50 = double size) setZoomValue(value)
 * for setting the current zoom range in % setSize(x,y) sets the size of the
 * underlying canvas. This can be very large.
 *
 * @Author Chantal Roth
 */
public class ZoomCanvas extends BasicTabView
        implements ActionListener, ChangeListener, DrawingCanvas, DrawListener, SelectionEventProducer, ContextActionHandlerIF {

    protected static ToolTipManager tipmanager = ToolTipManager.sharedInstance();
    private static boolean DEBUG = false;
    public final int DRAG = 1;
    public final int ZOOM = 2;
    public final int SELECT = 3;
    private int MODE = 2;
    public final int MOVING = 4;
    private boolean showtop = true;
    private boolean showleft = true;
    private GuiCanvas canvas = null;
    protected JScrollPane pane_canvas = null;
    private boolean horizontal = false;
    private JSlider zoomslider = null;
    private SelectionListener selectionlistener = null;
    private Color color_back = Color.white;
    protected Drawable selected = null;
    private double zoomfactor = 0.1;
    protected Point origin = new Point(0, 0);
    protected JViewport viewport;
    private boolean ignorechange = false;
    private int oldmousex = -1;
    private int oldmousey = -1;
    private int mousex = -1;
    private int mousey = -1;
    private int dragx = -1;
    private int dragy = -1;
    private boolean zoom_x = true;
    private boolean zoom_y = true;
    private double multiplier = 100;
    protected boolean MULTIUPDATE = false;
    // top and left panel
    private GuiCanvas canvastop = null;
    private GuiCanvas canvasleft = null;
    private JScrollPane pane_canvastop = null;
    private JScrollPane pane_canvasleft = null;
    public static final int TOP_HEIGHT = 40;
    public static final int LEFT_WIDTH = 200;
    private static final int STRETCH = 100;
    // private ExternalLinkManager linkmanager = ExternalLinkManager.getManager();
    private double zoomValue;
    // move to subclass together with select(baseobject)
    protected HashMap hash_objects;
// *****************************************************************
// CONSTRUCTOR
// *****************************************************************

    public ZoomCanvas() {
        this(null, 5000, 5000, GuiCanvas.NONE, true, true, false, false, false);
    }

    public ZoomCanvas(JFrame parent) {
        this(parent, 5000, 5000, GuiCanvas.NONE, true, true, false, false, false);
    }

    public ZoomCanvas(JFrame parent, int width, int height, int gridtype, boolean zoom_x, boolean zoom_y) {
        this(parent, width, height, gridtype, zoom_x, zoom_y, false, false, false);
    }

    public ZoomCanvas(JFrame parent, int width, int height, int gridtype, boolean zoom_x, boolean zoom_y, boolean horizontal) {
        this(parent, width, height, gridtype, zoom_x, zoom_y, horizontal, false, false);
    }

    public ZoomCanvas(JFrame parent, int width, int height, int gridtype, boolean zx, boolean zy, boolean horizontal, boolean showleft, boolean showtop) {
        super("ZoomCanvas");
        super.setContextActionHandler(this);
        this.zoom_x = zx;
        this.zoom_y = zy;
        this.showleft = showleft;
        this.showtop = showtop;
        tipmanager.setDismissDelay(300000);
        this.horizontal = horizontal;
        if (!horizontal) {
            zoomslider = new JSlider(SwingConstants.VERTICAL);
        } else {
            zoomslider = new JSlider(SwingConstants.HORIZONTAL);
        }
        zoomslider.setValue(realToSlider(10));

        canvas = new GuiCanvas(new ArrayList<Drawable>(), null, width, height, zoom_x, zoom_y);
        int h = TOP_HEIGHT;
        if (zoom_y) {
            h *= STRETCH;
        }
        int w = LEFT_WIDTH;
        if (zoom_x) {
            w *= STRETCH;
        }


        MouseListener popupListener = new PopupListener();
        canvas.addMouseListener(popupListener);
        canvas.setDrawListener(this);
        canvas.setGridType(gridtype);


        MouseAdapter mouseadapter = new MouseHandler(this, canvas);


        MouseMotionAdapter motionadapter = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //	p("**** mouse dragged");

                if (MODE == DRAG) {
                    //		p("Dragging whole canvas around");
                    mousex = e.getX();
                    mousey = e.getY();

                    int dx = mousex - oldmousex;
                    int dy = mousey - oldmousey;
                    if (oldmousex > -1) {
                        moveViewportCenter(dx, dy);
                    }
                } else {

                    //	p("drawing rect or moving: mousex="+mousex);

                    if (MODE == SELECT || MODE == MOVING) {

                        if (MODE == MOVING) {
                            canvas.setDrawRect(false);
                            mousex = e.getX();
                            mousey = e.getY();

                            double ddx = mousex - dragx;
                            double ddy = mousey - dragy;
                            //pp("Moving drawable around (dd): "+ddx+"/"+ddy);
                            if (Math.abs(ddx) + Math.abs(ddy) > 10) {
                                canvas.moveSelected(ddx, ddy);
                                dragx = mousex;
                                dragy = mousey;
                            }
                            return;
                        }
                        //	else p("Got no drawable");
                        // p("Should move the selected objects around");
                    }
                    if (MODE == ZOOM) {
                        //			p("drawing rect for zooming");
                    }
                    mousex = e.getX();
                    mousey = e.getY();

                }

            }
        };
        if (showtop) {
            canvastop = new GuiCanvas(new ArrayList<Drawable>(), null, width + w, h, zoom_x, zoom_y);
        }
        if (showleft) {
            canvasleft = new GuiCanvas(new ArrayList<Drawable>(), null, w, height + h, zoom_x, zoom_y);
            canvasleft.addMouseListener(popupListener);
            canvasleft.setDrawListener(this);
            MouseAdapter mouseadapter1 = new MouseHandler(this, canvasleft);
        }

        canvas.addMouseMotionListener(motionadapter);
        createGUI();
        registerEvents();
        setSize(width, height);
        setZoomRange(5, 100, 100);

    }

    public boolean isMultiUpdate() {
        return MULTIUPDATE || canvas.isMultiUpdate();
    }

    public void paintAfter(Graphics g) {
        // ADD STUFF HERE TO DRAW AFTER THE CANVAS IS FINISHED WITH DRAWING
    }

    public void paintAfterUntrasnformed(Graphics g) {
        // ADD STUFF HERE TO DRAW AFTER THE CANVAS IS FINISHED WITH DRAWING
    }

    protected void showComplete(int height, int border) {
        int h = getHeight();
        //	p("height of this panel is:"+h+", showing height "+height);
        if (h == 0) {
            h = 400;
        }
        h = Math.max(100, h - border);
        double fraction = (double) (h) / (double) (height);
        // fraction = 1 -> we can zoom in all the way, 100
        // fraction = 0.5 -> we need to zoom out 50
        // fraction = 0.1 -> we need to zoom out 10
        //	p("Fraction is:"+fraction);
        // zoom such that if the sequence is short, something is visible
        double val = fraction * 100.0d;
        //	p("zooming out to:"+val);
        setZoomValue(val);
//		repaint();
    }
// ***************************************************************************
// SELECT A BASE OBJECT
// XXX MOVE THIS TO ANOTHER CLASS!
// ***************************************************************************

    public void register(Object base, GuiObject g) {
        if (hash_objects == null) {
            hash_objects = new HashMap();
        }
        hash_objects.put(getId(base), g);
    }

    protected String getId(Object obj) {
        if (obj == null) {
            return null;
        }
        StringBuffer res = new StringBuffer(obj.getClass().getName());
        res = res.append('.');
        res = res.append(obj.hashCode());
        return res.toString();
    }

    public void select(Object obj) {
        if (obj == null || hash_objects == null) {
            err("select: object is null or hash of objects is null");
            return;
        }
        String id = getId(obj);
        if (id == null) {
            err("select: there  is no object " + obj + " in object hash");
            return;
        }
        GuiObject g = (GuiObject) hash_objects.get(id);
        if (g != null) {
//			  p("selecting object "+id);
            g.setSelected(true, true);
        }
//		   else p("Could not find object :"+id+" in hashmap");

    }

    public void deselect(Object obj) {
        String id = getId(obj);
        GuiObject g = (GuiObject) hash_objects.get(id);
        if (g != null) {
//			  p("selecting object "+id);
            g.setSelected(false, true);
        }
//		   else p("Could not find object :"+id+" in hashmap");

    }
// ***************************************************************************
// REGISTER AT CENTRAL MANAGER
// ***************************************************************************

    /**
     * From interface EventProducer
     */
    public void registerEvents() {
        //	CommunicationController.registerSelectionEvents(this);
    }

// *****************************************************************
// SELECTIONS
// *****************************************************************
    public void toggleSelection(Drawable d) {
        toggleSelection(d, true);
    }

    public void toggleSelection(Drawable d, boolean sendevent) {
        canvas.toggleSelection(d, sendevent);
    }

    public void select(Drawable d, boolean sendevent) {
        canvas.select(d, sendevent);
    }

    public void unselect(Drawable d, boolean sendevent) {
        canvas.unselect(d, sendevent);
    }

    public void select(Drawable d) {
        select(d, true);
    }

    public void unselect(Drawable d) {
        unselect(d, true);
    }

    public Drawable getSelectedDrawableAt(double x, double y, boolean shift) {
        return canvas.getSelectedDrawableAt(x, y, shift);
    }

    public Text getTextAt(double x, double y) {
        return canvas.getTextAt(x, y);
    }

    public ArrayList<Drawable> getDrawablesAt(Rectangle rect) {
        return canvas.getDrawablesAt(rect);
    }

    public GuiCanvas getCanvas() {
        return canvas;
    }

    public Drawable getDrawableAt(MouseEvent e) {
        return canvas.getDrawableAt((double) e.getX(), (double) e.getY());
    }

    public Text getTextAt(MouseEvent e) {
        return getTextAt((double) e.getX(), (double) e.getY());
    }

    public Drawable getSelectedDrawableAt(MouseEvent e) {
        boolean shift = e.isShiftDown() || e.isControlDown();
        return getSelectedDrawableAt(e.getX(), e.getY(), shift);
    }

    public Drawable getDrawableAt(double x, double y) {
        return canvas.getDrawableAt(x, y);
    }

    public Drawable getDrawableAtExcept(double x, double y, Class clazz, boolean text) {
        return canvas.getDrawableAtExcept(x, y, clazz, text);
    }

    public boolean overlaps(Drawable d) {
        return canvas.overlaps(d);
    }

    public QuadNode getQuad() {
        return canvas.getQuad();
    }
// *****************************************************************
// GET/SET METHODS
// *****************************************************************

    public void setBackGroundImage(java.awt.Image image) {
        canvas.setBackGroundImage(image);
    }

    public void setMode(int mode) {
        this.MODE = mode;
    }

    public int getFontWidth() {
        return canvas.getFontWidth();
    }

    public int getFontHeight() {
        return canvas.getFontHeight();
    }

    public void setStartingSize(int width, int height) {
        canvas.setStartingSize(width, height);
        int h = TOP_HEIGHT;
        if (zoom_y) {
            h *= STRETCH;
        }
        int w = LEFT_WIDTH;
        if (zoom_x) {
            w *= STRETCH;
        }
        if (showleft) {
            canvasleft.setStartingSize(w, height + h);
        }
        if (showtop) {
            canvastop.setStartingSize(width + w, h);
        }
    }

    public void setOrigin(Point p) {
        canvas.setOrigin(p);
    }

    public void setTranslate(Point p) {
        canvas.setTranslate(p);
    }

    public Point getTranslate() {
        return canvas.getTranslate();
    }

    public Point getOrigin() {
        return canvas.getOrigin();
    }

    // ELI
    public Point getAbsToCanvas(Point absp) {
        return canvas.getAbsToCanvas(absp);
    }
    // End ELI

    public Dimension getViewSize() {
        return viewport.getSize();
    }

    @Override
    public double getZoomFactor() {
        return canvas.getZoomFactor();
        // return zoomfactor;
    }

    @Override
    public double getXZoomFactor() {
        return canvas.getXZoomFactor();
        // return zoomfactor;
    }

    @Override
    public double getYZoomFactor() {
        return canvas.getYZoomFactor();
        // return zoomfactor;
    }

    public void setDrawables(ArrayList<Drawable> drawables) {
        canvas.setDrawables(drawables);
    }

    public void setLeftDrawables(ArrayList<Drawable> drawables) {
        if (showleft) {
            canvasleft.setDrawables(drawables);
        }
    }

    public void setTopDrawables(ArrayList<Drawable> drawables) {
        if (showtop) {
            canvastop.setDrawables(drawables);
        }
    }

    public void setSimpleDrawables(ArrayList<Drawable> drawables) {
        canvas.setSimpleDrawables(drawables);
    }

    public ArrayList<Drawable> getDrawables() {
        return canvas.getDrawables();
    }

    public void setTexts(ArrayList<Drawable> texts) {
        canvas.setTexts(texts);
    }

    public void setLeftTexts(ArrayList<Drawable> texts) {
        if (showleft) {
            canvasleft.setTexts(texts);
        }
    }

    public GuiCanvas getCanvasLeft() {
        return canvasleft;
    }

    public void setTopTexts(ArrayList<Drawable> texts) {
        if (showtop) {
            canvastop.setTexts(texts);
        }
    }

    public ArrayList<Drawable> getTexts() {
        return canvas.getTexts();
    }

    public void clear(Drawable d) {
        canvas.clear(d);
    }

    public void addDrawable(Drawable d) {
        canvas.addDrawable(d);
    }

    public void removeDrawables() {
        canvas.removeDrawables();
    }

    public void removeDrawables(ArrayList<Drawable> draws) {
        for (int i = 0; draws != null && i < draws.size(); i++) {
            Drawable d = (Drawable) draws.get(i);
            removeDrawable(d);
            if (d instanceof Text) {
                removeText((Text) d);
            }
        }
    }

    public void removeDrawable(Drawable d) {
        canvas.removeDrawable(d);
    }

    public void addText(Text d) {
        canvas.addText(d);
    }

    public void removeText(Text d) {
        canvas.removeText(d);
    }

    public void draw() {
        //	p("drawing canvas");
        canvas.draw();
    }

    public void draw(Drawable d) {
        //	p("drawing canvas");
        canvas.draw(d);
    }

    public void setZoomRange(int start, int end) {
        setZoomRange(start, end, 100);
    }

    public int sliderToReal(int slidervalue) {
        //int zoomfactor = (int) Math.exp((double) s / 1000.0);
        int zoomfactor = (int) (slidervalue / 1000.0);
        //	p("slider -> real:"+s+" ->"+r);
        return zoomfactor;

    }

    public int realToSlider(double zoomfactor) {
        //int s = (int) (Math.log(r) * 1000.0);
        int slidervalue = (int) (zoomfactor * 1000);
        //	p("slider -> real:"+s+" ->"+r);
        return slidervalue;
    }

    public double getMaxFactor() {
        return getMaxZoom();
    }

    private void setZoomRange(int start, int end, double multi) {

        double minfactor = getMinFactor();
        this.multiplier = multi;
        int zoomstart = Math.max((int) minfactor, start);
        int zoomend = Math.max((int) minfactor, end);
        p("setZoomRange: Setting slider min-max=" + zoomstart + "-" + zoomend + ", minfactor is: " + minfactor);
        ignorechange = true;

        zoomslider.setMinimum(realToSlider(zoomstart));
        zoomslider.setMaximum(realToSlider(zoomend));
        ignorechange = false;
        p("ignorechange is " + ignorechange);
    }

    protected void setZoomSliderVisible(boolean visible) {
        this.zoomslider.setVisible(visible);
    }

    protected void setScrollBarPolicy(int vsbPolicy, int hsbPolicy) {
        if (this.pane_canvas != null) {
            pane_canvas.setVerticalScrollBarPolicy(vsbPolicy);
            pane_canvas.setHorizontalScrollBarPolicy(hsbPolicy);
        }
    }

    public int getMinZoom() {
        return sliderToReal(zoomslider.getMinimum());
    }

    public int getMaxZoom() {
        return sliderToReal((int) zoomslider.getMaximum());
    }

    public void setMaxZoom() {
        double minfactor = getMinFactor();
        zoomfactor = getMaxZoom();
        zoomfactor = Math.max(zoomfactor, minfactor) / multiplier;
        //	p("zoomfactor:"+zoomfactor*multiplier);
        adjustCanvas(zoomfactor);
    }

    public void setMinZoom() {
        double minfactor = getMinFactor();
        zoomfactor = getMinZoom();
        zoomfactor = Math.max(zoomfactor, minfactor) / multiplier;
        //	p("zoomfactor:"+zoomfactor*multiplier);
        adjustCanvas(zoomfactor);
    }

    public void setZoomRange(double start, int end) {
        //	double minfactor = 	getMinFactor();
        multiplier = 100;
        if (start < 1) {
            multiplier = 1 / start * 100;
            start = 1;
            end = (int) (multiplier * end);
            //	p("multi, start, end:"+multiplier+"/"+start+"/"+end);
        }
        setZoomRange((int) start, end, multiplier);

    }

    public void setZoomValue(double value) {
        zoomValue = value;
        p("===== setZoomValue " + value);
        ignorechange = true;
        zoomslider.setValue(realToSlider(value));
        ignorechange = false;
        double zoomfactor = value / multiplier;

        adjustCanvas(zoomfactor);
    }

    public void setZoomsliderEnabled(boolean enabled) {
        this.zoomslider.setEnabled(enabled);
    }

    public double getZoomValue() {
        return zoomValue;
    }

    public int getMinimum() {
        return sliderToReal(zoomslider.getMinimum());
    }

    public void unselectAll() {
        canvas.unselectAll();
    }

    public Drawable getSelectedDrawable() {
        return selected;
    }

    public ArrayList<Drawable> getSelectedDrawables() {
        ArrayList<Drawable> ds = getDrawables();
        ArrayList<Drawable> sel = new ArrayList<Drawable>();
        for (int i = 0; ds != null && i < ds.size(); i++) {
            Drawable d = (Drawable) ds.get(i);
            if (d.isSelected()) {
                sel.add(d);
            }
        }
        return sel;
    }

    public void setSelectedDrawable(Drawable d) {
        selected = d;
    }

    /*
     * public void viewMoveTest() { p("moving port "); for (int i = 0; i < 500;
     * i+=10) { p("moving"); viewport.setViewPosition(new Point(i, i)); } }
     */
// *****************************************************************
// PRIVATE HELPER METHODS
// *****************************************************************
    private void initScrollPane(JScrollPane pane) {
        JScrollBar bar = pane.getHorizontalScrollBar();
        bar.setUnitIncrement(50);
        bar = pane_canvas.getVerticalScrollBar();
        bar.setUnitIncrement(50);
        viewport = pane_canvas.getViewport();
        viewport.setDoubleBuffered(true);
        viewport.setBackground(color_back);
    }

    public int getTopHeight() {
        return TOP_HEIGHT;
    }

    public int getLeftWidth() {
        return LEFT_WIDTH;
    }

    private void handleSliderChange() {
        p("******* slider state changed ****** ignorechange=" + ignorechange);
        if (ignorechange == true) {
            p("--------- Ignoring slider");
            return;
        }
        double minfactor = getMinFactor();
        // p("minfactor: "+minfactor);
        p("old slider value: " + zoomslider.getValue());

        if (minfactor * multiplier > zoomfactor) {
            p("=== minfactor " + minfactor + " is > than zoomfactor " + zoomfactor);
            setZoomRange(5, 100, 100);
        }
        zoomfactor = (double) sliderToReal(zoomslider.getValue());
        // p("new zoomfactor: " + zoomfactor + ", minfactor: " + minfactor);
        //	zoomfactor = zoomfactor/multiplier;
        zoomfactor = Math.max(zoomfactor, minfactor) / multiplier;
        //   p("zoomfactor:" + zoomfactor + ", multiplier is:" + multiplier);
        adjustCanvas(zoomfactor);
        //repaint();
        
    }

    private class HorListener implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            JScrollBar bar = pane_canvastop.getHorizontalScrollBar();
            bar.setValue(e.getValue());
        }
    }

    private class VerListener implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            JScrollBar bar = pane_canvasleft.getVerticalScrollBar();
            bar.setValue(e.getValue());
        }
    }

    private void createGUI() {
        setLayout(new BorderLayout());
        JPanel panel_center = new JPanel();
        panel_center.setLayout(new BorderLayout());

        //canvas.setLayout(null);
        JPanel pan_canvas = new JPanel();
        pan_canvas.setLayout(new BorderLayout());

        canvas.setBackground(color_back);
        p("CreateGui: factor="+zoomfactor);
        canvas.setZoomFactor(zoomfactor);
        pane_canvas = new JScrollPane(canvas, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        if (showtop) {
            canvastop.setOrigin(new Point(LEFT_WIDTH, 0));
            canvastop.setZoomFactor(zoomfactor);
            canvastop.setBackground(color_back);
            pane_canvastop = new JScrollPane(canvastop, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            pane_canvastop.setPreferredSize(new Dimension(1400, TOP_HEIGHT));
            pane_canvastop.setMaximumSize(new Dimension(1400, TOP_HEIGHT));
            initScrollPane(pane_canvastop);
            pane_canvas.getVerticalScrollBar().addAdjustmentListener(new VerListener());
            pan_canvas.add("North", pane_canvastop);
        }
        if (showleft) {
            canvasleft.setBackground(color_back);
            canvasleft.setZoomFactor(zoomfactor);
            pane_canvasleft = new JScrollPane(canvasleft, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            pane_canvasleft.setMaximumSize(new Dimension(LEFT_WIDTH, 1000));
            pane_canvasleft.setPreferredSize(new Dimension(LEFT_WIDTH, 1000));
            initScrollPane(pane_canvasleft);
            pane_canvas.getHorizontalScrollBar().addAdjustmentListener(new HorListener());
            pan_canvas.add("West", pane_canvasleft);

        }
        pan_canvas.add("Center", pane_canvas);
        initScrollPane(pane_canvas);
        add("Center", pan_canvas);
        if (!horizontal) {
            add("East", zoomslider);
        } else {
            add("South", zoomslider);
        }

        zoomslider.setPaintLabels(true);
        zoomslider.setPaintTicks(true);
        zoomslider.setPaintTrack(true);

        zoomslider.addChangeListener(this);
    }

// *****************************************************************
// COORDINATE MAPPING
// *****************************************************************
    public void showPosition(int value) {
        pane_canvas.getVerticalScrollBar().setValue(value);
    }

    public void showMiddle() {
        int value = pane_canvas.getVerticalScrollBar().getMaximum() / 2;
        pane_canvas.getVerticalScrollBar().setValue(value);

    }

    /**
     * returns the center point of the viewport, but only relative to the
     * viewport, NOT relative to the canvas
     */
    public Point getViewportCenter() {
        Dimension viewsize = viewport.getViewSize();
        double mx = (double) viewsize.getWidth() / 2;
        double my = (double) viewsize.getHeight() / 2;
        Point center = new Point((int) mx, (int) my);
        //	p("Viewport center is: "+center);
        return center;
    }

    /**
     * calculate the position of the point p, where p is a coordinate within
     * viewport. Basically move it according to viewport position and No zooming
     * is done.
     */
    public Point getViewportToCanvas(Point vp) {
        double vx = vp.getX();
        double vy = vp.getY();

        Point viewpos = viewport.getViewPosition();
        double startx = viewpos.getX();
        double starty = viewpos.getY();

        // these coords are NOT the absolute coords, but the zoomed ones
        double cx = startx + vx;
        double cy = starty + vy;

        return new Point((int) cx, (int) cy);
    }

    public void setViewportCenter(int startx, int starty) {
        //	Dimension viewsize = viewport.getViewSize();
        Dimension extent = viewport.getExtentSize();
        //	Point oldp = viewport.getViewPosition();

        double w = extent.getWidth();
        double h = extent.getHeight();

        //	p("oldp:"+oldp.toString()+"; oldcx, oldcy:"+mx+"/"+my);

        double upperx = canvas.getSize().getWidth() - w;
        double uppery = canvas.getSize().getHeight() - h;

        double px = Math.max(1, startx - w / 2);
        double py = Math.max(1, starty - h / 2);

        px = Math.min(px, upperx);
        py = Math.min(py, uppery);

        Point m = new Point((int) (px), (int) (py));
        //	p("view center:"+startx+"/"+starty+"; view start:"+m.toString());
        viewport.setViewPosition(m);
    }

    public void moveViewportCenter(int deltax, int deltay) {
        Dimension extent = viewport.getExtentSize();
        Point oldp = viewport.getViewPosition();

        //double w = extent.getWidth();
        //double h = extent.getHeight();

        double upperx = canvas.getSize().getWidth() - extent.getWidth();
        double uppery = canvas.getSize().getHeight() - extent.getHeight();

        //	p("max x/y:"+upperx+"/"+uppery);

        double px = Math.max(1, oldp.getX() - deltax);
        double py = Math.max(1, oldp.getY() - deltay);

        px = Math.min(px, upperx);
        py = Math.min(py, uppery);

        if (deltay == 0) {
            py = oldp.getY();
        }
        if (deltax == 0) {
            px = oldp.getX();
        }
        Point m = new Point((int) (px), (int) (py));
        //	p("dx, dy:"+deltay+"/"+deltax+"; px, py:"+m.toString());

        viewport.setViewPosition(m);
    }

    public void setViewportCenter(Dimension startsize, Dimension newsize) {

        //	p("oldsize:"+startsize.toString());
        //	p("newsize:"+newsize.toString());
        double width1 = startsize.getWidth();
        double height1 = startsize.getHeight();

        Point oldp = viewport.getViewPosition();
        Dimension ext = viewport.getExtentSize();

        double mx = oldp.getX() + ext.getWidth() / 2;
        double my = oldp.getY() + ext.getHeight() / 2;

        //	p("mx, my:"+mx+"/"+my);
        double factorx = 1.0;
        double factory = 1.0;
        if (zoom_x) {
            factorx = mx / width1;
        }
        if (zoom_y) {
            factory = my / height1;
        }

        //	p("factors:"+factorx+"/"+factory);

        double width2 = newsize.getWidth();
        double height2 = newsize.getHeight();

        double dx = -(width2 - width1) * factorx;
        double dy = -(height2 - height1) * factory;

        if (!zoom_y) {
            dy = 0.0;
        }
        if (!zoom_x) {
            dx = 0.0;
        }
        //	p("setViewport: moving:"+(int)dx+"/"+(int)dy);
        moveViewportCenter((int) dx, (int) dy);
    }

    public Rectangle getViewRect() {
        JViewport v = pane_canvas.getViewport();
        Rectangle r = v.getViewRect();
        return r;
    }

    public void zoomIn() {
//		p("zooming in");
        setMinZoom();
    }

    public void zoomOut() {
        int val = (int) (getMinFactor());
//		p("zooming out to:"+val);
        setZoomValue(val);
    }

    public double getMinFactor() {
        if (viewport == null) {
            p("Got no viewport yet");
            return 1;
        }
        Dimension extent = viewport.getExtentSize();
        Dimension abssize = canvas.getStartingSize();

        // p("viewport extent:"+extent.toString());
        //  p("canvas startsize:"+abssize.toString());

        double ew = extent.getWidth();
        double eh = extent.getHeight();

        double vw = abssize.getWidth();
        double vh = abssize.getHeight();

        double minfactorx = ew / vw * multiplier;
        double minfactory = eh / vh * multiplier;

        //  p("extent/absolute: "+eh+"/"+vh);
        p("getMinFactor: minfactorsx and y:" + minfactorx + "/" + minfactory);

        //return 1;
        if (!zoom_x) {
            return minfactory;
        } else if (!zoom_y) {
            return minfactorx;
        } else {
            return Math.min(minfactorx, minfactory);
        }
    }

// *****************************************************************
// NOTIFY SELECTION LISTENERS
// *****************************************************************
    @Override
    public synchronized void addSelectionListener(SelectionListener list) {
        //	p("Adding selection listener");
        selectionlistener = EventMulticaster.add(selectionlistener, list);

    }

    @Override
    public synchronized void removeSelectionListener(SelectionListener list) {
        selectionlistener = EventMulticaster.remove(selectionlistener, list);
    }

    /**
     * whenever a new event is produced, the event producer must call this
     * method in order to notify any listeners
     */
    protected synchronized void notifySelectionListeners(SelectionEvent evt) {
        if (selectionlistener != null) {
            selectionlistener.selectionPerformed(evt);
        }
    }

// *****************************************************************
// MAIN (for testing)
// *****************************************************************
    public static void main(String args[]) {
        String name = "ZoomCanvas";

        JFrame frame = new JFrame("ZoomCanvas Test");

        frame.setSize(800, 800);
        ZoomCanvas view = new ZoomCanvas(frame, 1000, 1000, GuiCanvas.ZOOMX, true, true);
        frame.getContentPane().add(view);
        frame.show();
        view.test1();
        view.repaint();
    }

    public void test1() {
        GuiRectangle rect = new GuiRectangle(new Point(65, 64),
                new Point(225, 100));
        rect.setMovable(true);
        this.addDrawable(rect);
    }

// *****************************************************************
// ACTION AND CHANGE EVENTS
// *****************************************************************
    @Override
    public ArrayList<Action> getContextSensitiveActions(ActionContext ctx) {
        p("===================== handling popup action");
        Drawable d = getDrawableAt(ctx.getX(), ctx.getY());
        if (d != null) {
            if (d instanceof GuiObject) {
                p("Got guiobject " + d);
                GuiObject g = (GuiObject) d;
                ArrayList<Action> actions = g.getActions();
                return actions;
            }
        } else {
            // check for selected objects
            ArrayList<Drawable> selected = new ArrayList<Drawable>();
            ArrayList<Drawable> drawables = this.getDrawables();

            for (int i = 0; i < drawables.size(); i++) {
                d = (Drawable) drawables.get(i);
                if ((d instanceof GuiObject) && d.isSelected()) {
                    selected.add(d);
                }
            }
            if (selected.size() == 1) {
                p("Got guiobject " + d);
                GuiObject g = (GuiObject) selected.get(0);
                ArrayList<Action> actions = g.getActions();
                return actions;
            } else {
                p("DOING MULTI LINK");
//                Vector objs = new Vector();
//                for (int i = 0; i < selected.size(); i++) {
//                    GuiObject g = (GuiObject) selected.get(i);
//                    objs.add(g.getBaseObject());
//                }
//                p("objects are: " + objs);
//                // xxx change using objs
//                if (objs != null && objs.size() > 0) {
////                       Vector links = linkmanager.getExternalLinks( (BaseObject)
////                           objs.get(0));
////                       return GuiObject.createLinkActions(links);
//                } 
            }

        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Object source = e.getSource();
    }

    protected void adjustCanvas(double zoomfactor) {
        
        zoomValue = zoomfactor * multiplier;
        p("adjustCanvas: setZoomValue with factor " + zoomfactor+", value="+zoomValue);
        canvas.setZoomFactor(zoomfactor);
        if (showleft) {
            canvasleft.setZoomFactor(zoomfactor);
        }
        if (showtop) {
            canvastop.setZoomFactor(zoomfactor);
        }
        p("REPAINTING");
       repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        handleSliderChange();

    }

    public void setSliderValuePercent(int value) {
        this.ignorechange = false;
        this.zoomslider.setValue(value * 1000);
        handleSliderChange();
    }

    private class MouseHandler extends MouseAdapter {

        private ZoomCanvas zoom;
        private GuiCanvas canvas;

        public MouseHandler(ZoomCanvas zoom, GuiCanvas canvas) {
            this.zoom = zoom;
            this.canvas = canvas;
            canvas.addMouseListener(this);
        }

        public void mouseClicked(MouseEvent e) {
            //		p("**** mouse clicked");
            Drawable d = canvas.getDrawableAt((double) e.getX(), (double) e.getY());
            if (d != null) {
                p("drawable is:" + d);
            }
            //	if (d != null) {
            //		canvas.draw(d);
            //	}
            //	Rectangle rect = new Rectangle(oldmousex, oldmousey, dx, dy);
            //	Vector res = getDrawablesAt(rect);
            if (e.getClickCount() == 2) {
                if (d != null) {
                    //			Log.info("double clicked on a drawable in ZoomCanvas, sending event");
                    SelectionEvent evt = new SelectionEvent(zoom, d, "DOUBLE", e.getPoint());
                    notifySelectionListeners(evt);
                }

            }

        }

        @Override
        public void mousePressed(MouseEvent e) {
            //	p("\n\n");
            //		p("**** mouse pressed");
            oldmousex = e.getX();
            oldmousey = e.getY();
            dragx = oldmousex;
            dragy = oldmousey;
            if (e.isMetaDown()) {
                //		p("meta down, mode = zoom");
                MODE = ZOOM;
            } else { // selection mode
                MODE = SELECT;
                canvas.setDrawRect(true);
                boolean shift = e.isShiftDown();

                Drawable d = canvas.getDrawableAt((double) e.getX(), (double) e.getY());
                //		p("got drawable:"+d);

                if (d == null) {
                    canvas.unselectExcept(null, shift);
                } else {
                    if (d.isSelectable()) {
                        canvas.setDrawRect(false);
                        if (!d.isSelected()) {
                            //			p("move: "+d+" is not selected, calling unselectexcept");
                            canvas.unselectExcept(d, shift);
                        }
                        if (d.isMovable()) {
                            //			pp("MODE = MOVING "+d);
                            MODE = MOVING;
                        }
                        //			else pp("Cannot move "+d);
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            canvas.setDrawRect(true);
            ///	p("***** mouse released");
            if (MODE == MOVING) {
                //	Log.info("zoomcanvas: mouse released, mode== moving");
                SelectionEvent evt = new SelectionEvent(zoom, this, "MOVED", e.getPoint());
                notifySelectionListeners(evt);
            }
            //	p("mode is "+ MODE+", mousex="+mousex);
            if (MODE != ZOOM && MODE != SELECT) {
                mousex = -1;
                //			 p("not zoom, not select, returning");
                //			 canvas.unselectAll();
                //			 p("unselect, return");
                return;
            }
            if (mousex <= 0) {
                //			p("mousex < 0 returning");
                //			 canvas.unselectAll();
                //			 p("unselect, return");
                return;
            }

            int endx = e.getX();
            int endy = e.getY();
            int dx = endx - oldmousex;
            int dy = endy - oldmousey;

            //Graphics g = canvas.getGraphics();
            //	p("clearing rect");
            //		drawXorRect(g, oldmousex, oldmousey, mousex-oldmousex, mousey-oldmousey);


            // we only want to do this if the rectangle is big enough
            if (Math.abs(dx) < 5 || Math.abs(dy) < 5) {
                mousex = -1;
                //		p("rect too small, returning");
                if (MODE == SELECT) {
                    //			canvas.unselectAll();
                    //			p("unselect, return");
                }
                return;
            }
            //	g.setPaintMode();


            // oldmousex, oldmousey
            if (MODE == SELECT) { // select all objects between start and end
                Rectangle rect = new Rectangle(oldmousex, oldmousey, dx, dy);
                ArrayList<Drawable> res = canvas.getDrawablesAt(rect);
                if (res == null) {
                    //			p("Got no drawables at "+rect.toString());
                }

                for (int i = 0; i < res.size(); i++) {
                    Drawable d = (Drawable) res.get(i);
                    if (d.isSelectable()) {
                        select(d, false);
                    }
                }
                SelectionEvent evt = new SelectionEvent(zoom, res, "SELECT", e.getPoint());
                evt.setArea(rect);
                notifySelectionListeners(evt);

            } else if (MODE == ZOOM) { // zoom into the region

                Dimension extent = viewport.getExtentSize();
                //	Dimension viewsize = viewport.getViewSize();

                double w = extent.getWidth();
                double h = extent.getHeight();

                double factorx = 1.0;
                double factory = 1.0;
                factorx = w / dx;
                factory = h / dy;

                double factor = Math.min(factorx, factory);

                double oldfactor = (double) sliderToReal(zoomslider.getValue()) / multiplier;
                double zoomfactor = oldfactor * factor;

                double mx = (oldmousex + mousex) / 2.0 / oldfactor * zoomfactor;
                double my = (oldmousey + mousey) / 2.0 / oldfactor * zoomfactor;

                //		p("zoomfactor:"+zoomfactor);
                if (zoomfactor * multiplier > sliderToReal(zoomslider.getMaximum())) {
                    double fact = zoomfactor * multiplier / sliderToReal(zoomslider.getMaximum());
                    mx = mx / fact;
                    my = my / fact;
                    //		p("Maximum reached, fact="+fact);

                    zoomfactor = sliderToReal(zoomslider.getMaximum());
                }


                //		p("mx/my="+(int)mx+"/"+(int)my);
                if (zoom_x == false) {
                    mx = 0.0;
                }
                if (zoom_y == false) {
                    my = 0.0;
                }
                zoomslider.setValue(realToSlider((int) (zoomfactor * multiplier)));
                setViewportCenter((int) mx, (int) my);
            }
            mousex = -1;

        }
    }

// *****************************************************************
// EVENTS
// *****************************************************************
    public void addMouseListener(MouseListener l) {
        canvas.addMouseListener(l);
    }

    public void addMouseMotionListener(MouseMotionListener l) {
        canvas.addMouseMotionListener(l);
    }

// *****************************************************************
// COLOR
// *****************************************************************
    public Color selectColor(Drawable d) {
        return canvas.selectColor(d);
    }
// *****************************************************************
// DEBUG
// *****************************************************************

    protected void p(String s) {
        if (ZoomCanvas.DEBUG) {
            Logger.getLogger("ZoomCanvas").info("ZoomCanvas:" + s);
        }
    }

    private void pp(String s) {
        Logger.getLogger("ZoomCanvas").info("ZoomCanvas:" + s);
    }

    protected void p(String s, boolean force) {
        if (ZoomCanvas.DEBUG || force) {
            Logger.getLogger("ZoomCanvas").info("ZoomCanvas:" + s);
        }
    }

    protected void err(String s) {
        Logger.getLogger("ZoomCanvas").warning("ZoomCanvas:" + s);
    }

    public void paintAfterUntransformed(Graphics g) {
        // overwrite
    }
}