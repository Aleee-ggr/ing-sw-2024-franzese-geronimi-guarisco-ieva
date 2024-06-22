package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the waiting room scene in the GUI.
 * Manages the display of player list and transitions to the next scene when all players have joined.
 */
public class WaitingRoomController implements Initializable {
    private ClientInterface client;
    private Timeline fetchPlayersTimeline;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @FXML
    private VBox listOfPlayers;

    /**
     * Sets the client for the controller.
     *
     * @param client the client interface to set
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }

    /**
     * Initializes the Waiting Room scene.
     * Fetches the list of players from the server and displays them.
     *
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
        if (client != null) {
            fetchPlayersPeriodically();
            waitForUpdate();
        }
    }

    /**
     * Initiates periodic fetching of player list from the server.
     * Updates the list of players displayed in the GUI.
     */
    private void fetchPlayersPeriodically() {
        try {
            client.fetchPlayers();
            updatePlayersList();
        } catch (IOException e) {
            ErrorMessageController.showErrorMessage("Impossible to fetch data from the server!", root);
        }
        fetchPlayersTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            try {
                client.fetchPlayers();
                updatePlayersList();
            } catch (IOException e) {
                ErrorMessageController.showErrorMessage("Impossible to fetch data from the server!", root);
            }
        }));
        fetchPlayersTimeline.setCycleCount(Timeline.INDEFINITE);
        fetchPlayersTimeline.play();
    }

    /**
     * Initiates waiting for all players to join the game.
     * Upon successful completion, transitions to the next scene.
     */
    private void waitForUpdate() {
        Task<Void> waitUpdateTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                client.waitUpdate();
                return null;
            }
        };

        waitUpdateTask.setOnSucceeded(event -> {
            try {
                client.fetchPlayers();
                updatePlayersList();
            } catch (IOException e) {
                ErrorMessageController.showErrorMessage("Impossible to fetch data from the server!", root);
            }

            if (fetchPlayersTimeline != null) {
                fetchPlayersTimeline.stop();
            }

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> {
                try {
                    stopFetchingPlayers();
                    client.fetchStartingObjectives();
                    client.fetchStartingCard();
                    changeToChooseObjectiveScene();
                } catch (IOException ex) {
                    ErrorMessageController.showErrorMessage("Impossible to fetch data from the server!", root);
                }
            });
            pause.play();
        });

        new Thread(waitUpdateTask).start();
    }

    /**
     * Transitions to the next scene where players choose their personal objective.
     */
    private void changeToChooseObjectiveScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ChooseObjectiveScene.fxml"));
            ChooseObjectiveController controller = new ChooseObjectiveController();
            controller.setClient(client);
            loader.setController(controller);
            Stage stage = (Stage) listOfPlayers.getScene().getWindow();
            stage.getScene().setRoot(loader.load());
        } catch (IOException ex) {
            ErrorMessageController.showErrorMessage("Error loading choose personal objective scene!", root);
        }
    }

    /**
     * Updates the list of players displayed in the GUI.
     */
    private void updatePlayersList() {
        ArrayList<String> playersList = client.getPlayers();
        listOfPlayers.getChildren().clear();
        for (String player : playersList) {
            Label playerLabel = new Label(player);
            playerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #432918; -fx-font-family: Trattatello; -fx-font-size: 30px;");
            listOfPlayers.getChildren().add(playerLabel);
        }
    }

    /**
     * Stops periodic fetching of player list.
     */
    private void stopFetchingPlayers() {
        if (fetchPlayersTimeline != null) {
            fetchPlayersTimeline.stop();
        }
    }
}
