import enums.Language;
import enums.SoundType;
import exceptions.AudioException;
import exceptions.VoiceException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.UnaryOperator;

public class AddTriggerController {
    //  FXML
    @FXML
    private ComboBox<String> triggerTypeComboBox;
    @FXML
    private ComboBox<String> triggerCategoryComboBox;
    @FXML
    private TextField triggerNameTextField;
    @FXML
    private TextField triggerCommandTextField;
    @FXML
    private TextField triggerSoundDataTField;
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button selectSoundPathFileButton;
    @FXML
    private Button playButton;
    @FXML
    private RadioButton radioButtonBeep;
    @FXML
    private RadioButton radioButtonTTS;
    @FXML
    private RadioButton radioButtonSoundFile;
    @FXML
    private Label triggerNameLabel;
    @FXML
    private Label triggerTypeLabel;
    @FXML
    private Label triggerCategoryLabel;
    @FXML
    private Label topBarLabel;
    @FXML
    private Label triggerCommandLabel;
    @FXML
    private Label delayLabel;
    @FXML
    private Label soundDataLabel;
    @FXML
    private CheckBox triggerCategoryCheckBox;
    @FXML
    private Spinner<Integer> triggerDelaySpinner;
    @FXML
    private BorderPane topLabelPane;


    //  Fields
    private double xOffset = 0;
    private double yOffset = 0;
    private Trigger newTrigger;
    private boolean isEdit = false;

    //  Getters
    public Trigger getNewTrigger() {
        return newTrigger;
    }

    //  Setters
    private void setNewTrigger(Trigger trigger) {
        this.newTrigger = trigger;
    }

    public void initialize() {
        comboBoxesInit();
        delaySpinnerInitialize();
        actionListeners();

    }

    private void comboBoxesInit(){
        triggerTypeComboBox.getItems().addAll(LanguageData.getInstance().getMsg("TriggerTypePersonal"),
                LanguageData.getInstance().getMsg("TriggerTypeRaid"));
        triggerTypeComboBox.getSelectionModel().select(0);
        triggerCategoryComboBox.getItems().add("Default");
        triggerCategoryComboBox.getSelectionModel().select(0);
    }

    private void actionListeners() {
        radioButtonBeep.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                selectSoundPathFileButton.setDisable(true);
                triggerSoundDataTField.setDisable(true);
                triggerSoundDataTField.clear();
            }
        });
        radioButtonTTS.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                selectSoundPathFileButton.setDisable(true);
                triggerSoundDataTField.setDisable(false);
            }
        });
        radioButtonSoundFile.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                selectSoundPathFileButton.setDisable(false);
                triggerSoundDataTField.setDisable(false);
            }
        });
    }

    @SuppressWarnings("Duplicates")
    private void delaySpinnerInitialize() {
        triggerDelaySpinner.setEditable(true);
        triggerDelaySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, 0));
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        triggerDelaySpinner.getEditor().setTextFormatter(textFormatter);

    }

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

    public void cancelTriggerButton() {
        cancelButton.getScene().getWindow().hide();
    }

    private void checkBoxesSetup() {
        triggerCategoryCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            String lastItem = triggerCategoryComboBox.getSelectionModel().getSelectedItem();
            if (newValue) {
                triggerCategoryComboBox.setEditable(true);
                triggerCategoryComboBox.getEditor().setText(null);
            } else {
                triggerCategoryComboBox.setEditable(false);
                if (newTrigger != null) {
                    triggerCategoryComboBox.getSelectionModel().select(newTrigger.getTriggerCategory().getValue());
                } else {
                    if (triggerCategoryComboBox.getItems().contains(lastItem)) {
                        triggerCategoryComboBox.getSelectionModel().select(lastItem);
                    } else {
                        triggerCategoryComboBox.getSelectionModel().select(0);
                    }
                }
            }
        });
    }

    private void validateErrorDialog(String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        if (isEdit) {
            alert.setTitle(LanguageData.getInstance().getMsg("errorWhileEditing"));
        } else {
            alert.setTitle(LanguageData.getInstance().getMsg("errorWhileCreating"));

        }
        alert.setContentText(errorText);
        alert.showAndWait();
    }

    private boolean validateInput() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();
        if (triggerNameTextField.getText().isEmpty()) {
            errorMessage.append(LanguageData.getInstance().getMsg("errorValidateName"));
            errorMessage.append("\n");
            isValid = false;
        }
        if (triggerCategoryCheckBox.isSelected() && (triggerCategoryComboBox.getEditor().getText() == null || triggerCategoryComboBox.getEditor().getText().isEmpty())) {
            errorMessage.append(LanguageData.getInstance().getMsg("errorValidateCategory"));
            errorMessage.append("\n");
            isValid = false;
        }
        if (triggerCommandTextField.getText() == null || triggerCommandTextField.getText().isEmpty()) {
            errorMessage.append(LanguageData.getInstance().getMsg("errorValidateCommand"));
            errorMessage.append("\n");
            isValid = false;
        }
        if (triggerSoundDataTField.getText().isEmpty() && !radioButtonBeep.isSelected()) {
                errorMessage.append(LanguageData.getInstance().getMsg("errorValidateSoundData"));
                isValid = false;
        }
        if (isValid) {
            return true;
        } else {
            validateErrorDialog(errorMessage.toString());
            return false;
        }
    }


