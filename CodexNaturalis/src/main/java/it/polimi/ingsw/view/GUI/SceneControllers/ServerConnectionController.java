package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the server connection scene in the GUI.
 * Manages user interactions and transitions to login scene.
 */
public class ServerConnectionController implements Initializable {
    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @FXML
    TextField serverIp;

    @FXML
    Button socketButton;

    @FXML
    Button rmiButton;

    /**
     * Changes the scene to the login scene when the corresponding button is clicked.
     * Initializes the client with the server IP and port based on the selected connection type (Socket/RMI).
     *
     * @param event The action event that triggered the method.
     */
    @FXML
    protected void changeLoginScene(ActionEvent event) {
        ClientInterface client;

        if (event.getSource() == socketButton) {
            client = new SocketClient(serverIp.getText(), 8000);
        } else {
            client = new RmiClient(serverIp.getText(), 9000);
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/LoginScene.fxml"));
            Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            LoginController loginController = loader.getController();
            loginController.setClient(client);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the scene to the game introduction scene when the "Back" button is clicked.
     *
     * @param event The action event that triggered the method.
     */
    @FXML
    protected void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameIntroduction.fxml"));
            Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
    }
}

