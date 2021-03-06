package com.github.orthae.discordtriggerstuff.controllers;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.ReusedCode;
import com.github.orthae.discordtriggerstuff.Settings;
import com.github.orthae.discordtriggerstuff.Trigger;
import com.github.orthae.discordtriggerstuff.enums.Language;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

public class ImportExportBase {
    //  FXML block
    @FXML
    private Label topBarLabel;
    @FXML
    private BorderPane topLabelPane;
    @FXML
    private Button cancelButton;
    @FXML
    private Button actButton;
    @FXML
    private Button clipboardButton;
    @FXML
    private Button fileButton;
    @FXML
    private TableView<Trigger> importExportTable;
    @FXML
    private TableColumn<Trigger, String> nameColumn;
    @FXML
    private TableColumn<Trigger, Boolean> typeColumn;
    @FXML
    private TableColumn<Trigger, String> commandColumn;
    @FXML
    private TableColumn<Trigger, String> categoryColumn;
    @FXML
    private TableColumn<Trigger, String> soundColumn;
    @FXML
    private TableColumn<Trigger, Boolean> importExportColumn;
    @FXML
    private TableColumn<Trigger, Number> delayColumn;

    //  Fields
    private double xOffset = 0;
    private double yOffset = 0;
    private Window owner = null;

    //  Getters
    public TableView<Trigger> getImportExportTable() {
        return importExportTable;
    }

    public TableColumn<Trigger, Boolean> getImportExportColumn() {
        return importExportColumn;
    }

    public Label getTopBarLabel() {
        return topBarLabel;
    }

    //  Methods
    private void windowMoving() {
        topLabelPane.setOnMousePressed(event -> {
            xOffset = topLabelPane.getScene().getWindow().getX() - event.getScreenX();
            yOffset = topLabelPane.getScene().getWindow().getY() - event.getScreenY();
        });

        topLabelPane.setOnMouseDragged(event -> {
            topLabelPane.getScene().getWindow().setX(event.getScreenX() + xOffset);
            topLabelPane.getScene().getWindow().setY(event.getScreenY() + yOffset);
        });
    }

    public void cancelButton() {
        cancelButton.getScene().getWindow().hide();
    }

