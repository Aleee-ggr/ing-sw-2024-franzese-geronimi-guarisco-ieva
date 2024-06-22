package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
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

    @FXML
    private ImageView firstHandCard;

    @FXML
    private ImageView secondHandCard;

    @FXML
    private ImageView thirdHandCard;

    @FXML
    private ImageView firstCommonObjective;

    @FXML
    private ImageView secondCommonObjective;

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
        String imagePath;
        Image image;
        int id;

        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());

        id = playerData.getStartingObjectives().getFirst().getId();
        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
        image = new Image(imagePath);
        firstObjective.setImage(image);

        id = playerData.getStartingObjectives().getLast().getId();
        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
        image = new Image(imagePath);
        secondObjective.setImage(image);

        id = playerData.getClientHand().getFirst().getId();
        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
        image = new Image(imagePath);
        firstHandCard.setImage(image);

        id = playerData.getClientHand().get(1).getId();
        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
        image = new Image(imagePath);
        secondHandCard.setImage(image);

        id = playerData.getClientHand().getLast().getId();
        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
        image = new Image(imagePath);
        thirdHandCard.setImage(image);

        id = playerData.getGlobalObjectives().getFirst().getId();
        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
        image = new Image(imagePath);
        firstCommonObjective.setImage(image);

        id = playerData.getGlobalObjectives().getLast().getId();
        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
        image = new Image(imagePath);
        secondCommonObjective.setImage(image);
    }

    /**
     * Handles the event where the user selects one of the starting objectives.
     * Depending on which objective the user clicks, this method will notify the client of the user's choice,
     * then load the next scene, which allows the user to choose the starting card side.
     *
     * @param mouseEvent the mouse event that triggered this method,
     *                   which contains information about which objective was clicked
     */
    public void changeBoardScene(MouseEvent mouseEvent) {
        boolean isValid = false;
        try {
            if (mouseEvent.getSource() == firstObjective) {
                isValid = client.choosePersonalObjective(playerData.getStartingObjectives().getFirst().getId());
            } else if (mouseEvent.getSource() == secondObjective) {
                isValid = client.choosePersonalObjective(playerData.getStartingObjectives().getLast().getId());
            }

            if (isValid) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameScene.fxml"));
                GameController controller = new GameController();
                controller.setClient(client);
                loader.setController(controller);
                Stage stage = (Stage) firstObjective.getScene().getWindow();
                stage.getScene().setRoot(loader.load());
                if (Screen.getPrimary().getVisualBounds().getWidth() <= 1920 || Screen.getPrimary().getVisualBounds().getHeight() <= 1080) {
                    stage.setFullScreen(true);
                } else {
                    stage.setMaxWidth(3840);
                    stage.setMaxHeight(2160);
                }
            } else {
                ErrorMessageController.showErrorMessage("Error choosing the personal objective!", root);
            }
        } catch (IOException e) {
            ErrorMessageController.showErrorMessage("Error loading game scene!", root);
        }
    }
}