//  EMD DPME


    public void playButton() {
        try{
            if (radioButtonBeep.isSelected()) {
                SoundManager.getInstance().playDebug(true, SoundManager.getInstance().DEFAULT_AUDIO_FILE);
                return;
            }
            if(triggerSoundDataTField.getText().isEmpty()){
                validateErrorDialog(LanguageData.getInstance().getMsg("errorValidateSoundData"));
            } else {
                if (radioButtonTTS.isSelected()) {
                    VoiceRSS.getInstance().debugTTS(triggerSoundDataTField.getText());
                    SoundManager.getInstance().playDebug(true, Paths.get("sounds/" + Settings.getInstance().getTtsLanguage() + "_"
                            + triggerSoundDataTField.getText().toLowerCase().trim().replaceAll(" ", "_") + ".wav"));
                    return;
                }
                if (radioButtonSoundFile.isSelected()) {
                    SoundManager.getInstance().playDebug(true, Paths.get(triggerSoundDataTField.getText()));
                }
            }
        } catch (AudioException e){
            AlertDialogs.audioExceptionDialog(e.getExceptionType());
        } catch (VoiceException e){
            AlertDialogs.voiceExceptionDialog(e.getExceptionType());
        }

    }


    public void addEditTriggerButton() {
        boolean isPersonal = (0 == triggerTypeComboBox.getSelectionModel().getSelectedIndex());
        String category = triggerCategoryComboBox.getSelectionModel().getSelectedItem();
        String name = triggerNameTextField.getText();
        String command = triggerCommandTextField.getText();
        int delay = triggerDelaySpinner.getValue();
        SoundType soundType;
        if (radioButtonBeep.isSelected()) {
            soundType = SoundType.BEEP;
        } else if (radioButtonTTS.isSelected()) {
            soundType = SoundType.TTS;
        } else {
            soundType = SoundType.SOUND_FILE;
        }
        String soundData = triggerSoundDataTField.getText();
        if (validateInput()) {
            if (isEdit) {
                newTrigger.setTriggerName(name);
                newTrigger.setTriggerCommand(command);
                newTrigger.setSoundData(soundData);
                newTrigger.setSoundType(soundType);
                newTrigger.setTriggerDelay(delay);
                newTrigger.setPersonal(isPersonal);
                newTrigger.setTriggerCategory(category);
            } else {
                newTrigger = new Trigger(isPersonal, category, name, command, delay, soundType, soundData);
            }
            createButton.getScene().getWindow().hide();
        }
    }

    public void basicWindowSetup() {
        categorySetup();
        windowMoving();
        checkBoxesSetup();
        languageSetup();
        radioButtonBeep.selectedProperty().set(true);
    }

    public void editWindowSetup(Trigger trigger) {
        setNewTrigger(trigger);
        isEdit = true;
        basicWindowSetup();
        if (trigger.getPersonal().getValue()) {
            triggerTypeComboBox.getSelectionModel().select(0);
        } else {
            triggerTypeComboBox.getSelectionModel().select(1);
        }
        triggerNameTextField.setText(trigger.getTriggerName().getValue());
        triggerCommandTextField.setText(trigger.getTriggerCommand().getValue());
        triggerSoundDataTField.setText(trigger.getSoundData().getValue());
        triggerDelaySpinner.getEditor().setText(Integer.toString(trigger.getTriggerDelay().getValue()));
        triggerCategoryComboBox.getSelectionModel().select(trigger.getTriggerCategory().getValue());
        switch (trigger.getSoundType()) {
            case BEEP:
                radioButtonBeep.selectedProperty().set(true);
                break;
            case SOUND_FILE:
                radioButtonSoundFile.selectedProperty().set(true);
                break;
            case TTS:
                radioButtonTTS.selectedProperty().set(true);
                break;
            default:
                radioButtonBeep.selectedProperty().set(true);
                break;
        }
    }

    private void categorySetup() {
        for (Trigger trigger : TriggerData.getInstance().getTriggersArray()) {
            if (!triggerCategoryComboBox.getItems().contains(trigger.getTriggerCategory().getValue())) {
                triggerCategoryComboBox.getItems().add(trigger.getTriggerCategory().getValue());
            }
        }


    }

    public void selectSoundFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LanguageData.getInstance().getMsg("addEditWindowSoundPathDialogTitle"));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("MP3, Aiff, Wav, Au, MIDI", "*.mp3", "*.aiff", "*.wav", "*.mid", "*.au");
        fileChooser.getExtensionFilters().add(filter);
        File soundFile = fileChooser.showOpenDialog(selectSoundPathFileButton.getScene().getWindow());
        if (soundFile != null) {
            triggerSoundDataTField.setText(soundFile.getAbsolutePath());
        }
    }

    private void languageSetup() {
        clearFont();
        if(Settings.getInstance().getLocale().equals(Language.Japanese)){
            japaneseFont();
        } else{
            westernFont();
        }
        selectSoundPathFileButton.setText(LanguageData.getInstance().getMsg("addEditWindowSoundPathButton"));
        triggerNameLabel.setText(LanguageData.getInstance().getMsg("addEditWindowTriggerNameLabel"));
        triggerTypeLabel.setText(LanguageData.getInstance().getMsg("addEditWindowTriggerTypeLabel"));
        triggerCategoryLabel.setText(LanguageData.getInstance().getMsg("addEditWindowTriggerCategoryLabel"));
        triggerCategoryComboBox.setPromptText(LanguageData.getInstance().getMsg("addEditWindowTriggerCategoryPrompt"));
        triggerCategoryCheckBox.setText(LanguageData.getInstance().getMsg("addEditWindowTriggerCategoryCheckBox"));
        triggerCommandLabel.setText(LanguageData.getInstance().getMsg("tableColumnCommand"));
        delayLabel.setText(LanguageData.getInstance().getMsg("tableColumnDelay"));
        soundDataLabel.setText(LanguageData.getInstance().getMsg("tableColumnSound"));
        cancelButton.setText(LanguageData.getInstance().getMsg("cancelButton"));
        playButton.setText(LanguageData.getInstance().getMsg("playButton"));
        if (isEdit) {
            topBarLabel.setText(LanguageData.getInstance().getMsg("addEditWindowTopBarLabelEdit"));
            createButton.setText(LanguageData.getInstance().getMsg("addEditWindowTriggerEditButton"));
        } else {
            createButton.setText(LanguageData.getInstance().getMsg("addEditWindowTriggerCreateButton"));
            topBarLabel.setText(LanguageData.getInstance().getMsg("addEditWindowTopBarLabelCreate"));
        }
    }

    private void japaneseFont(){
        selectSoundPathFileButton.getStyleClass().add("JapaneseFont");
        triggerNameLabel.getStyleClass().add("JapaneseFont");
        triggerTypeLabel.getStyleClass().add("JapaneseFont");
        triggerCategoryLabel.getStyleClass().add("JapaneseFont");
        triggerCategoryComboBox.getStyleClass().add("JapaneseFont");
        triggerCategoryCheckBox.getStyleClass().add("JapaneseFont");
        triggerCommandLabel.getStyleClass().add("JapaneseFont");
        delayLabel.getStyleClass().add("JapaneseFont");
        soundDataLabel.getStyleClass().add("JapaneseFont");
        cancelButton.getStyleClass().add("JapaneseFont");
        createButton.getStyleClass().add("JapaneseFont");
        topBarLabel.getStyleClass().add("JapaneseFont");
        playButton.getStyleClass().add("JapaneseFont");
    }

    private void westernFont(){
        selectSoundPathFileButton.getStyleClass().add("WesternFont");
        triggerNameLabel.getStyleClass().add("WesternFont");
        triggerTypeLabel.getStyleClass().add("WesternFont");
        triggerCategoryLabel.getStyleClass().add("WesternFont");
        triggerCategoryComboBox.getStyleClass().add("WesternFont");
        triggerCategoryCheckBox.getStyleClass().add("WesternFont");
        triggerCommandLabel.getStyleClass().add("WesternFont");
        delayLabel.getStyleClass().add("WesternFont");
        soundDataLabel.getStyleClass().add("WesternFont");
        cancelButton.getStyleClass().add("WesternFont");
        createButton.getStyleClass().add("WesternFont");
        topBarLabel.getStyleClass().add("WesternFont");
        playButton.getStyleClass().add("WesternFont");
    }

    private void clearFont(){
        selectSoundPathFileButton.getStyleClass().remove("WesternFont");
        selectSoundPathFileButton.getStyleClass().remove("JapaneseFont");
        triggerNameLabel.getStyleClass().remove("WesternFont");
        triggerNameLabel.getStyleClass().remove("JapaneseFont");
        triggerTypeLabel.getStyleClass().remove("WesternFont");
        triggerTypeLabel.getStyleClass().remove("JapaneseFont");
        triggerCategoryLabel.getStyleClass().remove("WesternFont");
        triggerCategoryLabel.getStyleClass().remove("JapaneseFont");
        triggerCategoryComboBox.getStyleClass().remove("WesternFont");
        triggerCategoryComboBox.getStyleClass().remove("JapaneseFont");
        triggerCategoryCheckBox.getStyleClass().remove("WesternFont");
        triggerCategoryCheckBox.getStyleClass().remove("JapaneseFont");
        triggerCommandLabel.getStyleClass().remove("WesternFont");
        triggerCommandLabel.getStyleClass().remove("JapaneseFont");
        delayLabel.getStyleClass().remove("WesternFont");
        delayLabel.getStyleClass().remove("JapaneseFont");
        soundDataLabel.getStyleClass().remove("WesternFont");
        soundDataLabel.getStyleClass().remove("JapaneseFont");
        cancelButton.getStyleClass().remove("WesternFont");
        cancelButton.getStyleClass().remove("JapaneseFont");
        createButton.getStyleClass().remove("WesternFont");
        createButton.getStyleClass().remove("JapaneseFont");
        topBarLabel.getStyleClass().remove("WesternFont");
        topBarLabel.getStyleClass().remove("JapaneseFont");
        playButton.getStyleClass().remove("JapaneseFont");
        playButton.getStyleClass().remove("WesternFont");
    }
}
