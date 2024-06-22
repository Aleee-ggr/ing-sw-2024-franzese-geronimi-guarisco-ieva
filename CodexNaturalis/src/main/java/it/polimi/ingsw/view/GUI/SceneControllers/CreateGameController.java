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

/**
 * Controller for the Create Game scene in the GUI.
 * This class manages user input for selecting the number of players and transitioning to the waiting room scene.
 * It allows the user to specify the number of players for a new game and initiates the creation of the game accordingly.
 */
public class CreateGameController implements Initializable {
    @FXML
    StackPane root;
    @FXML
    ImageView backgroundImage;
    private ClientInterface client;
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
        if (selected != null && gameName.getText() != null) {
            try {
                System.out.println(gameName.getText());
                int numPlayer = Integer.parseInt((String) selected.getUserData());
                client.newGame(numPlayer, gameName.getText());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/WaitingRoom.fxml"));
                WaitingRoomController controller = new WaitingRoomController();
                controller.setClient(client);
                loader.setController(controller);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            MainMenuController controller = new MainMenuController();
            controller.setClient(client);
            loader.setController(controller);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(loader.load());
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
    }
}