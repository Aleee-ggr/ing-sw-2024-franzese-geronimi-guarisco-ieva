package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The DrawCardController class manages the GUI functionality for drawing cards.
 * It allows the player to draw cards from decks and handles updating the view accordingly.
 */
public class DrawCardController implements Initializable, TabController {
    private ClientInterface client;
    private PlayerData playerData;
    private GameController gameController;
    private SharedUpdate updater;

    @FXML
    StackPane tabPane;

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

    /**
     * Sets the client interface, game controller, and shared updater for this controller.
     *
     * @param client        the client interface to communicate with the server
     * @param gameController the game controller that manages the game view
     * @param updater       the shared updater that triggers view updates
     */
    public void setClient(ClientInterface client, GameController gameController, SharedUpdate updater) {
        this.client = client;
        this.playerData = client.getPlayerData();
        this.gameController = gameController;
        this.updater = updater;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCards();
    }

    /**
     * Draws a card from the specified position and updates the game view accordingly.
     *
     * @param position the position of the card to draw
     */
    private void drawCard(int position) {
        try {
            client.drawCard(position);
            gameController.setHand(client.getUsername(), gameController.frontSide);
            setCards();
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> {
                gameController.setActiveTab(null);
                gameController.tabContainer.setVisible(false);
            });
            pause.play();
        } catch (IOException e) {
            ErrorMessageController.showErrorMessage("Impossible to fetch data from the server!", gameController.root);
        }
    }

    /**
     * Closes the draw card tab when the close button is clicked.
     *
     * @param event the action event triggered by closing the tab
     */
    @FXML
    private void closeTab(ActionEvent event) {
        tabPane.getParent().getParent().setVisible(false);
        gameController.setActiveTab(null);
    }

    /**
     * Updates the images of visible cards and decks based on fetched data from the server.
     * Allows interaction with cards and decks to draw cards when clicked, if the player's hand is not full.
     */
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
                changeTurn();
            });
            goldCard2.setOnMouseClicked(event -> {
                goldCard2.setImage(null);
                drawCard(1);
                changeTurn();
            });
            stdCard1.setOnMouseClicked(event -> {
                stdCard1.setImage(null);
                drawCard(2);
                changeTurn();
            });
            stdCard2.setOnMouseClicked(event -> {
                stdCard2.setImage(null);
                drawCard(3);
                changeTurn();
            });
            goldDeck.setOnMouseClicked(event -> {
                goldDeck.setImage(null);
                drawCard(4);
                changeTurn();
            });
            stdDeck.setOnMouseClicked(event -> {
                stdDeck.setImage(null);
                drawCard(5);
                changeTurn();
            });
        } else {
            goldCard1.setOnMouseClicked(event -> {
                ErrorMessageController.showErrorMessage("It's not your turn to draw a card!", gameController.root);
            });
            goldCard2.setOnMouseClicked(event -> {
                ErrorMessageController.showErrorMessage("It's not your turn to draw a card!", gameController.root);
            });
            stdCard1.setOnMouseClicked(event -> {
                ErrorMessageController.showErrorMessage("It's not your turn to draw a card!", gameController.root);
            });
            stdCard2.setOnMouseClicked(event -> {
                ErrorMessageController.showErrorMessage("It's not your turn to draw a card!", gameController.root);
            });
            goldDeck.setOnMouseClicked(event -> {
                ErrorMessageController.showErrorMessage("It's not your turn to draw a card!", gameController.root);
            });
            stdDeck.setOnMouseClicked(event -> {
                ErrorMessageController.showErrorMessage("It's not your turn to draw a card!", gameController.root);
            });
        }
    }

    protected void changeTurn() {
        gameController.turnMessage.setText("Waiting for your Turn...");
        gameController.myTurn = false;
    }

    private void errorDraw() {
        ErrorMessageController.showErrorMessage("It's not your turn to draw a card!", gameController.root);
    }

    /**
     * Updates the view after receiving updates from the server.
     * Executes on the JavaFX Application Thread to ensure UI safety.
     */
    @Override
    public void update() {
        Platform.runLater(this::setCards);
    }
}