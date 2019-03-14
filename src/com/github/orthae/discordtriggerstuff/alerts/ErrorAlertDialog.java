package com.github.orthae.discordtriggerstuff.alerts;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.Settings;
import com.github.orthae.discordtriggerstuff.enums.Language;

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
        setButtonCancel(LanguageData.getInstance().getMsg("ButtonAddEditCancel"));

    }

    private void japaneseFont(){

    }

    private void westernFont(){


    }


}
