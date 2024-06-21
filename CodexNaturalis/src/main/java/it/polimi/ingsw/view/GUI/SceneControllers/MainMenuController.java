package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller for the main menu scene in the GUI.
 * This class manages user interactions and scene transitions in the main menu,
 * including creating new games, joining existing games, and periodically fetching
 * the list of available games from the server.
 */
public class MainMenuController implements Initializable {
    @FXML
    StackPane root;
    @FXML
    ImageView backgroundImage;
    private ClientInterface client;
    private Timeline fetchGamesTimeline;
    @FXML
    private VBox gameButtonsContainer;

    /**
     * Changes the scene to the create game scene when the corresponding button is clicked.
     *
     * @param event the action event that triggered the method
     */
    @FXML
    protected void changeCreateGameScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/CreateGameScene.fxml"));
            CreateGameController controller = new CreateGameController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the client for the controller.
     *
     * @param client the client interface to set
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }

    /**
     * Initializes the controller.
     * Binds the background image size to the root container size
     * and starts fetching available games periodically if the client is set.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
        if (client != null) {
            fetchGamesPeriodically();
        }
    }

    /**
     * Fetches the available games periodically and updates the game buttons.
     * This method sets up a timeline that triggers the fetch and update actions every 5 seconds.
     */
    private void fetchGamesPeriodically() {
        try {
            client.fetchAvailableGames();
            updateGameButtons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fetchGamesTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            try {
                client.fetchAvailableGames();
                updateGameButtons();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        fetchGamesTimeline.setCycleCount(Timeline.INDEFINITE);
        fetchGamesTimeline.play();
    }

    /**
     * Updates the game buttons based on the available games fetched from the client.
     * This method clears the existing buttons and creates a new button for each available game.
     */
    private void updateGameButtons() {
        gameButtonsContainer.getChildren().clear();
        int i = 0;
        for (Map.Entry<UUID, String> entry: client.getAvailableGames().entrySet()) {
            i++;
            Button button = new Button(entry.getValue());
            button.setStyle("-fx-background-color: ffffff;" + "-fx-border-color: black;" + "-fx-border-width: 2;" + "-fx-border-style: solid;" + "-fx-pref-height: 100;" + "-fx-pref-width: 700;" + "-fx-text-fill: #432918;" + "-fx-font-family: Trattatello;" + "-fx-font-size: 30;" + "-fx-cursor: hand;");
            button.setOnAction(event -> {
                try {
                    boolean joined = client.joinGame(entry.getKey());
                    if (joined) {
                        stopFetchingGames();
                        try {
                            client.fetchGameState();
                            client.fetchPlayers();
                            client.fetchPersonalObjective();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        FXMLLoader loader;
                        switch (client.getGameState()) {
                            case SETUP:
                                loader = new FXMLLoader(getClass().getResource("/GUI/fxml/WaitingRoom.fxml"));
                                WaitingRoomController controller = new WaitingRoomController();
                                controller.setClient(client);
                                loader.setController(controller);
                                Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                break;
                            case MAIN:
                                loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameScene.fxml"));
                                GameController gameController = new GameController();
                                gameController.setClient(client);
                                loader.setController(gameController);
                                Scene scene1 = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                                Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage1.setScene(scene1);
                                stage1.setMaxWidth(3840);
                                stage1.setMaxHeight(2160);
                                stage1.setFullScreen(true);
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            gameButtonsContainer.getChildren().add(button);
        }
    }

    /**
     * Stops the periodic fetching of available games.
     */
    private void stopFetchingGames() {
        if (fetchGamesTimeline != null) {
            fetchGamesTimeline.stop();
        }
    }
}
