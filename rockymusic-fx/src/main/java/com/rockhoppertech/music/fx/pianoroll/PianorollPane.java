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

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.fx.keyboard.KeyboardPanel;
import com.rockhoppertech.music.fx.keyboard.WhiteKey;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 */
public class PianorollPane extends Pane {

    private static final Logger logger = LoggerFactory
            .getLogger(PianorollPane.class);

    private MIDITrack track;

    private Node dragee;
    // private RubberRect rubberRect;
    private int dragDeltaX;
    private int dragDeltaY;
    // private ResizeBorder selectedBorder;

    // index in the track
    private int selectedIndex;
    private int corner;
    private boolean isResizing;

    // snap stuff
    private double snapX;
    /**
     * The height of a piano key
     */
    private double snapY = 20d;
    boolean snapOn = false;

    private Line currentInsertBeatLine;

    //private double beatWidth = 60d;

    private DoubleProperty beatWidthProperty = new SimpleDoubleProperty(60d);

    private double snapBeatDivision = 4d;

    private double currentInsertBeat = 1d;

    private KeyboardPanel keyboard;

    public PianorollPane(KeyboardPanel keyboard) {
        getStyleClass().setAll("pianorollpane-control");
        this.setStyle("-fx-background-color: antiquewhite;");
        this.setWidth(this.getBeatWidth() * 64d);
        this.setPrefWidth(this.getBeatWidth() * 64d);
        this.setHeight(keyboard.getHeight());
        // this.setPrefHeight(800);
        // this.track = new MIDITrack();
        // this.track.add(new MIDINote());

        this.snapX = this.getBeatWidth() / this.snapBeatDivision;
        this.drawSnapGrid();

        WhiteKey wk = new WhiteKey(0, Orientation.VERTICAL);
        this.snapY = wk.getLayoutBounds().getHeight();

        this.keyboard = keyboard;
        // this.setTrack(track);

        keyboard.midiNoteProperty().addListener(new ChangeListener<MIDINote>() {
            @Override
            public void changed(ObservableValue<? extends MIDINote> observableValue, MIDINote oldMidiNote, MIDINote newMidiNote) {
                logger.debug("got a midi note {}", newMidiNote);
                newMidiNote.setStartBeat(currentInsertBeat);
                track.add(newMidiNote);
                createNode(newMidiNote);
                currentInsertBeat += newMidiNote.getDuration();
                updateInsertBeat();
            }
        });

        currentInsertBeatLine = new Line(0d, 0d, 0d, this.getHeight() );
        currentInsertBeatLine.setStroke(new Color(1, 0, 0, .8));
        currentInsertBeatLine.setStrokeWidth(2d);


        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                logger.debug("press x {} y {}", e.getX(), e.getY());
                int pitch = getPitchForY(e.getY());
                double beat = getBeatFromX(e.getX());
                logger.debug("press pitch {} beat {}", pitch, beat);
                currentInsertBeat = getBeatFromX(e.getX());
                updateInsertBeat();

            }
        });
        this.setupContextMenu();
    }

    public DoubleProperty beatWidthProperty() {
        return beatWidthProperty;
    }

    public double getBeatWidth() {return beatWidthProperty.get();}

    public void setBeatWidth(double d) {beatWidthProperty.set(d);}


    public void setCurrentInsertBeat(double currentInsertBeat) {
        this.currentInsertBeat = currentInsertBeat;
    }

    //TODO srsly?
    void removeGridLines() {
        ObservableList<Node> kids = this.getChildren();
        ObservableList<Node> lines = FXCollections.observableArrayList();
        for(Node n: kids) {
            if(n instanceof Line) {
                lines.add(n);
                //logger.debug("removing line {}", n);
            }
        }
        this.getChildren().removeAll(lines);


//        Task<Void> task = new Task<Void>() {
//            @Override protected Void call() throws Exception {
//                for(Node n: kids) {
//                    if (isCancelled()) break;
//
//
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(n instanceof Line) {
//                                getChildren().remove(n);
//                                logger.debug("removing line");
//                            }
//                        }
//                    });
//                }
//                return null;
//            }
//        };
//        new Thread(task).start();

    }

    void drawSnapGrid() {
        removeGridLines();

        logger.debug("Created lines with snapx {}", snapX);

        final double ncols = this.getLayoutBounds().getWidth();
        final double nrows = this.getLayoutBounds().getHeight();

        final double yoffset = 0d;

        for (double y = yoffset + this.snapY; y < nrows; y += this.snapY) {
            Line line = new Line(0,
                    y,
                    ncols,
                    y);
            line.setStroke(new Color(0, 0, 0, .1));
            this.getChildren().add(line);
        }

        for (double x = this.snapX; x < ncols; x += this.snapX) {
            Line line = new Line(x,
                    yoffset,
                    x,
                    nrows);
            line.setStroke(new Color(0, 0, 0, .1));
            this.getChildren().add(line);
            //logger.debug("Created line {}", line);
        }
    }

    void updateInsertBeat() {
        double lx = (currentInsertBeat -1) * this.getBeatWidth();
        lx -= lx % getSnapX();
        currentInsertBeatLine.setStartX(lx);
        currentInsertBeatLine.setEndX(lx);
    }
    void createNoteNodes() {
        this.getChildren().clear();
        drawSnapGrid();

        // update line x
        double lx = currentInsertBeat * this.getBeatWidth();
        currentInsertBeatLine.setStartX(lx);
        currentInsertBeatLine.setEndX(lx);
        getChildren().add(currentInsertBeatLine);

        for (MIDINote note : this.track) {
            createNode(note);
        }
    }

    void createNode(MIDINote note) {
        NoteNode node = new NoteNode();
        node.setNote(note);
        node.setPrefHeight(this.snapY);
        node.setPrefWidth(note.getDuration() * this.getBeatWidth());
        node.setLayoutX((note.getStartBeat() - 1d) * this.getBeatWidth());
        node.setLayoutY(getYforPitchNumber(note.getMidiNumber()));
        this.getChildren().add(node);
    }

    /**
     * @return the track
     */
    public MIDITrack getTrack() {
        return track;
    }

    /**
     * @param track the track to set
     */
    public void setTrack(MIDITrack track) {
        this.track = track;
        this.createNoteNodes();
    }

    double getYforPitchNumber(int midiNumber) {
        return keyboard.getKeyY(midiNumber);
    }

    public int getPitchForY(double y) {
        return this.keyboard.getPitchForY(y);
    }

    // public int getPitchAtY(double y) {
    // int heuristicFudge = 5;
    // // TODO do something better
    // int pitch = this.keyboard.getKeyAt(1,
    // y + heuristicFudge);
    // return pitch;
    // }

    double getBeatFromX(double x) {
        double beat = (x / getBeatWidth()) + 1d;
        if (beat < 1d) {
            beat = 1d;
        }
        return beat;
    }

    /**
     * @return the snapX
     */
    public double getSnapX() {
        return snapX;
    }

    /**
     * @param snapX the snapX to set
     */
    public void setSnapX(double snapX) {
        this.snapX = snapX;
    }

    /**
     * @return the snapY
     */
    public double getSnapY() {
        return snapY;
    }

    /**
     * @param snapY the snapY to set
     */
    public void setSnapY(double snapY) {
        this.snapY = snapY;
    }

    private BooleanProperty showInsertBeatProperty = new SimpleBooleanProperty();

    public void setShowInsertBeat(boolean b) {
        this.showInsertBeatProperty.set(b);
    }
    public boolean getShowInsertBeat() {
        return this.showInsertBeatProperty.get();
    }


    void setupContextMenu() {
        final ContextMenu cm = new ContextMenu();

        // MenuItem cmItem1 = new MenuItem("Copy");
        // cmItem1.setOnAction(new EventHandler<ActionEvent>() {
        // public void handle(ActionEvent e) {
        // Clipboard clipboard = Clipboard.getSystemClipboard();
        // ClipboardContent content = new ClipboardContent();
        // // content.putImage(pic.getImage());
        // clipboard.setContent(content);
        // }
        // });
        // cm.getItems().add(cmItem1);

        Slider beatWidthSlider = new Slider();
        beatWidthSlider.setMin(30d);
        beatWidthSlider.setMax(200d);
        beatWidthSlider.setValue(this.getBeatWidth());
        beatWidthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number oldValue, Number newValue) {

                //TODO also have to update the header
                PianorollPane.this.setBeatWidth(newValue.doubleValue());
                PianorollPane.this.snapX = PianorollPane.this.getBeatWidth() / PianorollPane.this.snapBeatDivision;
                PianorollPane.this.createNoteNodes();
            }
        });

        CustomMenuItem sliderMenuItem = new CustomMenuItem(beatWidthSlider);
                sliderMenuItem.setHideOnClick(false);
        cm.getItems().add(sliderMenuItem);
        // no tooltips on menu items
        //Tooltip tooltip = new Tooltip();
        //Tooltip.install(sliderMenuItem, tooltip);
        configureBeatDivisionComboBox();
        CustomMenuItem comboBoxMenuItem = new CustomMenuItem(beatDivisionComboBox);
        comboBoxMenuItem.setHideOnClick(false);
        cm.getItems().add(comboBoxMenuItem);

        CheckMenuItem cmi = new CheckMenuItem("Show Insert Beat");
        cmi.setSelected(false);
        cmi.selectedProperty().bind(showInsertBeatProperty);
        cmi.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov,
                                Boolean old, Boolean newVal) {
            }
        });
        cm.getItems().add(cmi);

        this.addEventFilter(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        logger.debug("mouse clicked");
                        if (e.getButton() == MouseButton.SECONDARY) {
                            cm.show(
                                    PianorollPane.this,
                                    e.getScreenX(),
                                    e.getScreenY());
                            e.consume();
                        }
                    }
                });
    }

    private ComboBox<Double> beatDivisionComboBox;

    void configureBeatDivisionComboBox() {
        this.beatDivisionComboBox = new ComboBox<Double>();

        ObservableList<Double> divisionList = FXCollections
                .observableArrayList();
        divisionList.addAll(Duration.THIRTY_SECOND_NOTE,Duration.SIXTEENTH_NOTE, Duration.EIGHTH_NOTE, Duration.QUARTER_NOTE);
        beatDivisionComboBox.setItems(divisionList);
        beatDivisionComboBox.getSelectionModel().select(1d/snapBeatDivision);

        beatDivisionComboBox.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Double>() {
                    public void changed(
                            ObservableValue<? extends Double> source,
                            Double oldValue, Double newValue) {
                        //logger.debug("You selected: " + newValue);
                        PianorollPane.this.snapBeatDivision = 1 / newValue;
                        PianorollPane.this.snapX = PianorollPane.this.getBeatWidth() / PianorollPane.this.snapBeatDivision;
                        PianorollPane.this.drawSnapGrid();
                    }
                });

        beatDivisionComboBox.setCellFactory(
                new Callback<ListView<Double>, ListCell<Double>>() {
                    @Override
                    public ListCell<Double> call(
                            ListView<Double> param) {

                        final ListCell<Double> cell = new ListCell<Double>() {
                            @Override
                            public void updateItem(Double item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    //TODO use a note symbol?
                                    setText(""+item);
                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });
    }
}
