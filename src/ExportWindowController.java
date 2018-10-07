import enums.Language;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExportWindowController extends ImportExportBase {
    @FXML
    Label exportToLabel;

    private void languageSetup() {
        clearFont();
        if(Settings.getInstance().getLocale().equals(Language.Japanese)){
            japaneseFont();
        } else {
            westernFont();
        }
        getImportExportColumn().setText(LanguageData.getInstance().getMsg("columnExport"));
        getTopBarLabel().setText(LanguageData.getInstance().getMsg("labelExportTriggers"));
        exportToLabel.setText(LanguageData.getInstance().getMsg("labelExportTo"));
    }

    private void clearFont(){
        exportToLabel.getStyleClass().remove("JapaneseFont");
        exportToLabel.getStyleClass().remove("WesternFont");

    }

    private void japaneseFont(){
        exportToLabel.getStyleClass().add("JapaneseFont");
    }

    private void westernFont(){
        exportToLabel.getStyleClass().add("WesternFont");

    }

    public void initialize() {
        super.initialize();
        languageSetup();
        getTriggers();
    }

    public void exportACT() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(LanguageData.getInstance().getMsg("alertExport"));
        alert.setHeaderText(null);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.CANCEL){
           return;
        }
        System.out.println(alert.getResult());
        Path actConfig = Paths.get(System.getenv("APPDATA"), "Advanced Combat Tracker/Config/Advanced Combat Tracker.config.xml");
        try (BufferedReader br = new BufferedReader(new FileReader(actConfig.toFile()))) {
            StringBuilder stringBuilder = new StringBuilder();
            while (br.ready()) {
                String line = br.readLine();
                stringBuilder.append(line);
                if (line.trim().equals("<CustomTriggers>")) {
                    for (Trigger trigger : getImportExportTable().getItems()) {
                        if (trigger.getEnabled().getValue()) {
                            String regEx = trigger.getTriggerCommand().getValue();
                            String soundData = trigger.getSoundData().getValue();
                            String soundType;
                            switch (trigger.getSoundType()) {
                                case BEEP:
                                    soundType = "1";
                                    break;
                                case SOUND_FILE:
                                    soundType = "2";
                                    break;
                                case TTS:
                                    soundType = "3";
                                    break;
                                default:
                                    soundType = "0";
                                    break;
                            }
                            String category = trigger.getTriggerCategory().getValue();

                            String formattedTrigger = "<Trigger Active=\"True\" Regex=\"" + regEx + "\" SoundData=\"" + soundData + "\""
                                    + " enums.SoundType=\"" + soundType + "\"" + " CategoryRestrict=\"False\" Category=\"" + category + "\" TimerName=\"\" Tabbed=\"False\" />";
                            stringBuilder.append(formattedTrigger);
                        }
                    }
                }
            }
            br.close();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(actConfig.toFile()));
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportClipboard() {
        exportTriggers(false);
    }

    public void exportFile() {
        exportTriggers(true);
    }

    private void exportTriggers(boolean isFile) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("TriggerExport");
            doc.appendChild(rootElement);
            for (Trigger aTrigger : getImportExportTable().getItems()) {
                if (!aTrigger.getEnabled().getValue()) {
                    continue;
                }
                Element trigger = doc.createElement("Trigger");
                rootElement.appendChild(trigger);

                Element isPersonal = doc.createElement("TriggerIsPersonal");
                isPersonal.appendChild(doc.createTextNode(Boolean.toString(aTrigger.getPersonal().getValue())));
                trigger.appendChild(isPersonal);

                Element triggerCategory = doc.createElement("TriggerCategory");
                triggerCategory.appendChild(doc.createTextNode(aTrigger.getTriggerCategory().getValue()));
                trigger.appendChild(triggerCategory);

                Element triggerName = doc.createElement("TriggerName");
                triggerName.appendChild(doc.createTextNode(aTrigger.getTriggerName().getValue()));
                trigger.appendChild(triggerName);

                Element triggerCommand = doc.createElement("TriggerCommand");
                triggerCommand.appendChild(doc.createTextNode(aTrigger.getTriggerCommand().getValue()));
                trigger.appendChild(triggerCommand);

                Element triggerDelay = doc.createElement("TriggerDelay");
                triggerDelay.appendChild(doc.createTextNode(Integer.toString(aTrigger.getTriggerDelay().getValue())));
                trigger.appendChild(triggerDelay);

                Element soundType = doc.createElement("TriggerSoundType");
                soundType.appendChild(doc.createTextNode(aTrigger.getSoundType().toString()));
                trigger.appendChild(soundType);

                Element warningSoundPath = doc.createElement("TriggerSoundData");
                warningSoundPath.appendChild(doc.createTextNode(aTrigger.getSoundData().getValue()));
                trigger.appendChild(warningSoundPath);

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            if (isFile) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Trigger file", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    StreamResult result = new StreamResult(file);
                    transformer.transform(source, result);
                }
            } else {
                StringWriter stringWriter = new StringWriter();
                transformer.transform(source, new StreamResult(stringWriter));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(stringWriter.getBuffer().toString()), null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText(LanguageData.getInstance().getMsg("importCopiedToClipboard"));
                alert.show();
            }
        } catch (ParserConfigurationException | TransformerException e) {
            System.out.println("This should not happen");
        }
    }

    private void getTriggers() {
        for (Trigger trigger : TriggerData.getInstance().getTriggersArray()) {
            getImportExportTable().getItems().add(new Trigger(trigger));
        }
    }
}
