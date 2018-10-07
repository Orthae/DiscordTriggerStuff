import enums.SoundType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class TriggerData {
    //  Singleton
    private TriggerData() {
        loadTriggers();
    }

    public static TriggerData getInstance() {
        return TriggerDataHolder.INSTANCE;
    }

    public static class TriggerDataHolder {
        public static final TriggerData INSTANCE = new TriggerData();
    }

    //  Fields
    private ObservableList<Trigger> triggersArray = FXCollections.observableArrayList();
    private final Path TRIGGER_XML_FILE = FileSystems.getDefault().getPath("triggers.xml");

    //  Getters
    public ObservableList<Trigger> getTriggersArray() {
        return triggersArray;
    }

    //  Methods
    public void addTrigger(Trigger trigger) {
        triggersArray.add(trigger);
    }

    public void deleteTrigger(Trigger trigger) {
        triggersArray.remove(trigger);
    }

    private void loadTriggers() {
        try {
            File xmlTrigger = TRIGGER_XML_FILE.toFile();
            if (!xmlTrigger.exists()) {
                Logger.getInstance().log("XML Trigger file doesn't exist");
                return;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlTrigger);
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
                    String warningSoundPath = element.getElementsByTagName("TriggerSoundData").item(0).getTextContent();
                    SoundType soundType;
                    switch (element.getElementsByTagName("TriggerSoundType").item(0).getTextContent()) {
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
                    boolean isTriggerEnabled = Boolean.parseBoolean(element.getElementsByTagName("TriggerIsEnabled").item(0).getTextContent());
                    triggersArray.add(new Trigger(isPersonal, triggerCategory, triggerName, triggerCommand, triggerDelay, soundType, warningSoundPath, isTriggerEnabled));
                }
            }
        } catch (ParserConfigurationException | SAXException e) {
            AlertDialogs.settingsLoadingExceptionDialog();
            Logger.getInstance().log("XML error while reading trigger file");
        } catch (IOException e) {
            Logger.getInstance().log("IO Exception while loading triggers from file");
        }
    }

    public void saveTriggerData() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("TriggerData");
            doc.appendChild(rootElement);
            for (Trigger aTrigger : getTriggersArray()) {
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

                Element isTriggerEnabled = doc.createElement("TriggerIsEnabled");
                isTriggerEnabled.appendChild(doc.createTextNode(Boolean.toString(aTrigger.getEnabled().getValue())));
                trigger.appendChild(isTriggerEnabled);

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(TRIGGER_XML_FILE.toFile());
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            Logger.getInstance().log("Error while saving trigger file, this shouldn't happen");
        }
    }
}


