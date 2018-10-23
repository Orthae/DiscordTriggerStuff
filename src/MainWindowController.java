import com.voicerss.tts.Languages;
import enums.Language;
import exceptions.AudioException;
import exceptions.VoiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;


public class MainWindowController {
    //  @FXML block
    @FXML
    private ToggleButton topBarMainButton;
    @FXML
    private ToggleButton topBarTriggerTableButton;
    @FXML
    private ToggleButton topBarUnlockButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button discordConnectButton;
    @FXML
    private Button discordDisconnectButton;
    @FXML
    private Button discordJoinChannelButton;
    @FXML
    private Button discordLeaveChannelButton;
    @FXML
    private Button actFindLogFileButton;
    @FXML
    private Button actSelectLogFileButton;
    @FXML
    private Button actFindLogFolderButton;
    @FXML
    private Button actSelectLogFolderButton;
    @FXML
    private Button deleteLogFiles;
    @FXML
    private Button addTriggerTableButton;
    @FXML
    private Button editTriggerTableButton;
    @FXML
    private Button deleteTriggerTableButton;
    @FXML
    private Button activateTriggersTableButton;
    @FXML
    private Button deactivateTriggersTableButton;
    @FXML
    private Button discordAddBotButton;
    @FXML
    private Button testVoiceRRSButton;
    @FXML
    private Button importTriggersButton;
    @FXML
    private Button exportTriggersButton;
    @FXML
    private Label discordConnectLabel;
    @FXML
    private Label discordSelectServerLabel;
    @FXML
    private Label discordSelectChannelLabel;
    @FXML
    private Label tableSortingTypeLabel;
    @FXML
    private Label tableSortingCategoryLabel;
    @FXML
    private Label discordVolumeLabel;
    @FXML
    private Label localVolumeLabel;
    @FXML
    private Label actLogFileLabel;
    @FXML
    private Label actLogFileStatusLabel;
    @FXML
    private Label discordVolumeLabelTable;
    @FXML
    private Label localVolumeLabelTable;
    @FXML
    private Label logFolderSizeLabel;
    @FXML
    private Label appLanguageLabel;
    @FXML
    private Label discordTokenLabel;
    @FXML
    private Label discordClientID;
    @FXML
    private Label voiceRRSToken;
    @FXML
    private Label actLogFolderLabel;
    @FXML
    private Label ttsLanguageLabel;
    @FXML
    private ComboBox<String> discordSelectServerComboBox;
    @FXML
    private ComboBox<String> discordSelectChannelComboBox;
    @FXML
    private ComboBox<String> tableSortingTypeComboBox;
    @FXML
    private ComboBox<String> tableSortingCategoryComboBox;
    @FXML
    private ComboBox<Language> languageComboBox;
    @FXML
    private ComboBox<String> ttsLanguageComboBox;
    @FXML
    private TextField discordTokenTField;
    @FXML
    private TextField voiceRRSTokenTField;
    @FXML
    private TextField logFolderTField;
    @FXML
    private TextField dummyTField1;
    @FXML
    private TextField dummyTField2;
    @FXML
    private TextField dummyTField3;
    @FXML
    private TextField dummyTField4;
    @FXML
    private TextField discordClientIDTField;
    @FXML
    private TableView<Trigger> triggerTableView;
    @FXML
    private TableColumn<Trigger, Boolean> triggerTableColumnEnabled;
    @FXML
    private TableColumn<Trigger, String> triggerTableColumnTriggerName;
    @FXML
    private TableColumn<Trigger, Boolean> triggerTableColumnTriggerType;
    @FXML
    private TableColumn<Trigger, String> triggerTableColumnTriggerCategory;
    @FXML
    private TableColumn<Trigger, String> triggerTableColumnTriggerCommand;
    @FXML
    private TableColumn<Trigger, Number> triggerTableColumnTriggerDelay;
    @FXML
    private TableColumn<Trigger, String> triggerTableColumnTriggerSound;
    @FXML
    private Slider discordVolumeSlider;
    @FXML
    private Slider localVolumeSlider;
    @FXML
    private Slider discordVolumeSliderTable;
    @FXML
    private Slider localVolumeSliderTable;
    @FXML
    private CheckBox muteDiscordCheckBox;
    @FXML
    private CheckBox muteLocalCheckBox;
//    @FXML
//    private CheckBox tableDiscordPlaybackCheckBox;
    @FXML
    private CheckBox tokenCheckbox;
    @FXML
    private CheckBox muteDiscordCheckBoxTable;
    @FXML
    private CheckBox muteLocalCheckBoxTable;
    @FXML
    private CheckBox logFolderCheckBox;
    @FXML
    private BorderPane appWindow;
    @FXML
    private HBox mainWindow;
    @FXML
    private HBox triggerWindow;
    @FXML
    private HBox topButtonBar;
    @FXML
    private StackPane mainStackPane;

    //  Fields
    private double xOffset = 0;
    private double yOffset = 0;

