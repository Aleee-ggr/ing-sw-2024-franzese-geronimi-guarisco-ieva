package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class represents the controller for the create game scene in the GUI.
 * It handles user input for selecting the number of players and transitioning
 * to the waiting room scene.
 */
public class CreateGameController {
    private ClientInterface client;

    @FXML
    private RadioButton twoPlayers;

    @FXML
    private RadioButton threePlayers;

    @FXML
    private RadioButton fourPlayers;

    @FXML
    private ToggleGroup numPlayers;

    /**
     * Handles the action when the user clicks on the button to change to the waiting room scene.
     * @param event The action event triggered by the user.
     */
    @FXML
    protected void changeWaitingRoomScene(ActionEvent event) {
        RadioButton selected = (RadioButton) numPlayers.getSelectedToggle();
        if (selected != null) {
            try {
                int numPlayer = Integer.parseInt((String) selected.getUserData());
                client.newGame(numPlayer);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/WaitingRoom.fxml"));
                WaitingRoomController controller = new WaitingRoomController();
                controller.setClient(client);
                loader.setController(controller);
                Scene scene = new Scene(loader.load(), 1920, 1080);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes the scene back to the Main Menu when the user clicks "Back".
     * @param event The ActionEvent triggered by the user's interaction.
     */
    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/MainMenu.fxml"));
            MainMenuController controller = new MainMenuController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), 1920, 1080);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the client for the controller.
     * @param client the client interface to set
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }
}