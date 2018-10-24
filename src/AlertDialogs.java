import enums.AudioExceptionType;
import enums.VoiceExceptionType;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertDialogs {

    public static void errorDialogShow(String msg){
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
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceRequestCap"));
                break;
            case REQUEST_TO_LARGE:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceRequestToLarge"));
                break;
            case LANGUAGE_NOT_SUPPORTED:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceLanguageNotSupported"));
                break;
            case LANGUAGE_NOT_SPECIFIED:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceLanguageNotSpecified"));
                break;
            case TEXT_NOT_SPECIFIED:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceTextNotSpecified"));
                break;
            case API_KEY_NOT_AVAILABLE:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceKeyNotAvailable"));
                break;
            case API_KEY_NOT_DEFINED:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceKeyNotDefined"));
                break;
            case SSML_NOT_SUPPORTED:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceSSMLNotSupported"));
                break;
            case IO:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceIO"));
                break;
            case ILLEGAL_CHAR:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceIllegalChar"));
                break;
            case UNKNOWN:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceUnknown"));
                break;
            default:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertVoiceDefault"));
        }
    }

    public static void audioExceptionDialog(AudioExceptionType exceptionType) {
        switch (exceptionType) {
            case UNSUPPORTED_DISCORD_FILE:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertAudioUnsupported"));
                break;
            case DISCORD_NOT_CONNECTED:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertAudioDiscordNotConnected"));
                break;
            case UNSUPPORTED_AUDIO_FILE:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertAudioUnsupported"));
                break;
            case UNKNOWN:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertAudioUnknown"));
                break;
            case IO_DISCORD:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertIOException"));
                break;
            case UNSPECIFIED_AUDIO_FILE:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertAudioUnspecified"));
                break;
            case UNAVAILABLE_AUDIO_FILE:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertAudioUnavailable"));
                break;
            case CORRUPTED_AUDIO_FILE:
                errorDialogShow(LanguageData.getInstance().getMsg("AlertAudioCorrupted"));
                break;
        }
    }

}
