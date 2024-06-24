package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller for the Create Game scene in the GUI.
 * This class manages user input for selecting the number of players and transitioning to the waiting room scene.
 * It allows the user to specify the number of players for a new game and initiates the creation of the game accordingly.
 */
public class CreateGameController implements Initializable {
    private ClientInterface client;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @FXML
    private RadioButton twoPlayers;

    @FXML
    private RadioButton threePlayers;

    @FXML
    private RadioButton fourPlayers;

    @FXML
    private ToggleGroup numPlayers;

    @FXML
    private TextField gameName;

    /**
     * Handles the action when the user clicks on the button to change to the waiting room scene.
     * Based on the user's selection, it starts a new game with the selected number of players.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    protected void changeWaitingRoomScene(ActionEvent event) {
        RadioButton selected = (RadioButton) numPlayers.getSelectedToggle();
        if (gameName.getText() != null) {
            try {
                int numPlayer = Integer.parseInt((String) selected.getUserData());
                UUID uuid = client.newGame(numPlayer, gameName.getText());
                if (uuid != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/WaitingRoom.fxml"));
                    WaitingRoomController waitingRoomController = new WaitingRoomController();
                    waitingRoomController.setClient(client);
                    loader.setController(waitingRoomController);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.getScene().setRoot(loader.load());
                } else {
                    ErrorMessageController.showErrorMessage("There's already a game with this name!", root);
                }
            } catch (IOException e) {
                ErrorMessageController.showErrorMessage("Impossible to create the game!", root);
            }
        } else {
            ErrorMessageController.showErrorMessage("Insert game's name first!", root);
        }
    }

    /**
     * Changes the scene back to the Main Menu when the user clicks "Back".
     *
     * @param event The ActionEvent triggered by the user's interaction.
     */
    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/MainMenu.fxml"));
            MainMenuController mainMenuController = new MainMenuController();
            mainMenuController.setClient(client);
            loader.setController(mainMenuController);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(loader.load());
        } catch (IOException e) {
            ErrorMessageController.showErrorMessage("Error while loading main menu scene!", root);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
    }
}