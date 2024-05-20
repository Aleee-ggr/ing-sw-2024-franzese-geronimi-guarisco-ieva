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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseStartingCardSideController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    private ImageView frontSide;

    @FXML
    private ImageView backSide;

    public void setClient(ClientInterface client) {
        this.client = client;
        playerData = client.getPlayerData();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int startingCard = playerData.getStartingCard().getId();
        String imagePathFrontSide = String.format("GUI/images/cards.nogit/front/%03d.png", startingCard);
        Image imageFrontSide = new Image(imagePathFrontSide);
        frontSide.setImage(imageFrontSide);

        String imagePathBackSide = String.format("GUI/images/cards.nogit/back/%03d.png", startingCard);
        Image imageBackSide = new Image(imagePathBackSide);
        backSide.setImage(imageBackSide);
    }

    public void changeBoardScene(MouseEvent mouseEvent) {
        try {
            if (mouseEvent.getSource() == frontSide) {
                client.placeStartingCard(true);
            } else if (mouseEvent.getSource() == backSide) {
                client.placeStartingCard(false);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameScene.fxml"));
            GameController controller = new GameController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), 1600, 900);
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
