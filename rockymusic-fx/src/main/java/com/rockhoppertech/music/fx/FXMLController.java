package com.rockhoppertech.music.fx;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.components.RTIDialog;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.parse.MIDIParserException;
import com.rockhoppertech.music.midi.parse.MIDIStringParser;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class FXMLController {

	private static final Logger logger = LoggerFactory
			.getLogger(FXMLController.class);
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private MenuItem fileMenuOpen;

	@FXML
	private MenuItem fileMenuQuit;

	@FXML
	private TextArea midiStringText;

	@FXML
	private Button parseButton;

	@FXML
	private Button playButton;

	@FXML
	private Button addTrackButton;

	@FXML
	private CheckMenuItem viewMenuDisplayList;

	@FXML
	private ListView<MIDITrack> trackList;

	ObservableList<MIDITrack> tracks;

	MIDITrack currentTrack;

	File currentSaveFile;

	@FXML
	private TextArea trackText;

	@FXML
	private BorderPane borderPane;

	private Stage stage;

	@FXML
	void displayListAction(ActionEvent event) {
		if (viewMenuDisplayList.isSelected()) {
			// borderPane.getChildren().add(trackList);
			borderPane.setLeft(trackList);
		} else {
			// borderPane.getChildren().remove(trackList);
			borderPane.setLeft(null);
		}
	}

	@FXML
	void helpMenuAboutAction(ActionEvent event) {
		RTIDialog.showMessageDialog("Cool app, huh?");
	}

	/**
	 * Called from the list view's context menu
	 * @param event
	 */
	@FXML
	void clearListAction(ActionEvent event) {
		logger.debug("clear list");
		trackList.getSelectionModel().clearSelection();
//		trackList.getItems().clear();
		// or
		tracks.clear();
	}

	@FXML
	void fileMenuOpenAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter(
				"MIDI String files (*.mds)", "*.mds");
		fileChooser.getExtensionFilters().add(extentionFilter);

		fileChooser.setTitle("Open MIDI String File");
		File f = fileChooser.showOpenDialog(stage);
		readFile(f);
		stage.setTitle(f.getName());

	}

	@FXML
	void fileMenuSaveAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save MIDI String File");
		if (currentSaveFile == null) {
			currentSaveFile = fileChooser.showSaveDialog(this.stage);
		}
		writeFile(currentSaveFile);
	}

	@FXML
	void fileMenuSaveAsAction(ActionEvent event) {
		logger.debug("save as");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save MIDI String File");
		currentSaveFile = fileChooser.showSaveDialog(null);
		writeFile(currentSaveFile);
	}

	@FXML
	void fileMenuQuitAction(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void addTrackToListAction(ActionEvent event) {
		if (currentTrack != null) {
			tracks.add(currentTrack);
			logger.debug("added track to list {}", currentTrack);
		} else {
			logger.debug("current track is null");
		}
	}

	@FXML
	void parseAction(ActionEvent event) {

	}

	@FXML
	void playAction(ActionEvent event) {
		MIDIStringParser parser = new MIDIStringParser();

		try {
			MIDITrack track = parser.parseString(this.midiStringText.getText());
			track.setName("track-" + System.currentTimeMillis());
			this.trackText.setText(track.toString());
			track.play();
			currentTrack = track;
			addTrackButton.setDisable(false);
		} catch (MIDIParserException e) {
			logger.error("Cannot create track {}", e.getMessage(), e);
			RTIDialog.showMessageDialog("Cannot create track: \n" + e.getMessage());
		}

	}

	@FXML
	void initialize() {
		assert fileMenuOpen != null : "fx:id=\"fileMenuOpen\" was not injected: check your FXML file 'Scene.fxml'.";
		assert fileMenuQuit != null : "fx:id=\"fileMenuQuit\" was not injected: check your FXML file 'Scene.fxml'.";
		assert midiStringText != null : "fx:id=\"midiStringText\" was not injected: check your FXML file 'Scene.fxml'.";
		assert parseButton != null : "fx:id=\"parseButton\" was not injected: check your FXML file 'Scene.fxml'.";
		assert playButton != null : "fx:id=\"playButton\" was not injected: check your FXML file 'Scene.fxml'.";
		assert trackList != null : "fx:id=\"trackList\" was not injected: check your FXML file 'Scene.fxml'.";
		assert trackText != null : "fx:id=\"trackText\" was not injected: check your FXML file 'Scene.fxml'.";

		tracks = FXCollections.observableArrayList();
		trackList.setItems(tracks);
		trackList.setEditable(true);
		trackList.autosize();
		trackList.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<MIDITrack>() {
					@Override
					public void changed(
							ObservableValue<? extends MIDITrack> ov,
							MIDITrack oldTrack, MIDITrack newTrack) {
						midiStringText.setText(newTrack.toMIDIString());
						trackText.setText(newTrack.toString());
					}
				});

		// we want the MIDITrack's name displayed in the ListView
		trackList
				.setCellFactory(new Callback<ListView<MIDITrack>, ListCell<MIDITrack>>() {
					@Override
					public ListCell<MIDITrack> call(ListView<MIDITrack> listView) {
						return new ListCell<MIDITrack>() {
							@Override
							public void updateItem(MIDITrack item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
									return;
								}
								if (item != null) {
									setText(item.getName());
								}
							}
						};
					}
				});
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				midiStringText.requestFocus();
			}
		});

	}


	void writeFile(File file) {
		String content = midiStringText.getText();
		Path path = file.toPath();
		// Path path = FileSystems.getDefault().getPath("","");
		// try {
		// Files.write( path,
		// content.getBytes(),
		// StandardOpenOption.CREATE);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		Charset charset = Charset.forName("US-ASCII");
		try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
			writer.write(content, 0, content.length());
		} catch (IOException x) {
			logger.error(x.getLocalizedMessage(), x);
		}
	}

	void readFile(File file) {
		Path path = Paths.get(file.getAbsolutePath());
		List<String> alllLines = null;
		try {
			alllLines = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
		if (alllLines != null) {
			for (String s : alllLines) {
				midiStringText.appendText(s);
				midiStringText.appendText("\n");
			}
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
