package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * This class is the controller for the main menu scene in the GUI.
 * It handles the user interactions and scene transitions.
 */
public class MainMenuController implements Initializable {
    private ClientInterface client;

    @FXML
    private VBox gameButtonsContainer;

    /**
     * Changes the scene to the create game scene when the corresponding button is clicked.
     *
     * @param event the action event that triggered the method
     */
    @FXML
    protected void changeCreateGameScene(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/CreateGameScene.fxml"));
            Scene scene = new Scene(loader.load(), 1600, 900);
            CreateGameController controller = loader.getController();
            controller.setClient(client);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the scene to the login scene.
     *
     * @param event the action event that triggered the method
     */
    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/LoginScene.fxml"));
            Scene scene = new Scene(loader.load(), 1600, 900);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the client for the controller.
     *
     * @param client the client interface to set
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }

    /**
     * Initializes the controller.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        if (client != null) {
            System.out.println("Client connected");
            try {
                client.fetchAvailableGames();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (UUID uuid : client.getAvailableGames()) {
                System.out.println(uuid);
                Button button = new Button(uuid.toString());
                button.setStyle("-fx-background-color: ffffff;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-style: solid;" +
                                "-fx-pref-height: 100;" +
                                "-fx-pref-width: 700;" +
                                "-fx-text-fill: #432918;" +
                                "-fx-font-family: Trattatello;" +
                                "-fx-font-size: 30;" +
                                "-fx-cursor: hand;"
                );
                button.setOnAction(event -> {
                    try {
                        client.joinGame(uuid);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/WaitingRoom.fxml"));
                        WaitingRoomController controller = new WaitingRoomController();
                        controller.setClient(client);
                        loader.setController(controller);
                        Scene scene = new Scene(loader.load(), 1600, 900);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                gameButtonsContainer.getChildren().add(button);
            }
        }
        System.out.println("MainMenuController initialized");
    }
}
