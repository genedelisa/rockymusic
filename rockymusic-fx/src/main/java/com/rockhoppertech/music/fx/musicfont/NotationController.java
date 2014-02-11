package com.rockhoppertech.music.fx.musicfont;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.rockhoppertech.music.fx.musicfont.model.StaffModel;
import com.rockhoppertech.music.midi.js.ConsoleReceiver;
import com.rockhoppertech.music.midi.js.MIDISender;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

public class NotationController {
    private static final Logger logger = LoggerFactory
            .getLogger(NotationController.class);

    StaffModel model;
    NotationView view;
    Stage stage;
    MIDISender midiSender;

    // GUI. maybe fxml sometime
    private Canvas canvas;
    private TextField textField;

    /**
     * @param model
     * @param view
     */
    public NotationController(StaffModel model, NotationView view) {
        this.model = model;
        this.view = view;
        this.midiSender = new MIDISender();
        this.midiSender.addReceiver(new ConsoleReceiver());
        //
        this.setCanvas(this.view.getCanvas());
    }

    public void setFontSize(double fontSize) {
        this.model.setFontSize(fontSize);
    }

    int pitch;

    public void mousePressed(MouseEvent evt) {
        pitch = model.whichNote(evt.getY());
        this.midiSender.sendNoteOn(pitch, 64);
    }

    public void mouseReleased(MouseEvent evt) {
        this.midiSender.sendNoteOff(pitch);
    }

    public void mouseDragged(MouseEvent evt) {

    }

    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage
     *            the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCanvas(Canvas canvas2) {
        this.canvas = canvas2;
        this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                mousePressed(evt);
            }
        });
        this.canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                mouseReleased(evt);
            }
        });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                mouseDragged(evt);
            }
        });

    }

    public void notestringTextFieldAction(ActionEvent event) {
        String ns = textField.getText();
        MIDITrack track = MIDITrackBuilder
                .create()
                .noteString(ns)
                .build();
        logger.debug("track from string\n{}", track);
        this.model.setTrack(track);
    }

    /**
     * @param textField
     *            the textField to set
     */
    public void setTextField(TextField textField) {
        this.textField = textField;
        this.textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notestringTextFieldAction(event);
            }
        });
    }

}
