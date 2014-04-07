package com.rockhoppertech.music.fx.keyboard;

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

import com.rockhoppertech.music.PitchFactory;
import com.rockhoppertech.music.midi.js.Instrument;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDISender;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

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

    // given a y, get the pitch
    //private Map<Double, Integer> pitches;

    NavigableMap<Double, Integer> pitches = new TreeMap<>();

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

    int sendOctave = 5;
    int sendPitch = 0;
    int sendMidiPitchNumber;
    int sendVelocity = 64;
    private MIDISender midiSender;
    private ComboBox<Instrument> instrumentComboBox;

    public KeyboardPanel(Orientation orientation, int startOctave,
            int numberOfOctaves) {
        this.startOctave = startOctave;
        this.numberOfOctaves = numberOfOctaves;
        this.orientation = orientation;
        this.locations = new HashMap<Integer, Point2D>();
        //this.pitches = new TreeMap<Double, Integer>();

        this.setPreferredSize();
        this.setupKeys();
        this.midiSender = new MIDISender();
        this.setupKeyEvents();
        this.configureInstrumentComboBox();
        this.setupContextMenu();
        PianoKey.setMIDISender(midiSender);
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

        // CustomMenuItem sliderMenuItem = new CustomMenuItem(new Slider());
        // sliderMenuItem.setHideOnClick(false);
        // cm.getItems().add(sliderMenuItem);

        CustomMenuItem comboBoxMenuItem = new CustomMenuItem(instrumentComboBox);
        comboBoxMenuItem.setHideOnClick(false);
        cm.getItems().add(comboBoxMenuItem);

        this.addEventFilter(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        logger.debug("mouse clicked");
                        if (e.getButton() == MouseButton.SECONDARY) {
                            cm.show(
                                    KeyboardPanel.this,
                                    e.getScreenX(),
                                    e.getScreenY());
                            e.consume();
                        }
                    }
                });
    }

    private ObjectProperty<MIDINote> midiNoteProperty;

    public ObjectProperty<MIDINote> midiNoteProperty() {
        return midiNoteProperty;
    }
    public MIDINote getMIDINote() {
        return midiNoteProperty.get();
    }
    public void setMIDINote(MIDINote n) {
        midiNoteProperty.set(n);
    }

    /**
     * Play notes when keys are pressed.
     */
    private void setupKeyEvents() {

        this.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                midiSender.sendNoteOff(sendMidiPitchNumber);
            }
        });
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                MIDINote note = null;
                if (event.getCode() == KeyCode.A) {

                    sendMidiPitchNumber = PitchFactory.getPitch(
                            "A" + sendOctave)
                            .getMidiNumber();

                    // these don't seem to work
                    if (event.isShiftDown()) {
                        sendMidiPitchNumber++;
                    }
                    if (event.isControlDown()) {
                        sendMidiPitchNumber--;
                    }
                    midiSender.sendNoteOn(sendMidiPitchNumber, sendVelocity);



                } else if (event.getCode() == KeyCode.B) {
                    sendMidiPitchNumber = PitchFactory.getPitch(
                            "B" + sendOctave)
                            .getMidiNumber();
                    midiSender.sendNoteOn(sendMidiPitchNumber, sendVelocity);
                } else if (event.getCode() == KeyCode.C) {
                    sendMidiPitchNumber = PitchFactory.getPitch(
                            "C" + sendOctave)
                            .getMidiNumber();
                    midiSender.sendNoteOn(sendMidiPitchNumber, sendVelocity);
                } else if (event.getCode() == KeyCode.D) {
                    sendMidiPitchNumber = PitchFactory.getPitch(
                            "D" + sendOctave)
                            .getMidiNumber();
                    midiSender.sendNoteOn(sendMidiPitchNumber, sendVelocity);
                } else if (event.getCode() == KeyCode.E) {
                    sendMidiPitchNumber = PitchFactory.getPitch(
                            "E" + sendOctave)
                            .getMidiNumber();
                    midiSender.sendNoteOn(sendMidiPitchNumber, sendVelocity);
                } else if (event.getCode() == KeyCode.F) {
                    sendMidiPitchNumber = PitchFactory.getPitch(
                            "F" + sendOctave)
                            .getMidiNumber();
                    midiSender.sendNoteOn(sendMidiPitchNumber, sendVelocity);
                } else if (event.getCode() == KeyCode.G) {
                    sendMidiPitchNumber = PitchFactory.getPitch(
                            "G" + sendOctave)
                            .getMidiNumber();
                    midiSender.sendNoteOn(sendMidiPitchNumber, sendVelocity);
                } else if (event.getCode() == KeyCode.DIGIT0) {
                    sendOctave = 0;
                } else if (event.getCode() == KeyCode.DIGIT1) {
                    sendOctave = 1;
                } else if (event.getCode() == KeyCode.DIGIT2) {
                    sendOctave = 2;
                } else if (event.getCode() == KeyCode.DIGIT3) {
                    sendOctave = 3;
                } else if (event.getCode() == KeyCode.DIGIT4) {
                    sendOctave = 4;
                } else if (event.getCode() == KeyCode.DIGIT5) {
                    sendOctave = 5;
                } else if (event.getCode() == KeyCode.DIGIT6) {
                    sendOctave = 6;
                } else if (event.getCode() == KeyCode.DIGIT7) {
                    sendOctave = 7;
                } else if (event.getCode() == KeyCode.DIGIT8) {
                    sendOctave = 8;
                } else if (event.getCode() == KeyCode.DIGIT9) {
                    sendOctave = 9;
                }
                note = new MIDINote(sendMidiPitchNumber);
                setMIDINote(note);
            }
        });

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                requestFocus();
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
            }
        });
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

            this.pitches.put(y, wk.getKey());
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

            this.pitches.put(y, bk.getKey());
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
            this.pitches.put(y, wk.getKey());
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
            this.pitches.put(y, bk.getKey());
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

    public int getPitchForY(double y) {
        logger.debug("getting pitch for y {}", y);
        logger.debug("pitches {}", pitches);
       // y -= y % (PianoKey.getWhiteKeyHeight(Orientation.VERTICAL) / 2d);
        //logger.debug("y snapped {}", y);
        
        Double floor = this.pitches.floorKey(y);
        logger.debug("floor {}", floor);
        int p = this.pitches.get(floor);
        logger.debug("pitch {}", p);
        return p;
    }


