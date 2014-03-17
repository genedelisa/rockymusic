package com.rockhoppertech.music.fx.keyboard;

import java.io.Serializable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDISender;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class PianoKey extends Region implements Serializable {

    /**
     * Serialization.
     */
    private static final long serialVersionUID = -8390530248224536040L;

    private static final Logger logger = LoggerFactory
            .getLogger(PianoKey.class);

    protected Orientation orientation = Orientation.HORIZONTAL;

    private BooleanProperty displayPitchProperty = new SimpleBooleanProperty(
            false);

    public BooleanProperty displayPitchProperty() {
        return displayPitchProperty;
    }

    private BooleanProperty displayCKeysProperty = new SimpleBooleanProperty(
            false);

    public BooleanProperty displayCKeysProperty() {
        return displayCKeysProperty;
    }

    // the model :)
    protected int key;

    protected Text text;

    private static double zoom = 1d;
    private static double fontSize = 10d;
    private static Font theFont;
    public static boolean zoomWidth = true;
    public static boolean zoomHeight = true;

    private static MIDISender midiSender;
    static {
        midiSender = new MIDISender();
        theFont = new Font("monospaced", fontSize);
    }

    static void updateFont() {
        theFont = new Font("monospaced", (fontSize * zoom));
    }

    public PianoKey(int key) {
        this.key = key;
        this.setDisplayPitch(false);
        this.setDisplayCKeys(false);

        this.text = new Text();
        this.text.setFont(theFont);
        this.getChildren().add(text);

        logger.debug("created key for {}", this.key);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                logger.debug("clicked {}", PianoKey.this.key);
                midiSender.play(new MIDINote(PianoKey.this.key));
                logger.debug(" key: {}", PianoKey.this);
            }
        });
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                activeStyle();
                text.setVisible(true);
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                defaultStyle();
                text.setVisible(false);
            }
        });
    }

    public void defaultStyle() {
    }

    public void activeStyle() {
    }

    public void setDisplayPitch(boolean b) {
        this.displayPitchProperty.set(b);
    }

    public boolean getDisplayPitch() {
        return displayPitchProperty.get();
    }

    public void setDisplayCKeys(boolean b) {
        this.displayCKeysProperty.set(b);
    }

    public boolean getDisplayCKeys() {
        return displayCKeysProperty.get();
    }

    protected void updateText() {

        double textX = 0;
        double textY = 0;
        String theText = null;

        // if (this.getDisplayPitch() == true) {
        // theText = PitchFormat.midiNumberToString(this.key);
        // } else {
        // theText = "" + this.key;
        // }
        //
        // if (this.getDisplayCKeys() && (this.key % 12) == 0) {
        // theText = PitchFormat.midiNumberToString(this.key);
        // this.text.setText(theText);
        // }

        theText = PitchFormat.midiNumberToString(this.key);
        text.setText(theText);

        double w = this.getLayoutBounds().getWidth();
        // w = this.getWidth();
        double h = this.getLayoutBounds().getHeight();
        double tw = text.getLayoutBounds().getWidth();
        double th = text.getLayoutBounds().getHeight();
        logger.debug("w {} tw {}", w, tw);
        if (this.orientation == Orientation.HORIZONTAL) {
            textX = w / 2d - tw / 2d;
            textY = h - th;
        } else {
            // 4 pixels from the right edge
            //textX = w - tw - 4;
            textX = w - tw;
            //textY = h / 2d + th / 2d;
            textY = h / 2d;
        }
        text.setLayoutX(textX);
        text.setLayoutY(textY);
        text.setVisible(false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append(" key=").append(this.key).append(' ');
        sb.append(" w=").append(this.getLayoutBounds().getWidth()).append(' ');
        sb.append(" h=").append(this.getLayoutBounds().getHeight()).append(' ');
        sb.append(" x=").append(this.getLayoutX()).append(' ');
        sb.append(" y=").append(this.getLayoutY());
        return sb.toString();
    }

    public int getKey() {
        return this.key;
    }

    /**
     * @return the zoom
     */
    public static double getZoom() {
        return zoom;
    }

    /**
     * @param zoom
     *            the zoom to set
     */
    public static void setZoom(double z) {
        zoom = z;
        updateFont();
    }

}
