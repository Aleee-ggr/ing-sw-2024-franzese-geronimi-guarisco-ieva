package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.ClientData;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.RotateBoard;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Glow;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * GameController class manages the GUI for the game.
 * It handles various aspects of the game interface, including:
 * - Displaying the game board and player boards.
 * - Managing player resources and updating resource counts.
 * - Handling user interactions through buttons and drag-and-drop functionalities.
 * - Controlling game state updates and threading for real-time updates.
 * - Implementing multiple tabs for different game functionalities (e.g., drawing cards, scoring).
 * - Supporting zoom and scroll functionalities for the game board.
 * - Displaying chat messages and indicating new messages with a visual cue.
 * - Managing opponent data display and setup.
 * - Implementing game-specific UI elements such as buttons for flipping cards and centering the board.
 *
 * This controller class uses JavaFX for UI components and threading mechanisms for real-time updates.
 * It interacts with a ClientInterface for game data and manages data updates through SharedUpdate and other helper classes.
 *
 * The game-specific logic for placing cards, updating game state, and handling turns is embedded in this class.
 */
public class GameController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;
    private SharedUpdate updater;
    private Coordinates center;
    protected boolean frontSide = true;
    private final Map<Coordinates, StackPane> validPlacementPanes = new HashMap<>();
    private static final double ZOOM_FACTOR = 1.1;
    private static final double MIN_SCALE = 0.3;
    private static final double MAX_SCALE = 3.0;

    private HashMap<String, TabController> tabControllers;
    private String activeTab;

    private ImageView selectedHandCard = null;

    protected List<ChatMessage> messages = null;

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
    VBox buttonsContainer;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @FXML
    Text username;

    @FXML
    Text turnMessage;

    @FXML
    Button centerBoardButton;

    @FXML
    Button flipButton;

    @FXML
    Circle newMessages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updater = new SharedUpdate();
        new ClientUpdate(client, this, updater).start();
        new ClientRenderUpdateThread(client, this, updater).start();
        tabControllers = new HashMap<>();
        tabControllers.put("DrawCard", new DrawCardController());
        tabControllers.put("Score", new ScoreController());
        tabControllers.put("Chat", new ChatController());
        tabControllers.put("MiniBoard", new MiniBoardController());
        tabControllers.put("Objectives", new ObjectivesController());
        activeTab = null;

        scrollPane.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            centerBoardButton();
        });
        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            centerBoardButton();
        });

        setPersonalData();
        setEndGameThread();
        setChatThread();
    }

    /**
     * Sets up resources display on the UI based on the player's resource counts.
     * Uses mappings of resource types to corresponding UI Text elements for display.
     */
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
                resourceText.get(entry.getKey()).setStyle("-fx-font-family: Trattatello;" + "-fx-font-weight: bold;" + "-fx-text-fill: #432918;" + "-fx-font-size: 20");
            }
        }
    }

    /**
     * Sets up personal data for the current client user.
     * Sets username, initializes board setup, displays starting card, and sets up valid placement areas.
     */
    private void setPersonalData() {
        username.setText(client.getUsername());
        turnMessage.setText("Waiting for your Turn...");
        board.getChildren().clear();
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
        setHand(client.getUsername(), frontSide);

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
            setPion(client.getUsername(), stackPane);

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
                if (card.equals(playerData.getOrder().getFirst())) {
                    setPion(client.getUsername(), stackPane);
                }

                board.add(stackPane, calculateBoardCoordinates(coordinates).x(), calculateBoardCoordinates(coordinates).y());
                GridPane.setHalignment(stackPane, HPos.CENTER);
                GridPane.setValignment(stackPane, VPos.CENTER);
            }
        }

        setupValidPlacements();
    }

    /**
     * Sets up a thread to handle chat messages display and updates.
     * Monitors for new chat messages and updates the UI accordingly.
     */
    private void setChatThread() {
        Task<Void> chatThread = new Task<>() {
            @Override
            protected Void call() throws Exception {
                boolean running = true;
                while (running) {
                    if (messages != null && client.getChat() != null) {
                        if (!messages.equals(client.getChat())) {
                            newMessages.setVisible(true);
                            messages = client.getChat();
                        }
                    } else {
                        messages = client.getChat();
                    }

                    if (client.getGameState().equals(GameState.STOP)) {
                        running = false;
                    }
                    Thread.sleep(1000);
                }
                return null;
            }
        };

        new Thread(chatThread).start();
    }

    /**
     * Sets up opponent data display on the board.
     * Displays opponent's board and initializes UI components for opponent-specific data.
     *
     * @param usernameOpponent The username of the opponent whose data needs to be displayed.
     */
    public void setupOpponentData(String usernameOpponent) {
        ClientData opponentData = client.getOpponentData().get(usernameOpponent);

        if (buttonsContainer.getChildren().size() == 1) {
            Button button = new Button();
            button.setText("YOUR BOARD");
            button.setPrefWidth(250);
            button.setPrefHeight(78);
            button.setId("yourBoard");

            Glow glow = new Glow();
            glow.setLevel(0.3);
            button.setEffect(glow);

            button.getStyleClass().add("button");

            button.setOnMouseClicked(event -> {
                setPersonalData();
                centerBoard();
                board.setScaleX(1.0);
                board.setScaleY(1.0);
                setActiveTab(null);
                buttonsContainer.getChildren().remove(button);
                buttonsContainer.getChildren().add(flipButton);
            });
            buttonsContainer.getChildren().add(button);
        }

        board.getChildren().clear();

        for (Card card: opponentData.getOrder()) {
            Coordinates coordinates = opponentData.getBoard().inverse().get(card);
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
            if (card.equals(opponentData.getOrder().getFirst())) {
                setPion(usernameOpponent, stackPane);
            }

            board.add(stackPane, calculateBoardCoordinates(coordinates).x(), calculateBoardCoordinates(coordinates).y());
            GridPane.setHalignment(stackPane, HPos.CENTER);
            GridPane.setValignment(stackPane, VPos.CENTER);
        }

        setHand(client.getUsername(), frontSide);
    }

    /**
     * Fetches initial game data from the server.
     * Fetches various game-related data such as player hands, objectives, valid placements, etc.
     *
     * @throws RuntimeException If there is an error in fetching data from the server.
     */
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
            client.fetchAvailableColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up the display of cards in the hand container based on the player's or opponent's hand.
     * If the username matches the client's username, displays the client's hand with card images.
     * If the username does not match, displays the opponent's hand with generic card backs.
     *
     * @param username   The username of the player whose hand is to be displayed.
     * @param frontSide  Boolean flag indicating whether to display card fronts (true) or backs (false).
     *                   Fronts are displayed for the client's hand, backs for opponents.
     * @throws RuntimeException If there is an error in fetching data from the server during initialization.
     */
    protected void setHand(String username, boolean frontSide) {
        try {
            client.fetchClientHand();
            client.fetchOpponentsHandColor();
            client.fetchOpponentsHandType();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String imagePath = "";
        handContainer.getChildren().clear();
        int size = playerData.getClientHand().size();
        int i = 0;
        if (username.equals(client.getUsername())) {
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

                image.getStyleClass().add("card");
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
        } else {
            ArrayList<Resource> handColor = ((OpponentData) client.getOpponentData().get(username)).getHandColor();
            for (int j = 0; j < handColor.size(); j++) {
                Resource resource = handColor.get(j);
                if (!((OpponentData) client.getOpponentData().get(username)).getHandIsGold().get(j)) {
                    switch (resource) {
                        case FUNGI -> imagePath = "GUI/images/cards.nogit/back/001.png";
                        case PLANT -> imagePath = "GUI/images/cards.nogit/back/011.png";
                        case ANIMAL -> imagePath = "GUI/images/cards.nogit/back/021.png";
                        case INSECT -> imagePath = "GUI/images/cards.nogit/back/031.png";
                    }
                } else {
                    switch (resource) {
                        case FUNGI -> imagePath = "GUI/images/cards.nogit/back/041.png";
                        case PLANT -> imagePath = "GUI/images/cards.nogit/back/051.png";
                        case ANIMAL -> imagePath = "GUI/images/cards.nogit/back/061.png";
                        case INSECT -> imagePath = "GUI/images/cards.nogit/back/071.png";
                    }
                }

                ImageView image = new ImageView(imagePath);
                image.setFitHeight(151.5);
                image.setFitWidth(225);
                image.preserveRatioProperty();

                handContainer.getChildren().add(image);
            }
        }
    }

    /**
     * Sets the client interface and initializes player data based on the client's data.
     *
     * @param client The client interface to set.
     */
    public void setClient(ClientInterface client) {
        this.client = client;
        playerData = client.getPlayerData();
    }

    /**
     * Calculates the board coordinates after rotating them by -45 degrees.
     *
     * @param coordinates The original coordinates to rotate.
     * @return The rotated board coordinates.
     */
    private Coordinates calculateBoardCoordinates(Coordinates coordinates) {
        coordinates = RotateBoard.rotateCoordinates(coordinates, -45);
        return new Coordinates(
                center.x() + coordinates.x(),
                center.y() - coordinates.y()
        );
    }

    /**
     * Calculates the center coordinates of the board based on its dimensions.
     * The center coordinates are used for positioning elements relative to the board.
     */
    private void calculateBoardCenterCoordinates() {
        center = new Coordinates(
                board.getColumnCount() / 2,
                board.getRowCount() / 2
        );
    }

    /**
     * Creates a ClipboardContent object and sets the provided text as its string content.
     *
     * @param text The text to be set in the ClipboardContent.
     * @return The created ClipboardContent object with the text set.
     */
    private ClipboardContent clipboardContent(String text) {
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(text);
        return clipboardContent;
    }

    /**
     * Sets up valid placements on the board for player's cards.
     * This method clears existing placement panes, calculates board coordinates,
     * creates ImageView objects for each valid placement, and sets up event handlers
     * for mouse clicks and drag-and-drop operations.
     */
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

    /**
     * Changes the active tab to the Draw Card tab, loading it if necessary.
     * Updates tab dimensions and sets the client and controllers accordingly.
     * Handles tab visibility and sets it as the active tab.
     *
     * @throws RuntimeException If there is an error loading or setting up the Draw Card tab.
     */
    @FXML
    private void changeDrawCardScene() {
        try {
            if (activeTab == null || !activeTab.equals("DrawCard")) {
                updateTabDimensions();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/DrawCardTab.fxml"));
                DrawCardController controller = (DrawCardController) tabControllers.get("DrawCard");
                controller.setClient(client, this, updater);
                loader.setController(controller);
                StackPane drawPane = loader.load();
                tabContainer.setPrefWidth(600);
                tabContainer.setPrefHeight(800);
                tabPane.getChildren().setAll(drawPane);
                tabContainer.setVisible(true);
                setActiveTab("DrawCard");
            } else {
                updateTabDimensions();
                setActiveTab(null);
                tabContainer.setVisible(false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Changes the active tab to the Score tab. If the Score tab is already active, hides the tab container.
     *
     * @param event The ActionEvent triggering the tab change.
     */
    @FXML
    private void changeScoreScene(ActionEvent event) {
        try {
            if (activeTab == null || !activeTab.equals("Score")) {
                updateTabDimensions();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ScoreTab.fxml"));
                ScoreController controller = (ScoreController) tabControllers.get("Score");
                controller.setClient(client, this, updater);
                loader.setController(controller);
                StackPane scorePane = loader.load();
                tabPane.getChildren().setAll(scorePane);
                tabContainer.setVisible(true);
                setActiveTab("Score");
            } else {
                setActiveTab(null);
                tabContainer.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the active tab to the Chat tab. If the Chat tab is already active, hides the tab container.
     *
     * @param event The ActionEvent triggering the tab change.
     */
    @FXML
    private void changeChatScene(ActionEvent event) {
        try {
            if (activeTab == null || !activeTab.equals("Chat")) {
                newMessages.setVisible(false);
                tabContainer.setPrefWidth(900);
                tabContainer.setPrefHeight(500);
                tabContainer.setTranslateX((tabContainer.getScene().getWindow().getWidth() - tabContainer.getPrefWidth()) / 2);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ChatTab.fxml"));
                ChatController controller = (ChatController) tabControllers.get("Chat");
                controller.setClient(client, this, updater);
                loader.setController(controller);
                StackPane chatPane = loader.load();
                tabPane.getChildren().setAll(chatPane);
                tabContainer.setVisible(true);
                setActiveTab("Chat");
            } else {
                updateTabDimensions();
                setActiveTab(null);
                tabContainer.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the active tab to the Objectives tab. If the Objectives tab is already active, hides the tab container.
     *
     * @param event The ActionEvent triggering the tab change.
     */
    @FXML
    private void changeObjectivesScene(ActionEvent event) {
        try {
            if (activeTab == null || !activeTab.equals("Objectives")) {
                updateTabDimensions();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ObjectivesTab.fxml"));
                ObjectivesController controller = (ObjectivesController) tabControllers.get("Objectives");
                controller.setClient(this, client);
                loader.setController(controller);
                StackPane objectivesPane = loader.load();
                tabPane.getChildren().setAll(objectivesPane);
                tabContainer.setVisible(true);
                setActiveTab("Objectives");
            } else {
                setActiveTab(null);
                tabContainer.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the active tab to the MiniBoard tab. If the MiniBoard tab is already active, hides the tab container.
     *
     * @param event The ActionEvent triggering the tab change.
     */
    @FXML
    private void changeMiniBoardScene(ActionEvent event) {
        try {
            if (activeTab == null || !activeTab.equals("MiniBoard")) {
                updateTabDimensions();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/MiniBoardTab.fxml"));
                MiniBoardController controller = (MiniBoardController) tabControllers.get("MiniBoard");
                controller.setClient(client, this, updater);
                loader.setController(controller);
                StackPane miniBoardPane = loader.load();
                tabPane.getChildren().setAll(miniBoardPane);
                tabContainer.setVisible(true);
                setActiveTab("MiniBoard");
            } else {
                setActiveTab(null);
                tabContainer.setVisible(false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Places a card on the board based on the selected hand card and board coordinates.
     * Updates the UI with the placed card's image, adjusts styles, and handles subsequent game actions.
     *
     * @param stackPane The StackPane representing the placement area on the board.
     * @param imageView The ImageView displaying the card's image.
     * @param boardCoordinates The Coordinates object specifying the board position.
     * @return true if the card was successfully placed, false otherwise.
     */
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
            setHand(client.getUsername(), frontSide);

            turnMessage.setText("Your Turn: Draw a Card!");

            if (activeTab == null || !activeTab.equals("DrawCard")) {
                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(e -> { changeDrawCardScene(); });
                pause.play();
            }

            setupValidPlacements();
        }

        return placed;
    }

    /**
     * Flips the cards in the hand display between front and back sides.
     * Updates the UI to display the front or back side of the cards.
     *
     * @param event The ActionEvent triggering the card flip.
     */
    @FXML
    private void flipCards(ActionEvent event) {
        if (frontSide) {
            setHand(client.getUsername(), false);
            frontSide = false;
        } else {
            setHand(client.getUsername(), true);
            frontSide = true;
        }
    }

    /**
     * Updates the view of the active tab, if not currently on the Objectives tab.
     * Calls the update method of the corresponding tab controller to refresh its content.
     */
    protected void updateView() {
        if (activeTab != null && !activeTab.equals("Objectives")) {
            tabControllers.get(activeTab).update();
        }
    }

    /**
     * Updates the dimensions of the tab container based on its current width.
     * Adjusts the tab container's preferred width and height and resets its translation.
     */
    private void updateTabDimensions() {
        if (tabContainer.getPrefWidth() == 600) {
            tabContainer.setPrefWidth(350);
            tabContainer.setPrefHeight(1080);
        } else {
            tabContainer.setPrefWidth(350);
            tabContainer.setPrefHeight(1080);
            tabContainer.setTranslateX(0);
        }
    }

    /**
     * Sets the turn message to indicate the player's turn to place a card.
     */
    protected void setTurn() {
        turnMessage.setText("Your Turn: Place a Card!");
    }

    /**
     * Centers the board button based on the current scroll pane values.
     * Adds or removes the center board button dynamically based on scroll pane positions.
     */
    private void centerBoardButton() {
        if (centerBoardButton != null) {
            buttonsContainer.getChildren().remove(centerBoardButton);
            centerBoardButton = null;
        }

        double hValue = scrollPane.getHvalue();
        double vValue = scrollPane.getVvalue();

        boolean showButton = hValue != 0.0 || vValue != 0.0;

        if (showButton) {
            centerBoardButton = new Button("CENTER BOARD");
            centerBoardButton.setPrefWidth(250);
            centerBoardButton.setPrefHeight(78);
            centerBoardButton.setOnAction(event -> {
                centerBoard();
                board.setScaleX(1.0);
                board.setScaleY(1.0);
            });
            Glow glow = new Glow();
            glow.setLevel(0.3);
            centerBoardButton.setEffect(glow);

            centerBoardButton.getStyleClass().add("button");

            buttonsContainer.getChildren().add(centerBoardButton);
        }
    }

    /**
     * Centers the board view within the scroll pane by adjusting horizontal and vertical scroll values.
     * Removes the center board button if it exists.
     */
    protected void centerBoard() {
        double hMax = scrollPane.getHmax();
        double vMax = scrollPane.getVmax();

        double hCenter = hMax / 2;
        double vCenter = vMax / 2;

        scrollPane.setHvalue(hCenter);
        scrollPane.setVvalue(vCenter);

        if (centerBoardButton != null) {
            buttonsContainer.getChildren().remove(centerBoardButton);
            centerBoardButton = null;
        }
    }

    /**
     * Sets the active tab based on the provided tab name.
     *
     * @param activeTab The name of the tab to set as active.
     */
    protected void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    /**
     * Monitors the game state from the client and switches to the end game scene when the game stops.
     */
    private void setEndGameThread() {
        Task<Void> endGameThread = new Task<>() {
            @Override
            protected Void call() throws Exception {
                boolean running = true;
                while (running) {
                    if (client.getGameState().equals(GameState.STOP)) {
                        running = false;
                    }
                    Thread.sleep(1000);
                }
                return null;
            }
        };

        endGameThread.setOnSucceeded(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/EndGameScene.fxml"));
                EndGameController controller = new EndGameController();
                controller.setClient(client);
                loader.setController(controller);
                Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                Stage stage = (Stage) board.getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        new Thread(endGameThread).start();
    }

    /**
     * Sets a player's pawn image on the provided StackPane based on the player's username and color.
     * Determines the appropriate pawn image based on the player's color and starting card.
     *
     * @param username The username of the player.
     * @param stackPane The StackPane on which to set the pawn image.
     */
    private void setPion(String username, StackPane stackPane) {
        ImageView pion = null;
        Color playerColor;
        Integer startingCardId;

        if (username.equals(client.getUsername())) {
            playerColor = playerData.getPlayerColor();
            startingCardId = playerData.getStartingCard().getId();
        } else {
            playerColor = ((OpponentData) client.getOpponentData().get(username)).getPlayerColor();
            startingCardId = client.getOpponentData().get(username).getOrder().getFirst().getId();
        }

        switch (playerColor) {
            case RED -> pion = new ImageView("GUI/images/score.nogit/CODEX_pion_rouge.png");
            case BLUE -> pion = new ImageView("GUI/images/score.nogit/CODEX_pion_bleu.png");
            case YELLOW -> pion = new ImageView("GUI/images/score.nogit/CODEX_pion_jaune.png");
            case GREEN -> pion = new ImageView("GUI/images/score.nogit/CODEX_pion_vert.png");
        }

        pion.setFitWidth(25);
        pion.setFitHeight(25);
        pion.setPreserveRatio(true);

        stackPane.getChildren().add(pion);

        if (client.getPlayers().getFirst().equals(username)) {
            ImageView firstPion = new ImageView("GUI/images/score.nogit/CODEX_pion_noir.png");
            firstPion.setFitWidth(25);
            firstPion.setFitHeight(25);
            firstPion.setPreserveRatio(true);

            stackPane.getChildren().add(firstPion);

            if (startingCardId < 0) {
                StackPane.setMargin(pion, new Insets(0, 32, 0, 0));
                StackPane.setMargin(firstPion, new Insets(0, 0, 0, 32));
            } else {
                StackPane.setMargin(pion, new Insets(0, 55, 0, 0));
                StackPane.setMargin(firstPion, new Insets(0, 0, 0, 55));
            }
        } else {
            if (startingCardId < 0) {
                StackPane.setMargin(pion, new Insets(0, 32, 0, 0));
            } else {
                StackPane.setMargin(pion, new Insets(0, 55, 0, 0));
            }
        }
    }
}