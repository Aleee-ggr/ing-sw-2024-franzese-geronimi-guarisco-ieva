package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The ChooseColorController class handles the color selection interface in the GUI.
 * It allows players to choose their player color and proceed to the game screen.
 */
public class ChooseColorController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    HBox pionsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client.fetchAvailableColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Color playerColor: playerData.getAvailableColors()) {
            ImageView pion = null;

            switch (playerColor) {
                case RED -> pion = new ImageView("GUI/images/score.nogit/CODEX_pion_rouge.png");
                case BLUE -> pion = new ImageView("GUI/images/score.nogit/CODEX_pion_bleu.png");
                case YELLOW -> pion = new ImageView("GUI/images/score.nogit/CODEX_pion_jaune.png");
                case GREEN -> pion = new ImageView("GUI/images/score.nogit/CODEX_pion_vert.png");
            }

            pion.setFitWidth(300);
            pion.setFitHeight(300);
            pion.preserveRatioProperty();

            pion.setOnMouseClicked(event -> {
                try {
                    client.choosePlayerColor(playerColor);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameScene.fxml"));
                    GameController controller = new GameController();
                    controller.setClient(client);
                    loader.setController(controller);
                    Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                    Stage stage = (Stage) pionsContainer.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setMaxWidth(3840);
                    stage.setMaxHeight(2160);
                    stage.setFullScreen(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            pionsContainer.getChildren().add(pion);
        }
    }

    /**
     * Sets the client interface and initializes the player data.
     *
     * @param client the client interface to communicate with the server
     */
    public void setClient(ClientInterface client) {
        this.client = client;
        this.playerData = client.getPlayerData();
    }
}