    private void tableInitialize() {
        importExportTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setCellValueFactory(param -> param.getValue().getTriggerName());
        nameColumn.setMinWidth(110);
        typeColumn.setCellValueFactory(param -> param.getValue().getPersonal());
        typeColumn.setCellFactory(param -> {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().add(LanguageData.getInstance().getMsg("TriggerTypeRaid"));
            comboBox.getItems().add(LanguageData.getInstance().getMsg("TriggerTypePersonal"));
            TableCell<Trigger, Boolean> cell = new TableCell<Trigger, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        if (item) {
                            comboBox.getSelectionModel().select(1);
                        } else {
                            comboBox.getSelectionModel().select(0);
                        }
                        setGraphic(comboBox);
                    }
                }
            };
            comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow() != null && cell.getTableRow().getItem() != null) {
                    Boolean value = newValue.intValue() != 0;
                    ((Trigger) cell.getTableRow().getItem()).setPersonal(value);
                }
            });
            return cell;
        });
        typeColumn.setMinWidth(105);
        typeColumn.setResizable(false);
        commandColumn.setCellValueFactory(param -> param.getValue().getTriggerCommand());
        commandColumn.setMinWidth(275);
        categoryColumn.setCellValueFactory(param -> param.getValue().getTriggerCategory());
        categoryColumn.setMinWidth(120);
        delayColumn.setMaxWidth(75);
        delayColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getTriggerDelay().getValue()) );
        delayColumn.setCellFactory(param -> new TableCell<Trigger, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                if (empty) {
                    setGraphic(null);
                } else {
                    Spinner<Integer> spinner = new Spinner<>();
                    spinner.setEditable(true);
                    TextFormatter<String> textFormatter = new TextFormatter<>(ReusedCode.SPINNER_FORMATTER);
                    spinner.getEditor().setTextFormatter(textFormatter);
                    spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, item.intValue()));
                    spinner.getValueFactory().valueProperty().addListener(observable -> ((Trigger) getTableRow().getItem()).setTriggerDelay(spinner.valueProperty().get()));
                    spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue){
                            spinner.increment(0);
                        }
                    });
                    setGraphic(spinner);
                }
            }
        });
        soundColumn.setCellValueFactory(param -> param.getValue().getSoundData());
        soundColumn.setCellFactory(param -> {
            Button button = new Button();
            button.getStyleClass().add("TablePlayButton");
            Image playButtonNotPressed = new javafx.scene.image.Image(getClass().getResourceAsStream("/com/github/orthae/discordtriggerstuff/img/play.png"));
            Image playButtonPressed = new Image(getClass().getResourceAsStream("/com/github/orthae/discordtriggerstuff/img/playpressed.png"));
            button.setGraphic(new ImageView(playButtonNotPressed));
            TableCell<Trigger, String> cell = new TableCell<Trigger, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                    //  JavaFX 10+
                    //  button.setOnAction(event -> getTableRow().getItem().runTrigger());
                        button.setOnAction(event -> ((Trigger) getTableRow().getItem()).runTrigger());
                        setGraphic(button);
                    }
                }
            };
            button.pressedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    button.setGraphic(new ImageView(playButtonPressed));
                } else {
                    button.setGraphic(new ImageView(playButtonNotPressed));
                }
            });

            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            cell.setStyle("-fx-padding: 0 0 0 0;");
            return cell;
        });
        soundColumn.setMaxWidth(50);
        importExportColumn.setCellValueFactory(param -> param.getValue().getEnabled());
        importExportColumn.setCellFactory(param -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Trigger, Boolean> cell = new TableCell<Trigger, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(item);
                        setGraphic(checkBox);
                    }
                }
            };
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow() != null && cell.getTableRow().getItem() != null) {
                //  JavaFX 10+
                //  cell.getTableRow().getItem().setEnabled(newValue);
                    ((Trigger) cell.getTableRow().getItem()).setEnabled(newValue);
                }
            });
            return cell;
        });
        importExportColumn.setMaxWidth(50);
    }

    private void languageSetup() {
        clearFont();
        if (Settings.getInstance().getLocale().equals(Language.Japanese)) {
            japaneseFont();
        } else {
            westernFont();
        }
        nameColumn.setText(LanguageData.getInstance().getMsg("tableColumnName"));
        typeColumn.setText(LanguageData.getInstance().getMsg("tableColumnType"));
        commandColumn.setText(LanguageData.getInstance().getMsg("tableColumnCommand"));
        categoryColumn.setText(LanguageData.getInstance().getMsg("tableColumnCategory"));
        soundColumn.setText(LanguageData.getInstance().getMsg("tableColumnSound"));
        delayColumn.setText(LanguageData.getInstance().getMsg("tableColumnDelay"));
        cancelButton.setText(LanguageData.getInstance().getMsg("ButtonAddEditCancel"));
        actButton.setText(LanguageData.getInstance().getMsg("buttonACT"));
        clipboardButton.setText(LanguageData.getInstance().getMsg("buttonClipboard"));
        fileButton.setText(LanguageData.getInstance().getMsg("buttonFile"));
    }

    private void clearFont() {
        cancelButton.getStyleClass().remove("JapaneseFont");
        cancelButton.getStyleClass().remove("WesternFont");
        actButton.getStyleClass().remove("JapaneseFont");
        actButton.getStyleClass().remove("WesternFont");
        clipboardButton.getStyleClass().remove("JapaneseFont");
        clipboardButton.getStyleClass().remove("WesternFont");
        fileButton.getStyleClass().remove("JapaneseFont");
        fileButton.getStyleClass().remove("WesternFont");
    }

    private void westernFont() {
        cancelButton.getStyleClass().add("WesternFont");
        actButton.getStyleClass().add("WesternFont");
        clipboardButton.getStyleClass().add("WesternFont");
        fileButton.getStyleClass().add("WesternFont");
        topBarLabel.getStyleClass().add("TopLabelWestern");
    }

    private void japaneseFont() {
        cancelButton.getStyleClass().add("JapaneseFont");
        actButton.getStyleClass().add("JapaneseFont");
        clipboardButton.getStyleClass().add("JapaneseFont");
        fileButton.getStyleClass().add("JapaneseFont");
        topBarLabel.getStyleClass().add("TopLabelJapanese");
    }

    public void initialize() {
        languageSetup();
        windowMoving();
        tableInitialize();
    }

    public void setOwner(Window owner){
        this.owner = owner;
    }

    public Window getOwner(){
        return owner;
    }
}
