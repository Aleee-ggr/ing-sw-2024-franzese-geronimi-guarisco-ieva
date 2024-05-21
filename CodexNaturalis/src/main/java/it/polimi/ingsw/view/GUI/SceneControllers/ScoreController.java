package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ScoreController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    private ImageView plateau;

    @FXML
    private VBox listOfPlayers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client.fetchScoreMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String imagePath = "GUI/images/score.nogit/plateau.png";
        Image image = new Image(imagePath);
        plateau.setImage(image);

        Map<String, Integer> scoreMap = client.getScoreMap();
        listOfPlayers.getChildren().clear();
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            Label label = new Label(entry.getKey() + ": " + entry.getValue());
            label.setStyle("-fx-font-weight: bold;" + "-fx-text-fill: #432918;" + "-fx-font-family: Trattatello;" + "-fx-font-size: 50px;");
            listOfPlayers.getChildren().add(label);
        }

    }

    public void setClient(ClientInterface client) {
        this.client = client;
        this.playerData = client.getPlayerData();
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameScene.fxml"));
            GameController controller = new GameController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = null;
            scene = new Scene(loader.load(), 1600, 900);
            Stage stage = (Stage) listOfPlayers.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
