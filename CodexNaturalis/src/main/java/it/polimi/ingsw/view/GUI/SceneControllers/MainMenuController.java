package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainMenuController implements Initializable {
    private ClientInterface client;

    @FXML
    private VBox gameButtonsContainer;

    @FXML
    protected void changeCreateGameScene(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ConnectionScene.fxml"));
            Scene scene = new Scene(loader.load(), 1600, 900);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/LoginScene.fxml"));
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
            System.out.println("Client connected");
            try {
                client.fetchAvailableGames();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (UUID uuid : client.getAvailableGames()) {
                System.out.println(uuid);
                Button button = new Button(uuid.toString());
                gameButtonsContainer.getChildren().add(button);
            }
        }
        System.out.println("MainMenuController initialized");
    }
}
