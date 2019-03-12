package com.github.orthae.discordtriggerstuff.alerts;

import com.github.orthae.discordtriggerstuff.Logger;
import com.github.orthae.discordtriggerstuff.ReusedCode;
import com.github.orthae.discordtriggerstuff.enums.DialogButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;

public abstract class AlertDialog {
    @FXML
    private BorderPane alertTitlePane;
    @FXML
    private Label alertTitle;
    @FXML
    private Label alertMessage;
    @FXML
    private Button buttonCancel;
    @FXML
    private ImageView imageViewIcon;
    private DialogButton dialogButton = null;
    private Stage alertStage = null;

    // Getters
    public Stage getAlertStage(){
        return alertStage;
    }
    public DialogButton getDialogButton(){
        return dialogButton;
    }
    public BorderPane getTitlePane(){
        return alertTitlePane;
    }
    public Label getAlertTitle(){
        return alertTitle;
    }

    // Setters
    public void setAlertStage(Stage dialogStage) {
        this.alertStage = dialogStage;
    }
    public void setAlertMessage(String string){
        alertMessage.setText(string);
    }
    public void setIcon(InputStream inputStream){
        imageViewIcon.setImage(new Image(inputStream));
    }
    public void setAlertTitle(String string){
        alertTitle.setText(string);
    }



    // Factory methods
    public static AlertDialog createErrorDialog(){
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AlertDialog.class.getResource("/com/github/orthae/discordtriggerstuff/fxml/errorDialog.fxml"));
        try {
            Parent root = fxmlLoader.load();
            dialogStage.setScene(new Scene(root));
        } catch (IOException e) {
            Logger.getInstance().log("Creating AlertDialog");
        }
        ErrorAlertDialog dialog = fxmlLoader.getController();
        ReusedCode.enableWindowMoving(dialog.getTitlePane());
        dialog.setAlertStage(dialogStage);
        return dialog;
    }

    public static AlertDialog createConfirmDialog(){


     return null;
    }

    // Class methods
    public void buttonCancel(){
        dialogButton = DialogButton.CANCEL;
        alertTitlePane.getScene().getWindow().hide();
    }

    private void languageSetup(){


    }


    private void japaneseFont(){
        alertMessage.getStyleClass().add("JapaneseFont");
        alertTitle.getStyleClass().add("TopLabelJapanese");
    }

    private void westernFont(){
        alertMessage.getStyleClass().add("WesternFont");
        alertTitle.getStyleClass().add("TopLabelWestern");
    }





}