//    public int getKeyAt(double x, double y) {
//
//        // this.locations.put(new Integer(wk.getKey()), new Point2D(x, y));
//
//        for (Entry<Integer, Point2D> e : locations.entrySet()) {
//            Rectangle r = new Rectangle(e.getValue().getX(),
//
//                    e.getValue().getY(),
//                    100d,
//                    PianoKey.getWhiteKeyHeight(this.orientation));
//            if (r.contains(x, y)) {
//                return e.getKey().intValue();
//            }
//        }
//
//        return 0;
//
//        // PianoKey k = (PianoKey) getComponentAt(x, y);
//        // if (k == null) {
//        // logger.debug("bad location for key" + x + " y=" + y);
//        // return 0;
//        // }
//        // return k.getKey();
//    }

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

    /**
     * for a ScrollPane
     * 
     * @return bounds
     */
    public Bounds getPreferredScrollableViewportSize() {
        PianoKey wk = new WhiteKey(0, this.orientation);

        // 2 octaves

        if (this.orientation == Orientation.HORIZONTAL)
            return new BoundingBox(0d, 0d, wk.getPrefWidth() * 14d,
                    wk.getPrefHeight());
        else
            return new BoundingBox(0, 0, wk.getPrefHeight(),
                    wk.getPrefHeight() * 14);
    }

    public double getOctaveWidth() {
        PianoKey wk = new WhiteKey(0, Orientation.HORIZONTAL);
        return wk.getPrefWidth() * 7d;
    }

    public double getOctaveHeight() {
        PianoKey wk = new WhiteKey(0, Orientation.VERTICAL);
        return wk.getPrefHeight() * 7d;
    }

    /**
     * ScrollPane doesn't have this yet.
     * 
     * @return the increment
     */
    public double getScrollableUnitIncrement() {
        PianoKey wk = new WhiteKey(0, this.orientation);
        if (this.orientation == Orientation.HORIZONTAL)
            return wk.getPrefWidth();
        else
            return wk.getPrefHeight();
    }

    /**
     * ScrollPane doesn't have this yet.
     * 
     * @return the increment
     */
    public double getScrollableBlockIncrement() {
        PianoKey wk = new WhiteKey(0, this.orientation);
        if (this.orientation == Orientation.HORIZONTAL)
            return wk.getPrefWidth() * 7;
        else
            return wk.getPrefHeight() * 7;
    }

    void configureInstrumentComboBox() {
        this.instrumentComboBox = new ComboBox<Instrument>();

        ObservableList<Instrument> instrumentList = FXCollections
                .observableArrayList();
        for (Instrument p : Instrument.getAll()) {
            logger.debug("adding instrument {}", p);
            instrumentList.add(p);
        }
        instrumentComboBox.setItems(instrumentList);
        instrumentComboBox.getSelectionModel().selectFirst();

        instrumentComboBox.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Instrument>() {
                    public void changed(
                            ObservableValue<? extends Instrument> source,
                            Instrument oldValue, Instrument newValue) {
                        logger.debug("You selected: " + newValue);
                        // model.getSelectedTrack().setInstrument(newValue);
                        midiSender.setProgram(newValue.getPatch().getProgram());
                        // midiSender.setInstrument(newValue);
                    }
                });

        instrumentComboBox.setCellFactory(
                new Callback<ListView<Instrument>, ListCell<Instrument>>() {
                    @Override
                    public ListCell<Instrument> call(
                            ListView<Instrument> param) {

                        final ListCell<Instrument> cell = new ListCell<Instrument>() {
                            @Override
                            public void updateItem(Instrument item,
                                    boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item.getName());
                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });

        // otherwise, you'd get the Instrument toString when combo is in
        // unselected state
        instrumentComboBox.setConverter(new StringConverter<Instrument>() {
            @Override
            public String toString(Instrument inst) {
                if (inst == null) {
                    return null;
                } else {
                    return inst.getName();
                }
            }

            @Override
            public Instrument fromString(String name) {
                return Instrument.getByName(name);
            }
        });
    }
}
