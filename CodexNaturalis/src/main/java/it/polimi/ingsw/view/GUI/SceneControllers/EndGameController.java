package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The EndGameController class manages the end game screen in the GUI.
 * It displays the final scores of players and announces the winner.
 */
public class EndGameController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    VBox playerScoreContainer;

    @FXML
    Label winMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client.fetchScoreMap();
            client.fetchPlayersColors();
            List<Map.Entry<String, Integer>> entryList = client.getScoreMap().entrySet().stream().sorted((e1, e2) -> e2.getValue() - e1.getValue()).toList();
            Color playerColor;

            if (entryList.getFirst().getKey().equals(client.getUsername())) {
                winMessage.setText("YOU WON! CONGRATULATIONS!");
            } else {
                winMessage.setText("YOU LOST! " + entryList.getFirst().getKey() + " WON!");
            }

            for (Map.Entry<String, Integer> entry : entryList) {
                if (entry.getKey().equals(client.getUsername())) {
                    playerColor = playerData.getPlayerColor();
                } else {
                    playerColor = ((OpponentData) client.getOpponentData().get(entry.getKey())).getPlayerColor();
                }

                Label nameLabel = new Label(entry.getKey());
                nameLabel.setStyle(String.format("-fx-font-weight: bold; -fx-text-fill: %s; -fx-font-family: Trattatello; -fx-font-size: 50px;", playerColor));

                Label scoreLabel = new Label(": " + entry.getValue());
                scoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #432918; -fx-font-family: Trattatello; -fx-font-size: 50px;");

                HBox playerInfo = new HBox(nameLabel, scoreLabel);
                playerInfo.setAlignment(Pos.CENTER);
                playerScoreContainer.getChildren().add(playerInfo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the client interface and initializes player data for the end game screen.
     *
     * @param client the client interface to communicate with the server
     */
    public void setClient(ClientInterface client) {
        this.client = client;
        this.playerData = client.getPlayerData();
    }
}
