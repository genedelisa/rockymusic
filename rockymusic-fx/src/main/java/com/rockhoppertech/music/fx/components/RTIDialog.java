package com.rockhoppertech.music.fx.components;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class RTIDialog extends Stage {

	public static final RTIDialog msgDialog;
	static {
		msgDialog = new RTIDialog();
		msgDialog.configureMessageDialog();
	}

	public static void showMessageDialog(String msg) {
		msgDialog.setMessage(msg).display();
	}

	private Label messageLabel;

	private RTIDialog setMessage(String msg) {
		messageLabel.setText(msg);
		return this;
	}

	public RTIDialog() {
		this.initStyle(StageStyle.UTILITY);
		this.setResizable(false);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setIconified(false);
	}

	private void configureMessageDialog() {

		Image image = new Image("/images/dialog-information-icon-64x64.png");
		ImageView icon = new ImageView(image);
		BorderPane.setAlignment(icon, Pos.CENTER);		

		final Button btn = ButtonBuilder.create()
				.id("okButton")
				.text("OK")
				.defaultButton(true)
				// .style("-fx-base: #b6e7c9;")
				.onAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						RTIDialog.this.close();
					}
				})
				.build();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btn.requestFocus();
			}
		});

		HBox buttonBox = HBoxBuilder.create()
				.id("buttonBox")
				.alignment(Pos.CENTER)
				.spacing(10)
				.children(btn)
				//.style("-fx-border-width: 1; -fx-border-color: red;")
				.build();
		BorderPane.setAlignment(buttonBox, Pos.CENTER);
		BorderPane.setMargin(buttonBox, new Insets(5, 5, 5, 5));

		this.messageLabel = LabelBuilder.create()
				.id("messageLabel")
				// .text(msg)
				.wrapText(true)
				//.style("-fx-border-width: 1; -fx-border-color: green;")
				.style("-fx-font-size: 24px; -fx-font-weight: bold;")
				.build();
		BorderPane.setAlignment(messageLabel, Pos.CENTER);
		BorderPane.setMargin(messageLabel, new Insets(15, 15, 15, 15));

		BorderPane pane = BorderPaneBuilder.create()
				.center(messageLabel)
				.bottom(buttonBox)
				.left(icon)
				.build();

		Scene scene = SceneBuilder.create()
				.root(pane)
				.build();
		this.setScene(scene);
	}

	void display() {
		this.sizeToScene();
		this.centerOnScreen();
		this.showAndWait();
	}
}
