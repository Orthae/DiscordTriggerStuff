package com.github.orthae.discordtriggerstuff.controllers;

import com.github.orthae.discordtriggerstuff.*;
import com.github.orthae.discordtriggerstuff.alerts.AlertDialog;
import com.github.orthae.discordtriggerstuff.enums.Language;
import com.github.orthae.discordtriggerstuff.enums.SoundType;
import com.github.orthae.discordtriggerstuff.exceptions.AudioException;
import com.github.orthae.discordtriggerstuff.exceptions.VoiceException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Paths;

public abstract class AddEditBase {
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

    private Window owner;

    //  Getters
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

    public RadioButton getRadioButtonBeep() {
        return radioButtonBeep;
    }

    public RadioButton getRadioButtonTTS() {
        return radioButtonTTS;
    }

    public RadioButton getRadioButtonSoundFile() {
        return radioButtonSoundFile;
    }

    public Label getTopBarLabel() {
        return topBarLabel;
    }

    public Spinner<Integer> getTriggerDelaySpinner() {
        return triggerDelaySpinner;
    }

    public BorderPane getTopLabelPane() {
        return topLabelPane;
    }

    // Setters
    public void setOwner(Window owner){
        this.owner = owner;
    }

    //  Fields
    public void initialize() {
        comboBoxesInit();
        delaySpinnerInitialize();
        actionListeners();
        languageSetup();
        ReusedCode.enableWindowMoving(topLabelPane);
        categorySetup();
        checkBoxesSetup();
    }

    private void comboBoxesInit() {
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
        TextFormatter<String> textFormatter = new TextFormatter<>(ReusedCode.SPINNER_FORMATTER);
        triggerDelaySpinner.getEditor().setTextFormatter(textFormatter);
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
                if (lastItem[0] != null) {
                    triggerCategoryComboBox.getSelectionModel().select(lastItem[0]);
                } else {
                    triggerCategoryComboBox.getSelectionModel().select(0);
                }
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();
        if (triggerNameTextField.getText().isEmpty()) {
            errorMessage.append(LanguageData.getInstance().getMsg("AlertValidateName"));
            errorMessage.append("\n");
            isValid = false;
        }
        if (triggerCategoryCheckBox.isSelected() && (triggerCategoryComboBox.getEditor().getText() == null || triggerCategoryComboBox.getEditor().getText().isEmpty())) {
            errorMessage.append(LanguageData.getInstance().getMsg("AlertValidateCategory"));
            errorMessage.append("\n");
            isValid = false;
        }
        if (triggerCommandTextField.getText() == null || triggerCommandTextField.getText().isEmpty()) {
            errorMessage.append(LanguageData.getInstance().getMsg("AlertValidateCommand"));
            errorMessage.append("\n");
            isValid = false;
        }
        if (triggerSoundDataTField.getText().isEmpty() && !radioButtonBeep.isSelected()) {
            errorMessage.append(LanguageData.getInstance().getMsg("AlertValidateSoundData"));
            isValid = false;
        }
        if (isValid) {
            return true;
        } else {
            AlertDialog.createErrorDialog(owner).setAlertMessage(errorMessage.toString()).showAndWait();
            return false;
        }
    }

    public Trigger getFields() {
        if (validateInput()) {
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
         try {
             if (radioButtonBeep.isSelected()) {
                SoundManager.getInstance().playDebug(true, SoundManager.getInstance().DEFAULT_AUDIO_FILE);
                return;
            }
            if (triggerSoundDataTField.getText().isEmpty()) {
                AlertDialog.createErrorDialog(owner).setAlertMessage(LanguageData.getInstance().getMsg("AlertValidateSoundData")).showAndWait();
            } else {
                if (radioButtonTTS.isSelected()) {
                    VoiceManager.getInstance().debugTTS(triggerSoundDataTField.getText());
                    SoundManager.getInstance().playDebug(true, Paths.get(ReusedCode.getFormattedFilePath(triggerSoundDataTField.getText())));
                    return;
                }
                if (radioButtonSoundFile.isSelected()) {
                    SoundManager.getInstance().playDebug(true, Paths.get(triggerSoundDataTField.getText()));
                }
            }
        } catch (AudioException e) {
            AlertDialog.createErrorDialog(owner).setAlertMessage(e.getAlertMessage()).showAndWait();
            Logger.getInstance().log("AudioException thrown while playing audio in AddEditBase class" + e.getExceptionType().toString());
        } catch (VoiceException e) {
            AlertDialog.createErrorDialog(owner).setAlertMessage(e.getAlertMessage()).showAndWait();
            Logger.getInstance().log("VoiceException thrown while getting TTS file in AddEditBase class " + e.getExceptionType().toString());
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
        fileChooser.setTitle(LanguageData.getInstance().getMsg("DialogAddEditSoundPathTitle"));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("MP3, Aiff, Wav, Au, MIDI", "*.mp3", "*.aiff", "*.wav", "*.mid", "*.au");
        fileChooser.getExtensionFilters().add(filter);
        File soundFile = fileChooser.showOpenDialog(selectSoundPathFileButton.getScene().getWindow());
        if (soundFile != null) {
            triggerSoundDataTField.setText(soundFile.getAbsolutePath());
        }
    }

    private void languageSetup() {
        if (Settings.getInstance().getLocale().equals(Language.Japanese)) {
            japaneseFont();
        } else {
            westernFont();
        }
        selectSoundPathFileButton.setText(LanguageData.getInstance().getMsg("ButtonAddEditBrowse"));
        triggerNameLabel.setText(LanguageData.getInstance().getMsg("LabelAddEditName"));
        triggerTypeLabel.setText(LanguageData.getInstance().getMsg("LabelAddEditType"));
        triggerCategoryLabel.setText(LanguageData.getInstance().getMsg("LabelAddEditCategory"));
        triggerCategoryComboBox.setPromptText(LanguageData.getInstance().getMsg("ComboBoxAddEditTriggerCategoryPrompt"));
        triggerCategoryCheckBox.setText(LanguageData.getInstance().getMsg("CheckBoxAddEditTriggerCategory"));
        triggerCommandLabel.setText(LanguageData.getInstance().getMsg("LabelAddEditCommand"));
        delayLabel.setText(LanguageData.getInstance().getMsg("LabelAddEditDelay"));
        soundDataLabel.setText(LanguageData.getInstance().getMsg("LabelAddEditSound"));
        cancelButton.setText(LanguageData.getInstance().getMsg("ButtonAddEditCancel"));
        playButton.setText(LanguageData.getInstance().getMsg("ButtonAddEditPlay"));
        radioButtonBeep.setText(LanguageData.getInstance().getMsg("RadioButtonAddEditBeep"));
        radioButtonTTS.setText(LanguageData.getInstance().getMsg("RadioButtonAddEditTTS"));
        radioButtonSoundFile.setText(LanguageData.getInstance().getMsg("RadioButtonAddEditSoundFile"));
    }

    private void japaneseFont() {
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
        playButton.getStyleClass().add("JapaneseFont");
        topBarLabel.getStyleClass().add("TopLabelJapanese");
    }

    private void westernFont() {
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
        playButton.getStyleClass().add("WesternFont");
        topBarLabel.getStyleClass().add("TopLabelWestern");
    }
}
