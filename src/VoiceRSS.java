import com.voicerss.tts.*;

import java.io.File;
import java.io.FileOutputStream;
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

    //  Methods
    public void setAPIToken(String token) {
        voiceProvider.setApiKey(token);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void requestTTS(String stringToSpeech) {
        if (Paths.get("sounds/" + Settings.getInstance().getTtsLanguage() + "_" + stringToSpeech.toLowerCase().trim().replaceAll(" ", "_") + ".wav").toFile().exists()) {
            return;
        }
        voiceParameters.setText(stringToSpeech);
        voiceParameters.setLanguage(Settings.getInstance().getTtsLanguage());
        try {
            byte[] voice = voiceProvider.speech(voiceParameters);
            if (!Paths.get("sounds").toFile().exists()) {
                new File("sounds").mkdir();
            }
            FileOutputStream fos = new FileOutputStream("sounds/" + Settings.getInstance().getTtsLanguage() + "_" + stringToSpeech.toLowerCase().trim().replaceAll(" ", "_") + ".wav");
            fos.write(voice, 0, voice.length);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            System.out.println("Exception  " + e.getMessage());}
    }

    public boolean testTTS(){
        voiceParameters.setText("Test");
        voiceParameters.setLanguage(Settings.getInstance().getTtsLanguage());
        try {
            voiceProvider.speech(voiceParameters);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}






