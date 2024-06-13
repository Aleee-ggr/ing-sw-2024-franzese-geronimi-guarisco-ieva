package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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
 * Controller class for the ChooseStartingCardSide scene in the GUI.
 * This class is responsible for displaying the front and back sides of the starting card
 * and handling the user's selection. It initializes the scene with the appropriate images
 * and updates the client with the user's chosen card side.
 *
 * The scene allows the user to choose between the front and back side of the starting card.
 * Once a choice is made, the controller transitions to the main game scene.
 */
public class ChooseStartingCardSideController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @FXML
    private ImageView frontSide;

    @FXML
    private ImageView backSide;

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
        int startingCard = playerData.getStartingCard().getId();
        String imagePathFrontSide = String.format("GUI/images/cards.nogit/front/%03d.png", startingCard);
        Image imageFrontSide = new Image(imagePathFrontSide);
        frontSide.setImage(imageFrontSide);

        String imagePathBackSide = String.format("GUI/images/cards.nogit/back/%03d.png", startingCard);
        Image imageBackSide = new Image(imagePathBackSide);
        backSide.setImage(imageBackSide);
    }

    /**
     * Handles the event where the user selects the front or back side of the starting card.
     * Depending on which side the user clicks, this method will notify the client of the user's choice,
     * then load the main game scene.
     *
     * @param mouseEvent the mouse event that triggered this method,
     *                  which contains information about which side was clicked
     */
    public void changeBoardScene(MouseEvent mouseEvent) {
        try {
            if (mouseEvent.getSource() == frontSide) {
                client.placeStartingCard(true);
            } else if (mouseEvent.getSource() == backSide) {
                client.placeStartingCard(false);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ChooseColorScene.fxml"));
            ChooseColorController controller = new ChooseColorController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
