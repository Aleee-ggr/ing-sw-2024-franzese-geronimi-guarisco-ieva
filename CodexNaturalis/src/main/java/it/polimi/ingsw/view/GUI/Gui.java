package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the entry point for the GUI.
 */
public class Gui extends Application {
    public static void start() {
        launch();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource("/GUI/fxml/GameIntroduction.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        double baseWidth = 1920;
        double baseHeight = 1080;

        if (screenWidth <= baseWidth || screenHeight <= baseHeight) {
            stage.setFullScreen(true);
        } else {
            stage.setWidth(baseWidth);
            stage.setHeight(baseHeight);
        }

        stage.setMaxWidth(baseWidth);
        stage.setMaxHeight(baseHeight);
        stage.setMinWidth(1600);
        stage.setMinHeight(900);

        stage.setTitle("Codex Naturalis");
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.show();
    }
}