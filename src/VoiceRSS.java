import com.voicerss.tts.*;
import exceptions.VoiceException;
import enums.VoiceExceptionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VoiceRSS {
    //  Singleton
    private VoiceRSS() {
        this.voiceProvider = new VoiceProvider(Settings.getInstance().getVoiceRRSToken());
        this.voiceParameters = new VoiceParameters(null, null);
        voiceParameters.setCodec(AudioCodec.WAV);
        voiceParameters.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
        voiceParameters.setBase64(false);
        voiceParameters.setSSML(false);
        voiceParameters.setRate(0);
    }

    public static VoiceRSS getInstance() {
        return VoiceRSSHolder.INSTANCE;
    }

    public static class VoiceRSSHolder {
        public static final VoiceRSS INSTANCE = new VoiceRSS();
    }

    //  Fields
    private VoiceProvider voiceProvider;
    private VoiceParameters voiceParameters;

    //  Getters
    public VoiceProvider getVoiceProvider() {
        return voiceProvider;
    }

    public VoiceParameters getVoiceParameters() {
        return voiceParameters;
    }

    //  Methods
    public void setAPIToken(String token) {
        getVoiceProvider().setApiKey(token);
    }

    public void requestTTS(String textToSpeech) {
        try {
            saveSpeechToFile(textToSpeech);
        } catch (VoiceException e) {
            Logger.getInstance().log("VoiceException thrown in requestTTS(String) method " + e.getExceptionType());
        }
    }

    public void debugTTS(String textToSpeech) throws VoiceException {
        saveSpeechToFile(textToSpeech);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveSpeechToFile(String textToSpeech) throws VoiceException {
        Path filePath;
        try {
            filePath = Paths.get(ReusedCode.getFormattedFilePath(textToSpeech));
        } catch (InvalidPathException e) {
            throw new VoiceException(VoiceExceptionType.ILLEGAL_CHAR);
        }
        if (filePath.toFile().exists()) {
            return;
        }
        byte[] voice = getTextToSpeech(textToSpeech);
        if (!Paths.get("sounds").toFile().exists()) {
            new File("sounds").mkdir();
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filePath.toFile());
            fos.write(voice, 0, voice.length);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Logger.getInstance().log("FileNotFoundException while saving TTS file this should not happen");
        } catch (IOException e) {
            throw new VoiceException(VoiceExceptionType.IO);
        }
    }

    public byte[] getTextToSpeech(String textToSpeech) throws VoiceException {
        getVoiceParameters().setText(textToSpeech);
        getVoiceParameters().setLanguage(Settings.getInstance().getTtsLanguage());
        byte[] voice;
        try {
            voice = getVoiceProvider().speech(getVoiceParameters());
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.contains("The subscription is expired or requests count limitation is exceeded!")) {
                throw new VoiceException(VoiceExceptionType.REQUEST_COUNT_EXCEEDED);
            }
            if (msg.contains("The request content length is too large!")) {
                throw new VoiceException(VoiceExceptionType.REQUEST_TO_LARGE);
            }
            if (msg.contains("The language does not support!")) {
                throw new VoiceException(VoiceExceptionType.LANGUAGE_NOT_SUPPORTED);
            }
            if (msg.contains("The language is not specified!")) {
                throw new VoiceException(VoiceExceptionType.LANGUAGE_NOT_SPECIFIED);
            }
            if (msg.contains("The text is not specified!")) {
                throw new VoiceException(VoiceExceptionType.TEXT_NOT_SPECIFIED);
            }
            if (msg.contains("The API key is not available!")) {
                throw new VoiceException(VoiceExceptionType.API_KEY_NOT_AVAILABLE);
            }
            if (msg.contains("The API key is undefined")) {
                throw new VoiceException(VoiceExceptionType.API_KEY_NOT_DEFINED);
            }
            if (msg.contains("The subscription does not support SSML!")) {
                throw new VoiceException(VoiceExceptionType.SSML_NOT_SUPPORTED);
            } else {
                throw new VoiceException(VoiceExceptionType.UNKNOWN);
            }
        }
        return voice;
    }
}






