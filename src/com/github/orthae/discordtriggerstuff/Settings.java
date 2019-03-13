package com.github.orthae.discordtriggerstuff;

import com.github.orthae.discordtriggerstuff.alerts.AlertDialog;
import com.github.orthae.discordtriggerstuff.enums.Language;
import com.github.orthae.discordtriggerstuff.enums.SoundType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Settings {
    //  Singleton
    private Settings() {
        loadSettings();
    }

    public static Settings getInstance() {
        return SettingsHolder.INSTANCE;
    }

    public static class SettingsHolder {
        public static final Settings INSTANCE = new Settings();
    }

    //  Fields
    public final double MIN_WIDTH = 850.0;
    public final double MIN_HEIGHT = 450.0;
    private final Path OLD_SETTING_FILE = FileSystems.getDefault().getPath("settings.xml");
    private final Path OLD_TRIGGER_FILE = FileSystems.getDefault().getPath("triggers.xml");
    private final Path SETTING_FILE = FileSystems.getDefault().getPath("dts_config.xml");
    private final int SETTING_VERSION = 1;

    private double localPlayerVolume = 100;
    private boolean localPlayerMute = false;
    private double discordPlayerVolume = 100;
    private boolean discordPlayerMute = false;
    private String discordToken = "";
    private String voiceRRSToken = "";
    private String logFolder = "";
    private String ttsLanguage = "en-gb";
    private String clientID = "";
    private Language locale = Language.English;
    private ObservableList<Trigger> triggerList = FXCollections.observableArrayList();

    //  Getters
    public String getLogFolder() {
        return logFolder;
    }

    public double getLocalPlayerVolume() {
        return localPlayerVolume;
    }

    public boolean isLocalPlayerMute() {
        return localPlayerMute;
    }

    public boolean isDiscordPlayerMute() {
        return discordPlayerMute;
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public double getDiscordPlayerVolume() {
        return discordPlayerVolume;
    }

    public String getVoiceRRSToken() {
        return voiceRRSToken;
    }

    public Language getLocale() {
        return locale;
    }

    public String getTtsLanguage() {
        return ttsLanguage;
    }

    public String getClientID() {
        return clientID;
    }

    public synchronized ObservableList<Trigger> getTriggerList() {
            return triggerList;
    }

    //  Setters
    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }

    public void setLocalPlayerVolume(double localPlayerVolume) {
        this.localPlayerVolume = localPlayerVolume;
        SoundManager.getInstance().updateLocalVolume();
    }

    public void setLocalPlayerMute(boolean localPlayerMute) {
        this.localPlayerMute = localPlayerMute;
        SoundManager.getInstance().updateLocalVolume();
    }

    public void setDiscordPlayerVolume(double discordPlayerVolume) {
        this.discordPlayerVolume = discordPlayerVolume;
        SoundManager.getInstance().updateDiscordVolume();
    }

    public void setDiscordPlayerMute(boolean discordPlayerMute) {
        this.discordPlayerMute = discordPlayerMute;
        SoundManager.getInstance().updateDiscordVolume();
    }

    public void setDiscordToken(String discordToken) {
        this.discordToken = discordToken;
    }

    public void setVoiceRRSToken(String voiceRRSToken) {
        this.voiceRRSToken = voiceRRSToken;
    }

    public void setLocale(Language language) {
        this.locale = language;
    }

    public void setTtsLanguage(String languages) {
        this.ttsLanguage = languages;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }


//  Methods
//  Duplicates will happen due legacy setting support

    @SuppressWarnings("Duplicates")
    private void loadLegacySettingFile() {
        try {
            boolean error = false;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(OLD_SETTING_FILE.toFile());
            document.normalize();

            if (document.getElementsByTagName("LocalVolumeSlider").item(0) != null) {
                setLocalPlayerVolume(Double.parseDouble(document.getElementsByTagName("LocalVolumeSlider").item(0).getTextContent()));
            } else {
                Logger.getInstance().log("Could not find \"LocalVolumeSlider\" tag in XML setting file ");
                error = true;
            }
            if (document.getElementsByTagName("LocalVolumeMute").item(0) != null) {
                setLocalPlayerMute(Boolean.parseBoolean(document.getElementsByTagName("LocalVolumeMute").item(0).getTextContent()));
            } else {
                Logger.getInstance().log("Could not find \"LocalVolumeMute\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("DiscordVolumeSlider").item(0).getTextContent() != null) {
                setDiscordPlayerVolume(Double.parseDouble(document.getElementsByTagName("DiscordVolumeSlider").item(0).getTextContent()));
            } else {
                Logger.getInstance().log("Could not find \"DiscordVolumeSlider\" tag in XML setting file ");
                error = true;
            }
            if (document.getElementsByTagName("DiscordVolumeMute").item(0) != null) {
                setDiscordPlayerMute(Boolean.parseBoolean(document.getElementsByTagName("DiscordVolumeMute").item(0).getTextContent()));
            } else {
                Logger.getInstance().log("Could not find \"DiscordVolumeMute\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("DiscordToken").item(0) != null) {
                setDiscordToken(document.getElementsByTagName("DiscordToken").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"DiscordToken\" tag in XML setting file ");
                error = true;
            }
            if (document.getElementsByTagName("VoiceRRSToken").item(0) != null) {
                setVoiceRRSToken(document.getElementsByTagName("VoiceRRSToken").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"VoiceRRSToken\" tag in XML setting file ");
                error = true;
            }
            if (document.getElementsByTagName("LogFolder").item(0) != null) {
                setLogFolder(document.getElementsByTagName("LogFolder").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"LogFolder\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("Language").item(0) != null) {
                switch (document.getElementsByTagName("Language").item(0).getTextContent()) {
                    case "English":
                        setLocale(Language.English);
                        break;
                    case "French":
                        setLocale(Language.French);
                        break;
                    case "German":
                        setLocale(Language.German);
                        break;
                    case "Japanese":
                        setLocale(Language.Japanese);
                        break;
                }
            } else {
                Logger.getInstance().log("Could not find \"Language\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("TTSLanguage").item(0) != null) {
                setTtsLanguage(document.getElementsByTagName("TTSLanguage").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"TTSLanguage\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("DiscordClientID").item(0) != null) {
                setClientID(document.getElementsByTagName("DiscordClientID").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"DiscordClientID\" tag in XML setting file ");
                error = true;
            }
            if (error) {
                Logger.getInstance().log("Old setting file is corrupted, some setting might have been lost.");
                AlertDialog.createErrorDialog().setAlertMessage(LanguageData.getInstance().getMsg("AlertSettingsCorrupted")).show();
            }
        } catch (ParserConfigurationException | NullPointerException | SAXException e) {
                AlertDialog.createErrorDialog().setAlertMessage(LanguageData.getInstance().getMsg("AlertSettingsCorrupted")).show();
            Logger.getInstance().log(e.toString() + " thrown while reading setting file, file might be corrupted");
        } catch (IOException e) {
            AlertDialog.createErrorDialog().setAlertMessage(LanguageData.getInstance().getMsg("AlertIOException")).show();
            Logger.getInstance().log("IO Exception thrown while reading setting file");
        }
    }

    @SuppressWarnings("Duplicates")
    private void loadLegacyTriggerFile() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(OLD_TRIGGER_FILE.toFile());
            document.normalize();
            NodeList nList = document.getElementsByTagName("Trigger");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    try{
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
                        getTriggerList().add(new Trigger(isPersonal, triggerCategory, triggerName, triggerCommand, triggerDelay, soundType, warningSoundPath, isTriggerEnabled));
                    } catch (NullPointerException e){
                        Logger.getInstance().log("Corrupted trigger, skiping");
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException e) {
            AlertDialog.createErrorDialog().setAlertMessage(LanguageData.getInstance().getMsg("AlertSettingsCorrupted")).show();
            Logger.getInstance().log("XML error while reading legacy trigger file");
        } catch (IOException e) {
            Logger.getInstance().log("IO Exception while loading triggers from legacy file");
        }
    }

    @SuppressWarnings("Duplicates")
    private void loadSettings() {
        boolean error = false;
        try {
            if (!SETTING_FILE.toFile().exists()) {
                Logger.getInstance().log("Config file not found, attempting to find old setting file");
                if (OLD_SETTING_FILE.toFile().exists()) {
                    Logger.getInstance().log("Old config file found, loading");
                    loadLegacySettingFile();
                } else {
                    Logger.getInstance().log("Old config fine not found");
                }
                if (OLD_TRIGGER_FILE.toFile().exists()) {
                    Logger.getInstance().log("Old trigger file found, loading");
                    loadLegacyTriggerFile();
                } else {
                    Logger.getInstance().log("Old trigger file not found");
                }
                return;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(SETTING_FILE.toFile());
            document.normalize();

            if (document.getElementsByTagName("LocalVolumeSlider").item(0) != null) {
                setLocalPlayerVolume(Double.parseDouble(document.getElementsByTagName("LocalVolumeSlider").item(0).getTextContent()));
            } else {
                Logger.getInstance().log("Could not find \"LocalVolumeSlider\" tag in XML setting file ");
                error = true;
            }
            if (document.getElementsByTagName("LocalVolumeMute").item(0) != null) {
                setLocalPlayerMute(Boolean.parseBoolean(document.getElementsByTagName("LocalVolumeMute").item(0).getTextContent()));
            } else {
                Logger.getInstance().log("Could not find \"LocalVolumeMute\" tag in XML setting file ");
                   error = true;
            }

            if (document.getElementsByTagName("DiscordVolumeSlider").item(0).getTextContent() != null) {
                setDiscordPlayerVolume(Double.parseDouble(document.getElementsByTagName("DiscordVolumeSlider").item(0).getTextContent()));
            } else {
                Logger.getInstance().log("Could not find \"DiscordVolumeSlider\" tag in XML setting file ");
                error = true;
            }
            if (document.getElementsByTagName("DiscordVolumeMute").item(0) != null) {
                setDiscordPlayerMute(Boolean.parseBoolean(document.getElementsByTagName("DiscordVolumeMute").item(0).getTextContent()));
            } else {
                Logger.getInstance().log("Could not find \"DiscordVolumeMute\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("DiscordToken").item(0) != null) {
                setDiscordToken(document.getElementsByTagName("DiscordToken").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"DiscordToken\" tag in XML setting file ");
                error = true;
            }
            if (document.getElementsByTagName("VoiceRRSToken").item(0) != null) {
                setVoiceRRSToken(document.getElementsByTagName("VoiceRRSToken").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"VoiceRRSToken\" tag in XML setting file ");
                error = true;
            }
            if (document.getElementsByTagName("LogFolder").item(0) != null) {
                setLogFolder(document.getElementsByTagName("LogFolder").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"LogFolder\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("Language").item(0) != null) {
                switch (document.getElementsByTagName("Language").item(0).getTextContent()) {
                    case "English":
                        setLocale(Language.English);
                        break;
                    case "French":
                        setLocale(Language.French);
                        break;
                    case "German":
                        setLocale(Language.German);
                        break;
                    case "Japanese":
                        setLocale(Language.Japanese);
                        break;
                }
            } else {
                Logger.getInstance().log("Could not find \"Language\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("TTSLanguage").item(0) != null) {
                setTtsLanguage(document.getElementsByTagName("TTSLanguage").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"TTSLanguage\" tag in XML setting file ");
                error = true;
            }

            if (document.getElementsByTagName("DiscordClientID").item(0) != null) {
                setClientID(document.getElementsByTagName("DiscordClientID").item(0).getTextContent());
            } else {
                Logger.getInstance().log("Could not find \"DiscordClientID\" tag in XML setting file ");
                error = true;
            }

            // Trigger load
            NodeList nList = document.getElementsByTagName("Trigger");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    try{
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
                        getTriggerList().add(new Trigger(isPersonal, triggerCategory, triggerName, triggerCommand, triggerDelay, soundType, warningSoundPath, isTriggerEnabled));
                    } catch (NullPointerException e){
                        error = true;
                        Logger.getInstance().log("Trigger corrupted, skipping.");
                    }
                }
            }

            if (error) {
                AlertDialog.createErrorDialog().setAlertMessage(LanguageData.getInstance().getMsg("AlertSettingsCorrupted")).show();
            }
        } catch (ParserConfigurationException | NullPointerException | SAXException e) {
            AlertDialog.createErrorDialog().setAlertMessage(LanguageData.getInstance().getMsg("AlertSettingsCorrupted")).show();
            Logger.getInstance().log(e.toString() + " thrown while reading setting file, file might be corrupted");
        } catch (IOException e) {
            AlertDialog.createErrorDialog().setAlertMessage(LanguageData.getInstance().getMsg("AlertIOException")).show();
            Logger.getInstance().log("IO Exception thrown while reading setting file");
        }
    }


    public void saveSettings() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root node
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("DiscordTriggerStuff");
            doc.appendChild(rootElement);

            //  Settings elements
            Element settings = doc.createElement("Settings");
            rootElement.appendChild(settings);

            Element settingVersion = doc.createElement("SettingVersion");
            settingVersion.appendChild(doc.createTextNode(Integer.toString(SETTING_VERSION)));
            settings.appendChild(settingVersion);

            Element localVolume = doc.createElement("LocalVolumeSlider");
            localVolume.appendChild(doc.createTextNode(Double.toString(getLocalPlayerVolume())));
            settings.appendChild(localVolume);

            Element localVolumeMute = doc.createElement("LocalVolumeMute");
            localVolumeMute.appendChild(doc.createTextNode(Boolean.toString(isLocalPlayerMute())));
            settings.appendChild(localVolumeMute);

            Element discordVolume = doc.createElement("DiscordVolumeSlider");
            discordVolume.appendChild(doc.createTextNode(Double.toString(getDiscordPlayerVolume())));
            settings.appendChild(discordVolume);

            Element discordVolumeMute = doc.createElement("DiscordVolumeMute");
            discordVolumeMute.appendChild(doc.createTextNode(Boolean.toString(isDiscordPlayerMute())));
            settings.appendChild(discordVolumeMute);

            Element discordToken = doc.createElement("DiscordToken");
            discordToken.appendChild(doc.createTextNode(getDiscordToken()));
            settings.appendChild(discordToken);

            Element voiceRRSToken = doc.createElement("VoiceRRSToken");
            voiceRRSToken.appendChild(doc.createTextNode(getVoiceRRSToken()));
            settings.appendChild(voiceRRSToken);

            Element logFolder = doc.createElement("LogFolder");
            logFolder.appendChild(doc.createTextNode(getLogFolder()));
            settings.appendChild(logFolder);

            Element language = doc.createElement("Language");
            language.appendChild(doc.createTextNode(getLocale().toString()));
            settings.appendChild(language);

            Element ttsLanguage = doc.createElement("TTSLanguage");
            ttsLanguage.appendChild(doc.createTextNode(getTtsLanguage()));
            settings.appendChild(ttsLanguage);

            Element discordClientID = doc.createElement("DiscordClientID");
            discordClientID.appendChild(doc.createTextNode(getClientID()));
            settings.appendChild(discordClientID);

            // Triggers
            Element triggerList = doc.createElement("TriggerList");
            rootElement.appendChild(triggerList);

            for (Trigger aTrigger : getTriggerList()) {
                Element trigger = doc.createElement("Trigger");
                triggerList.appendChild(trigger);

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
            StreamResult result = new StreamResult(SETTING_FILE.toFile());
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            Logger.getInstance().log(e.toString() + " while saving settings file, setting might have been corrupted");
        }
    }

}
