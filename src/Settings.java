import enums.Language;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

public class Settings {
//  Singleton
    private Settings(){
        loadSettings();
    }

    public static Settings getInstance(){
    return SettingsHolder.INSTANCE;
    }

    public static class SettingsHolder {
        public static final Settings INSTANCE = new Settings();
    }

//  Fields
    public final double MIN_WIDTH = 850.0;
    public final double MIN_HEIGHT = 450.0;
    private final Path SETTING_FILE = FileSystems.getDefault().getPath("settings.xml");
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

    //  Getters
    public String getLogFolder(){
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

    public String getDiscordToken(){
        return discordToken;
    }

    public double getDiscordPlayerVolume() {
        return discordPlayerVolume;
    }

    public String getVoiceRRSToken(){
        return voiceRRSToken;
    }

    public Language getLocale(){
        return locale;
    }

    public String getTtsLanguage(){
        return ttsLanguage;
    }

    public String getClientID(){
        return clientID;
    }
//  Setters

    public void setLogFolder(String logFolder){
        this.logFolder = logFolder;
    }

    public void setLocalPlayerVolume(double localPlayerVolume) {
        this.localPlayerVolume = localPlayerVolume;
    }

    public void setLocalPlayerMute(boolean localPlayerMute) {
        this.localPlayerMute = localPlayerMute;
    }

    public void setDiscordPlayerVolume(double discordPlayerVolume) {
        this.discordPlayerVolume = discordPlayerVolume;
    }

    public void setDiscordPlayerMute(boolean discordPlayerMute) {
        this.discordPlayerMute = discordPlayerMute;
    }

    public void setDiscordToken(String discordToken){
        this.discordToken = discordToken;
    }

    public void setVoiceRRSToken(String voiceRRSToken){
        this.voiceRRSToken = voiceRRSToken;
    }

    public void setLocale(Language language){
        this.locale = language;
    }

    public void setTtsLanguage(String languages){
        this.ttsLanguage = languages;
    }

    public void setClientID(String clientID){
        this.clientID = clientID;
    }

//  Methods
    private void loadSettings(){
        try {
            File settingFile = SETTING_FILE.toFile();
            if (!settingFile.exists()) {
                Logger.getInstance().log("Setting file doesn't exist");
                return;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(settingFile);
            document.normalize();
            setLocalPlayerVolume(Double.parseDouble(document.getElementsByTagName("LocalVolumeSlider").item(0).getTextContent()));
            setLocalPlayerMute(Boolean.parseBoolean(document.getElementsByTagName("LocalVolumeMute").item(0).getTextContent()));
            setDiscordPlayerVolume(Double.parseDouble(document.getElementsByTagName("DiscordVolumeSlider").item(0).getTextContent()));
            setDiscordPlayerMute(Boolean.parseBoolean(document.getElementsByTagName("DiscordVolumeMute").item(0).getTextContent()));
            setDiscordToken(document.getElementsByTagName("DiscordToken").item(0).getTextContent());
            setVoiceRRSToken(document.getElementsByTagName("VoiceRRSToken").item(0).getTextContent());
            setLogFolder(document.getElementsByTagName("LogFolder").item(0).getTextContent());
            switch (document.getElementsByTagName("Language").item(0).getTextContent()){
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
            setTtsLanguage(document.getElementsByTagName("TTSLanguage").item(0).getTextContent());
            setClientID(document.getElementsByTagName("DiscordClientID").item(0).getTextContent());
        } catch (ParserConfigurationException | NullPointerException | SAXException e) {
        AlertDialogs.settingsLoadingExceptionDialog();
        Logger.getInstance().log(e.toString() + " thrown while reading setting file, file might be corrupted");
        } catch (IOException e) {
        AlertDialogs.ioExceptionDialog();
        Logger.getInstance().log("IO Exception thrown while reading setting file");
        }
    }

    public void saveSettings(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("AppSettings");
            doc.appendChild(rootElement);

            //  Elements
            Element settings = doc.createElement("Settings");
            rootElement.appendChild(settings);

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
