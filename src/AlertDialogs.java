import enums.AudioExceptionType;
import enums.VoiceExceptionType;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertDialogs {

    private static void errorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageData.getInstance().getMsg("AlertError"));
        alert.setHeaderText(null);
        alert.setContentText(msg);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alertStage.show();
    }

    public static void voiceExceptionDialog(VoiceExceptionType voiceExceptionType){
        switch (voiceExceptionType){
            case REQUEST_COUNT_EXCEEDED:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceRequestCap"));
                break;
            case REQUEST_TO_LARGE:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceRequestToLarge"));
                break;
            case LANGUAGE_NOT_SUPPORTED:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceLanguageNotSupported"));
                break;
            case LANGUAGE_NOT_SPECIFIED:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceLanguageNotSpecified"));
                break;
            case TEXT_NOT_SPECIFIED:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceTextNotSpecified"));
                break;
            case API_KEY_NOT_AVAILABLE:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceKeyNotAvailable"));
                break;
            case API_KEY_NOT_DEFINED:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceKeyNotDefined"));
                break;
            case SSML_NOT_SUPPORTED:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceSSMLNotSupported"));
                break;
            case IO:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceIO"));
                break;
            case ILLEGAL_CHAR:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceIllegalChar"));
                break;
            case UNKNOWN:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceUnknown"));
                break;
            default:
                errorDialog(LanguageData.getInstance().getMsg("AlertVoiceDefault"));
        }
    }

    public static void audioExceptionDialog(AudioExceptionType exceptionType) {
        switch (exceptionType) {
            case UNSUPPORTED_DISCORD_FILE:
                errorDialog(LanguageData.getInstance().getMsg("AlertAudioUnsupported"));
                break;
            case DISCORD_NOT_CONNECTED:
                errorDialog(LanguageData.getInstance().getMsg("AlertAudioDiscordNotConnected"));
                break;
            case UNSUPPORTED_AUDIO_FILE:
                errorDialog(LanguageData.getInstance().getMsg("AlertAudioUnsupported"));
                break;
            case UNKNOWN:
                errorDialog(LanguageData.getInstance().getMsg("AlertAudioUnknown"));
                break;
            case IO_DISCORD:
                errorDialog(LanguageData.getInstance().getMsg("AlertIOException"));
                break;
            case UNSPECIFIED_AUDIO_FILE:
                errorDialog(LanguageData.getInstance().getMsg("AlertAudioUnspecified"));
                break;
            case UNAVAILABLE_AUDIO_FILE:
                errorDialog(LanguageData.getInstance().getMsg("AlertAudioUnavailable"));
                break;
            case CORRUPTED_AUDIO_FILE:
                errorDialog(LanguageData.getInstance().getMsg("AlertAudioCorrupted"));
                break;
        }
    }

    public static void settingsLoadingExceptionDialog(){
        errorDialog(LanguageData.getInstance().getMsg("AlertSettingsCorrupted"));
    }

    public static void ioExceptionDialog(){
        errorDialog(LanguageData.getInstance().getMsg("AlertIOException"));
    }

    public static void exportExceptionDialog(){
        errorDialog(LanguageData.getInstance().getMsg("AlertExportFailed"));
    }
}
