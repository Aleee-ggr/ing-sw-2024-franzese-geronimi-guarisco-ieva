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
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());

        setCardImage(firstObjective, playerData.getStartingObjectives().getFirst().getId());
        setCardImage(secondObjective, playerData.getStartingObjectives().getLast().getId());
        setCardImage(firstHandCard, playerData.getClientHand().getFirst().getId());
        setCardImage(secondHandCard, playerData.getClientHand().get(1).getId());
        setCardImage(thirdHandCard, playerData.getClientHand().getLast().getId());
        setCardImage(firstCommonObjective, playerData.getGlobalObjectives().getFirst().getId());
        setCardImage(secondCommonObjective, playerData.getGlobalObjectives().getLast().getId());
    }

    /**
     * Sets the image of a card on the given ImageView based on the provided card ID.
     * The image is loaded from the specified path format using the card ID.
     *
     * @param imageView the ImageView on which the card image will be set
     * @param id the ID of the card whose image is to be displayed
     */
    private void setCardImage(ImageView imageView, int id) {
        String imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
        Image image = new Image(imagePath);
        imageView.setImage(image);
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
                GameController gameController = new GameController();
                gameController.setClient(client);
                loader.setController(gameController);
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
            ErrorMessageController.showErrorMessage("Error while loading game scene!", root);
        }
    }
}
