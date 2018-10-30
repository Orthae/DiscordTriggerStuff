import javafx.fxml.FXML;

public class EditTriggerController extends AddEditBase {

    private Trigger editedTrigger;

    public void initialize() {
        super.initialize();
    }

    private Trigger getEditedTrigger(){
        return editedTrigger;
    }

    private void setEditedTrigger(Trigger editedTrigger) {
        this.editedTrigger = editedTrigger;
    }

    @FXML
    private void addEditTriggerButton(){
        Trigger trigger = getFields();
        if(trigger != null){
        getEditedTrigger().setTriggerName(trigger.getTriggerName().getValue());
        getEditedTrigger().setPersonal(trigger.getPersonal().getValue());
        getEditedTrigger().setTriggerCategory(trigger.getTriggerCategory().getValue());
        getEditedTrigger().setTriggerCommand(trigger.getTriggerCommand().getValue());
        getEditedTrigger().setTriggerDelay(trigger.getTriggerDelay().getValue());
        getEditedTrigger().setSoundType(trigger.getSoundType());
        getEditedTrigger().setSoundData(trigger.getSoundData().getValue());
        getTopLabelPane().getScene().getWindow().hide();
        }
    }

    public void editSetup(Trigger trigger){
            setEditedTrigger(trigger);
            getTriggerNameTextField().setText(trigger.getTriggerName().getValue());
            if(trigger.getPersonal().getValue()){
                getTriggerTypeComboBox().getSelectionModel().select(0);
            } else {
                getTriggerTypeComboBox().getSelectionModel().select(1);
            }
            getTriggerCategoryComboBox().getSelectionModel().select(trigger.getTriggerCategory().getValue());
            getTriggerCommandTextField().setText(trigger.getTriggerCommand().getValue());
            getTriggerDelaySpinner().getEditor().setText(trigger.getTriggerDelay().getValue().toString());
            switch (trigger.getSoundType()){
                case BEEP:
                    getRadioButtonBeep().fire();
                    break;
                case SOUND_FILE:;
                getRadioButtonSoundFile().fire();
                    break;
                case TTS:
                    getRadioButtonTTS().fire();
                    break;
            }
            getTriggerSoundDataTField().setText(trigger.getSoundData().getValue());
    }
}