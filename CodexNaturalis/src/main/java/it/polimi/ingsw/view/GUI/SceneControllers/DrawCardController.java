package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrawCardController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;
    private GameController gameController;

    @FXML
    StackPane tabPane;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

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

    @FXML
    private VBox decksContainer;

    @FXML
    private VBox cardsContainer;

    public void setClient(ClientInterface client, GameController gameController) {
        this.client = client;
        this.playerData = client.getPlayerData();
        this.gameController = gameController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCards();
    }

    private void drawCard(int position) {
        try {
            client.drawCard(position);
            gameController.setHand(gameController.frontSide);

            setCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeTab(ActionEvent event) {
        tabPane.getParent().getParent().setVisible(false);
    }

    @FXML
    private void setCards() {
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
            goldCard1.setOnMouseClicked(event -> {
                goldCard1.setImage(null);
                drawCard(0);
            });
            goldCard2.setOnMouseClicked(event -> {
                goldCard2.setImage(null);
                drawCard(1);
            });
            stdCard1.setOnMouseClicked(event -> {
                stdCard1.setImage(null);
                drawCard(2);
            });
            stdCard2.setOnMouseClicked(event -> {
                stdCard2.setImage(null);
                drawCard(3);
            });
            goldDeck.setOnMouseClicked(event -> {
                goldDeck.setImage(null);
                drawCard(4);
            });
            stdDeck.setOnMouseClicked(event -> {
                stdDeck.setImage(null);
                drawCard(5);
            });

        }
    }
}
