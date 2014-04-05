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

import com.rockhoppertech.music.fx.DragContext;
import com.rockhoppertech.music.fx.keyboard.KeyboardPanel;
import com.rockhoppertech.music.fx.keyboard.WhiteKey;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
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

    private double beatWidth = 60d;

    private double snapBeatDivision = 4d;

    private double currentInsertBeat = 1d;

    private KeyboardPanel keyboard;

    public PianorollPane(KeyboardPanel keyboard) {
        getStyleClass().setAll("pianorollpane-control");
        this.setStyle("-fx-background-color: antiquewhite;");
        this.setWidth(this.beatWidth * 64d);
        this.setPrefWidth(this.beatWidth * 64d);
        this.setHeight(keyboard.getHeight());
        // this.setPrefHeight(800);
        // this.track = new MIDITrack();
        // this.track.add(new MIDINote());

        this.snapX = this.beatWidth / this.snapBeatDivision;
        this.drawSnapGrid();

        WhiteKey wk = new WhiteKey(0, Orientation.VERTICAL);
        this.snapY = wk.getLayoutBounds().getHeight();

        this.keyboard = keyboard;
        // this.setTrack(track);

        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                logger.debug("press x {} y {}", e.getX(), e.getY());
                int pitch = getPitchForY(e.getY());
                double beat = getBeatFromX(e.getX());
                logger.debug("press pitch {} beat {}", pitch, beat);

            }
        });
    }

    public void setCurrentInsertBeat(double currentInsertBeat) {
        this.currentInsertBeat = currentInsertBeat;
    }

    void drawSnapGrid() {

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
        }
    }

    void createNoteNodes() {
        this.getChildren().clear();
        drawSnapGrid();
        for (MIDINote note : this.track) {
            NoteNode node = new NoteNode();
            node.setNote(note);
            node.setPrefHeight(this.snapY);
            node.setPrefWidth(note.getDuration() * this.beatWidth);
            node.setLayoutX((note.getStartBeat() - 1d) * this.beatWidth);
            node.setLayoutY(getYforPitchNumber(note.getMidiNumber()));

            //this.getChildren().add(enableDrag(node));
            //this.getChildren().add(makeDraggable(node));
            //this.getChildren().add(addMouseHandlers(node));
            this.getChildren().add(node);
        }
    }

    /**
     * @return the track
     */
    public MIDITrack getTrack() {
        return track;
    }

    /**
     * @param track
     *            the track to set
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
        double beat = (x / beatWidth) + 1d;
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
     * @param snapX
     *            the snapX to set
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
     * @param snapY
     *            the snapY to set
     */
    public void setSnapY(double snapY) {
        this.snapY = snapY;
    }

    /**
     * @return the beatWidth
     */
    public double getBeatWidth() {
        return beatWidth;
    }

    /**
     * @param beatWidth
     *            the beatWidth to set
     */
    public void setBeatWidth(double beatWidth) {
        this.beatWidth = beatWidth;
    }

    private Node makeDraggable(final Node node) {
        final DragContext dragContext = new DragContext();
        final Group wrapGroup = new Group(node);

        wrapGroup.addEventFilter(
                MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {
                        // disable mouse events for all children
                        // mouseEvent.consume();
                    }
                });

        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {

                        // remember initial mouse cursor coordinates
                        // and node position
                        dragContext.mouseAnchorX = mouseEvent.getX();
                        dragContext.mouseAnchorY = mouseEvent.getY();
                        dragContext.initialTranslateX =
                                node.getTranslateX();
                        dragContext.initialTranslateY =
                                node.getTranslateY();
                        node.setCursor(Cursor.MOVE);

                    }
                });

        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {

                        // shift node from its initial position by delta
                        // calculated from mouse cursor movement
                        double xx = dragContext.initialTranslateX
                                + mouseEvent.getX()
                                - dragContext.mouseAnchorX;
                        System.err.println("xx " + xx);
                        // xx = xx - xx % 25;
                        // xx -= xx % 25;
                        xx -= xx % snapX;
                        if (xx < 0) {
                            xx = 0;
                        }
                        System.err.println("sx " + xx);
                        node.setTranslateX(xx);

                        node.setTranslateY(
                                dragContext.initialTranslateY
                                        + mouseEvent.getY()
                                        - dragContext.mouseAnchorY);
                        logger.debug("dragged to beat {}", getBeatFromX(xx));
                        // TODO might not be performant with big tracks.
                        // configurable?
                        if (node instanceof NoteNode) {
                            NoteNode tn = (NoteNode) node;
                            tn.getNote().setStartBeat(getBeatFromX(xx));
                        }

                    }
                });

        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {

                        double xx = dragContext.initialTranslateX
                                + mouseEvent.getX()
                                - dragContext.mouseAnchorX;
                        // xx -= xx % 25;
                        xx -= xx % snapX;
                        if (xx < 0) {
                            xx = 0;
                        }
                        double yy = dragContext.initialTranslateY
                                + mouseEvent.getY()
                                - dragContext.mouseAnchorY;
                        
                        yy -= yy % snapY;
                        
                        
                        node.setTranslateX(xx);
                        node.setTranslateY(yy);
                        logger.debug("released at beat {}", getBeatFromX(xx));
                        if (node instanceof NoteNode) {
                            NoteNode tn = (NoteNode) node;
                            MIDINote note = tn.getNote();
                            note.setStartBeat(getBeatFromX(xx));
                            note.setMidiNumber(getPitchForY(yy));
                            
                        }
                        node.setCursor(Cursor.DEFAULT);

                    }

                });

        return wrapGroup;
    }

    private Node addMouseHandlers(final Node node) {
        final DragContext dragContext = new DragContext();

        node.addEventHandler(
                MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {
                        // disable mouse events for all children
                        // mouseEvent.consume();
                    }
                }
        );

        node.addEventHandler(
                MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {

                        // remember initial mouse cursor coordinates
                        // and node position
                        dragContext.mouseAnchorX = mouseEvent.getX();
                        dragContext.mouseAnchorY = mouseEvent.getY();
                        dragContext.initialTranslateX =
                                node.getTranslateX();
                        dragContext.initialTranslateY =
                                node.getTranslateY();
                        node.setCursor(Cursor.MOVE);

                    }
                }
        );

        node.addEventHandler(
                MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {

                        // shift node from its initial position by delta
                        // calculated from mouse cursor movement
                        double xx = dragContext.initialTranslateX
                                + mouseEvent.getX()
                                - dragContext.mouseAnchorX;
                        System.err.println("xx " + xx);
                        // xx = xx - xx % 25;
                        // xx -= xx % 25;

                        // xx -= xx % snapX;
                       /* if (xx < 0) {
                            xx = 0;
                        }*/
                        System.err.println("sx " + xx);
                        node.setTranslateX(xx);

                        node.setTranslateY(
                                dragContext.initialTranslateY
                                        + mouseEvent.getY()
                                        - dragContext.mouseAnchorY
                        );
                        logger.debug("dragged to beat {}", getBeatFromX(xx));

                        // TODO might not be performant with big tracks.
                        // configurable?
                        if (node instanceof NoteNode) {
                            NoteNode tn = (NoteNode) node;
                            //   tn.getNote().setStartBeat(getBeatFromX(xx));
                        }

                    }
                }
        );

        node.addEventHandler(
                MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {

                        double xx = dragContext.initialTranslateX
                                + mouseEvent.getX()
                                - dragContext.mouseAnchorX;
                        // xx -= xx % 25;
                        xx -= xx % snapX;
                        if (xx < 0) {
                            xx = 0;
                        }
                        double yy = dragContext.initialTranslateY
                                + mouseEvent.getY()
                                - dragContext.mouseAnchorY;

                        yy -= yy % snapY;


                        node.setTranslateX(xx);
                        node.setTranslateY(yy);
                        logger.debug("released at beat {}", getBeatFromX(xx));
                        if (node instanceof NoteNode) {
                            NoteNode tn = (NoteNode) node;
                            MIDINote note = tn.getNote();
                            note.setStartBeat(getBeatFromX(xx));
                            note.setMidiNumber(getPitchForY(yy));

                        }
                        node.setCursor(Cursor.DEFAULT);

                    }

                }
        );

        return node;
    }


    private double nodeX;
    private double nodeY;
    private double mouseX;
    private double mouseY;
    static class Delta { double x, y; }

    private Node enableDrag(final Node node) {
        final Delta dragDelta = new Delta();
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
               // dragDelta.x = node.getLayoutX() - mouseEvent.getX();
                //dragDelta.y = node.getLayoutY() - mouseEvent.getY();
                node.getScene().setCursor(Cursor.MOVE);

                final double parentScaleX = node.getParent().
                        localToSceneTransformProperty().getValue().getMxx();
                final double parentScaleY = node.getParent().
                        localToSceneTransformProperty().getValue().getMyy();

                // record the current mouse X and Y position on Node
                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();

                nodeX = node.getLayoutX() * parentScaleX;
                nodeY = node.getLayoutY() * parentScaleY;

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
                //node.setLayoutX(mouseEvent.getX() + dragDelta.x);
                //node.setLayoutY(mouseEvent.getY() + dragDelta.y);
                //node.setLayoutX(mouseEvent.getX());
               // node.setLayoutY(mouseEvent.getY());


                final double parentScaleX = node.getParent().
                        localToSceneTransformProperty().getValue().getMxx();
                final double parentScaleY = node.getParent().
                        localToSceneTransformProperty().getValue().getMyy();

                // Get the exact moved X and Y

                double offsetX = mouseEvent.getSceneX() - mouseX;
                double offsetY = mouseEvent.getSceneY() - mouseY;

                nodeX += offsetX;
                nodeY += offsetY;

                double scaledX = nodeX * 1 / parentScaleX;
                double scaledY = nodeY * 1 / parentScaleY;

                node.setLayoutX(scaledX);
                node.setLayoutY(scaledY);

                // again set current Mouse x AND y position
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

    private Node origenableDrag(final Node node) {
        final Delta dragDelta = new Delta();
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = node.getLayoutX() - mouseEvent.getX();
                dragDelta.y = node.getLayoutY() - mouseEvent.getY();
                node.getScene().setCursor(Cursor.MOVE);
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
                //node.setLayoutX(mouseEvent.getX() + dragDelta.x);
                //node.setLayoutY(mouseEvent.getY() + dragDelta.y);
                node.setLayoutX(mouseEvent.getX());
                node.setLayoutY(mouseEvent.getY());

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

}
