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

public class ErrorDialogController extends AlertDialogs {
    @FXML
    private BorderPane topLabelPane;
    @FXML
    private Label topBarLabel;
    @FXML
    private Label textLabel;
    @FXML
    private Button buttonConfirm;
    @FXML
    private Button buttonCancel;
    @FXML
    private ImageView imageViewIcon;
    private DialogButton dialogButton = null;



    private void iconSetup(){
        Image image = new Image(getClass().getResourceAsStream("/com/github/orthae/discordtriggerstuff/img/info_32.png"));
        imageViewIcon.setImage(image);
    }

    public void initialize(){
        ReusedCode.enableWindowMoving(topLabelPane);
        iconSetup();
        languageSetup();
        textLabel.setWrapText(true);
    }

    private void languageSetup(){
        if (Settings.getInstance().getLocale().equals(Language.Japanese)) {
            japaneseFont();
        } else {
            westernFont();
        }
        topBarLabel.setText(LanguageData.getInstance().getMsg("AlertConfirmation"));
        buttonConfirm.setText(LanguageData.getInstance().getMsg("AlertButtonConfirm"));
        buttonCancel.setText(LanguageData.getInstance().getMsg("AlertButtonCancel"));
    }

    private void japaneseFont(){
        textLabel.getStyleClass().add("JapaneseFont");
        buttonConfirm.getStyleClass().add("JapaneseFont");
        buttonCancel.getStyleClass().add("JapaneseFont");
        topBarLabel.getStyleClass().add("TopLabelJapanese");
    }

    private void westernFont(){
        textLabel.getStyleClass().add("WesternFont");
        buttonConfirm.getStyleClass().add("WesternFont");
        buttonCancel.getStyleClass().add("WesternFont");
        topBarLabel.getStyleClass().add("TopLabelWestern");
    }

    public void buttonAccepted(){
        dialogButton = DialogButton.CONFIRM;
        buttonConfirm.getScene().getWindow().hide();
    }

    public void buttonCancel(){
        dialogButton = DialogButton.CANCEL;
        buttonCancel.getScene().getWindow().hide();
    }

    public DialogButton getDialogButton(){
        return dialogButton;
    }

    public void setTextLabel(String text){
        textLabel.setText(text);
    }


}
