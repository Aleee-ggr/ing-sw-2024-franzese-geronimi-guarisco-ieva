package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
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
     */
    public void setClient(ClientInterface client, GameController gameController) {
        this.client = client;
        this.playerData = client.getPlayerData();
        this.gameController = gameController;
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

        boolean isPlayerTurn = playerData.getClientHand().size() != 3;

        setPlayerTurnActionOrError(goldCard1, 0, isPlayerTurn);
        setPlayerTurnActionOrError(goldCard2, 1, isPlayerTurn);
        setPlayerTurnActionOrError(stdCard1, 2, isPlayerTurn);
        setPlayerTurnActionOrError(stdCard2, 3, isPlayerTurn);
        setPlayerTurnActionOrError(goldDeck, 4, isPlayerTurn);
        setPlayerTurnActionOrError(stdDeck, 5, isPlayerTurn);
    }

    /**
     * Sets the action to be performed when a player clicks on a card during their turn.
     * When the card is clicked, the card's image is cleared, a new card is drawn,
     * and the turn is changed to the next player.
     *
     * @param card the ImageView representing the card
     * @param cardIndex the index of the card being interacted with
     */
    private void setPlayerTurnAction(ImageView card, int cardIndex) {
        card.setOnMouseClicked(event -> {
            card.setImage(null);
            drawCard(cardIndex);
            changeTurn();
        });
    }

    /**
     * Sets the action to be performed when a player attempts to click on a card
     * when it's not their turn. Displays an error message indicating it's not their turn.
     *
     * @param card the ImageView representing the card
     */
    private void setErrorMessageAction(ImageView card) {
        card.setOnMouseClicked(event -> {
            ErrorMessageController.showErrorMessage("It's not your turn to draw a card!", gameController.root);
        });
    }

    /**
     * Sets the appropriate action for a card based on whether it is the player's turn.
     * If it is the player's turn, the card will perform the turn action. Otherwise,
     * it will display an error message.
     *
     * @param card the ImageView representing the card
     * @param cardIndex the index of the card being interacted with
     * @param isPlayerTurn a boolean indicating if it is the player's turn
     */
    private void setPlayerTurnActionOrError(ImageView card, int cardIndex, boolean isPlayerTurn) {
        if (isPlayerTurn) {
            setPlayerTurnAction(card, cardIndex);
        } else {
            setErrorMessageAction(card);
        }
    }

    /**
     * Changes the turn to the next player by updating the game state.
     * Displays a message indicating the player should wait for their turn and sets the turn flag to false.
     */
    private void changeTurn() {
        gameController.turnMessage.setText("Waiting for your Turn...");
        gameController.myTurn = false;
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