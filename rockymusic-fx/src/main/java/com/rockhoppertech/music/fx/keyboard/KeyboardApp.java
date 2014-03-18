package com.rockhoppertech.music.fx.keyboard;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class KeyboardApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Keyboard");
        final Pane root = new Pane();
        //BorderPane bp = new BorderPane();

        
        KeyboardPanel keyboard = new KeyboardPanel(Orientation.HORIZONTAL,
                KeyboardPanel.MIN_OCTAVE, KeyboardPanel.MAX_OCTAVE);
        ScrollPane sp = new ScrollPane();
        sp.setLayoutX(100);
        sp.setPrefWidth(keyboard.getOctaveWidth());
        sp.setContent(keyboard);
        root.getChildren().add(sp);
        
        //sp.setViewportBounds(keyboard.getPreferredScrollableViewportSize());
        
        
        KeyboardPanel vkeyboard = new KeyboardPanel(Orientation.VERTICAL,
                KeyboardPanel.MIN_OCTAVE, KeyboardPanel.MAX_OCTAVE);
        ScrollPane sp2 = new ScrollPane();
        sp2.setLayoutX(0);
        //sp2.setPrefHeight(200);
        sp2.setPrefHeight(keyboard.getOctaveHeight());
        sp2.setContent(vkeyboard);
        root.getChildren().add(sp2);
        
        final Scene scene = new Scene(root, 800, 200, Color.rgb(160, 160, 160));
        stage.setScene(scene);
        stage.show();
    }

}
