package com.github.orthae.discordtriggerstuff.alerts;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.ReusedCode;
import com.github.orthae.discordtriggerstuff.Settings;
import com.github.orthae.discordtriggerstuff.enums.DialogButton;
import com.github.orthae.discordtriggerstuff.enums.Language;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class ErrorAlertDialog extends AlertDialog {


    public void initialize(){
        setIcon(getClass().getResourceAsStream("/com/github/orthae/discordtriggerstuff/img/delete_32.png"));
        languageSetup();
//        textLabel.setWrapText(true);
    }

    private void languageSetup(){
        if (Settings.getInstance().getLocale().equals(Language.Japanese)) {
            japaneseFont();
        } else {
            westernFont();
        }
        setAlertTitle(LanguageData.getInstance().getMsg("AlertError"));
    }

    private void japaneseFont(){

    }

    private void westernFont(){


    }


}
