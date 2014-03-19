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
