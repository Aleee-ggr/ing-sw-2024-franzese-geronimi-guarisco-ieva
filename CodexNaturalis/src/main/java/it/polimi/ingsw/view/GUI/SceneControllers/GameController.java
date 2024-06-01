package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.RotateBoard;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;
    private Coordinates center;
    private boolean frontSide = true;
    private final Map<Coordinates, StackPane> validPlacementPanes = new HashMap<>();
    private static final double ZOOM_FACTOR = 1.1;
    private static final double MIN_SCALE = 0.3;
    private static final double MAX_SCALE = 3.0;

    private ImageView selectedHandCard = null;

    @FXML
    GridPane board;

    @FXML
    ScrollPane scrollPane;

    @FXML
    VBox tabContainer;

    @FXML
    StackPane tabPane;

    @FXML
    Text fungiCount;

    @FXML
    Text animalCount;

    @FXML
    Text insectCount;

    @FXML
    Text plantCount;

    @FXML
    Text manuscriptCount;

    @FXML
    Text inkwellCount;

    @FXML
    Text quillCount;

    @FXML
    HBox handContainer;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
        tabContainer.setVisible(false);
        scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.isControlDown()) {
                double zoomFactor = (event.getDeltaY() > 0) ? ZOOM_FACTOR : 1 / ZOOM_FACTOR;
                double newScaleX = board.getScaleX() * zoomFactor;
                double newScaleY = board.getScaleY() * zoomFactor;

                if (newScaleX >= MIN_SCALE && newScaleX <= MAX_SCALE && newScaleY >= MIN_SCALE && newScaleY <= MAX_SCALE) {
                    board.setScaleX(newScaleX);
                    board.setScaleY(newScaleY);
                }

                event.consume();
            }
        });

        fetchData();
        setupResources();
        calculateBoardCenterCoordinates();
        setHand(frontSide);

        System.out.println(playerData.getValidPlacements());

        for (Coordinates coordinates : playerData.getValidPlacements()) {
            System.out.println(RotateBoard.rotateCoordinates(coordinates, -45));
        }
        System.out.println(playerData.getBoard());



        if (playerData.getBoard().isEmpty()) {
            int startingCard = playerData.getStartingCard().getId();
            String pathSide;
            if (startingCard > 0) {
                pathSide = String.format("GUI/images/cards.nogit/front/%03d.png", startingCard);
            } else {
                pathSide = String.format("GUI/images/cards.nogit/back/%03d.png", -startingCard);
            }
            ImageView image = new ImageView(pathSide);
            image.setFitHeight(100.95);
            image.setFitWidth(150);
            image.preserveRatioProperty();

            StackPane stackPane = new StackPane(image);

            board.add(stackPane, center.x(), center.y());
            GridPane.setHalignment(stackPane, HPos.CENTER);
            GridPane.setValignment(stackPane, VPos.CENTER);
        } else {
            for (Card card: playerData.getOrder()) {
                Coordinates coordinates = playerData.getBoard().inverse().get(card);
                int id = card.getId();
                String pathSide;

                if (id > 0) {
                    pathSide = String.format("GUI/images/cards.nogit/front/%03d.png", id);
                } else {
                    pathSide = String.format("GUI/images/cards.nogit/back/%03d.png", -id);
                }
                ImageView image = new ImageView(pathSide);
                image.setFitHeight(100.95);
                image.setFitWidth(150);
                image.preserveRatioProperty();

                StackPane stackPane = new StackPane(image);

                board.add(stackPane, calculateBoardCoordinates(coordinates).x(), calculateBoardCoordinates(coordinates).y());
                GridPane.setHalignment(stackPane, HPos.CENTER);
                GridPane.setValignment(stackPane, VPos.CENTER);
            }
        }

        setupValidPlacements();
    }

    private void setupResources() {
        Map<Resource, Text> resourceText = new HashMap<>();
        resourceText.put(Resource.FUNGI, fungiCount);
        resourceText.put(Resource.ANIMAL, animalCount);
        resourceText.put(Resource.PLANT, plantCount);
        resourceText.put(Resource.INSECT, insectCount);
        resourceText.put(Resource.QUILL, quillCount);
        resourceText.put(Resource.INKWELL, inkwellCount);
        resourceText.put(Resource.MANUSCRIPT, manuscriptCount);

        for (Map.Entry<Resource, Integer> entry : playerData.getResources().entrySet()) {
            if (entry.getKey() != Resource.NONE && entry.getKey() != Resource.NONCOVERABLE) {
                resourceText.get(entry.getKey()).setText(String.valueOf(entry.getValue()));
            }
        }
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

    private void setHand(boolean frontSide) {
        String imagePath;
        handContainer.getChildren().clear();
        int size = playerData.getClientHand().size();
        int i = 0;
        for (Card card: playerData.getClientHand()) {
            int id = card.getId();
            i++;
            if (frontSide) {
                imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", id);
            } else {
                imagePath = String.format("GUI/images/cards.nogit/back/%03d.png", id);
            }
            ImageView image = new ImageView(imagePath);
            image.setFitHeight(151.5);
            image.setFitWidth(225);
            image.preserveRatioProperty();
            if (frontSide) {
                image.setId(String.valueOf(id));
            } else {
                image.setId(String.valueOf(-id));
            }

            if(size != i) {
                HBox.setMargin(image, new Insets(0, 25, 25, 0));
            } else {
                HBox.setMargin(image, new Insets(0, 0, 25, 0));
            }

            handContainer.getChildren().add(image);

            image.setOnMouseClicked(mouseEvent -> {
                selectedHandCard = image;
            });

            image.setOnDragDetected(event -> {
                selectedHandCard = image;
                Dragboard db = image.startDragAndDrop(TransferMode.MOVE);
                db.setContent(clipboardContent(String.valueOf(id)));
                db.setDragView(image.getImage());
                event.consume();
            });
        }
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

    private ClipboardContent clipboardContent(String text) {
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(text);
        return clipboardContent;
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
            imageView.setFitHeight(100.95);
            imageView.setFitWidth(150);
            imageView.preserveRatioProperty();

            StackPane stackPane = new StackPane(imageView);
            stackPane.setStyle("-fx-border-color: #432918; -fx-border-width: 4;");

            stackPane.setOnMouseClicked(event -> {
                placeCard(stackPane, imageView, boardCoordinates);
            });

            stackPane.setOnDragOver(event -> {
                if (event.getGestureSource() != stackPane && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            stackPane.setOnDragDropped(event -> {
                boolean placed;

                placed = placeCard(stackPane, imageView, boardCoordinates);

                event.setDropCompleted(placed);
                event.consume();
            });

            board.add(stackPane, boardCoordinates.x(), boardCoordinates.y());
            GridPane.setHalignment(stackPane, HPos.CENTER);
            GridPane.setValignment(stackPane, VPos.CENTER);
            validPlacementPanes.put(boardCoordinates, stackPane);
        }
    }

    @FXML
    private void changeDrawCardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/DrawCardScene.fxml"));
            DrawCardController controller = new DrawCardController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = null;
            scene = new Scene(loader.load(), 1920, 1080);
            Stage stage = (Stage) board.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void changeScoreScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ScoreTab.fxml"));
            ScoreController controller = new ScoreController();
            controller.setClient(client);
            loader.setController(controller);
            StackPane scorePane = loader.load();
            tabPane.getChildren().setAll(scorePane);
            tabContainer.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeObjectivesScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ObjectivesTab.fxml"));
            ObjectivesController controller = new ObjectivesController();
            controller.setClient(client);
            loader.setController(controller);
            StackPane objectivesPane = loader.load();
            tabPane.getChildren().setAll(objectivesPane);
            tabContainer.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeMiniBoardScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/MiniBoardTab.fxml"));
            MiniBoardController controller = new MiniBoardController();
            controller.setClient(client);
            loader.setController(controller);
            StackPane miniBoardPane = loader.load();
            tabPane.getChildren().setAll(miniBoardPane);
            tabContainer.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private boolean placeCard(StackPane stackPane, ImageView imageView, Coordinates boardCoordinates) {
        boolean placed;
        String imagePath;
        try {
            placed = client.placeCard(playerData.getValidPlacements().get(Integer.parseInt(imageView.getId())), Integer.parseInt(selectedHandCard.getId()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (placed) {
            if (Integer.parseInt(selectedHandCard.getId()) > 0) {
                imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", Integer.parseInt(selectedHandCard.getId()));
            } else {
                imagePath = String.format("GUI/images/cards.nogit/back/%03d.png", -Integer.parseInt(selectedHandCard.getId()));
            }
            Image cardImage = new Image(imagePath);

            imageView.setImage(cardImage);
            stackPane.setStyle("");
            selectedHandCard = null;
            validPlacementPanes.remove(boardCoordinates);

            fetchData();
            setupResources();
            setHand(frontSide);

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> { changeDrawCardScene(); });
            pause.play();
        }

        return placed;
    }

    @FXML
    private void flipCards(ActionEvent event) {
        if (frontSide) {
            setHand(false);
            frontSide = false;
        } else {
            setHand(true);
            frontSide = true;
        }
    }
}
