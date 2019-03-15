package com.github.orthae.discordtriggerstuff.alerts;

import com.github.orthae.discordtriggerstuff.LanguageData;

public class ErrorAlertDialog extends AlertDialog {


    public void initialize(){
        super.initialize();
        setIcon(getClass().getResourceAsStream("/com/github/orthae/discordtriggerstuff/img/delete_32.png"));
        languageSetup();
//        textLabel.setWrapText(true);
    }

    private void languageSetup(){
        setAlertTitle(LanguageData.getInstance().getMsg("AlertError"));
        setButtonCancel(LanguageData.getInstance().getMsg("ButtonAddEditCancel"));

    }



}
