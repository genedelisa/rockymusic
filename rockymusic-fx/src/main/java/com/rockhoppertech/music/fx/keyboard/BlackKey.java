package com.rockhoppertech.music.fx.keyboard;

import javafx.geometry.Orientation;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class BlackKey extends PianoKey {
    /**
     * Serialization.
     */
    private static final long serialVersionUID = -6965809144512854016L;

    public BlackKey(int key, Orientation orientation) {
        super(key);
        this.orientation = orientation;

       defaultStyle();

        if (this.orientation == Orientation.HORIZONTAL) {
            this.setPrefWidth(10);
            this.setPrefHeight(45);
            this.setWidth(10);
            this.setHeight(45);
        } else if (this.orientation == Orientation.VERTICAL) {
            this.setPrefWidth(45);
            this.setPrefHeight(10);
            this.setWidth(45);
            this.setHeight(10);
        }
        if (PianoKey.zoomWidth) {
            this.setPrefWidth(this.getPrefWidth() * PianoKey.getZoom());
        }

        if (PianoKey.zoomHeight) {
            this.setPrefHeight(this.getPrefHeight() * PianoKey.getZoom());
        }
        updateText();

    }
    
    public void defaultStyle() {
        this.setStyle("-fx-background-color: black; -fx-fill: white;");
        //this.setStyle("-fx-background-color: black; -fx-fill: white; -fx-border-color: red; -fx-border-width: 1px;");
        this.text
                .setStyle("-fx-background-color: black; -fx-fill: white; -fx-stroke: white;");
    }
    public void activeStyle() {
        this.setStyle("-fx-background-color: yellow; -fx-fill: white;");
        //this.setStyle("-fx-background-color: yellow; -fx-fill: white; -fx-border-color: red; -fx-border-width: 1px;");
        this.text
                .setStyle("-fx-background-color: yellow; -fx-fill: white; -fx-stroke: black;");
    }
}
