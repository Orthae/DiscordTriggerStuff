package com.github.orthae.discordtriggerstuff.alerts;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.Logger;
import com.github.orthae.discordtriggerstuff.Settings;
import com.github.orthae.discordtriggerstuff.enums.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;

public class AlertDialogs {

// TODO Finish rewriting into AlertDialog class, change to method chaining for less code (?)
// TODO Figure out what to do with exceptions dialogs to avoid code duplication

    public static DialogButton confirmDialog(Window owner, String text){
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AlertDialogs.class.getResource("/com/github/orthae/discordtriggerstuff/fxml/confirmDialog.fxml"));
        try {
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            Logger.getInstance().log("Could't load /com/github/orthae/discordtriggerstuff/fxml/confirmDialog.fxml");
        }
        ConfirmAlertDialog confirmDialogController = fxmlLoader.getController();
        confirmDialogController.setAlertMessage(text);
        stage.toFront();
        stage.showAndWait();
     return confirmDialogController.getDialogButton();
    }


    public static DialogButton deleteTriggersDialog(Window owner, int size){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP1"));
            if (!Settings.getInstance().getLocale().equals(Language.Japanese)) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(size);
            if (!Settings.getInstance().getLocale().equals(Language.Japanese)) {
                stringBuilder.append(" ");
            }
            if (size == 1) {
                stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP3"));
            } else {
                stringBuilder.append(LanguageData.getInstance().getMsg("tableDeleteConfirmP2"));
            }
        return confirmDialog(owner, stringBuilder.toString());
    }

    public static void addBotDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageData.getInstance().getMsg("AlertError"));
        alert.setHeaderText(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        Label label = new Label();
        label.setText(LanguageData.getInstance().getMsg("AlertDiscordCouldntAddBotBrowser"));
        TextField textField = new TextField();
        textField.setText("https://discordapp.com/oauth2/authorize?client_id=" + Settings.getInstance().getClientID() + "&scope=bot");
        textField.setEditable(false);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(label, 0, 0);
        gridPane.add(textField, 0, 1);
        alert.getDialogPane().contentProperty().setValue(gridPane);
        alertStage.show();
    }

}