    //  Methods
    private void actionListeners() {
        muteDiscordCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Settings.getInstance().setDiscordPlayerMute(true);
                discordVolumeSlider.setDisable(true);
                discordVolumeSliderTable.setDisable(true);
                if (!muteDiscordCheckBoxTable.isSelected()) {
                    muteDiscordCheckBoxTable.setSelected(true);
                }
            } else {
                Settings.getInstance().setDiscordPlayerMute(false);
                discordVolumeSlider.setDisable(false);
                discordVolumeSliderTable.setDisable(false);
                if (muteDiscordCheckBoxTable.isSelected()) {
                    muteDiscordCheckBoxTable.setSelected(false);
                }
            }
        });

        muteDiscordCheckBoxTable.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                muteDiscordCheckBox.setSelected(true);
            } else {
                muteDiscordCheckBox.setSelected(false);
            }
        });

        muteLocalCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                localVolumeSlider.setDisable(true);
                localVolumeSliderTable.setDisable(true);
                Settings.getInstance().setLocalPlayerMute(true);
                if (!muteLocalCheckBoxTable.isSelected()) {
                    muteLocalCheckBoxTable.setSelected(true);
                }
            } else {
                localVolumeSlider.setDisable(false);
                localVolumeSliderTable.setDisable(false);
                Settings.getInstance().setLocalPlayerMute(false);
                if (muteLocalCheckBoxTable.isSelected()) {
                    muteLocalCheckBoxTable.setSelected(false);
                }
            }
        });

        muteLocalCheckBoxTable.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                muteLocalCheckBox.setSelected(true);
            } else {
                muteLocalCheckBox.setSelected(false);
            }
        });

        localVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Settings.getInstance().setLocalPlayerVolume(newValue.doubleValue());
            localVolumeSliderTable.setValue(newValue.doubleValue());
        });

        localVolumeSliderTable.valueProperty().addListener((observable, oldValue, newValue) -> localVolumeSlider.setValue(newValue.doubleValue()));

        discordVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Settings.getInstance().setDiscordPlayerVolume(newValue.doubleValue());
            discordVolumeSliderTable.setValue(newValue.doubleValue());
        });

        discordVolumeSliderTable.valueProperty().addListener((observable, oldValue, newValue) -> discordVolumeSlider.setValue(newValue.doubleValue()));

        discordSelectServerComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !discordSelectServerComboBox.getSelectionModel().getSelectedItem().equals(LanguageData.getInstance().getMsg("mainDiscordComboBoxPrompt"))) {
                discordSelectChannelComboBox.setDisable(false);
                discordSelectChannelComboBox.getItems().clear();
                for (IVoiceChannel channel : DiscordManager.getInstance().getGuildList().get(discordSelectServerComboBox.getSelectionModel().getSelectedIndex()).getVoiceChannels()) {
                    discordSelectChannelComboBox.getItems().add(channel.getName());
                }
                discordSelectChannelComboBox.getSelectionModel().select(0);
            }
        });

        discordTokenTField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                Settings.getInstance().setDiscordToken(newValue);
            }
        });

        voiceRRSTokenTField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                Settings.getInstance().setVoiceRRSToken(newValue);
                VoiceRSS.getInstance().setAPIToken(newValue);
            }
        });

        logFolderTField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                Settings.getInstance().setLogFolder(newValue);
                logFolderSize();
            }
        });

        languageComboBox.getItems().addAll(Language.English, Language.German, Language.French, Language.Japanese);

        languageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                Settings.getInstance().setLocale(newValue);
                LanguageData.getInstance().changeLanguage(newValue);
                languageSetup();
                if (!newValue.equals(Language.English)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(null);
                    alert.setHeaderText(null);
                    alert.setContentText(LanguageData.getInstance().getMsg("languageNotFinished"));
                    alert.show();
                }
            }
        });

        ttsLanguageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                String lang;
                switch (newValue) {
                    case "Catalan":
                        lang = Languages.Catalan;
                        break;
                    case "Chinese (China)":
                        lang = Languages.Chinese_China;
                        break;
                    case "Chinese (Hong Kong)":
                        lang = Languages.Chinese_HongKong;
                        break;
                    case "Chinese (Taiwan)":
                        lang = Languages.Chinese_Taiwan;
                        break;
                    case "Danish":
                        lang = Languages.Danish;
                        break;
                    case "Dutch":
                        lang = Languages.Dutch;
                        break;
                    case "English (Australia)":
                        lang = Languages.English_Australia;
                        break;
                    case "English (Canada)":
                        lang = Languages.English_Canada;
                        break;
                    case "English (Great Britain)":
                        lang = Languages.English_GreatBritain;
                        break;
                    case "English (India)":
                        lang = Languages.English_India;
                        break;
                    case "English (United States)":
                        lang = Languages.English_UnitedStates;
                        break;
                    case "Finnish":
                        lang = Languages.Finnish;
                        break;
                    case "French (Canada)":
                        lang = Languages.French_Canada;
                        break;
                    case "French (France)":
                        lang = Languages.French_France;
                        break;
                    case "German":
                        lang = Languages.German;
                        break;
                    case "Italian":
                        lang = Languages.Italian;
                        break;
                    case "Japanese":
                        lang = Languages.Japanese;
                        break;
                    case "Korean":
                        lang = Languages.Korean;
                        break;
                    case "Norwegian":
                        lang = Languages.Norwegian;
                        break;
                    case "Polish":
                        lang = Languages.Polish;
                        break;
                    case "Portuguese (Brazil)":
                        lang = Languages.Portuguese_Brazil;
                        break;
                    case "Portuguese (Portugal)":
                        lang = Languages.Portuguese_Portugal;
                        break;
                    case "Russian":
                        lang = Languages.Russian;
                        break;
                    case "Spanish (Mexico)":
                        lang = Languages.Spanish_Mexico;
                        break;
                    case "Spanish (Spain)":
                        lang = Languages.Spanish_Spain;
                        break;
                    case "Swedish (Sweden)":
                        lang = Languages.Swedish;
                        break;
                    default:
                        lang = Languages.English_GreatBritain;
                        break;
                }
                Settings.getInstance().setTtsLanguage(lang);
            }
        });

        tokenCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                discordTokenTField.setVisible(true);
                voiceRRSTokenTField.setVisible(true);
                discordClientIDTField.setVisible(true);
            } else {
                discordTokenTField.setVisible(false);
                voiceRRSTokenTField.setVisible(false);
                discordClientIDTField.setVisible(false);
            }
        });

        logFolderCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                logFolderTField.setVisible(true);
            } else {
                logFolderTField.setVisible(false);
            }
        });

        discordClientIDTField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                Settings.getInstance().setClientID(newValue);
            }
        });

        tableSortingTypeComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            tableSortingCategoryComboBox.getItems().clear();
            tableSortingCategoryComboBox.getItems().add(LanguageData.getInstance().getMsg("tableTriggerAll"));
            switch (newValue.intValue()) {
                case 0:
                    for (Trigger trigger : Settings.getInstance().getTriggerList()) {
                        if (!tableSortingCategoryComboBox.getItems().contains(trigger.getTriggerCategory().getValue())) {
                            tableSortingCategoryComboBox.getItems().add(trigger.getTriggerCategory().getValue());
                        }
                    }
                    break;
                case 1:
                    for (Trigger trigger : Settings.getInstance().getTriggerList()) {
                        if (trigger.getPersonal().getValue()) {
                            if (!tableSortingCategoryComboBox.getItems().contains(trigger.getTriggerCategory().getValue())) {
                                tableSortingCategoryComboBox.getItems().add(trigger.getTriggerCategory().getValue());
                            }
                        }
                    }
                    break;
                case 2:
                    for (Trigger trigger : Settings.getInstance().getTriggerList()) {
                        if (!trigger.getPersonal().getValue()) {
                            if (!tableSortingCategoryComboBox.getItems().contains(trigger.getTriggerCategory().getValue())) {
                                tableSortingCategoryComboBox.getItems().add(trigger.getTriggerCategory().getValue());
                            }
                        }
                    }
                    break;
            }
            tableSortingCategoryComboBox.getSelectionModel().select(0);
        });

        tableSortingCategoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Trigger> listType = FXCollections.observableArrayList();
            ObservableList<Trigger> listCategory = FXCollections.observableArrayList();
            switch (tableSortingTypeComboBox.getSelectionModel().getSelectedIndex()) {
                case 0:
                    listType.addAll(Settings.getInstance().getTriggerList());
                    break;
                case 1:
                    for (Trigger trigger : Settings.getInstance().getTriggerList()) {
                        if (trigger.getPersonal().getValue()) {
                            listType.add(trigger);
                        }
                    }
                    break;
                case 2:
                    for (Trigger trigger : Settings.getInstance().getTriggerList()) {
                        if (!trigger.getPersonal().getValue()) {
                            listType.add(trigger);
                        }
                    }
                    break;
            }
            for (Trigger trigger : listType) {
                if (tableSortingCategoryComboBox.getSelectionModel().getSelectedIndex() == 0) {
                    listCategory = listType;
                } else {
                    if (tableSortingCategoryComboBox.getSelectionModel().getSelectedItem() != null) {
                        if (tableSortingCategoryComboBox.getSelectionModel().getSelectedItem().equals(trigger.getTriggerCategory().getValue())) {
                            listCategory.add(trigger);
                        }
                    }
                }
            }
            triggerTableView.setItems(listCategory);
            triggerTableView.refresh();
        });
    }

    public void quitButton() {
        discordDisconnectButton.fire();
        Settings.getInstance().saveSettings();
        LogReader.getInstance().stopThread();
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    public void selectLogFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(LanguageData.getInstance().getMsg("selectLogFolder"));
        File folder = directoryChooser.showDialog(appWindow.getScene().getWindow());
        if (folder != null) {
            logFolderTField.setText(folder.getPath());
        }
    }

    public void findLogFolder() {
        String log = LogReader.getInstance().findLogFolder();
        if (log == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle(LanguageData.getInstance().getMsg("AlertError"));
            alert.setContentText(LanguageData.getInstance().getMsg("errorCouldntFindLogFolderContent"));
            alert.showAndWait();
        } else {
            logFolderTField.setText(log);
            logFolderSize();
        }
    }

    private void logFolderSize() {
        double total = 0;
        if (logFolderTField.getText() == null || logFolderTField.getText().isEmpty()) {
            logFolderNotFound();
            return;
        }
        try {
            DirectoryStream<Path> logFiles = Files.newDirectoryStream(Paths.get(Settings.getInstance().getLogFolder()), "*.log");
            for (Path file : logFiles) {
                total = total + ((double) file.toFile().length() / 1048576);
            }
        } catch (IOException | InvalidPathException e) {
            logFolderNotFound();
            return;
        }
        if (total > 512.0) {
            if (total > 1024) {
                total = total / 1024;
                logFolderSizeLabel.setText(String.format("%s %.2f %s", LanguageData.getInstance().getMsg("logFolderSize"),
                        total, LanguageData.getInstance().getMsg("unitsGB")));
            } else {
                logFolderSizeLabel.setText(String.format("%s %.2f %s", LanguageData.getInstance().getMsg("logFolderSize"),
                        total, LanguageData.getInstance().getMsg("unitsMB")));
            }
            clearLogFolderStyles();
            logFolderSizeLabel.getStyleClass().add("WarningTextRed");
        } else {
            logFolderSizeLabel.setText(String.format("%s %.2f %s", LanguageData.getInstance().getMsg("logFolderSize"),
                    total, LanguageData.getInstance().getMsg("unitsMB")));
            clearLogFolderStyles();
            logFolderSizeLabel.getStyleClass().add("WarningTextGreen");
        }
    }

    private void logFolderNotFound() {
        logFolderSizeLabel.setText(LanguageData.getInstance().getMsg("labelLogFolderNotFound"));
        clearLogFolderStyles();
        logFolderSizeLabel.getStyleClass().add("WarningTextRed");
    }

    private void clearLogFolderStyles() {
        logFolderSizeLabel.getStyleClass().remove("WarningTextGreen");
        logFolderSizeLabel.getStyleClass().remove("WarningTextRed");
    }

    private void moveWindowEventHandlerDisable() {
        topButtonBar.setOnMouseDragged(null);
        topButtonBar.setOnMousePressed(null);
        mainStackPane.setOnMouseDragged(null);
        mainStackPane.setOnMousePressed(null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void deleteLogFiles() {
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get(Settings.getInstance().getLogFolder()), "*.log");
            for (Path file : files) {
                file.toFile().delete();
            }
            logFolderSize();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void moveWindowEventHandlerEnable() {
        topButtonBar.setOnMousePressed(event -> {
            xOffset = appWindow.getScene().getWindow().getX() - event.getScreenX();
            yOffset = appWindow.getScene().getWindow().getY() - event.getScreenY();
        });
        topButtonBar.setOnMouseDragged(event -> {
            appWindow.getScene().getWindow().setX(event.getScreenX() + xOffset);
            appWindow.getScene().getWindow().setY(event.getScreenY() + yOffset);
        });
        mainStackPane.setOnMousePressed(event -> {
            xOffset = appWindow.getScene().getWindow().getX();
            yOffset = appWindow.getScene().getWindow().getY();
        });
        mainStackPane.setOnMouseDragged(event -> {
            double width = event.getScreenX() - xOffset;
            double height = event.getScreenY() - yOffset;
            if (width >= Settings.getInstance().MIN_WIDTH) {
                appWindow.getScene().getWindow().setWidth(width);
            }
            if (height >= Settings.getInstance().MIN_HEIGHT) {
                appWindow.getScene().getWindow().setHeight(height);
            }
        });
    }

    public void loadSettings() {
        localVolumeSlider.valueProperty().set(Settings.getInstance().getLocalPlayerVolume());
        muteLocalCheckBox.selectedProperty().set(Settings.getInstance().isLocalPlayerMute());
        discordVolumeSlider.valueProperty().set(Settings.getInstance().getDiscordPlayerVolume());
        muteDiscordCheckBox.selectedProperty().set(Settings.getInstance().isDiscordPlayerMute());
        discordTokenTField.setText(Settings.getInstance().getDiscordToken());
        voiceRRSTokenTField.setText(Settings.getInstance().getVoiceRRSToken());
        logFolderTField.setText(Settings.getInstance().getLogFolder());
        languageComboBox.getSelectionModel().select(Settings.getInstance().getLocale());
        switch (Settings.getInstance().getTtsLanguage()) {
            case "ca-es":
                ttsLanguageComboBox.getSelectionModel().select("Catalan");
                break;
            case "zh-cn":
                ttsLanguageComboBox.getSelectionModel().select("Chinese (China)");
                break;
            case "zh-hk":
                ttsLanguageComboBox.getSelectionModel().select("Chinese (Hong Kong)");
                break;
            case "zh-tw":
                ttsLanguageComboBox.getSelectionModel().select("Chinese (Taiwan)");
                break;
            case "da-dk":
                ttsLanguageComboBox.getSelectionModel().select("Danish");
                break;
            case "nl-nl":
                ttsLanguageComboBox.getSelectionModel().select("Dutch");
                break;
            case "en-au":
                ttsLanguageComboBox.getSelectionModel().select("English (Australia)");
                break;
            case "en-ca":
                ttsLanguageComboBox.getSelectionModel().select("English (Canada)");
                break;
            case "en-gb":
                ttsLanguageComboBox.getSelectionModel().select("English (Great Britain)");
                break;
            case "en-in":
                ttsLanguageComboBox.getSelectionModel().select("English (India)");
                break;
            case "en-us":
                ttsLanguageComboBox.getSelectionModel().select("English (United States)");
                break;
            case "fi-fi":
                ttsLanguageComboBox.getSelectionModel().select("Finnish");
                break;
            case "fr-ca":
                ttsLanguageComboBox.getSelectionModel().select("French (Canada)");
                break;
            case "fr-fr":
                ttsLanguageComboBox.getSelectionModel().select("French (France)");
                break;
            case "de-de":
                ttsLanguageComboBox.getSelectionModel().select("German");
                break;
            case "it-it":
                ttsLanguageComboBox.getSelectionModel().select("Italian");
                break;
            case "ja-jp":
                ttsLanguageComboBox.getSelectionModel().select("Japanese");
                break;
            case "ko-kr":
                ttsLanguageComboBox.getSelectionModel().select("Korean");
                break;
            case "nb-no":
                ttsLanguageComboBox.getSelectionModel().select("Norwegian");
                break;
            case "pl-pl":
                ttsLanguageComboBox.getSelectionModel().select("Polish");
                break;
            case "pt-br":
                ttsLanguageComboBox.getSelectionModel().select("Portuguese (Brazil)");
                break;
            case "pt-pt":
                ttsLanguageComboBox.getSelectionModel().select("Portuguese (Portugal)");
                break;
            case "ru-ru":
                ttsLanguageComboBox.getSelectionModel().select("Russian");
                break;
            case "es-mx":
                ttsLanguageComboBox.getSelectionModel().select("Spanish (Mexico)");
                break;
            case "es-es":
                ttsLanguageComboBox.getSelectionModel().select("Spanish (Spain)");
                break;
            case "sv-se":
                ttsLanguageComboBox.getSelectionModel().select("Swedish (Sweden)");
                break;
            default:
                ttsLanguageComboBox.getSelectionModel().select("English (Great Britain)");
                break;
        }
        discordClientIDTField.setText(Settings.getInstance().getClientID());
    }

    private void ttsComboBoxInit() {
        ttsLanguageComboBox.getItems().add("Catalan");
        ttsLanguageComboBox.getItems().add("Chinese (China)");
        ttsLanguageComboBox.getItems().add("Chinese (Hong Kong)");
        ttsLanguageComboBox.getItems().add("Chinese (Taiwan)");
        ttsLanguageComboBox.getItems().add("Danish");
        ttsLanguageComboBox.getItems().add("Dutch");
        ttsLanguageComboBox.getItems().add("English (Australia)");
        ttsLanguageComboBox.getItems().add("English (Canada)");
        ttsLanguageComboBox.getItems().add("English (Great Britain)");
        ttsLanguageComboBox.getItems().add("English (India)");
        ttsLanguageComboBox.getItems().add("Finnish");
        ttsLanguageComboBox.getItems().add("French (Canada)");
        ttsLanguageComboBox.getItems().add("French (France)");
        ttsLanguageComboBox.getItems().add("German");
        ttsLanguageComboBox.getItems().add("Italian");
        ttsLanguageComboBox.getItems().add("Japanese");
        ttsLanguageComboBox.getItems().add("Korean");
        ttsLanguageComboBox.getItems().add("Norwegian");
        ttsLanguageComboBox.getItems().add("Polish");
        ttsLanguageComboBox.getItems().add("Portuguese (Brazil)");
        ttsLanguageComboBox.getItems().add("Portuguese (Portugal)");
        ttsLanguageComboBox.getItems().add("Russian");
        ttsLanguageComboBox.getItems().add("Spanish (Mexico)");
        ttsLanguageComboBox.getItems().add("Spanish (Spain)");
        ttsLanguageComboBox.getItems().add("Swedish (Sweden)");
    }

    private void noItemSelectedError() {
//  TODO move to AlertDialogs
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(LanguageData.getInstance().getMsg("tableErrorTitle"));
        alert.setContentText(LanguageData.getInstance().getMsg("tableErrorNoItemSelected"));
        alert.showAndWait();
    }

    public void activateTriggers() {
        for (Trigger trigger : triggerTableView.getSelectionModel().getSelectedItems()) {
            trigger.setEnabled(true);
        }
        triggerTableView.getSelectionModel().clearSelection();
        reFiltering();
    }

    public void deactivateTriggers() {
        for (Trigger trigger : triggerTableView.getSelectionModel().getSelectedItems()) {
            trigger.setEnabled(false);
        }
        triggerTableView.getSelectionModel().clearSelection();
        reFiltering();
    }

    public void unlockButton() {
        if (topBarUnlockButton.isSelected()) {
            moveWindowEventHandlerEnable();
        } else {
            moveWindowEventHandlerDisable();
        }
    }

    public void discordJoinChannel() {
        int guildIndex = discordSelectServerComboBox.getSelectionModel().getSelectedIndex();
        int channelIndex = discordSelectChannelComboBox.getSelectionModel().getSelectedIndex();
        List<IGuild> guildList = DiscordManager.getInstance().getGuildList();
        if (DiscordManager.getInstance().getVoiceChannel() != null && !DiscordManager.getInstance().getGuild().equals(guildList.get(guildIndex))) {
            if (DiscordManager.getInstance().isClientReady()) {
                DiscordManager.getInstance().getVoiceChannel().leave();
            }
        }
        DiscordManager.getInstance().joinChannel(guildIndex, channelIndex);
        discordLeaveChannelButton.setDisable(false);
    }

    public void discordLeaveChannel() {
        if (DiscordManager.getInstance().getVoiceChannel() != null) {
            DiscordManager.getInstance().leaveChannel();
        }
        discordLeaveChannelButton.setDisable(true);
    }

    public void discordLogOut() {
        discordSelectServerComboBox.setDisable(true);
        discordSelectServerComboBox.getItems().add(0, LanguageData.getInstance().getMsg("mainDiscordComboBoxPrompt"));
        discordSelectServerComboBox.getSelectionModel().select(0);
        discordSelectChannelComboBox.setDisable(true);
        discordSelectChannelComboBox.getItems().add(0, LanguageData.getInstance().getMsg("mainDiscordComboBoxPrompt"));
        discordSelectChannelComboBox.getSelectionModel().select(0);
        discordConnectButton.setDisable(false);
        discordDisconnectButton.setDisable(true);
        discordJoinChannelButton.setDisable(true);
        discordLeaveChannelButton.setDisable(true);
        if (DiscordManager.getInstance().getVoiceChannel() != null) {
            discordLeaveChannel();
        }
        DiscordManager.getInstance().logOut();
    }

    public void discordLogIn() {
        Runnable discordLogin = () -> {
            discordConnectButton.setDisable(true);
            DiscordManager.getInstance().logIn(Settings.getInstance().getDiscordToken());
            if (DiscordManager.getInstance().getDiscordClient() == null) {
                Platform.runLater(() -> {
//  TODO move to AlertDialogs
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText(DiscordManager.getInstance().getErrorMessage());
                    alert.showAndWait();
                    discordConnectButton.setDisable(false);
                });
            } else {
                while (!DiscordManager.getInstance().isClientReady()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("This should not happen");
                    }
                }
                Platform.runLater(() -> {
                    discordSelectServerComboBox.getItems().clear();
                    discordSelectChannelComboBox.getItems().clear();
                    discordSelectServerComboBox.getItems().clear();
                    for (IGuild guild : DiscordManager.getInstance().getGuildList()) {
                        discordSelectServerComboBox.getItems().add(guild.getName());
                    }
                    discordDisconnectButton.setDisable(false);
                    discordSelectServerComboBox.setDisable(false);
                    discordSelectServerComboBox.getSelectionModel().select(0);
                    discordJoinChannelButton.setDisable(false);
                    discordLeaveChannelButton.setDisable(true);
                });
            }
        };
        new Thread(discordLogin).start();
    }

    public void importButton() {
        Stage importTriggerWindow = new Stage();
        importTriggerWindow.initOwner(appWindow.getScene().getWindow());
        importTriggerWindow.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fxml/importTriggerWindow.fxml"));
        try {
            Parent root = fxmlLoader.load();
            importTriggerWindow.setScene(new Scene(root));
        } catch (IOException e) {
//  TODO add alert pop up
            Logger.getInstance().log("IOException thrown while loading \"fxml/importTriggerWindow.fxml\"");
        }
        importTriggerWindow.showAndWait();
        reFiltering();
    }

    public void deleteTrigger() {
//  TODO move to AlertDialogs
        if (triggerTableView.getSelectionModel().getSelectedItems().size() == 0) {
            noItemSelectedError();
            return;
        }
        if (triggerTableView.getSelectionModel().getSelectedItems() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            int size = triggerTableView.getSelectionModel().getSelectedItems().size();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP1"));
            if (!Settings.getInstance().getLocale().equals(Language.Japanese)) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(size);
            if (!Settings.getInstance().getLocale().equals(Language.Japanese)) {
                stringBuilder.append(" ");
            }
            if (size == 1) {
                stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP3"));
            } else {
                stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP2"));
            }

            alert.setContentText(stringBuilder.toString());
            alert.showAndWait();
            if (alert.getResult() == ButtonType.CANCEL) {
                return;
            }
        }

        for (Trigger trigger : triggerTableView.getSelectionModel().getSelectedItems()) {
            Settings.getInstance().getTriggerList().remove(trigger);
        }

        triggerTableView.getSelectionModel().clearSelection();
        reFiltering();
    }

    public void addTriggerButton() {
        Stage addEditWindow = new Stage();
        addEditWindow.initOwner(appWindow.getScene().getWindow());
        addEditWindow.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fxml/addTriggerWindow.fxml"));
        try {
            Parent root = fxmlLoader.load();
            addEditWindow.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddTriggerController addTriggerController = fxmlLoader.getController();
        addTriggerController.basicWindowSetup();
        addEditWindow.showAndWait();
//  TODO move it inside AddTriggerController
        Trigger newTrigger;
        if (addTriggerController.getNewTrigger() != null) {
            newTrigger = addTriggerController.getNewTrigger();
            Settings.getInstance().getTriggerList().add(newTrigger);
        }
        reFiltering();
    }

    private void reFiltering() {
        String categoryIndex = tableSortingCategoryComboBox.getSelectionModel().getSelectedItem();
        int typeIndex = tableSortingTypeComboBox.getSelectionModel().getSelectedIndex();
        tableSortingTypeComboBox.getSelectionModel().select(-1);
        tableSortingTypeComboBox.getSelectionModel().select(typeIndex);
        tableSortingCategoryComboBox.getSelectionModel().select(-1);
        if (tableSortingCategoryComboBox.getItems().contains(categoryIndex)) {
            tableSortingCategoryComboBox.getSelectionModel().select(categoryIndex);
        } else {
            tableSortingCategoryComboBox.getSelectionModel().select(0);
        }
    }

    public void voiceRRSTest() {
        try {
            VoiceRSS.getInstance().getTextToSpeech("test");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(LanguageData.getInstance().getMsg("AlertInformation"));
            alert.setHeaderText(null);
            alert.setContentText(LanguageData.getInstance().getMsg("AlertVoiceWorks"));
            alert.show();
        } catch (VoiceException e) {
            AlertDialogs.voiceExceptionDialog(e.getExceptionType());
            Logger.getInstance().log("VoiceRSS test failed: " + e.getExceptionType().toString());
        }
    }

    public void addBotToServer() {
        if (Desktop.isDesktopSupported()) {
            try {
                if (!discordClientIDTField.getText().trim().isEmpty()) {
                    URI link = new URI("https://discordapp.com/oauth2/authorize?client_id=" + Settings.getInstance().getClientID() + "&scope=bot");
                    Desktop.getDesktop().browse(link);
                } else {
//  TODO move to AlertDialogs
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(LanguageData.getInstance().getMsg("AlertError"));
                    alert.setHeaderText(null);
                    alert.setContentText(LanguageData.getInstance().getMsg("errorClientID"));
                    alert.show();
                }
            } catch (IOException | URISyntaxException e) {
                //  TODO move to AlertDialogs
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(LanguageData.getInstance().getMsg("AlertError"));
                alert.setHeaderText(null);
                alert.setContentText(LanguageData.getInstance().getMsg("errorCouldntAddBot"));
                alert.show();
            }
        }
    }

    public void initialize() {
        actionListeners();
        loadSettings();
        ttsComboBoxInit();
        tableSetup();
        languageSetup();
        buttonStateSetup();
        logFolderSize();
    }

    private void tableSetup() {
        triggerTableView.setItems(Settings.getInstance().getTriggerList());
        triggerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        triggerTableView.getSelectionModel().setCellSelectionEnabled(false);
        triggerTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        triggerTableColumnTriggerCommand.setMinWidth(225);
        triggerTableColumnTriggerCommand.setCellValueFactory(param -> param.getValue().getTriggerCommand());

        tableSortingTypeComboBox.getItems().add(LanguageData.getInstance().getMsg("tableTriggerAll"));
        tableSortingTypeComboBox.getItems().add(LanguageData.getInstance().getMsg("TriggerTypePersonal"));
        tableSortingTypeComboBox.getItems().add(LanguageData.getInstance().getMsg("TriggerTypeRaid"));
        tableSortingTypeComboBox.getSelectionModel().select(0);

        triggerTableColumnEnabled.setCellFactory(param -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Trigger, Boolean> cell = new TableCell<Trigger, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {

                        checkBox.setSelected(item);
                        setGraphic(checkBox);
//                        JavaFX 10+
//                        TableRow<Trigger> tableRow = getTableRow();
                        TableRow tableRow = getTableRow();
                        if (checkBox.isSelected()) {
                            tableRow.getStyleClass().remove("TableRowDisabled");
                            tableRow.getStyleClass().add("TableRowEnabled");
                        } else {
                            tableRow.getStyleClass().remove("TableRowEnabled");
                            tableRow.getStyleClass().add("TableRowDisabled");
                        }

                    }
                }
            };
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow().getItem() != null) {
//                        JavaFX 10+
//                    cell.getTableRow().getItem().setEnabled(newValue);
                    ((Trigger) cell.getTableRow().getItem()).setEnabled(newValue);

                }
            });
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return cell;
        });
        triggerTableColumnEnabled.setCellValueFactory(param -> param.getValue().getEnabled());
        triggerTableColumnEnabled.setMinWidth(30);
        triggerTableColumnEnabled.setMaxWidth(30);
        triggerTableColumnEnabled.setSortable(false);
        triggerTableColumnEnabled.setResizable(false);

        triggerTableColumnTriggerName.setCellValueFactory(param -> param.getValue().getTriggerName());

        triggerTableColumnTriggerType.setCellValueFactory(param -> param.getValue().getPersonal());
        triggerTableColumnTriggerType.setCellFactory(param -> {
            Label label = new Label();
            label.getStyleClass().add("TableTypeRowLabels");
            TableCell<Trigger, Boolean> cell = new TableCell<Trigger, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        if (item) {
                            label.setText(LanguageData.getInstance().getMsg("TriggerTypePersonal"));
                        } else {
                            label.setText(LanguageData.getInstance().getMsg("TriggerTypeRaid"));
                        }
                        setGraphic(label);
                    }
                }
            };
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return cell;
        });
        triggerTableColumnTriggerType.setMinWidth(70);
        triggerTableColumnTriggerType.setMaxWidth(70);
        triggerTableColumnTriggerType.setResizable(false);

        triggerTableColumnTriggerSound.setCellFactory(param -> {
            Button button = new Button();
            button.getStyleClass().add("TablePlayButton");
            Image playButtonNotPressed = new Image(getClass().getResourceAsStream("img/play.png"));
            Image playButtonPressed = new Image(getClass().getResourceAsStream("img/playpressed.png"));
            button.setGraphic(new ImageView(playButtonNotPressed));
            TableCell<Trigger, String> cell = new TableCell<Trigger, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                            button.setOnAction(event -> {
                                try{
//                                    JavaFX 10
//                                    getTableRow().getItem().debugTrigger();
                                    ((Trigger) getTableRow().getItem()).debugTrigger();
                                } catch (AudioException e){
                                    AlertDialogs.audioExceptionDialog(e.getExceptionType());
                                } catch (VoiceException e) {
                                    AlertDialogs.voiceExceptionDialog(e.getExceptionType());
                                }
                            });
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
//  TODO test if padding needed
            cell.setStyle("-fx-padding: 0 0 0 0;");
            return cell;
        });
        triggerTableColumnTriggerSound.setCellValueFactory(param -> param.getValue().getSoundData());
        triggerTableColumnTriggerSound.setMinWidth(50);
        triggerTableColumnTriggerSound.setMaxWidth(50);
        triggerTableColumnTriggerSound.setResizable(false);
        triggerTableColumnTriggerSound.setSortable(false);

        triggerTableColumnTriggerCategory.setCellValueFactory(param -> param.getValue().getTriggerCategory());

        triggerTableColumnTriggerDelay.setMinWidth(50);
        triggerTableColumnTriggerDelay.setMaxWidth(50);
        triggerTableColumnTriggerDelay.setResizable(false);
        triggerTableColumnTriggerDelay.setCellValueFactory(param -> param.getValue().getTriggerDelay());
    }

    public void exportButton() {
        Stage exportWindow = new Stage();
        exportWindow.initOwner(appWindow.getScene().getWindow());
        exportWindow.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fxml/exportTriggerWindow.fxml"));
        try {
            Parent root = fxmlLoader.load();
            exportWindow.setScene(new Scene(root));
        } catch (IOException e) {
//  TODO add alert pop up
           Logger.getInstance().log("Couldn't load \"fxml/exportTriggerWindow.fxml\" IOException");
        }
        exportWindow.showAndWait();
    }

    private void buttonStateSetup() {
        discordDisconnectButton.setDisable(true);
        discordSelectServerComboBox.setDisable(true);
        discordSelectChannelComboBox.setDisable(true);
        discordJoinChannelButton.setDisable(true);
        discordLeaveChannelButton.setDisable(true);
    }

    public void actSelectLogFile() {
        LogReader.getInstance().manuallySelectLogFile(appWindow.getScene().getWindow());
        logFileStatusLabelUpdate();
    }

    public void editTriggerButton() {
        if (triggerTableView.getSelectionModel().getSelectedItems().size() > 1) {
//  TODO move to AlertDialogs
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle(LanguageData.getInstance().getMsg("tableErrorTitle"));
            alert.setContentText(LanguageData.getInstance().getMsg("tableErrorToManySelected"));
            alert.showAndWait();
            return;
        }
        if (triggerTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            noItemSelectedError();
            return;
        }
        Stage addEditWindow = new Stage();
        addEditWindow.initOwner(appWindow.getScene().getWindow());
        addEditWindow.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fxml/addTriggerWindow.fxml"));
        try {
            Parent root = fxmlLoader.load();
            addEditWindow.setScene(new Scene(root));
        } catch (IOException e) {
//  TODO add alert pop up
            Logger.getInstance().log("Couldn't load \"fxml/addTriggerWindow.fxml\" IOException ");
        }
        AddTriggerController addTriggerController = fxmlLoader.getController();
        addTriggerController.editWindowSetup(triggerTableView.getSelectionModel().getSelectedItem());
        addEditWindow.showAndWait();
        reFiltering();
    }

    private void logFileStatusLabelUpdate() {
        if (LogReader.getInstance().getLogFilePath() != null) {
            actLogFileStatusLabel.setText(LanguageData.getInstance().getMsg("labelReadingLogFile") + LogReader.getInstance().getLogFilePath().getFileName());
            clearLogFileStatusStyle();
            actLogFileStatusLabel.getStyleClass().add("LogStatusLabelGreen");
        } else {
            actLogFileStatusLabel.setText(LanguageData.getInstance().getMsg("labelWaitingLogFile"));
            clearLogFileStatusStyle();
            actLogFileStatusLabel.getStyleClass().add("LogStatusLabelYellow");
        }
    }

    private void clearLogFileStatusStyle(){
        actLogFileStatusLabel.getStyleClass().remove("LogStatusLabelGreen");
        actLogFileStatusLabel.getStyleClass().remove("LogStatusLabelYellow");
    }


    public void showMainWindow() {
        if (!topBarMainButton.isSelected()) {
            topBarMainButton.setSelected(true);
        } else {
            hideWindows();
            mainWindow.setVisible(true);
        }
    }

    public void showTriggerWindow() {
        if (!topBarTriggerTableButton.isSelected()) {
            topBarTriggerTableButton.setSelected(true);
        } else {
            hideWindows();
            triggerWindow.setVisible(true);
        }
    }

    private void hideWindows() {
        mainWindow.setVisible(false);
        triggerWindow.setVisible(false);
    }

    public void actFindLogFile() {
        LogReader.getInstance().findLogFile();
        logFileStatusLabelUpdate();
    }

    private void westernFont() {
        discordConnectLabel.getStyleClass().add("WesternFont");
        discordConnectButton.getStyleClass().add("WesternFont");
        discordDisconnectButton.getStyleClass().add("WesternFont");
        discordSelectServerLabel.getStyleClass().add("WesternFont");
        discordSelectChannelLabel.getStyleClass().add("WesternFont");
        discordSelectServerComboBox.getStyleClass().add("WesternFont");
        discordSelectChannelComboBox.getStyleClass().add("WesternFont");
        discordJoinChannelButton.getStyleClass().add("WesternFont");
        discordLeaveChannelButton.getStyleClass().add("WesternFont");
        discordVolumeLabel.getStyleClass().add("WesternFont");
        muteDiscordCheckBox.getStyleClass().add("WesternFont");
        localVolumeLabel.getStyleClass().add("WesternFont");
        muteLocalCheckBox.getStyleClass().add("WesternFont");
        actFindLogFolderButton.getStyleClass().add("WesternFont");
        actSelectLogFolderButton.getStyleClass().add("WesternFont");
        activateTriggersTableButton.getStyleClass().add("WesternFont");
        deactivateTriggersTableButton.getStyleClass().add("WesternFont");
        addTriggerTableButton.getStyleClass().add("WesternFont");
        editTriggerTableButton.getStyleClass().add("WesternFont");
        deleteTriggerTableButton.getStyleClass().add("WesternFont");
        tableSortingTypeLabel.getStyleClass().add("WesternFont");
        tableSortingCategoryLabel.getStyleClass().add("WesternFont");
        discordVolumeLabelTable.getStyleClass().add("WesternFont");
        muteDiscordCheckBoxTable.getStyleClass().add("WesternFont");
        muteLocalCheckBoxTable.getStyleClass().add("WesternFont");
        localVolumeLabelTable.getStyleClass().add("WesternFont");
        dummyTField1.getStyleClass().add("WesternFont");
        dummyTField2.getStyleClass().add("WesternFont");
        dummyTField3.getStyleClass().add("WesternFont");
        dummyTField4.getStyleClass().add("WesternFont");
        actLogFileLabel.getStyleClass().add("WesternFont");
        actFindLogFileButton.getStyleClass().add("WesternFont");
        actSelectLogFileButton.getStyleClass().add("WesternFont");
        deleteLogFiles.getStyleClass().add("WesternFont");
        appLanguageLabel.getStyleClass().add("WesternFont");
        discordTokenLabel.getStyleClass().add("WesternFont");
        discordClientID.getStyleClass().add("WesternFont");
        discordAddBotButton.getStyleClass().add("WesternFont");
        voiceRRSToken.getStyleClass().add("WesternFont");
        tokenCheckbox.getStyleClass().add("WesternFont");
        logFolderCheckBox.getStyleClass().add("WesternFont");
        actLogFolderLabel.getStyleClass().add("WesternFont");
        ttsLanguageLabel.getStyleClass().add("WesternFont");
        testVoiceRRSButton.getStyleClass().add("WesternFont");
        importTriggersButton.getStyleClass().add("WesternFont");
        exportTriggersButton.getStyleClass().add("WesternFont");
    }

    private void japaneseFont() {
        discordConnectLabel.getStyleClass().add("JapaneseFont");
        discordConnectButton.getStyleClass().add("JapaneseFont");
        discordDisconnectButton.getStyleClass().add("JapaneseFont");
        discordSelectServerLabel.getStyleClass().add("JapaneseFont");
        discordSelectChannelLabel.getStyleClass().add("JapaneseFont");
        discordSelectServerComboBox.getStyleClass().add("JapaneseFont");
        discordSelectChannelComboBox.getStyleClass().add("JapaneseFont");
        discordJoinChannelButton.getStyleClass().add("JapaneseFont");
        discordLeaveChannelButton.getStyleClass().add("JapaneseFont");
        discordVolumeLabel.getStyleClass().add("JapaneseFont");
        muteDiscordCheckBox.getStyleClass().add("JapaneseFont");
        localVolumeLabel.getStyleClass().add("JapaneseFont");
        muteLocalCheckBox.getStyleClass().add("JapaneseFont");
        actFindLogFolderButton.getStyleClass().add("JapaneseFont");
        actSelectLogFolderButton.getStyleClass().add("JapaneseFont");
        activateTriggersTableButton.getStyleClass().add("JapaneseFont");
        deactivateTriggersTableButton.getStyleClass().add("JapaneseFont");
        addTriggerTableButton.getStyleClass().add("JapaneseFont");
        editTriggerTableButton.getStyleClass().add("JapaneseFont");
        deleteTriggerTableButton.getStyleClass().add("JapaneseFont");
        tableSortingTypeLabel.getStyleClass().add("JapaneseFont");
        tableSortingCategoryLabel.getStyleClass().add("JapaneseFont");
        discordVolumeLabelTable.getStyleClass().add("JapaneseFont");
        muteDiscordCheckBoxTable.getStyleClass().add("JapaneseFont");
        muteLocalCheckBoxTable.getStyleClass().add("JapaneseFont");
        localVolumeLabelTable.getStyleClass().add("JapaneseFont");
        triggerTableColumnTriggerName.getStyleClass().add("JapaneseFont");
        triggerTableColumnTriggerType.getStyleClass().add("JapaneseFont");
        triggerTableColumnTriggerCommand.getStyleClass().add("JapaneseFont");
        triggerTableColumnTriggerCategory.getStyleClass().add("JapaneseFont");
        triggerTableColumnTriggerSound.getStyleClass().add("JapaneseFont");
        triggerTableColumnTriggerDelay.getStyleClass().add("JapaneseFont");
        dummyTField1.getStyleClass().add("JapaneseFont");
        dummyTField2.getStyleClass().add("JapaneseFont");
        dummyTField3.getStyleClass().add("JapaneseFont");
        dummyTField4.getStyleClass().add("JapaneseFont");
        actLogFileLabel.getStyleClass().add("JapaneseFont");
        actFindLogFileButton.getStyleClass().add("JapaneseFont");
        actSelectLogFileButton.getStyleClass().add("JapaneseFont");
        deleteLogFiles.getStyleClass().add("JapaneseFont");
        appLanguageLabel.getStyleClass().add("JapaneseFont");
        discordTokenLabel.getStyleClass().add("JapaneseFont");
        discordClientID.getStyleClass().add("JapaneseFont");
        discordAddBotButton.getStyleClass().add("JapaneseFont");
        voiceRRSToken.getStyleClass().add("JapaneseFont");
        tokenCheckbox.getStyleClass().add("JapaneseFont");
        logFolderCheckBox.getStyleClass().add("JapaneseFont");
        actLogFolderLabel.getStyleClass().add("JapaneseFont");
        ttsLanguageLabel.getStyleClass().add("JapaneseFont");
        testVoiceRRSButton.getStyleClass().add("JapaneseFont");
        importTriggersButton.getStyleClass().add("JapaneseFont");
        exportTriggersButton.getStyleClass().add("JapaneseFont");
    }

    private void clearFont() {
        discordConnectLabel.getStyleClass().removeAll("JapaneseFont");
        discordConnectLabel.getStyleClass().remove("WesternFont");
        discordConnectButton.getStyleClass().remove("JapaneseFont");
        discordConnectButton.getStyleClass().remove("WesternFont");
        discordDisconnectButton.getStyleClass().remove("JapaneseFont");
        discordDisconnectButton.getStyleClass().remove("WesternFont");
        discordSelectServerLabel.getStyleClass().remove("JapaneseFont");
        discordSelectServerLabel.getStyleClass().remove("WesternFont");
        discordSelectChannelLabel.getStyleClass().remove("JapaneseFont");
        discordSelectChannelLabel.getStyleClass().remove("WesternFont");
        discordSelectServerComboBox.getStyleClass().remove("JapaneseFont");
        discordSelectServerComboBox.getStyleClass().remove("WesternFont");
        discordSelectChannelComboBox.getStyleClass().remove("JapaneseFont");
        discordSelectChannelComboBox.getStyleClass().remove("WesternFont");
        discordJoinChannelButton.getStyleClass().remove("JapaneseFont");
        discordJoinChannelButton.getStyleClass().remove("WesternFont");
        discordLeaveChannelButton.getStyleClass().remove("JapaneseFont");
        discordLeaveChannelButton.getStyleClass().remove("WesternFont");
        discordVolumeLabel.getStyleClass().remove("JapaneseFont");
        discordVolumeLabel.getStyleClass().remove("WesternFont");
        muteDiscordCheckBox.getStyleClass().remove("JapaneseFont");
        muteDiscordCheckBox.getStyleClass().remove("WesternFont");
        localVolumeLabel.getStyleClass().remove("JapaneseFont");
        localVolumeLabel.getStyleClass().remove("WesternFont");
        muteLocalCheckBox.getStyleClass().remove("JapaneseFont");
        muteLocalCheckBox.getStyleClass().remove("WesternFont");
        actFindLogFolderButton.getStyleClass().remove("JapaneseFont");
        actFindLogFolderButton.getStyleClass().remove("WesternFont");
        actSelectLogFolderButton.getStyleClass().remove("JapaneseFont");
        actSelectLogFolderButton.getStyleClass().remove("WesternFont");
        activateTriggersTableButton.getStyleClass().remove("JapaneseFont");
        activateTriggersTableButton.getStyleClass().remove("WesternFont");
        deactivateTriggersTableButton.getStyleClass().remove("JapaneseFont");
        deactivateTriggersTableButton.getStyleClass().remove("WesternFont");
        addTriggerTableButton.getStyleClass().remove("JapaneseFont");
        addTriggerTableButton.getStyleClass().remove("WesternFont");
        editTriggerTableButton.getStyleClass().remove("JapaneseFont");
        editTriggerTableButton.getStyleClass().remove("WesternFont");
        deleteTriggerTableButton.getStyleClass().remove("JapaneseFont");
        deleteTriggerTableButton.getStyleClass().remove("WesternFont");
        tableSortingTypeLabel.getStyleClass().remove("JapaneseFont");
        tableSortingTypeLabel.getStyleClass().remove("WesternFont");
        tableSortingCategoryLabel.getStyleClass().remove("JapaneseFont");
        tableSortingCategoryLabel.getStyleClass().remove("WesternFont");
        discordVolumeLabelTable.getStyleClass().remove("JapaneseFont");
        discordVolumeLabelTable.getStyleClass().remove("WesternFont");
        muteDiscordCheckBoxTable.getStyleClass().remove("JapaneseFont");
        muteDiscordCheckBoxTable.getStyleClass().remove("WesternFont");
        muteLocalCheckBoxTable.getStyleClass().remove("JapaneseFont");
        muteLocalCheckBoxTable.getStyleClass().remove("WesternFont");
        localVolumeLabelTable.getStyleClass().remove("JapaneseFont");
        localVolumeLabelTable.getStyleClass().remove("WesternFont");
        triggerTableColumnTriggerName.getStyleClass().remove("JapaneseFont");
        triggerTableColumnTriggerType.getStyleClass().remove("JapaneseFont");
        triggerTableColumnTriggerCommand.getStyleClass().remove("JapaneseFont");
        triggerTableColumnTriggerCategory.getStyleClass().remove("JapaneseFont");
        triggerTableColumnTriggerSound.getStyleClass().remove("JapaneseFont");
        triggerTableColumnTriggerDelay.getStyleClass().remove("JapaneseFont");
        dummyTField1.getStyleClass().remove("JapaneseFont");
        dummyTField1.getStyleClass().remove("WesternFont");
        dummyTField2.getStyleClass().remove("JapaneseFont");
        dummyTField2.getStyleClass().remove("WesternFont");
        dummyTField3.getStyleClass().remove("JapaneseFont");
        dummyTField3.getStyleClass().remove("WesternFont");
        dummyTField4.getStyleClass().remove("JapaneseFont");
        dummyTField4.getStyleClass().remove("WesternFont");
        actLogFileLabel.getStyleClass().remove("JapaneseFont");
        actLogFileLabel.getStyleClass().remove("WesternFont");
        actFindLogFileButton.getStyleClass().remove("JapaneseFont");
        actFindLogFileButton.getStyleClass().remove("WesternFont");
        actSelectLogFileButton.getStyleClass().remove("JapaneseFont");
        actSelectLogFileButton.getStyleClass().remove("WesternFont");
        deleteLogFiles.getStyleClass().remove("JapaneseFont");
        deleteLogFiles.getStyleClass().remove("WesternFont");
        appLanguageLabel.getStyleClass().remove("JapaneseFont");
        appLanguageLabel.getStyleClass().remove("WesternFont");
        discordTokenLabel.getStyleClass().remove("JapaneseFont");
        discordTokenLabel.getStyleClass().remove("WesternFont");
        discordClientID.getStyleClass().remove("JapaneseFont");
        discordClientID.getStyleClass().remove("WesternFont");
        discordAddBotButton.getStyleClass().remove("JapaneseFont");
        discordAddBotButton.getStyleClass().remove("WesternFont");
        voiceRRSToken.getStyleClass().remove("JapaneseFont");
        voiceRRSToken.getStyleClass().remove("WesternFont");
        tokenCheckbox.getStyleClass().remove("JapaneseFont");
        tokenCheckbox.getStyleClass().remove("WesternFont");
        logFolderCheckBox.getStyleClass().remove("JapaneseFont");
        logFolderCheckBox.getStyleClass().remove("WesternFont");
        actLogFolderLabel.getStyleClass().remove("JapaneseFont");
        actLogFolderLabel.getStyleClass().remove("WesternFont");
        ttsLanguageLabel.getStyleClass().remove("JapaneseFont");
        ttsLanguageLabel.getStyleClass().remove("WesternFont");
        testVoiceRRSButton.getStyleClass().remove("JapaneseFont");
        testVoiceRRSButton.getStyleClass().remove("WesternFont");
        importTriggersButton.getStyleClass().remove("JapaneseFont");
        importTriggersButton.getStyleClass().remove("WesternFont");
        exportTriggersButton.getStyleClass().remove("JapaneseFont");
        exportTriggersButton.getStyleClass().remove("WesternFont");
    }

    private void languageSetup() {
        reFiltering();
        clearFont();
        logFileStatusLabelUpdate();
        logFolderSize();
        if (Settings.getInstance().getLocale().equals(Language.Japanese)) {
            japaneseFont();
        } else {
            westernFont();
        }

        discordConnectLabel.setText(LanguageData.getInstance().getMsg("mainLabelConnectToDiscord"));
        discordConnectButton.setText(LanguageData.getInstance().getMsg("mainButtonDiscordConnect"));
        discordDisconnectButton.setText(LanguageData.getInstance().getMsg("mainButtonDiscordDisconnect"));
        discordSelectServerLabel.setText(LanguageData.getInstance().getMsg("mainLabelSelectServer"));
        discordSelectChannelLabel.setText(LanguageData.getInstance().getMsg("mainLabelSelectChannel"));
        discordSelectServerComboBox.setPromptText(LanguageData.getInstance().getMsg("mainDiscordComboBoxPrompt"));
        discordSelectChannelComboBox.setPromptText(LanguageData.getInstance().getMsg("mainDiscordComboBoxPrompt"));
        discordJoinChannelButton.setText(LanguageData.getInstance().getMsg("mainDiscordJoinChannel"));
        discordLeaveChannelButton.setText(LanguageData.getInstance().getMsg("mainDiscordLeaveChannel"));
        discordVolumeLabel.setText(LanguageData.getInstance().getMsg("mainDiscordVolume"));
        muteDiscordCheckBox.setText(LanguageData.getInstance().getMsg("muteCheckbox"));
        localVolumeLabel.setText(LanguageData.getInstance().getMsg("mainLocalVolume"));
        muteLocalCheckBox.setText(LanguageData.getInstance().getMsg("muteCheckbox"));
        actFindLogFolderButton.setText(LanguageData.getInstance().getMsg("findLogFolder"));
        actSelectLogFolderButton.setText(LanguageData.getInstance().getMsg("selectLogFolder"));
        activateTriggersTableButton.setText(LanguageData.getInstance().getMsg("tableActivateTriggers"));
        deactivateTriggersTableButton.setText(LanguageData.getInstance().getMsg("tableDeactivateTriggers"));
        addTriggerTableButton.setText(LanguageData.getInstance().getMsg("tableAddTriggerButton"));
        editTriggerTableButton.setText(LanguageData.getInstance().getMsg("tableEditTriggerButton"));
        deleteTriggerTableButton.setText(LanguageData.getInstance().getMsg("tableDeleteTriggerButton"));
        tableSortingTypeLabel.setText(LanguageData.getInstance().getMsg("tableTriggersTypeLabel"));
        tableSortingCategoryLabel.setText(LanguageData.getInstance().getMsg("tableTriggerCategoryLabel"));
        discordVolumeLabelTable.setText(LanguageData.getInstance().getMsg("mainDiscordVolume"));
        muteDiscordCheckBoxTable.setText(LanguageData.getInstance().getMsg("muteCheckbox"));
        muteLocalCheckBoxTable.setText(LanguageData.getInstance().getMsg("muteCheckbox"));
        localVolumeLabelTable.setText(LanguageData.getInstance().getMsg("mainLocalVolume"));
        triggerTableColumnTriggerName.setText(LanguageData.getInstance().getMsg("tableColumnName"));
        triggerTableColumnTriggerType.setText(LanguageData.getInstance().getMsg("tableColumnType"));
        triggerTableColumnTriggerCommand.setText(LanguageData.getInstance().getMsg("tableColumnCommand"));
        triggerTableColumnTriggerCategory.setText(LanguageData.getInstance().getMsg("tableColumnCategory"));
        triggerTableColumnTriggerSound.setText(LanguageData.getInstance().getMsg("tableColumnSound"));
        triggerTableColumnTriggerDelay.setText(LanguageData.getInstance().getMsg("tableColumnDelay"));
        dummyTField1.setText(LanguageData.getInstance().getMsg("mainHiddenTField"));
        dummyTField2.setText(LanguageData.getInstance().getMsg("mainHiddenTField"));
        dummyTField3.setText(LanguageData.getInstance().getMsg("mainHiddenTField"));
        dummyTField4.setText(LanguageData.getInstance().getMsg("mainHiddenTField"));
        actLogFileLabel.setText(LanguageData.getInstance().getMsg("mainLogFileLabel"));
        actFindLogFileButton.setText(LanguageData.getInstance().getMsg("mainFindLogFile"));
        actSelectLogFileButton.setText(LanguageData.getInstance().getMsg("mainSelectLogFile"));
        deleteLogFiles.setText(LanguageData.getInstance().getMsg("mainDeleteLogsButton"));
        appLanguageLabel.setText(LanguageData.getInstance().getMsg("mainAppLanguageLabel"));
        discordTokenLabel.setText(LanguageData.getInstance().getMsg("mainDiscordToken"));
        discordClientID.setText(LanguageData.getInstance().getMsg("mainDiscordClientID"));
        discordAddBotButton.setText(LanguageData.getInstance().getMsg("mainDiscordAddBot"));
        voiceRRSToken.setText(LanguageData.getInstance().getMsg("mainVoiceRRSTokenLabel"));
        tokenCheckbox.setText(LanguageData.getInstance().getMsg("mainShowTokens"));
        logFolderCheckBox.setText(LanguageData.getInstance().getMsg("mainShowLogFolder"));
        actLogFolderLabel.setText(LanguageData.getInstance().getMsg("mainLogFolder"));
        ttsLanguageLabel.setText(LanguageData.getInstance().getMsg("mainTTSLanguage"));
        testVoiceRRSButton.setText(LanguageData.getInstance().getMsg("buttonVoiceRRSTest"));
        importTriggersButton.setText(LanguageData.getInstance().getMsg("buttonImport"));
        exportTriggersButton.setText(LanguageData.getInstance().getMsg("buttonExport"));
    }

}
