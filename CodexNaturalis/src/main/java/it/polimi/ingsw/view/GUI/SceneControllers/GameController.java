package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.RotateBoard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;
    private Coordinates center;
    private final Map<Coordinates, StackPane> validPlacementPanes = new HashMap<>();
    private List<Card> placedCards;



    private ImageView selectedHandCard = null;

    @FXML
    GridPane board;

    @FXML
    ImageView firstHandCard;

    @FXML
    ImageView secondHandCard;

    @FXML
    ImageView thirdHandCard;

    @FXML
    ImageView plateau;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchData();
        calculateBoardCenterCoordinates();
        setPlateau();
        setHand();

        System.out.println(playerData.getValidPlacements());

        for (Coordinates coordinates : playerData.getValidPlacements()) {
            System.out.println(RotateBoard.rotateCoordinates(coordinates, -45));
        }
        System.out.println(playerData.getBoard());



        if (playerData.getBoard().isEmpty()) {
            int startingCard = playerData.getStartingCard().getId();
            String pathSide = null;
            if (startingCard > 0) {
                pathSide = String.format("GUI/images/cards.nogit/front/%03d.png", startingCard);
            } else {
                pathSide = String.format("GUI/images/cards.nogit/back/%03d.png", -startingCard);
            }
            ImageView image = new ImageView(pathSide);
            //image.setId(String.valueOf(i));
            image.setFitHeight(67.3);
            image.setFitWidth(100);
            image.preserveRatioProperty();

            StackPane stackPane = new StackPane(image);

            board.add(stackPane, center.x(), center.y());
            GridPane.setHalignment(stackPane, HPos.CENTER);
            GridPane.setValignment(stackPane, VPos.CENTER);
        } else {
            for (Coordinates coordinates : playerData.getBoard().keySet()) {
                int id = playerData.getBoard().get(coordinates).getId();
                String pathSide = null;
                if (id > 0) {
                    pathSide = String.format("GUI/images/cards.nogit/front/%03d.png", id);
                } else {
                    pathSide = String.format("GUI/images/cards.nogit/back/%03d.png", -id);
                }
                ImageView image = new ImageView(pathSide);
                //image.setId(String.valueOf(i));
                image.setFitHeight(67.3);
                image.setFitWidth(100);
                image.preserveRatioProperty();

                StackPane stackPane = new StackPane(image);

                board.add(stackPane, calculateBoardCoordinates(coordinates).x(), calculateBoardCoordinates(coordinates).y());
                GridPane.setHalignment(stackPane, HPos.CENTER);
                GridPane.setValignment(stackPane, VPos.CENTER);
            }
        }

        setupValidPlacements();
        setupHandCardClickHandlers();
    }

    private void fetchData() {
        try {
            client.fetchClientHand();
            client.fetchCommonObjectives();
            client.fetchValidPlacements();
            client.fetchPlayersBoards();
            client.fetchPlayersPlacingOrder();
            client.fetchPlayersResources();
            client.fetchScoreMap();
            client.fetchGameState();
            client.fetchVisibleCardsAndDecks();
            client.fetchOpponentsHandColor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setHand() {
        int firstHandCardId = playerData.getClientHand().get(0).getId();
        String imagePathFirst = String.format("GUI/images/cards.nogit/front/%03d.png", firstHandCardId);
        Image imageFirst = new Image(imagePathFirst);
        firstHandCard.setImage(imageFirst);

        int secondHandCardId = playerData.getClientHand().get(1).getId();
        String imagePathSecond = String.format("GUI/images/cards.nogit/front/%03d.png", secondHandCardId);
        Image imageSecond = new Image(imagePathSecond);
        secondHandCard.setImage(imageSecond);

        int thirdHandCardId = playerData.getClientHand().get(2).getId();
        String imagePathThird = String.format("GUI/images/cards.nogit/front/%03d.png", thirdHandCardId);
        Image imageThird = new Image(imagePathThird);
        thirdHandCard.setImage(imageThird);
    }

    private void setPlateau() {
        String path = "GUI/images/score.nogit/plateau.png";
        Image image = new Image(path);
        plateau.setImage(image);
    }

    public void setClient(ClientInterface client) {
        this.client = client;
        playerData = client.getPlayerData();
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
                board.getColumnCount() / 2,
                board.getRowCount() / 2
        );
    }

    private void setupHandCardClickHandlers() {
        firstHandCard.setOnMouseClicked(event -> {
            selectedHandCard = firstHandCard;
        });

        secondHandCard.setOnMouseClicked(event -> {
            selectedHandCard = secondHandCard;
        });

        thirdHandCard.setOnMouseClicked(event -> {
            selectedHandCard = thirdHandCard;
        });
    }

    private void setupValidPlacements() {
        for (StackPane pane : validPlacementPanes.values()) {
            board.getChildren().remove(pane);
        }
        validPlacementPanes.clear();

        for (int i = 0; i < playerData.getValidPlacements().size(); i++) {
            Coordinates boardCoordinates = calculateBoardCoordinates(playerData.getValidPlacements().get(i));
            System.out.println(boardCoordinates);

            ImageView imageView = new ImageView();
            imageView.setId(String.valueOf(i));
            imageView.setFitHeight(67.3);
            imageView.setFitWidth(100);
            imageView.preserveRatioProperty();

            StackPane stackPane = new StackPane(imageView);
            stackPane.setStyle("-fx-border-color: #432918; -fx-border-width: 4;");

            stackPane.setOnMouseClicked(event -> {
                int selectedCardId;

                if (selectedHandCard != null) {
                    if (selectedHandCard == firstHandCard) {
                        selectedCardId = playerData.getClientHand().getFirst().getId();
                    } else if (selectedHandCard == secondHandCard) {
                        selectedCardId = playerData.getClientHand().get(1).getId();
                    } else {
                        selectedCardId = playerData.getClientHand().getLast().getId();
                    }

                    String imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", selectedCardId);
                    Image cardImage = new Image(imagePath);

                    imageView.setImage(cardImage);
                    stackPane.setStyle("");

                    try {
                        client.placeCard(playerData.getValidPlacements().get(Integer.parseInt(imageView.getId())), selectedCardId);
                        selectedHandCard = null;
                        validPlacementPanes.remove(boardCoordinates);
                        changeDrawCardScene();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            board.add(stackPane, boardCoordinates.x(), boardCoordinates.y());
            GridPane.setHalignment(stackPane, HPos.CENTER);
            GridPane.setValignment(stackPane, VPos.CENTER);
            validPlacementPanes.put(boardCoordinates, stackPane);
        }
    }

    private void changeDrawCardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/DrawCardScene.fxml"));
            DrawCardController controller = new DrawCardController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = null;
            scene = new Scene(loader.load(), 1600, 900);
            Stage stage = (Stage) board.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
