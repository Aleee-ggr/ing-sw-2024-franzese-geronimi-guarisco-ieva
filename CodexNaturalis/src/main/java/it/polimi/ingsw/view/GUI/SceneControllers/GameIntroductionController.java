package it.polimi.ingsw.view.GUI.SceneControllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the controller for the game introduction scene in the GUI.
 * It handles the user interactions and scene transitions.
 */
public class GameIntroductionController {

    /**
     * Changes the scene to the connection scene when the corresponding button is clicked.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    protected void changeConnectionScene(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ConnectionScene.fxml"));
            Scene scene = new Scene(loader.load(), 1600, 900);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the application when the corresponding button is clicked.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}