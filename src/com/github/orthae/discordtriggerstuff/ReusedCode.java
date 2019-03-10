package com.github.orthae.discordtriggerstuff;

import javafx.scene.Node;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class ReusedCode {

    public static final UnaryOperator<TextFormatter.Change> SPINNER_FORMATTER = change -> {
        String text = change.getText();
        if (text.matches("[0-9]*")) {
            return change;
        }
        return null;
    };

    public static void enableWindowMoving(Node node){
        final double[] xOffset = new double[1];
        final double[] yOffset = new double[1];

        node.setOnMousePressed(event -> {
            xOffset[0] = node.getScene().getWindow().getX() - event.getScreenX();
            yOffset[0] = node.getScene().getWindow().getY() - event.getScreenY();
        });

        node.setOnMouseDragged(event -> {
            node.getScene().getWindow().setX(event.getScreenX() + xOffset[0]);
            node.getScene().getWindow().setY(event.getScreenY() + yOffset[0]);
        });
    }

    public static String getFormattedFilePath(String filePath){
        return ("sounds/" + Settings.getInstance().getTtsLanguage() + "_" + filePath.toLowerCase().trim().replaceAll(" ", "_") + ".wav");
    }

}
