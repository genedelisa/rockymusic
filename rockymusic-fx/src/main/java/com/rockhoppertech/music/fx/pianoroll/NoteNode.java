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

import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.fx.ResizeHandle;
import com.rockhoppertech.music.midi.js.MIDINote;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.adapter.JavaBeanDoublePropertyBuilder;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private ResizeHandle leftHandle;
    private Tooltip tooltip;
    // for dragging
    private double nodeX;
    private double nodeY;
    private double mouseX;
    private double mouseY;

    // grabbed when mouse is pressed
    private PianorollPane parent;

    public NoteNode() {
        getStyleClass().setAll("notenode-control");
        this.setStyle("-fx-background-color: gray;");

        this.rightHandle = new ResizeHandle(ResizeHandle.ResizeMode.RIGHT);
        this.getChildren().add(rightHandle);
        rightHandle.layoutXProperty().bind(
                this.widthProperty().subtract(rightHandle.getPrefWidth()));
        rightHandle
                .layoutYProperty()
                .bind(
                        this.heightProperty()
                                .subtract(rightHandle.prefHeight(-1)).divide(2));

        this.leftHandle = new ResizeHandle(ResizeHandle.ResizeMode.LEFT);
        this.getChildren().add(leftHandle);
        leftHandle.layoutXProperty().set(0);
        leftHandle
                .layoutYProperty()
                .bind(
                        this.heightProperty()
                                .subtract(leftHandle.prefHeight(-1)).divide(2));

        this.text = new Text();
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

        this.tooltip = new Tooltip();
        Tooltip.install(this, this.tooltip);

        this.enableDrag(this);
    }


    private Node enableDrag(final Node node) {

        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                    node.getScene().setCursor(Cursor.MOVE);

                final double parentScaleX = node.getParent().
                        localToSceneTransformProperty().getValue().getMxx();
                final double parentScaleY = node.getParent().
                        localToSceneTransformProperty().getValue().getMyy();

                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();
                nodeX = node.getLayoutX() * parentScaleX;
                nodeY = node.getLayoutY() * parentScaleY;
                parent = (PianorollPane) node.getParent();
                rightHandle.setSnapX(parent.getSnapX());
                leftHandle.setSnapX(parent.getSnapX());
            }
        });

        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.getScene().setCursor(Cursor.HAND);
            }
        });

        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final double parentScaleX = node.getParent().
                        localToSceneTransformProperty().getValue().getMxx();
                final double parentScaleY = node.getParent().
                        localToSceneTransformProperty().getValue().getMyy();

                double offsetX = mouseEvent.getSceneX() - mouseX;
                double offsetY = mouseEvent.getSceneY() - mouseY;

                nodeX += offsetX;
                nodeY += offsetY;

                double scaledX = nodeX * 1 / parentScaleX;
                double scaledY = nodeY * 1 / parentScaleY;

                // snap
                //scaledY -= scaledY % (PianoKey.getWhiteKeyHeight(Orientation.VERTICAL) / 2d);
                scaledY -= scaledY % ( parent.getSnapY() / 2d );
                scaledX -= scaledX % ( parent.getSnapX());

                //TODO check for b and e since there is no black key
                if(parent != null) {
                    int pitch = parent.getPitchForY(scaledY);
                    logger.debug("pitch {}", pitch);
                    note.setMidiNumber(pitch);
                    double beat = parent.getBeatFromX(scaledX);
                    logger.debug("beat {}", beat);
                    note.setStartBeat(beat);
                }

                node.setLayoutX(scaledX);
                node.setLayoutY(scaledY);

                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();
            }
        });

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    node.getScene().setCursor(Cursor.HAND);
                }
            }
        });

        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    node.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });
        return node;
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
        logger.debug("setting note {}", note);
        updateLabels();
        this.setupProperties();
    }
    
    void updateLabels() {
        String ps = PitchFormat.getInstance().format(note.getPitch());
        this.text.setText(ps);
        String s =String.format(
                "%s %.2f %.2f",
                ps,
                note.getStartBeat(),
                note.getDuration()
        );
        tooltip.setText(s);
    }
    

    public void play() {
        // this.note
    }

    private DoubleProperty startBeatProperty;
    private DoubleProperty durationProperty;
    //private IntegerProperty midiNumberProperty;
    private ObjectProperty pitchProperty;

    public DoubleProperty startBeatProperty() {
        return startBeatProperty;
    }

    public DoubleProperty durationProperty() {
        return durationProperty;
    }

    /*public IntegerProperty midiNumberProperty() {
        return midiNumberProperty;
    }*/
    public ObjectProperty pitchProperty() {
        return pitchProperty;
    }

    void setupProperties() {
        logger.debug("setting up listeners for note {}", this.note);
        try {
            startBeatProperty = JavaBeanDoublePropertyBuilder.create()
                    .bean(this.note)
                    .name("startBeat")
                    .build();
            startBeatProperty.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> arg0,
                        Number old, Number newStart) {
                    logger.debug("listener start beat new {}", newStart);
                    updateLabels();
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
                    updateLabels();
                }
            });
            //TODO why doesn't this work?
            pitchProperty = JavaBeanObjectPropertyBuilder.create()
                    .bean(this.note)
                    .name("pitch")
                    .build();
            pitchProperty.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> arg0,
                        Number old, Number newPitch) {
                    logger.debug("listener pitch new {}", newPitch);
                    updateLabels();
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
        }
    }

}
