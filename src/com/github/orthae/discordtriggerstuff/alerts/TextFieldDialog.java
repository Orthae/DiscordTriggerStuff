package com.github.orthae.discordtriggerstuff.alerts;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.Settings;
import com.github.orthae.discordtriggerstuff.enums.Language;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class TextFieldDialog extends AlertDialog {
    @FXML
    private Button buttonCopyToClipboard;
    @FXML
    private TextField alertTextField;

    public void buttonCopyToClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(alertTextField.getText()), null);
    }

    public TextFieldDialog setAlertTextField(String text){
        alertTextField.setText(text);
        return this;
    }

    public void initialize(){
    super.initialize();
    languageSetup();
    setIcon(getClass().getResourceAsStream("/com/github/orthae/discordtriggerstuff/img/delete_32.png"));
    alertTextField.setEditable(false);
    }

    public void languageSetup(){
        if (Settings.getInstance().getLocale().equals(Language.Japanese)) {
            japaneseFont();
        } else {
            westernFont();
        }
        setAlertTitle(LanguageData.getInstance().getMsg("AlertError"));
        setButtonCancel(LanguageData.getInstance().getMsg("AlertButtonCancel"));
    }

    public void japaneseFont(){
    buttonCopyToClipboard.getStyleClass().add("JapaneseFont");
    }

    public void westernFont(){
        buttonCopyToClipboard.getStyleClass().add("WesternFont");
    }



}
