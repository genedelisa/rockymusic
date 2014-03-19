package com.rockhoppertech.music.fx.pianoroll;

/*
 * #%L
 * Rocky Music FX
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.adapter.JavaBeanDoublePropertyBuilder;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.fx.DragContext;
import com.rockhoppertech.music.fx.ResizeHandle;
import com.rockhoppertech.music.fx.keyboard.PianoKey;
import com.rockhoppertech.music.midi.js.MIDINote;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class NoteNode extends Region {

    private static final Logger logger = LoggerFactory
            .getLogger(NoteNode.class);

    private MIDINote note;
    private Text text;
    private ResizeHandle rightHandle;
    private DragContext dragContext;
    private PianorollPane parent;

    public NoteNode() {
        getStyleClass().setAll("notenode-control");
        this.setStyle("-fx-background-color: gray;");

        this.rightHandle = new ResizeHandle();
        this.getChildren().add(rightHandle);
        rightHandle.layoutXProperty().bind(
                this.widthProperty().subtract(rightHandle.getPrefWidth()));
        rightHandle
                .layoutYProperty()
                .bind(
                        this.heightProperty()
                                .subtract(rightHandle.prefHeight(-1)).divide(2));

        this.dragContext = new DragContext();
       // this.setupMouse();
    }

    void setupMouse() {
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                logger.debug("drag x {} y {}", e.getX(), e.getY());
                double xx = dragContext.initialTranslateX
                        + e.getX()
                        - dragContext.mouseAnchorX;
                logger.debug("xx {}", xx);
                // xx -= xx % snapX;
                if (xx < 0) {
                    xx = 0;
                }
                // logger.debug("snapped xx {}", xx);
                // Region parent = (Region) NoteNode.this.getParent();

                double yy = dragContext.initialTranslateY
                        + e.getY()
                        - dragContext.mouseAnchorY;
                logger.debug("yy {}", yy);

                NoteNode.this.setTranslateX(xx);
                NoteNode.this.setTranslateY(yy);
            }
        });
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                logger.debug("press x {} y {}", e.getX(), e.getY());

                dragContext.mouseAnchorX = e.getX();
                dragContext.mouseAnchorY = e.getY();
                dragContext.initialTranslateX =
                        NoteNode.this.getTranslateX();
                dragContext.initialTranslateY =
                        NoteNode.this.getTranslateY();
                NoteNode.this.setCursor(Cursor.MOVE);
                parent = (PianorollPane) getParent();
            }
        });
        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                logger.debug("released x {}", e.getX());
                double xx = dragContext.initialTranslateX
                        + e.getX()
                        - dragContext.mouseAnchorX;
                // xx -= xx % 25;
                // xx -= xx % snapX;
                if (xx < 0) {
                    xx = 0;
                }
                double yy = dragContext.initialTranslateY
                        + e.getY()
                        - dragContext.mouseAnchorY;
                
                yy -= yy % PianoKey.getWhiteKeyHeight(Orientation.VERTICAL);
                NoteNode.this.setTranslateX(xx);
                NoteNode.this.setTranslateY(yy);

                int pitch = parent.getPitchForY(yy);
                logger.debug("pitch {}", pitch);
                note.setMidiNumber(pitch);

                // logger.debug("released at beat {}", getBeatForX(xx));
                // if (ResizeHandle.this instanceof TrackNode) {
                // TrackNode tn = (TrackNode) node;
                // tn.getTrack().setStartBeat(getBeatForX(xx));
                // }
                NoteNode.this.setCursor(Cursor.DEFAULT);
            }
        });
    }

    /**
     * @return the note
     */
    public MIDINote getNote() {
        return note;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(MIDINote note) {
        this.note = note;
        logger.debug("new note {}", note);

        String ps = PitchFormat.getInstance().format(note.getPitch());

        this.text = new Text(ps);
        this.text.setStyle("-fx-stroke:white; -fx-stroke-width: 1;");
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.layoutXProperty()
                .bind(
                        this.widthProperty().subtract(text.prefWidth(-1))
                                .divide(2));
        text.layoutYProperty()
                .bind(
                        this.heightProperty().divide(2)
                                .add(text.prefHeight(-1)).divide(2));

        this.getChildren().add(text);

        Tooltip t = new Tooltip(String.format(
                "%s %.2f %.2f",
                ps,
                note.getStartBeat(),
                note.getDuration()
                ));
        Tooltip.install(this, t);
        this.setupProperties();
    }
    
    void updateLabels() {
        String ps = PitchFormat.getInstance().format(note.getPitch());
        this.text.setText(ps);
    }
    

    public void play() {
        // this.note
    }

    private DoubleProperty startBeatProperty;
    private DoubleProperty durationProperty;
    private IntegerProperty pitchProperty;

    public DoubleProperty startBeatProperty() {
        return startBeatProperty;
    }

    public DoubleProperty durationProperty() {
        return durationProperty;
    }

    public IntegerProperty pitchProperty() {
        return pitchProperty;
    }

    void setupProperties() {
        try {
            startBeatProperty = JavaBeanDoublePropertyBuilder.create()
                    .bean(this.note)
                    .name("startBeat")
                    .build();
            startBeatProperty.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> arg0,
                        Number old, Number newStart) {
                    logger.debug("listener new {}", newStart);
                }
            });
            durationProperty = JavaBeanDoublePropertyBuilder.create()
                    .bean(this.note)
                    .name("duration")
                    .build();
            durationProperty.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> arg0,
                        Number old, Number newDuration) {
                    logger.debug("listener new {}", newDuration);
                }
            });
            pitchProperty = JavaBeanIntegerPropertyBuilder.create()
                    .bean(this.note)
                    .name("midiNumber")
                    .build();
            pitchProperty.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> arg0,
                        Number old, Number newPitch) {
                    logger.debug("listener new {}", newPitch);
                    updateLabels();
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
