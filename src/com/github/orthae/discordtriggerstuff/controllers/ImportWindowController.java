package com.github.orthae.discordtriggerstuff.controllers;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.Logger;
import com.github.orthae.discordtriggerstuff.Settings;
import com.github.orthae.discordtriggerstuff.Trigger;
import com.github.orthae.discordtriggerstuff.alerts.AlertDialog;
import com.github.orthae.discordtriggerstuff.enums.Language;
import com.github.orthae.discordtriggerstuff.enums.SoundType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ImportWindowController extends ImportExportBase {
    @FXML
    private Button importButton;
    @FXML
    private Label getTriggersLabel;

    public void initialize() {
        super.initialize();
        languageSetup();
    }

    private void languageSetup(){
        clearFont();
        if(Settings.getInstance().getLocale().equals(Language.Japanese)){
            japaneseFont();
        } else {
            westernFont();
        }
        getTriggersLabel.setText(LanguageData.getInstance().getMsg("labelGetTriggersFrom"));
        importButton.setText(LanguageData.getInstance().getMsg("importButton"));
        getImportExportColumn().setText(LanguageData.getInstance().getMsg("importButton"));
        getTopBarLabel().setText(LanguageData.getInstance().getMsg("importTriggersLabel"));
    }

    private void clearFont(){
        importButton.getStyleClass().remove("JapaneseFont");
        importButton.getStyleClass().remove("WesternFont");

    }

    private void japaneseFont(){
        importButton.getStyleClass().add("JapaneseFont");
    }

    private void westernFont(){
        importButton.getStyleClass().add("WesternFont");
    }

    public void importTriggers() {
        for (Trigger importedTrigger : getImportExportTable().getItems()) {
            if (!importedTrigger.getEnabled().getValue()) {
                continue;
            }
            Settings.getInstance().getTriggerList().add(importedTrigger);
        }
        importButton.getScene().getWindow().hide();
    }

    public void getTriggersACT() {
        try {
            Path actConfig = Paths.get(System.getenv("APPDATA"), "Advanced Combat Tracker/Config/Advanced Combat Tracker.config.xml");
            File actFile = new File(actConfig.toUri());
            if (!actFile.exists()) {
                AlertDialog.createErrorDialog(getOwner()).setAlertMessage(LanguageData.getInstance().getMsg("AlertImportExportACTNotFound")).getAlertStage().show();
                Logger.getInstance().log("ACT config file not found");
                return;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(actFile);
            document.normalize();
            NodeList nList = document.getElementsByTagName("Trigger");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    String triggerCommand = element.getAttribute("Regex");
                    String triggerName = LanguageData.getInstance().getMsg("importTriggerName");
                    String triggerCategory = element.getAttribute("Category");
                    int triggerDelay = 0;
                    SoundType soundType;
                    switch (Integer.parseInt(element.getAttribute("SoundType"))) {
                        case 0:
                        case 1:
                            soundType = SoundType.BEEP;
                            break;
                        case 2:
                            soundType = SoundType.SOUND_FILE;
                            break;
                        case 3:
                            soundType = SoundType.TTS;
                            break;
                        default:
                            soundType = SoundType.BEEP;
                            break;
                    }
                    String soundData = element.getAttribute("SoundData");
                    getImportExportTable().getItems().add(new Trigger(true, triggerCategory, triggerName, triggerCommand, triggerDelay, soundType, soundData, true));
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            getImportExportTable().getItems().clear();
            Logger.getInstance().log("Exception thrown while reading ACT config file");
            Logger.getInstance().log(e.getMessage());
            AlertDialog.createErrorDialog(getOwner()).setAlertMessage(LanguageData.getInstance().getMsg("AlertImportErrorWhileReading")).showAndWait();
        }
        if(getImportExportTable().getItems().isEmpty()){
            Logger.getInstance().log("Could not import triggers, import table is empty");
            AlertDialog.createErrorDialog(getOwner()).setAlertMessage(LanguageData.getInstance().getMsg("AlertImportTriggersNotFound")).showAndWait();
        }
    }

    public void getTriggersFile(){
        getTriggers(true);
    }

    public void getTriggersClipboard(){
        getTriggers(false);
    }

    private void getTriggers(boolean isFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document;
            if(isFile){
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XML trigger file", "*.xml");
                fileChooser.getExtensionFilters().add(filter);
                document = dBuilder.parse(fileChooser.showOpenDialog(null));
            } else {
                String clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                document = dBuilder.parse(new InputSource(new StringReader(clipboard)));
            }
            document.normalize();
            NodeList nList = document.getElementsByTagName("Trigger");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    boolean isPersonal = Boolean.parseBoolean(element.getElementsByTagName("TriggerIsPersonal").item(0).getTextContent());
                    String triggerCategory = element.getElementsByTagName("TriggerCategory").item(0).getTextContent();
                    String triggerName = element.getElementsByTagName("TriggerName").item(0).getTextContent();
                    String triggerCommand = element.getElementsByTagName("TriggerCommand").item(0).getTextContent();
                    int triggerDelay = Integer.parseInt(element.getElementsByTagName("TriggerDelay").item(0).getTextContent());
                    String soundData = element.getElementsByTagName("TriggerSoundData").item(0).getTextContent();
                    SoundType soundType;
                    switch (element.getElementsByTagName("TriggerSoundType").item(0).getTextContent()){
                        case "BEEP":
                            soundType = SoundType.BEEP;
                            break;
                        case "TTS":
                            soundType = SoundType.TTS;
                            break;
                        case "SOUND_FILE":
                            soundType = SoundType.SOUND_FILE;
                            break;
                        default:
                            soundType = SoundType.BEEP;
                            break;
                    }
                    getImportExportTable().getItems().add(new Trigger(isPersonal, triggerCategory, triggerName, triggerCommand, triggerDelay, soundType, soundData, true));
                }
            }
        }
        catch(ParserConfigurationException | IOException | SAXException | UnsupportedFlavorException e){
            getImportExportTable().getItems().clear();
            AlertDialog.createErrorDialog(getOwner()).setAlertMessage(LanguageData.getInstance().getMsg("AlertImportTriggersNotFound")).showAndWait();
            Logger.getInstance().log(e.getMessage() + " while importing triggers");
        }
        if (getImportExportTable().getItems().isEmpty()) {
            AlertDialog.createErrorDialog(getOwner()).setAlertMessage(LanguageData.getInstance().getMsg("AlertImportTriggersNotFound")).showAndWait();
            Logger.getInstance().log("Could not import triggers, import table is empty");
        }
    }


}
