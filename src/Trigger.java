import enums.SoundType;
import exceptions.AudioException;
import exceptions.VoiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.nio.file.Paths;

public class Trigger {
    //  Constructors
    public Trigger(boolean personal, String category, String name, String command, int delay, SoundType soundType, String soundPath) {
        this(personal, category, name, command, delay, soundType, soundPath, true);
    }

    // XML load constructor
    public Trigger(boolean personal, String category, String name, String command, int delay, SoundType soundType, String soundPath, boolean isEnabled) {
        this.personal = new SimpleBooleanProperty(personal);
        this.triggerCategory = new SimpleStringProperty(category);
        this.triggerName = new SimpleStringProperty(name);
        this.triggerCommand = new SimpleStringProperty(command);
        this.triggerDelay = new SimpleIntegerProperty(delay);
        this.soundType = soundType;
        this.soundData = new SimpleStringProperty(soundPath);
        this.enabled = new SimpleBooleanProperty(isEnabled);
    }

    // Copy constructor
    public Trigger(Trigger trigger) {
        this.personal = new SimpleBooleanProperty(trigger.getPersonal().getValue());
        this.triggerCategory = new SimpleStringProperty(trigger.getTriggerCategory().getValue());
        this.triggerName = new SimpleStringProperty(trigger.getTriggerName().getValue());
        this.triggerCommand = new SimpleStringProperty(trigger.getTriggerCommand().getValue());
        this.triggerDelay = new SimpleIntegerProperty(trigger.getTriggerDelay().getValue());
        this.soundType = trigger.getSoundType();
        this.soundData = new SimpleStringProperty(trigger.getSoundData().getValue());
        this.enabled = new SimpleBooleanProperty(trigger.getEnabled().getValue());
    }

    //  Fields
    private SimpleBooleanProperty personal;
    private SimpleStringProperty triggerCategory;
    private SimpleStringProperty triggerName;
    private SimpleStringProperty triggerCommand;
    private SimpleIntegerProperty triggerDelay;
    private SoundType soundType;
    private SimpleStringProperty soundData;
    private SimpleBooleanProperty enabled;

    //  Getters
    public SimpleBooleanProperty getPersonal() {
        return personal;
    }

    public SimpleStringProperty getTriggerCategory() {
        return triggerCategory;
    }

    public SimpleStringProperty getTriggerName() {
        return triggerName;
    }

    public SimpleStringProperty getTriggerCommand() {
        return triggerCommand;
    }

    public SimpleIntegerProperty getTriggerDelay() {
        return triggerDelay;
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public SimpleStringProperty getSoundData() {
        return soundData;
    }

    public SimpleBooleanProperty getEnabled() {
        return enabled;
    }

    //  Setters
    public void setPersonal(boolean personal) {
        this.personal.setValue(personal);
    }

    public void setTriggerCategory(String category) {
        triggerCategory.setValue(category);
    }

    public void setTriggerName(String name) {
        triggerName.set(name);
    }

    public void setTriggerCommand(String command) {
        triggerCommand.set(command);
    }

    public void setTriggerDelay(int delay) {
        triggerDelay.set(delay);
    }

    public void setSoundType(SoundType soundType) {
        this.soundType = soundType;
    }

    public void setSoundData(String path) {
        soundData.set(path);
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    //  Methods
    public void runTrigger() {
        boolean isPersonal = getPersonal().getValue();
        String soundData = getSoundData().getValue();
        switch (getSoundType()) {
            case BEEP:
                SoundManager.getInstance().play(isPersonal, SoundManager.getInstance().DEFAULT_AUDIO_FILE);
                break;
            case SOUND_FILE:
                SoundManager.getInstance().play(isPersonal, Paths.get(soundData));
                break;
            case TTS:
                VoiceRSS.getInstance().requestTTS(soundData);
                SoundManager.getInstance().play(isPersonal, Paths.get(ReusedCode.getFormattedFilePath(soundData)));
        }
    }

    public void debugTrigger() throws AudioException, VoiceException {
        boolean isPersonal = getPersonal().getValue();
        String soundData = getSoundData().getValue();
        switch (getSoundType()) {
            case BEEP:
                SoundManager.getInstance().playDebug(isPersonal, SoundManager.getInstance().DEFAULT_AUDIO_FILE);
                break;
            case SOUND_FILE:
                SoundManager.getInstance().playDebug(isPersonal, Paths.get(soundData));
                break;
            case TTS:
                VoiceRSS.getInstance().debugTTS(soundData);
                SoundManager.getInstance().playDebug(isPersonal, Paths.get(ReusedCode.getFormattedFilePath(soundData)));
        }
    }
}

