import enums.Language;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.function.UnaryOperator;

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

//  Getters
    public TableView<Trigger> getImportExportTable(){
        return importExportTable;
    }

    public TableColumn<Trigger, Boolean> getImportExportColumn(){
        return importExportColumn;
    }

    public Label getTopBarLabel(){
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

    public void cancelButton(){
        cancelButton.getScene().getWindow().hide();
    }

    private void tableInitialize(){
        importExportTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setCellValueFactory(param -> param.getValue().getTriggerName());
        nameColumn.setMinWidth(110);
        typeColumn.setCellValueFactory(param -> param.getValue().getPersonal());
        typeColumn.setCellFactory(param -> {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().add("Raid");
            comboBox.getItems().add("Personal");
            TableCell<Trigger, Boolean> cell = new TableCell<>() {
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
                    cell.getTableRow().getItem().setPersonal(value);
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
        delayColumn.setCellValueFactory(param -> param.getValue().getTriggerDelay());
        delayColumn.setCellFactory(param -> {
            Spinner<Integer> spinner = new Spinner<>();
            spinner.setEditable(true);
            //noinspection Duplicates
            UnaryOperator<TextFormatter.Change> filter = change -> {
                String text = change.getText();
                if (text.matches("[0-9]*")) {
                    return change;
                }
                return null;
            };
            TextFormatter<String> textFormatter = new TextFormatter<>(filter);
            spinner.getEditor().setTextFormatter(textFormatter);
            //noinspection UnnecessaryLocalVariable
            TableCell<Trigger, Number> cell = new TableCell<>(){
                @Override
                protected void updateItem(Number item, boolean empty) {
                        if(empty){
                            setGraphic(null);
                        } else {
                            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, item.intValue()));
                            setGraphic(spinner);
                        }
                }
            };

            return cell;
        });


        soundColumn.setCellValueFactory(param -> param.getValue().getSoundData());
        soundColumn.setCellFactory(param -> {
            Button button = new Button();
            button.getStyleClass().add("TablePlayButton");
            javafx.scene.image.Image playButtonNotPressed = new javafx.scene.image.Image(getClass().getResourceAsStream("img/play.png"));
            javafx.scene.image.Image playButtonPressed = new Image(getClass().getResourceAsStream("img/playpressed.png"));
            button.setGraphic(new ImageView(playButtonNotPressed));
            TableCell<Trigger, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        button.setOnAction(event -> getTableRow().getItem().runTrigger());
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
            TableCell<Trigger, Boolean> cell = new TableCell<>() {
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
                    cell.getTableRow().getItem().setEnabled(newValue);
                }
            });
            return cell;
        });
        importExportColumn.setMaxWidth(50);
    }

    private void languageSetup(){
        clearFont();
        if(Settings.getInstance().getLocale().equals(Language.Japanese)){
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
        cancelButton.setText(LanguageData.getInstance().getMsg("cancelButton"));
        actButton.setText(LanguageData.getInstance().getMsg("buttonACT"));
        clipboardButton.setText(LanguageData.getInstance().getMsg("buttonClipboard"));
        fileButton.setText(LanguageData.getInstance().getMsg("buttonFile"));
    }

    private void clearFont(){
        cancelButton.getStyleClass().remove("JapaneseFont");
        cancelButton.getStyleClass().remove("WesternFont");
        actButton.getStyleClass().remove("JapaneseFont");
        actButton.getStyleClass().remove("WesternFont");
        clipboardButton.getStyleClass().remove("JapaneseFont");
        clipboardButton.getStyleClass().remove("WesternFont");
        fileButton.getStyleClass().remove("JapaneseFont");
        fileButton.getStyleClass().remove("WesternFont");
    }

    private void westernFont(){
        cancelButton.getStyleClass().add("WesternFont");
        actButton.getStyleClass().add("WesternFont");
        clipboardButton.getStyleClass().add("WesternFont");
        fileButton.getStyleClass().add("WesternFont");
    }

    private void japaneseFont(){
        cancelButton.getStyleClass().add("JapaneseFont");
        actButton.getStyleClass().add("JapaneseFont");
        clipboardButton.getStyleClass().add("JapaneseFont");
        fileButton.getStyleClass().add("JapaneseFont");
    }

    public void initialize() {
        languageSetup();
        windowMoving();
        tableInitialize();


    }

//  DONE








}
