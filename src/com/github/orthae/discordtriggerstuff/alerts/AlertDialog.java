package com.github.orthae.discordtriggerstuff.alerts;

import com.github.orthae.discordtriggerstuff.Logger;
import com.github.orthae.discordtriggerstuff.ReusedCode;
import com.github.orthae.discordtriggerstuff.Settings;
import com.github.orthae.discordtriggerstuff.enums.DialogButton;
import com.github.orthae.discordtriggerstuff.enums.Language;
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
import javafx.stage.Window;

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

    public ImageView getImageViewIcon(){
        return imageViewIcon;
    }

    // Setters
    public void setAlertStage(Stage dialogStage) {
        this.alertStage = dialogStage;
    }
    public AlertDialog setAlertMessage(String string){
        alertMessage.setText(string);
        return this;
    }
    public AlertDialog setIcon(InputStream inputStream){
        imageViewIcon.setImage(new Image(inputStream));
        return this;
    }
    public AlertDialog setAlertTitle(String string){
        alertTitle.setText(string);
        return this;
    }

    public void setButtonCancel(String string){
        buttonCancel.setText(string);
    }

    public AlertDialog setOwner(Window owner){
        getAlertStage().initOwner(owner);
        return this;
    }

    public void setDialogButton(DialogButton dialogButton){
        this.dialogButton = dialogButton;
    }


    // Factory methods
    // TODO rework to accept Window as arg and remove setOwner
    public static AlertDialog createErrorDialog(){
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AlertDialog.class.getResource("/com/github/orthae/discordtriggerstuff/fxml/errorDialog.fxml"));
        try {
            Parent root = fxmlLoader.load();
            dialogStage.setScene(new Scene(root));
        } catch (IOException e) {
            Logger.getInstance().log("IO error while creating error dialog");
        }
        ErrorAlertDialog dialog = fxmlLoader.getController();
        ReusedCode.enableWindowMoving(dialog.getTitlePane());
        dialogStage.toFront();
        dialog.setAlertStage(dialogStage);
        return dialog;
    }


    public static AlertDialog createConfirmDialog(){
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AlertDialog.class.getResource("/com/github/orthae/discordtriggerstuff/fxml/confirmDialog.fxml"));
        try {
            Parent root = fxmlLoader.load();
            dialogStage.setScene(new Scene(root));
        } catch (IOException e) {
            Logger.getInstance().log("IO error while creating confirm dialog");
        }
        ConfirmAlertDialog dialog = fxmlLoader.getController();
        ReusedCode.enableWindowMoving(dialog.getTitlePane());
        dialogStage.toFront();
        dialog.setAlertStage(dialogStage);
        return dialog;
    }

    // Class methods
    public void initialize(){
        languageSetup();

    }

    public void show(){
        getAlertStage().show();
    }

    public AlertDialog showAndWait(){
        getAlertStage().showAndWait();
        return this;
    }

    public void buttonCancel(){
        dialogButton = DialogButton.CANCEL;
        alertTitlePane.getScene().getWindow().hide();
    }

    private void languageSetup(){
        if (Settings.getInstance().getLocale().equals(Language.Japanese)) {
            japaneseFont();
        } else {
            westernFont();
        }

    }


    private void japaneseFont(){
        alertMessage.getStyleClass().add("JapaneseFont");
        alertTitle.getStyleClass().add("TopBarLabelJapanese");
        buttonCancel.getStyleClass().add("JapaneseFont");
    }

    private void westernFont(){
        alertMessage.getStyleClass().add("WesternFont");
        alertTitle.getStyleClass().add("TopBarLabelWestern");
        buttonCancel.getStyleClass().add("WesternFont");
    }





}
