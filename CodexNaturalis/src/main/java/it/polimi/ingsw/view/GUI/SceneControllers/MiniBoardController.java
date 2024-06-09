package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.ClientData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.RotateBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MiniBoardController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calculateBoardCenterCoordinates();
        try {
            client.fetchPlayersBoards();
            client.fetchPlayersPlacingOrder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GridPane[] boards = {board1, board2, board3};
        int playerIndex = 0;

        for (Map.Entry<String, ClientData> entry: client.getOpponentData().entrySet()) {
            if (!entry.getKey().equals(client.getUsername())) {
                GridPane currentBoard = boards[playerIndex % boards.length];
                playerIndex++;

                currentBoard.setOnMouseClicked(event -> {
                    gameController.setupOpponentData(entry.getKey());
                });

                if (entry.getValue().getBoard().isEmpty()) {
                    int startingCard = entry.getValue().getOrder().getFirst().getId();
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

                    StackPane stackPane = new StackPane(image);

                    currentBoard.add(stackPane, center.x(), center.y());
                    GridPane.setHalignment(stackPane, HPos.CENTER);
                    GridPane.setValignment(stackPane, VPos.CENTER);
                } else {
                    for (Card card: entry.getValue().getOrder()) {
                        Coordinates coordinates = entry.getValue().getBoard().inverse().get(card);
                        int id = card.getId();
                        String pathSide;

                        if (id > 0) {
                            pathSide = String.format("GUI/images/cards.nogit/front/%03d.png", id);
                        } else {
                            pathSide = String.format("GUI/images/cards.nogit/back/%03d.png", -id);
                        }
                        ImageView image = new ImageView(pathSide);
                        image.setFitHeight(50.475);
                        image.setFitWidth(75);
                        image.preserveRatioProperty();

                        StackPane stackPane = new StackPane(image);

                        currentBoard.add(stackPane, calculateBoardCoordinates(coordinates).x(), calculateBoardCoordinates(coordinates).y());
                        GridPane.setHalignment(stackPane, HPos.CENTER);
                        GridPane.setValignment(stackPane, VPos.CENTER);
                    }
                }
            }
        }
    }

    public void setClient(ClientInterface client, GameController gameController) {
        this.client = client;
        this.playerData = client.getPlayerData();
        this.gameController = gameController;
    }

    @FXML
    private void closeTab(ActionEvent event) {
        tabPane.getParent().getParent().setVisible(false);
    }

    private Coordinates calculateBoardCoordinates(Coordinates coordinates) {
        coordinates = RotateBoard.rotateCoordinates(coordinates, -45);
        return new Coordinates(
                center.x() + coordinates.x(),
                center.y() - coordinates.y()
        );
    }

    private void calculateBoardCenterCoordinates() {
        center = new Coordinates(
                board1.getColumnCount() / 2,
                board1.getRowCount() / 2
        );
    }
}