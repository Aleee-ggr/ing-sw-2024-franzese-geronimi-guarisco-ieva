package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrawCardController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    private ImageView goldDeck;

    @FXML
    private ImageView stdDeck;

    @FXML
    private ImageView goldCard1;

    @FXML
    private ImageView goldCard2;

    @FXML
    private ImageView stdCard1;

    @FXML
    private ImageView stdCard2;

    public void setClient(ClientInterface client) {
        this.client = client;
        this.playerData = client.getPlayerData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client.fetchVisibleCardsAndDecks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Card card: client.getDecksBacks()) {
            int id = card.getId();
            String imagePath = String.format("GUI/images/cards.nogit/back/%03d.png", id);
            Image image = new Image(imagePath);
            if (id <= 40) {
                stdDeck.setImage(image);
            } else {
                goldDeck.setImage(image);
            }
        }

        for (int i = 0; i < client.getVisibleCards().size(); i++) {
            int id = client.getVisibleCards().get(i).getId();
            String imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
            Image image = new Image(imagePath);
            if (id <= 40) {
                if (stdCard1.getImage() == null) {
                    stdCard1.setImage(image);
                } else {
                    stdCard2.setImage(image);
                }
            } else {
                if (goldCard1.getImage() == null) {
                    goldCard1.setImage(image);
                } else {
                    goldCard2.setImage(image);
                }
            }
        }

        if (playerData.getClientHand().size() != 3) {
            goldCard1.setOnMouseClicked(event -> drawCard(0));
            goldCard2.setOnMouseClicked(event -> drawCard(1));
            stdCard1.setOnMouseClicked(event -> drawCard(2));
            stdCard2.setOnMouseClicked(event -> drawCard(3));
            stdDeck.setOnMouseClicked(event -> drawCard(4));
            goldDeck.setOnMouseClicked(event -> drawCard(5));
        }

    }

    private void drawCard(int position) {
        try {
            client.drawCard(position);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameScene.fxml"));
            GameController controller = new GameController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = null;
            scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            Stage stage = (Stage) goldDeck.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameScene.fxml"));
            GameController controller = new GameController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = null;
            scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            Stage stage = (Stage) goldDeck.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
