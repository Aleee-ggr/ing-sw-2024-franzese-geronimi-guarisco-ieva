package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateGameController {
    private ClientInterface client;

    @FXML
    private RadioButton twoPlayers;

    @FXML
    private RadioButton threePlayers;

    @FXML
    private RadioButton fourPlayers;

    @FXML
    private ToggleGroup numPlayers;

    @FXML
    protected void changeWaitingRoomScene(ActionEvent event) {
        RadioButton selected = (RadioButton) numPlayers.getSelectedToggle();
        if (selected != null) {
            try {
                int numPlayer = Integer.parseInt((String) selected.getUserData());
                client.newGame(numPlayer);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/WaitingRoom.fxml"));
                WaitingRoomController controller = new WaitingRoomController();
                controller.setClient(client);
                loader.setController(controller);
                Scene scene = new Scene(loader.load(), 1600, 900);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/MainMenu.fxml"));
            Scene scene = new Scene(loader.load(), 1600, 900);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClient(ClientInterface client) {
        this.client = client;
    }
}