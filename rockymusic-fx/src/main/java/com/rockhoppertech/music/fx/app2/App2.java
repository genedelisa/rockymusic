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

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabBuilder;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPaneBuilder;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDINoteBuilder;
import com.rockhoppertech.music.midi.js.MIDISender;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class App2 extends Application {

    private static final Logger logger = LoggerFactory.getLogger(App2.class);
    Stage stage;
    Scene scene;
    BorderPane root;
    Controller controller;
    private static ObservableList<MIDINote> tableDataList;
    private static ObservableList<MIDIGMPatch> midiPatchList;
    private static ObservableList<Integer> programList;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.controller = new Controller();
        this.configureScene();
        this.configureStage();
        logger.debug("started");
    }

    private void configureStage() {
        stage.setTitle("App 2");

        // make it full screen
        stage.setX(0);
        stage.setY(0);
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());

        stage.setScene(this.scene);
        controller.setStage(this.stage);
        stage.show();
    }

    private void configureScene() {

        // Image bgimage = new Image(getClass().getResourceAsStream(
        // "/images/background.jpg"));
        // ImageView imageView = ImageViewBuilder.create()
        // .image(bgimage)
        // .build();

        Button b = ButtonBuilder.create()
                .id("someButton")
                .text("Button")
                .style("-fx-font: 22 arial; -fx-base: #b6e7c9;")
                // .onAction(new EventHandler<ActionEvent>() {
                // @Override
                // public void handle(ActionEvent e) {
                // logger.debug("local button pressed {}", e);
                // }
                // })
                .build();

        // not a singleton: logger.debug("button builder {}",
        // ButtonBuilder.create());

        // the controller has the action handler
        this.controller.setButton(b);
        BorderPane.setAlignment(b, Pos.CENTER);

        root =
                BorderPaneBuilder
                        .create()
                        .id("rootpane")
                        .padding(new Insets(20))
                        // .style("-fx-padding: 30")
                        .center(b)
                        .build();

        this.scene = SceneBuilder.create()
                .root(root)
                .fill(Color.web("#103000"))
                .stylesheets("/styles/app2styles.css")
                .build();

        configureCombo();
        // configureTabs();

        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E")
                .build();
        track.sequential();
        TableView<MIDINote> table = createTable(track);
        BorderPane.setAlignment(table, Pos.CENTER);
        this.root.setCenter(table);
    }

    void configureCombo() {
        programList = FXCollections.observableArrayList();
        for (int i = 0; i < 128; i++) {
            programList.add(i);
        }

        midiPatchList = FXCollections.observableArrayList();
        for (MIDIGMPatch p : MIDIGMPatch.getAllPitched()) {
            midiPatchList.add(p);
        }

        final ComboBox<MIDIGMPatch> combo = ComboBoxBuilder
                .<MIDIGMPatch> create()
                .promptText("Select")
                .items(midiPatchList)
                .style("-fx-border-color: black; -fx-border-width: 1")
                .build();

        combo.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<MIDIGMPatch>() {
                    public void changed(
                            ObservableValue<? extends MIDIGMPatch> source,
                            MIDIGMPatch oldValue, MIDIGMPatch newValue) {
                        logger.debug("You selected: " + newValue);
                    }
                });

        combo.valueProperty().addListener(new ChangeListener<MIDIGMPatch>() {
            @Override
            public void changed(ObservableValue<? extends MIDIGMPatch> ov,
                    MIDIGMPatch oldValue,
                    MIDIGMPatch newValue) {
                logger.debug("changed to: " + newValue);

            }
        });

        combo.setCellFactory(
                new Callback<ListView<MIDIGMPatch>, ListCell<MIDIGMPatch>>() {
                    @Override
                    public ListCell<MIDIGMPatch> call(
                            ListView<MIDIGMPatch> param) {

                        final ListCell<MIDIGMPatch> cell = new ListCell<MIDIGMPatch>() {

                            @Override
                            public void updateItem(MIDIGMPatch item,
                                    boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item.getName());
                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });

        // combo.getSelectionModel().select(0);

        BorderPane.setAlignment(combo, Pos.CENTER);
        this.root.setTop(combo);
    }

    void configureTabs() {
        TextArea content = TextAreaBuilder.create()
                .text("Here is some\ncontent")
                .build();
        Tab tab = TabBuilder.create()
                .text("A tab")
                .content(content)
                .build();
        TabPane tp = TabPaneBuilder.create()
                .tabs(tab)
                .build();
        BorderPane.setAlignment(tp, Pos.CENTER);
        this.root.setBottom(tp);
    }

    /**
     * @param track
     *            the MIDITrack to display
     * @return a TableView
     */

    public static TableView<MIDINote> createTable(MIDITrack track) {

        tableDataList = FXCollections.observableArrayList();
        for (MIDINote note : track) {
            tableDataList.add(note);
        }

        ObservableList<Integer> midiValues;
        midiValues = FXCollections.observableArrayList();
        for (int i = 0; i < 128; i++) {
            midiValues.add(i);
        }
        ObservableList<String> pitchList = FXCollections.observableArrayList();
        for (int i = 0; i < 128; i++) {
            pitchList.add(PitchFormat
                    .midiNumberToString(i));
        }

        // if it's an int, i.e. midiNumber property
        // Callback<TableColumn<MIDINote, Integer>, TableCell<MIDINote,
        // Integer>> pitchCellFactory =
        // new Callback<TableColumn<MIDINote, Integer>, TableCell<MIDINote,
        // Integer>>() {
        // @Override
        // public TableCell<MIDINote, Integer> call(
        // TableColumn<MIDINote, Integer> arg) {
        // final TableCell<MIDINote, Integer> cell = new TableCell<MIDINote,
        // Integer>() {
        // @Override
        // protected void updateItem(Integer midiNumber,
        // boolean empty) {
        // super.updateItem(midiNumber, empty);
        // if (empty) {
        // setText(null);
        // } else {
        // // all this nonsense just to do this:
        // setText(PitchFormat
        // .midiNumberToString(midiNumber));
        // }
        // }
        // };
        // return cell;
        // }
        // };

        TableColumn<MIDINote, Double> beatColumn = TableColumnBuilder
                .<MIDINote, Double> create()
                .text("Start Beat")
                .cellValueFactory(
                        new PropertyValueFactory<MIDINote, Double>("startBeat"))
                .build();

        TableColumn<MIDINote, String> pitchColumn = TableColumnBuilder
                .<MIDINote, String> create()
                .text("Pitch")
                .editable(true)
                .cellValueFactory(
                        new PropertyValueFactory<MIDINote, String>(
                                "pitchString"))
                // .cellFactory(pitchCellFactory)
                .cellFactory(
                        ComboBoxTableCell.<MIDINote, String> forTableColumn(
                                pitchList))
                .build();

        TableColumn<MIDINote, Double> durationColumn = TableColumnBuilder
                .<MIDINote, Double> create()
                .text("Duration")
                .cellValueFactory(
                        new PropertyValueFactory<MIDINote, Double>(
                                "duration"))
                .build();

        ObservableList<Integer> channelList = FXCollections
                .observableArrayList();
        for (int i = 0; i < 16; i++) {
            channelList.add(i);
        }
        TableColumn<MIDINote, Integer> channelColumn = TableColumnBuilder
                .<MIDINote, Integer> create()
                .text("Channel")
                .cellValueFactory(
                        new PropertyValueFactory<MIDINote, Integer>(
                                "channel"))
                .cellFactory(
                        ComboBoxTableCell.<MIDINote, Integer> forTableColumn(
                                channelList))
                .build();

        TableColumn<MIDINote, Integer> velocityColumn = TableColumnBuilder
                .<MIDINote, Integer> create()
                .text("Velocity")
                .cellValueFactory(
                        new PropertyValueFactory<MIDINote, Integer>(
                                "velocity"))
                .cellFactory(
                        ComboBoxTableCell.<MIDINote, Integer> forTableColumn(
                                midiValues))
                .build();

        velocityColumn
                .setCellFactory(new Callback<TableColumn<MIDINote, Integer>, TableCell<MIDINote, Integer>>() {
                    @Override
                    public TableCell<MIDINote, Integer> call(
                            TableColumn<MIDINote, Integer> velocityColumn) {
                        return new TableCell<MIDINote, Integer>() {
                            final Slider slider = SliderBuilder
                                    .create()
                                    .min(0d)
                                    .max(127d)
                                    .blockIncrement(16d)
                                    .minorTickCount(16)
                                    .majorTickUnit(32d)
                                    .showTickLabels(true)
                                    .showTickMarks(false)
                                    .build();
                            {

                                slider.valueProperty().addListener(new
                                        ChangeListener<Number>() {
                                            public void changed(
                                                    ObservableValue<? extends
                                                    Number> ov,
                                                    Number oldValue,
                                                    Number newValue) {
                                                logger.debug(
                                                        "new velocity {} ov {}",
                                                        newValue,
                                                        ov);
                                                if (getTableRow() != null) {
                                                    MIDINote note = (MIDINote) getTableRow()
                                                            .getItem();
                                                    if (note != null) {
                                                        logger.debug(
                                                                "Table row {}",
                                                                note);
                                                        note.setVelocity(newValue
                                                                .intValue());
                                                    }
                                                }
                                            }
                                        });

                            }

                            @Override
                            public void updateItem(final Integer vel,
                                    boolean empty) {
                                super.updateItem(vel, empty);
                                if (vel != null) {
                                    setGraphic(slider);
                                    slider.setValue(vel);
                                } else {
                                    setGraphic(null);
                                }
                            }
                        };
                    }
                });

        TableColumn<MIDINote, String> programColumn = new TableColumn<>();
        programColumn.setText("Program");
        programColumn.setEditable(true);
        programColumn
                .setCellFactory(new Callback<TableColumn<MIDINote, String>, TableCell<MIDINote, String>>() {
                    @Override
                    public TableCell<MIDINote, String> call(
                            TableColumn<MIDINote, String> theColumn) {
                        return new ProgramStringListCell();
                    }
                });
        programColumn.setCellValueFactory(
                new PropertyValueFactory<MIDINote, String>(
                        "programName"));

        // also, pan, pitchBend

        TableColumn<MIDINote, MIDINote> buttonColumn = TableColumnBuilder
                .<MIDINote, MIDINote> create()
                .text("Action")
                .editable(true)
                .cellValueFactory(
                        new Callback<CellDataFeatures<MIDINote, MIDINote>, ObservableValue<MIDINote>>() {
                            @Override
                            public ObservableValue<MIDINote> call(
                                    CellDataFeatures<MIDINote, MIDINote> features) {
                                return new ReadOnlyObjectWrapper<MIDINote>(
                                        features
                                                .getValue());
                            }
                        }
                )
                .build();

        buttonColumn
                .setCellFactory(new Callback<TableColumn<MIDINote, MIDINote>, TableCell<MIDINote, MIDINote>>() {
                    @Override
                    public TableCell<MIDINote, MIDINote> call(
                            TableColumn<MIDINote, MIDINote> btnCol) {
                        return new TableCell<MIDINote, MIDINote>() {
                            final ImageView buttonGraphic = new ImageView();
                            final Button button = new Button();

                            {
                                button.setGraphic(buttonGraphic);
                                button.setMinWidth(130);
                            }

                            @Override
                            public void updateItem(final MIDINote note,
                                    boolean empty) {
                                super.updateItem(note, empty);
                                if (note != null) {
                                    button.setText("Play");
                                    setGraphic(button);
                                    button.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            logger.debug("note {}", note);
                                            MIDISender sender = new MIDISender();
                                            sender.play(note);

                                        }
                                    });
                                } else {
                                    setGraphic(null);
                                }
                            }
                        };
                    }
                });

        // TableView<MIDINote> table =
        // TableViewBuilder
        // .<MIDINote> create()
        // .items(tableDataList)
        // .columns(
        // beatColumn,
        // pitchColumn,
        // durationColumn,
        // velocityColumn,
        // channelColumn,
        // programColumn,
        // buttonColumn)
        // .editable(true)
        // .styleClass("midiTrackTable")
        // .columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
        // .build();

        TableView<MIDINote> table = new TableView<>();
        table.setItems(tableDataList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(beatColumn);
        table.getColumns().add(pitchColumn);
        table.getColumns().add(durationColumn);
        table.getColumns().add(velocityColumn);
        table.getColumns().add(channelColumn);
        table.getColumns().add(programColumn);
        table.getColumns().add(buttonColumn);
        // you get the generic problem
        // table2.getColumns().addAll(beatColumn,
        // pitchColumn,
        // durationColumn,
        // velocityColumn,
        // channelColumn,
        // programColumn,
        // buttonColumn);
        table.setEditable(true);
        // table2.setStyleClass("midiTrackTable");

        return table;
    }

    static class ProgramStringListCell extends
            ComboBoxTableCell<MIDINote, String> {

        static ObservableList<String> patchNames;
        static {
            patchNames = FXCollections.observableArrayList();
            for (MIDIGMPatch p : MIDIGMPatch.getAllPitched()) {
                patchNames.add(p.getName());
            }
        }

        public ProgramStringListCell() {
            super(patchNames);
        }

        public ProgramStringListCell(ObservableList<String> programStrings) {
            super(programStrings);
        }

        @Override
        public void updateItem(String patchName, boolean empty) {
            super.updateItem(patchName, empty);
            if (patchName != null) {
                setText(MIDIGMPatch.getPatch(patchName).getName());
                logger.debug("patch name {}", patchName);

                if (getTableRow() != null) {
                    MIDINote note = (MIDINote) getTableRow().getItem();
                    if (note != null) {
                        logger.debug("Table row {}", note);
                        int program = MIDIGMPatch.getPatch(patchName)
                                .getProgram();
                        note.setProgram(program);
                    }
                }
            }
        }
    }

    Node foo() {
        Button btn = new Button("Add");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                tableDataList.add(MIDINoteBuilder.create().pitch(Pitch.C5)
                        .build());
            }
        });

        VBox vb = VBoxBuilder.create()
                .spacing(5)
                .padding(new Insets(10, 0, 0, 10))
                .build();

        Label lbl = new Label("MIDI Track");
        lbl.setFont(new Font("Arial", 20));
        vb.getChildren().addAll(lbl, btn);
        return vb;
    }

    // public static class ComboBoxTableCell2<S, T> extends TableCell<S, T> {
    // private final ComboBox<MIDIGMPatch> comboBox;
    //
    // public ComboBoxTableCell2(ObservableList<MIDIGMPatch> patchList) {
    // this.comboBox = new ComboBox<MIDIGMPatch>();
    // comboBox.setItems(patchList);
    //
    // setAlignment(Pos.CENTER);
    // setGraphic(comboBox);
    // }
    //
    // @Override
    // public void updateItem(T item, boolean empty) {
    // super.updateItem(item, empty);
    // if (empty) {
    // setText(null);
    // setGraphic(null);
    // } else {
    // if (getTableRow() != null) {
    // Object o = getTableRow().getItem();
    //
    // }
    // }
    // }
    // }

}
