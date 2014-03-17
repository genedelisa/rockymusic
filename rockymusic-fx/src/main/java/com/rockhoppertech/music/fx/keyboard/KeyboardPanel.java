package com.rockhoppertech.music.fx.keyboard;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class KeyboardPanel extends Pane {

    private static final Logger logger = LoggerFactory
            .getLogger(KeyboardPanel.class);

    /**
     * 
     */
    private Orientation orientation = Orientation.HORIZONTAL;
    private Map<Integer, Point2D> locations;
    /**
     * The size of the keys drawn
     */
    private double pianoKeyZoom = 1.0;

    /**
     * Should you send midi messages?
     */
    private BooleanProperty sendMIDIMessageProperty = new SimpleBooleanProperty(
            true);

    /**
     * To find a key given a midi number.
     */
    private Map<Integer, PianoKey> pianoKeys;
    /**
     * The first octave drawn
     */
    private int startOctave = 0;
    /**
     * The number of octaves drawn
     */
    private int numberOfOctaves = 11;

    public static final int MAX_OCTAVE = 11;
    public static final int MIN_OCTAVE = 0;

    public KeyboardPanel() {
        this(Orientation.HORIZONTAL, MIN_OCTAVE, MAX_OCTAVE);
    }

    public KeyboardPanel(int startOctave, int numberOfOctaves) {
        this(Orientation.HORIZONTAL, startOctave, numberOfOctaves);
    }

    public KeyboardPanel(Orientation orientation, int startOctave,
            int numberOfOctaves) {
        this.startOctave = startOctave;
        this.numberOfOctaves = numberOfOctaves;
        this.orientation = orientation;
        this.locations = new HashMap<Integer, Point2D>();
        this.setPreferredSize();
        this.setupKeys();
    }

    public BooleanProperty sendMIDIMessageProperty() {
        return sendMIDIMessageProperty;
    }

    public void setSendMIDIMessage(boolean b) {
        sendMIDIMessageProperty.set(b);
    }

    public boolean getSendMIDIMessage() {
        return sendMIDIMessageProperty.get();
    }

    private void setupKeys() {
        this.pianoKeys = new HashMap<Integer, PianoKey>();
        if (this.orientation == Orientation.HORIZONTAL) {
            this.layoutHorizontal();
        } else if (this.orientation == Orientation.VERTICAL) {
            this.layoutVertical();
        }
    }

    private void layoutVertical() {
        this.getChildren().clear();
        // weird but it works to give 10 octaves sort of
        // ill fix later
        // TODO fix this to use startOctave and numberOfOctaves
        int j = -1;
        for (int oct = 9; oct >= 0; oct--, j++) {
            this.makeVerticalOctave(oct, j);
        }
    }

    private void layoutHorizontal() {
        this.getChildren().clear();
        for (int i = 0; i < numberOfOctaves; i++) {
            this.makeHorizontalOctave(i);
        }
    }

    private void makeHorizontalOctave(int oct) {
        double x = 0;
        double y = 2; // so you can see the border

        PianoKey bk = new BlackKey(0, this.orientation);
        PianoKey wk = new WhiteKey(0, this.orientation);

        int octave = (startOctave * 12) + (oct * 12);

        logger.debug("startoctave: " + startOctave + " octave " + octave);

        double ww = wk.getLayoutBounds().getWidth();
        double bw = bk.getLayoutBounds().getWidth();
        // double wh = wk.getLayoutBounds().getHeight();
        // double bh = bk.getLayoutBounds().getHeight();
        logger.debug("ww {} bw {}", ww, bw);

        // white keys
        x = (7 * ww * oct);
        // y = 2;
        int whitePc[] = { 0, 2, 4, 5, 7, 9, 11 };
        for (int i = 0; i < 7; i++) {
            if (whitePc[i] + (octave) > 127) {
                logger.debug("skipping the rest "
                        + (whitePc[i] + (startOctave + octave)));
                return;
            }
            wk = new WhiteKey(whitePc[i] + (octave), this.orientation);
            wk.setLayoutX(x);
            wk.setLayoutY(y);
            this.getChildren().add(wk);
            logger.debug("white key: {}", wk);

            this.locations.put(new Integer(wk.getKey()), new Point2D(x, y));
            this.pianoKeys.put(wk.getKey(), wk);
            x += ww;
        }

        // x = wsz.width - (bsz.width / 2) + (7 * wsz.width * oct);
        // black keys
        x = ww - (bw / 2) + (7 * ww * oct);
        int blackPc[] = { 1, 3, 6, 8, 10 };
        for (int i = 0; i < 5; i++) {
            if (i == 2)
                x += ww;
            if (blackPc[i] + (octave) > 127) {
                logger.debug("skipping the rest " + (blackPc[i] + octave));
                continue;
            }
            bk = new BlackKey(blackPc[i] + (octave), this.orientation);

            bk.setLayoutX(x);
            bk.setLayoutY(y);
            bk.toFront();
            this.getChildren().add(bk);
            logger.debug("black key: {}", bk);

            this.locations.put(new Integer(bk.getKey()), new Point2D(x, y));
            this.pianoKeys.put(bk.getKey(), bk);
            x += ww;
        }

    }

    PianoKey findKey(int num) {
        for (Integer key : this.pianoKeys.keySet()) {
            PianoKey pk = this.pianoKeys.get(key);
            if (pk.getKey() == num) {
                return pk;
            }
        }
        return null;

    }

    void makeVerticalOctave(int oct, int iter) {
        double x = 0;
        double y = 0;

        PianoKey bk = new BlackKey(0, this.orientation);
        PianoKey wk = new WhiteKey(0, this.orientation);

        // double ww = wk.getLayoutBounds().getWidth();
        // double bw = bk.getLayoutBounds().getWidth();
        double wh = wk.getLayoutBounds().getHeight();
        double bh = bk.getLayoutBounds().getHeight();

        int octave = oct * 12;

        // white keys
        y = iter * (7 * wh);
        x = 0;
        int whitePc[] = { 0, 2, 4, 5, 7, 9, 11 };
        for (int i = 6; i >= 0; i--) {
            wk = new WhiteKey(whitePc[i] + octave, this.orientation);
            wk.setLayoutX(x);
            wk.setLayoutY(y);
            this.getChildren().add(wk);
            this.locations.put(new Integer(wk.getKey()), new Point2D(x, y));
            y += wh;
            this.pianoKeys.put(wk.getKey(), wk);
        }

        // black keys
        y = wh - (bh / 2d) + (iter * 7 * wh);
        int blackPc[] = { 1, 3, 6, 8, 10 };
        for (int i = 4; i >= 0; i--) {
            if (i == 1)
                y += wh;
            bk = new BlackKey(blackPc[i] + octave, this.orientation);
            bk.setLayoutX(x);
            bk.setLayoutY(y);
            this.getChildren().add(bk);
            this.locations.put(new Integer(bk.getKey()), new Point2D(x, y));
            y += wh;
            this.pianoKeys.put(bk.getKey(), bk);
        }
    }

    public void setPreferredSize() {
        PianoKey wk = new WhiteKey(0, this.orientation);
        double ww = wk.getLayoutBounds().getWidth();
        double wh = wk.getLayoutBounds().getHeight();
        double pad = 1d;
        if (orientation == Orientation.HORIZONTAL) {
            // 7 keys in an octave and 10 octaves
            // return new Dimension(wsz.width * 7 * 11 + pad, wsz.height + pad);

            // 75 is the number of white keys for the full keyboard
            this.setPrefWidth(ww * 75 + pad);
            this.setPrefHeight(wh + pad);
            this.setWidth(ww * 75 + pad);
            this.setHeight(wh + pad);

        } else {
            this.setPrefWidth(ww);
            this.setPrefHeight(wh * 7 * 10);
            this.setWidth(ww);
            this.setHeight(wh * 7 * 10);
        }
        logger.debug("keyboard prefwidth {}", this.getPrefWidth());
        logger.debug("keyboard prefheight {}", this.getPrefHeight());
    }

    public double getKeyX(int key) {
        Point2D p = this.locations.get(new Integer(key));
        if (p != null)
            return p.getX();
        else
            throw new IllegalArgumentException("bad key" + key);
    }

    public double getKeyY(int key) {
        Point2D p = this.locations.get(new Integer(key));
        if (p != null)
            return p.getY();
        else
            throw new IllegalArgumentException("bad key" + key);
    }

    // public int getKeyAt(int x, int y) {
    // PianoKey k = (PianoKey) getComponentAt(x, y);
    // if (k == null) {
    // logger.debug("bad location for key" + x + " y=" + y);
    // return 0;
    // }
    // return k.getKey();
    // }

    /**
     * @param pianoKeyZoom
     *            the pianoKeyZoom to set
     */
    public void setPianoKeyZoom(double pianoKeyZoom) {
        this.pianoKeyZoom = pianoKeyZoom;
        PianoKey.setZoom(this.pianoKeyZoom);
        this.setupKeys();
    }

}
