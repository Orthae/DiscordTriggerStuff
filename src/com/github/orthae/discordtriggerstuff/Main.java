package com.github.orthae.discordtriggerstuff;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/github/orthae/discordtriggerstuff/fxml/appWindow.fxml"));
        primaryStage.setTitle("Discord Trigger Stuff");
        primaryStage.setResizable(true);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 850, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Logger.getInstance().log("Application started, inside main method");
        launch(args);
    }
}
