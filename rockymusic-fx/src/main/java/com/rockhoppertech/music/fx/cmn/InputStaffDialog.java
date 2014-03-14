package com.rockhoppertech.music.fx.cmn;

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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A dialog with an {@code InputStaff}. Bind to the pitchProperty.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class InputStaffDialog extends Stage {
    private InputStaff istaff;
    private IntegerProperty pitchProperty = new SimpleIntegerProperty(60);

    public IntegerProperty pitchProperty() {
        return pitchProperty;
    }

    public void setPitch(int pitch) {
        istaff.setPitch(pitch);
    }

    public InputStaffDialog() {
        this.initModality(Modality.WINDOW_MODAL);
        this.setTitle("Input a pitch");

        Button ok = new Button("OK");
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(5));

        istaff = new InputStaff();
        istaff.setFontSize(24d);
        istaff.draw();
        pitchProperty.bind(istaff.pitchProperty());
        box.getChildren().add(istaff);

        box.getChildren().add(ok);
        this.setScene(new Scene(box));
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                close();
            }
        });
    }

}
