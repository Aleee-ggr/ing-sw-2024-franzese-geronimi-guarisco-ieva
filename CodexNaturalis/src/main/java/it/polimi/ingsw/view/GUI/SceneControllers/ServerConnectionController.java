package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.network.rmi.RmiClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the controller for the server connection scene in the GUI.
 * It handles the user interactions and scene transitions.
 */
public class ServerConnectionController {
    @FXML
    TextField serverIp;

    @FXML
    TextField serverPort;

    /**
     * Changes the scene to the login scene when the corresponding button is clicked.
     * Initializes the client with the server IP and port.
     *
     * @param event the action event that triggered the method
     */
    @FXML
    protected void changeLoginScene(ActionEvent event) {
        ClientInterface client = new RmiClient(serverIp.getText(), Integer.parseInt(serverPort.getText()));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/LoginScene.fxml"));
            Scene scene = new Scene(loader.load(), 1920, 1080);
            LoginController loginController = loader.getController();
            loginController.setClient(client);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the scene to the game introduction scene.
     *
     * @param event the action event that triggered the method
     */
    @FXML
    protected void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameIntroduction.fxml"));
            Scene scene = new Scene(loader.load(), 1920, 1080);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
