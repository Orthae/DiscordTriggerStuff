package com.github.orthae.discordtriggerstuff.alerts;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.Settings;
import com.github.orthae.discordtriggerstuff.enums.DialogButton;
import com.github.orthae.discordtriggerstuff.enums.Language;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ConfirmAlertDialog extends AlertDialog {

    @FXML
    private Button buttonConfirm;

    public void initialize(){
        super.initialize();
        setIcon(getClass().getResourceAsStream("/com/github/orthae/discordtriggerstuff/img/info_32.png"));
        languageSetup();
    }

    private void languageSetup(){
        if (Settings.getInstance().getLocale().equals(Language.Japanese)) {
            japaneseFont();
        } else {
            westernFont();
        }
        buttonConfirm.setText(LanguageData.getInstance().getMsg("AlertButtonConfirm"));
        setButtonCancel(LanguageData.getInstance().getMsg("AlertButtonCancel"));
        setAlertTitle(LanguageData.getInstance().getMsg("AlertConfirmation"));
    }

    private void japaneseFont(){
        buttonConfirm.getStyleClass().add("JapaneseFont");

    }

    private void westernFont(){
        buttonConfirm.getStyleClass().add("WesternFont");
    }

    public void buttonAccepted(){
        setDialogButton(DialogButton.CONFIRM);
        buttonConfirm.getScene().getWindow().hide();
    }


}
