import enums.Language;
import javafx.fxml.FXML;

public class AddTriggerController extends AddEditBase {

    public void initialize() {
        super.initialize();
        getRadioButtonBeep().selectedProperty().set(true);
        languageSetup();
    }

    @FXML
    private void addEditTriggerButton() {
        Trigger newTrigger = getFields();
        if(newTrigger !=null){
            Settings.getInstance().getTriggerList().add(newTrigger);
            getTopBarLabel().getScene().getWindow().hide();
        }
    }

    private void languageSetup(){
        if(Settings.getInstance().getLocale() != Language.Japanese){
            westernFont();
        } else {
            japaneseFont();
        }
        getTopBarLabel().setText(LanguageData.getInstance().getMsg("LabelAddTriggerHeader"));
        getCreateEditButton().setText(LanguageData.getInstance().getMsg("ButtonAddTriggerCreate"));
    }

    private void westernFont(){

    }

    private void japaneseFont(){

    }

}
