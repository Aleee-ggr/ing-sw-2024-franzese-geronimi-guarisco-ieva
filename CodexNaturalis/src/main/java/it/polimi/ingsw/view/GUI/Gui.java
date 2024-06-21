package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
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
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        double baseWidth = 1920;
        double baseHeight = 1080;

        double scale = Math.min(screenWidth / baseWidth, screenHeight / baseHeight);

        double sceneWidth = baseWidth * scale;
        double sceneHeight = baseHeight * scale;
        Scene scene = new Scene(fxmlLoader.load());

        stage.setMinWidth(1600);
        stage.setMinHeight(900);
        stage.setWidth(sceneWidth);
        stage.setHeight(sceneHeight);
        stage.setMaxWidth(baseWidth);
        stage.setMaxHeight(baseHeight);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}