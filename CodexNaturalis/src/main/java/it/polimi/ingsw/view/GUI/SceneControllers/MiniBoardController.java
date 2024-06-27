package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.ClientData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.Fetch;
import it.polimi.ingsw.view.TUI.RotateBoard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The MiniBoardController class manages the mini boards displayed in the GUI for each opponent.
 * It allows interaction with opponent boards and sets up visual representation of opponent game states.
 */
public class MiniBoardController implements Initializable, TabController {
    private ClientInterface client;
    private Coordinates center;
    private GameController gameController;

    @FXML
    StackPane tabPane;

    @FXML
    GridPane board1;

    @FXML
    GridPane board2;

    @FXML
    GridPane board3;

    @FXML
    Label firstPlayerName;

    @FXML
    Label secondPlayerName;

    @FXML
    Label thirdPlayerName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBoards();
    }

    /**
     * Sets the client interface, game controller, and updater for the mini board controller.
     *
     * @param client         the client interface to communicate with the server
     * @param gameController the game controller managing the game view
     */
    public void setClient(ClientInterface client, GameController gameController) {
        this.client = client;
        this.gameController = gameController;
    }

    /**
     * Closes the mini board tab when the close button is clicked.
     *
     * @param event the action event triggered by clicking the close button
     */
    @FXML
    private void closeTab(ActionEvent event) {
        tabPane.getParent().getParent().setVisible(false);
        gameController.setActiveTab(null);
    }

    /**
     * Calculates the transformed coordinates for displaying cards on the mini boards.
     *
     * @param coordinates the original coordinates to be transformed
     * @return transformed coordinates adjusted for board orientation
     */
    private Coordinates calculateBoardCoordinates(Coordinates coordinates) {
        coordinates = RotateBoard.rotateCoordinates(coordinates, -45);
        return new Coordinates(
                center.x() + coordinates.x(),
                center.y() - coordinates.y()
        );
    }

    /**
     * Calculates the center coordinates of the mini boards for positioning cards.
     */
    private void calculateBoardCenterCoordinates() {
        center = new Coordinates(
                board1.getColumnCount() / 2,
                board1.getRowCount() / 2
        );
    }

    /**
     * Sets up the mini boards with opponent data and allows interaction with opponent boards.
     * Fetches opponent boards and placing orders from the server.
     */
    @FXML
    private void setBoards() {
        calculateBoardCenterCoordinates();
        Fetch.fetchSwitch(client);

        GridPane[] boards = {board1, board2, board3};
        Label[] playersNames = {firstPlayerName, secondPlayerName, thirdPlayerName};
        int playerIndex = 0;

        for (Map.Entry<String, ClientData> entry : client.getOpponentData().entrySet()) {
            if (!entry.getKey().equals(client.getUsername())) {
                GridPane currentBoard = boards[playerIndex % boards.length];
                playersNames[playerIndex % boards.length].setText(entry.getKey());
                playerIndex++;

                currentBoard.setOnMouseClicked(event -> {
                    gameController.buttonsContainer.getChildren().remove(gameController.flipButton);
                    gameController.centerBoard();
                    gameController.board.setScaleX(1.0);
                    gameController.board.setScaleY(1.0);
                    gameController.setOpponentData(entry.getKey());
                    gameController.setHand(entry.getKey(), false);
                    gameController.setActiveTab(null);
                });

                if (entry.getValue().getBoard().isEmpty()) {
                    StackPane stackPane = getStackPane(entry.getValue().getOrder().getFirst());

                    currentBoard.add(stackPane, center.x(), center.y());
                    GridPane.setHalignment(stackPane, HPos.CENTER);
                    GridPane.setValignment(stackPane, VPos.CENTER);
                } else {
                    for (Card card : entry.getValue().getOrder()) {
                        Coordinates coordinates = entry.getValue().getBoard().inverse().get(card);
                        StackPane stackPane = getStackPane(card);

                        currentBoard.add(stackPane, calculateBoardCoordinates(coordinates).x(), calculateBoardCoordinates(coordinates).y());
                        GridPane.setHalignment(stackPane, HPos.CENTER);
                        GridPane.setValignment(stackPane, VPos.CENTER);
                    }
                }
            }
        }

        for (Label playersName : playersNames) {
            if (!playersName.getText().isEmpty()) {
                playersName.getStyleClass().add("label-miniboard");
            }
        }
    }

    /**
     * Creates a StackPane containing an image of a card based on the provided card entry.
     * If the card ID is positive, the front of the card is shown. If the card ID is negative, the back of the card is shown.
     *
     * @param entry the Card object containing the card ID
     * @return a StackPane containing the ImageView of the card
     */
    private static StackPane getStackPane(Card entry) {
        int startingCard = entry.getId();
        String pathSide;
        if (startingCard > 0) {
            pathSide = String.format("GUI/images/cards.nogit/front/%03d.png", startingCard);
        } else {
            pathSide = String.format("GUI/images/cards.nogit/back/%03d.png", -startingCard);
        }
        ImageView image = new ImageView(pathSide);
        image.setFitHeight(50.475);
        image.setFitWidth(75);
        image.preserveRatioProperty();

        return new StackPane(image);
    }

    @Override
    public void update() {
        Platform.runLater(this::setBoards);
    }
}
