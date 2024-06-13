package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseColorController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    HBox colorsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client.fetchAvailableColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Color playerColor: playerData.getAvailableColors()) {
            Rectangle rectangle = new Rectangle(397.2, 397.2);
            rectangle.setFill(Paint.valueOf(playerColor.toString()));

            rectangle.setOnMouseClicked(event -> {
                try {
                    client.choosePlayerColor(playerColor);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/GameScene.fxml"));
                    GameController controller = new GameController();
                    controller.setClient(client);
                    loader.setController(controller);
                    Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                    Stage stage = (Stage) colorsContainer.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            colorsContainer.getChildren().add(rectangle);
        }
    }

    public void setClient(ClientInterface client) {
        this.client = client;
        this.playerData = client.getPlayerData();
    }
}

