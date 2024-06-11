package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller for the Score scene in the GUI.
 * Manages the display of player scores on the scoreboard.
 */
public class ScoreController implements Initializable, TabController {
    private ClientInterface client;
    private PlayerData playerData;
    private SharedUpdate updater;
    protected Thread updateThread;
    private Map<Integer, Point2D> scoreCoordinates;

    @FXML
    private ImageView plateau;

    @FXML
    private Pane scoreBoard;

    @FXML
    private VBox listOfPlayers;

    @FXML
    StackPane tabPane;

    /**
     * Closes the score tab when the close button is clicked.
     *
     * @param event The ActionEvent triggered by the close button.
     */
    @FXML
    protected void closeTab(ActionEvent event) {
        updateThread.interrupt();
        tabPane.getParent().getParent().setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPlateau();
    }

    /**
     * Sets the client for the controller.
     *
     * @param client The client interface to set.
     */
    public void setClient(ClientInterface client, SharedUpdate updater) {
        this.client = client;
        this.playerData = client.getPlayerData();
        this.updater = updater;
    }
    /**
     * Initializes the score coordinates for positioning markers on the scoreboard.
     */
    private void initializeScoreCoordinates() {
        scoreCoordinates.put(0, new Point2D(54, 528));
        scoreCoordinates.put(1, new Point2D(124, 528));
        scoreCoordinates.put(2, new Point2D(194, 528));
        scoreCoordinates.put(3, new Point2D(229, 464));
        scoreCoordinates.put(4, new Point2D(159, 464));
        scoreCoordinates.put(5, new Point2D(89, 464));
        scoreCoordinates.put(6, new Point2D(19, 464));
        scoreCoordinates.put(7, new Point2D(19, 400));
        scoreCoordinates.put(8, new Point2D(89, 400));
        scoreCoordinates.put(9, new Point2D(159, 400));
        scoreCoordinates.put(10, new Point2D(229, 400));
        scoreCoordinates.put(11, new Point2D(229, 336));
        scoreCoordinates.put(12, new Point2D(159, 336));
        scoreCoordinates.put(13, new Point2D(89, 336));
        scoreCoordinates.put(14, new Point2D(19, 336));
        scoreCoordinates.put(15, new Point2D(19, 272));
        scoreCoordinates.put(16, new Point2D(89, 272));
        scoreCoordinates.put(17, new Point2D(159, 272));
        scoreCoordinates.put(18, new Point2D(229, 272));
        scoreCoordinates.put(19, new Point2D(229, 208));
        scoreCoordinates.put(20, new Point2D(124, 177));
        scoreCoordinates.put(21, new Point2D(19, 208));
        scoreCoordinates.put(22, new Point2D(19, 144));
        scoreCoordinates.put(23, new Point2D(19, 80));
        scoreCoordinates.put(24, new Point2D(59, 28));
        scoreCoordinates.put(25, new Point2D(124, 16));
        scoreCoordinates.put(26, new Point2D(188, 28));
        scoreCoordinates.put(27, new Point2D(229, 80));
        scoreCoordinates.put(28, new Point2D(229, 144));
        scoreCoordinates.put(29, new Point2D(124, 94));
    }

    @FXML
    private void setPlateau() {
        scoreBoard.getChildren().clear();
        scoreCoordinates = new HashMap<>();
        initializeScoreCoordinates();

        try {
            client.fetchScoreMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String imagePath = "GUI/images/score.nogit/plateau.png";
        Image image = new Image(imagePath);
        plateau.setImage(image);

        Map<String, Integer> scoreMap = client.getScoreMap();
        System.out.println(scoreMap);
        listOfPlayers.getChildren().clear();
        Map<Integer, Integer> scoreCount = new HashMap<>();

        scoreMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {

                    Image markerImage;
                    if (entry.getKey().equals("2")) {
                        markerImage = new Image("GUI/images/score.nogit/CODEX_pion_bleu.png");
                    } else {
                        markerImage = new Image("GUI/images/score.nogit/CODEX_pion_rouge.png");
                    }
                    ImageView scorePion = new ImageView(markerImage);
                    scorePion.setFitHeight(50);
                    scorePion.setFitWidth(50);

                    int count = scoreCount.getOrDefault(entry.getValue(), 0);
                    scoreCount.put(entry.getValue(), count + 1);

                    scorePion.setLayoutX(scoreCoordinates.get(entry.getValue()).getX());
                    scorePion.setLayoutY(scoreCoordinates.get(entry.getValue()).getY() + count * -5);

                    scoreBoard.getChildren().add(scorePion);

                    Label label = new Label(entry.getKey() + ": " + entry.getValue());
                    label.setStyle("-fx-font-weight: bold; -fx-text-fill: #432918; -fx-font-family: Trattatello; -fx-font-size: 50px;");
                    listOfPlayers.getChildren().add(label);
                });

    }

    @Override
    public void update() {
        Platform.runLater(this::setPlateau);
    }
}