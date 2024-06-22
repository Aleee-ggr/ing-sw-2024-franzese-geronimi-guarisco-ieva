package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the ChooseObjective scene in the GUI.
 * This class is responsible for displaying the player's initial objectives and
 * handling the user's selection. It initializes the scene with the appropriate images
 * and updates the client with the user's chosen objective.
 *
 * The scene allows the user to choose between two starting objectives. Once a choice is made,
 * the controller transitions to the next scene where the user can choose the starting card side.
 */
public class ChooseObjectiveController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @FXML
    private ImageView firstObjective;

    @FXML
    private ImageView secondObjective;

    /**
     * Sets the client interface and retrieves the player data.
     *
     * @param client the client interface to be set
     */
    public void setClient(ClientInterface client) {
        this.client = client;
        playerData = client.getPlayerData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
        int firstObjectiveId = playerData.getStartingObjectives().getFirst().getId();
        String imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", firstObjectiveId);
        System.out.println(imagePath);

        Image image = new Image(imagePath);
        firstObjective.setImage(image);

        int secondObjectiveId = playerData.getStartingObjectives().getLast().getId();
        String imagePathSecond = String.format("GUI/images/cards.nogit/front/%03d.png", secondObjectiveId);
        Image imageSecond = new Image(imagePathSecond);
        secondObjective.setImage(imageSecond);
    }

    /**
     * Handles the event where the user selects one of the starting objectives.
     * Depending on which objective the user clicks, this method will notify the client of the user's choice,
     * then load the next scene, which allows the user to choose the starting card side.
     *
     * @param mouseEvent the mouse event that triggered this method,
     *                   which contains information about which objective was clicked
     */
    public void changeChooseStarting(MouseEvent mouseEvent) {
        try {
            if (mouseEvent.getSource() == firstObjective) {
                client.choosePersonalObjective(playerData.getStartingObjectives().getFirst().getId());
            } else if (mouseEvent.getSource() == secondObjective) {
                client.choosePersonalObjective(playerData.getStartingObjectives().getLast().getId());
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ChooseStartingCardSideScene.fxml"));
            ChooseStartingCardSideController controller = new ChooseStartingCardSideController();
            controller.setClient(client);
            loader.setController(controller);
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
