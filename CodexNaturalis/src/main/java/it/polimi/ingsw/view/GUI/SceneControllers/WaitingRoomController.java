package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WaitingRoomController implements Initializable {
    private ClientInterface client;

    @FXML
    private StackPane listofPlayers;

    @FXML
    protected void changeGameScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/MainMenu.fxml"));
            MainMenuController controller = new MainMenuController();
            controller.setClient(client);
            loader.setController(controller);
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

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        if (client != null) {
            try {
                client.fetchPlayers();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ArrayList<String> playersList = client.getPlayers();
            for (String player : playersList) {
                System.out.println(player);
                Label playerLabel = new Label(player);
                playerLabel.setStyle("-fx-font-weight: bold");
                listofPlayers.getChildren().add(playerLabel);
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

}