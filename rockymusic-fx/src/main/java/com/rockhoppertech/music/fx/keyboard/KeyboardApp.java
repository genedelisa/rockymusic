package com.rockhoppertech.music.fx.keyboard;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
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
        KeyboardPanel keyboard = new KeyboardPanel(Orientation.VERTICAL, KeyboardPanel.MIN_OCTAVE, KeyboardPanel.MAX_OCTAVE);
        //KeyboardPanel keyboard = new KeyboardPanel();
        //keyboard.setPrefSize(1000d, 50d);
        root.getChildren().add(keyboard);
        final Scene scene = new Scene(root, 800, 200, Color.rgb(160, 160, 160));
        stage.setScene(scene);
        stage.show();
    }

}
