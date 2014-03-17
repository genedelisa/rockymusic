package com.rockhoppertech.music.fx.keyboard;

import javafx.geometry.Orientation;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class WhiteKey extends PianoKey {
    /**
     * Serialization.
     */
    private static final long serialVersionUID = -6146993558159003771L;

    public WhiteKey(int key, Orientation orientation) {
        super(key);
        this.orientation = orientation;

        defaultStyle();

        if (this.orientation == Orientation.HORIZONTAL) {
            this.setPrefWidth(20);
            this.setPrefHeight(80);
            this.setWidth(20);
            this.setHeight(80);
        } else if (this.orientation == Orientation.VERTICAL) {
            this.setPrefWidth(80);
            this.setPrefHeight(20);
            this.setWidth(80);
            this.setHeight(20);
        }
        if (PianoKey.zoomWidth)
            this.setPrefWidth(this.getPrefWidth() * PianoKey.getZoom());

        if (PianoKey.zoomHeight)
            this.setPrefHeight(this.getPrefHeight() * PianoKey.getZoom());
        
        updateText();
    }

    public void defaultStyle() {
        this.setStyle("-fx-background-color: white; -fx-fill: black; -fx-border-color: black; -fx-border-width: 1px;");
        this.text
                .setStyle("-fx-background-color: white; -fx-fill: black; -fx-stroke: black;");
    }
    public void activeStyle() {
        this.setStyle("-fx-background-color: yellow; -fx-fill: black; -fx-border-color: red; -fx-border-width: 1px;");
        this.text
                .setStyle("-fx-background-color: white; -fx-fill: black; -fx-stroke: black;");
    }
}
