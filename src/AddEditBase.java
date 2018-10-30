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

public class AddEditBase {
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
        private Button createEditButton;
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

        //  Getters
//  TODO remove unnecessary getters
    public ComboBox<String> getTriggerTypeComboBox() {
        return triggerTypeComboBox;
    }

    public ComboBox<String> getTriggerCategoryComboBox() {
        return triggerCategoryComboBox;
    }

    public TextField getTriggerNameTextField() {
        return triggerNameTextField;
    }

    public TextField getTriggerCommandTextField() {
        return triggerCommandTextField;
    }

    public TextField getTriggerSoundDataTField() {
        return triggerSoundDataTField;
    }

    public Button getCreateEditButton() {
        return createEditButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getSelectSoundPathFileButton() {
        return selectSoundPathFileButton;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public RadioButton getRadioButtonBeep() {
        return radioButtonBeep;
    }

    public RadioButton getRadioButtonTTS() {
        return radioButtonTTS;
    }

    public RadioButton getRadioButtonSoundFile() {
        return radioButtonSoundFile;
    }

    public Label getTriggerNameLabel() {
        return triggerNameLabel;
    }

    public Label getTriggerTypeLabel() {
        return triggerTypeLabel;
    }

    public Label getTriggerCategoryLabel() {
        return triggerCategoryLabel;
    }

    public Label getTopBarLabel() {
        return topBarLabel;
    }

    public Label getTriggerCommandLabel() {
        return triggerCommandLabel;
    }

    public Label getDelayLabel() {
        return delayLabel;
    }

    public Label getSoundDataLabel() {
        return soundDataLabel;
    }

    public CheckBox getTriggerCategoryCheckBox() {
        return triggerCategoryCheckBox;
    }

    public Spinner<Integer> getTriggerDelaySpinner() {
        return triggerDelaySpinner;
    }

    public BorderPane getTopLabelPane() {
        return topLabelPane;
    }

    //  Fields
        private double xOffset = 0;
        private double yOffset = 0;

        public void initialize() {
            comboBoxesInit();
            delaySpinnerInitialize();
            actionListeners();
            languageSetup();
            windowMoving();
            categorySetup();
            checkBoxesSetup();
        }

        private void comboBoxesInit(){
            triggerTypeComboBox.getItems().addAll(LanguageData.getInstance().getMsg("TriggerTypePersonal"),
                    LanguageData.getInstance().getMsg("TriggerTypeRaid"));
            triggerTypeComboBox.getSelectionModel().select(0);
            triggerCategoryComboBox.getItems().add(LanguageData.getInstance().getMsg("TriggerTypeDefault"));
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
            final String[] lastItem = {null};
            triggerCategoryCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    lastItem[0] = triggerCategoryComboBox.getSelectionModel().getSelectedItem();
                    triggerCategoryComboBox.setEditable(true);
                    triggerCategoryComboBox.getEditor().setText(null);
                } else {
                    triggerCategoryComboBox.setEditable(false);
                    triggerCategoryComboBox.getEditor().setText(null);
                    if(lastItem[0] != null) {
                        triggerCategoryComboBox.getSelectionModel().select(lastItem[0]);
                    } else {
                        triggerCategoryComboBox.getSelectionModel().select(0);
                    }
                }
            });
        }

    //  TODO change resource bundle tags
        public boolean validateInput() {
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
                AlertDialogs.errorDialogShow(errorMessage.toString());
                return false;
            }
        }

        public Trigger getFields(){
            if(validateInput()){
                boolean isPersonal = (0 == getTriggerTypeComboBox().getSelectionModel().getSelectedIndex());
                String category = getTriggerCategoryComboBox().getSelectionModel().getSelectedItem();
                String name = getTriggerNameTextField().getText();
                String command = getTriggerCommandTextField().getText();
                int delay = getTriggerDelaySpinner().getValue();
                SoundType soundType;
//  TODO look for different way to doing code below, check toggleGroup or some other solution
                if (getRadioButtonBeep().isSelected()) {
                    soundType = SoundType.BEEP;
                } else if (getRadioButtonTTS().isSelected()) {
                    soundType = SoundType.TTS;
                } else {
                    soundType = SoundType.SOUND_FILE;
                }
                String soundData = getTriggerSoundDataTField().getText();
                return new Trigger((new Trigger(isPersonal, category, name, command, delay, soundType, soundData)));
            } else {
                return null;
            }
        }

        @FXML
        private void playButton() {
            try{
                if (radioButtonBeep.isSelected()) {
                    SoundManager.getInstance().playDebug(true, SoundManager.getInstance().DEFAULT_AUDIO_FILE);
                    return;
                }
                if(triggerSoundDataTField.getText().isEmpty()){
                    AlertDialogs.errorDialogShow(LanguageData.getInstance().getMsg("errorValidateSoundData"));
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

        private void categorySetup() {
            for (Trigger trigger : Settings.getInstance().getTriggerList()) {
                if (!triggerCategoryComboBox.getItems().contains(trigger.getTriggerCategory().getValue())) {
                    triggerCategoryComboBox.getItems().add(trigger.getTriggerCategory().getValue());
                }
            }
        }

        @FXML
        private void selectSoundFile() {
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
            createEditButton.getStyleClass().add("JapaneseFont");
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
            createEditButton.getStyleClass().add("WesternFont");
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
            createEditButton.getStyleClass().remove("WesternFont");
            createEditButton.getStyleClass().remove("JapaneseFont");
            topBarLabel.getStyleClass().remove("WesternFont");
            topBarLabel.getStyleClass().remove("JapaneseFont");
            playButton.getStyleClass().remove("JapaneseFont");
            playButton.getStyleClass().remove("WesternFont");
        }
    }
