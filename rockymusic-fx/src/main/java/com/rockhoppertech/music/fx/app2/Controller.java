package com.rockhoppertech.music.fx.app2;

/*
 * #%L
 * rockymusic-fx
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.components.RTIDialog;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */

public class Controller {
	private static final Logger logger = LoggerFactory.getLogger(App2.class);
	private Stage stage;
	private Button button;
	
	
	public void setButton(Button b) {
		this.button = b;
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				buttonAction(e);
			}
		});
	}

	public void buttonAction(ActionEvent e) {
		logger.debug("button pressed {}", e);
		RTIDialog.showMessageDialog("Button was clicked\n" + e.toString());
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
