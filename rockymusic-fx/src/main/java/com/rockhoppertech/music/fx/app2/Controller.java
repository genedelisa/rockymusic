package com.rockhoppertech.music.fx.app2;

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
