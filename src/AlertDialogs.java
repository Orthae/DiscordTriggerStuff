import enums.AudioExceptionType;
import enums.DiscordExceptionType;
import enums.Language;
import enums.VoiceExceptionType;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AlertDialogs {

    public static void errorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageData.getInstance().getMsg("AlertError"));
        alert.setHeaderText(null);
        alert.setContentText(msg);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alertStage.show();
    }

    public static void voiceExceptionDialog(VoiceExceptionType exceptionType){
        switch (exceptionType){
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

    public static void discordExceptionDialog(DiscordExceptionType exceptionType){
        switch (exceptionType){
            case UNAUTHORIZED:
                errorDialog(LanguageData.getInstance().getMsg("AlertDiscordLoginUnauthorized"));
                break;
        }
    }

    public static Alert deleteTriggersDialog(int size){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageData.getInstance().getMsg("AlertConfirmation"));
        alert.setHeaderText(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP1"));
            if (!Settings.getInstance().getLocale().equals(Language.Japanese)) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(size);
            if (!Settings.getInstance().getLocale().equals(Language.Japanese)) {
                stringBuilder.append(" ");
            }
            if (size == 1) {
                stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP3"));
            } else {
                stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP2"));
            }
            alert.setContentText(stringBuilder.toString());
        alert.showAndWait();
        return alert;
    }

    public static void addBotDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageData.getInstance().getMsg("AlertError"));
        alert.setHeaderText(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        Label label = new Label();
        label.setText(LanguageData.getInstance().getMsg("AlertDiscordCouldntAddBotBrowser"));
        TextField textField = new TextField();
        textField.setText("https://discordapp.com/oauth2/authorize?client_id=" + Settings.getInstance().getClientID() + "&scope=bot");
        textField.setEditable(false);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(label, 0, 0);
        gridPane.add(textField, 0, 1);
        alert.getDialogPane().contentProperty().setValue(gridPane);



        alertStage.show();

    }

}
