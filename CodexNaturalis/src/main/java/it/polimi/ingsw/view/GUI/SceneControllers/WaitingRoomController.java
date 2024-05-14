package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.concurrent.Task;
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
import java.util.ResourceBundle;

public class WaitingRoomController implements Initializable {
    private ClientInterface client;

    @FXML
    private StackPane listOfPlayers;

    /**
     * Changes the scene back to the Main Menu when the user clicks "Back".
     * @param event The ActionEvent triggered by the user's interaction.
     */
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

    /**
     * Sets the client for the controller.
     * @param client the client interface to set
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }

    /**
     * Initializes the Waiting Room scene.
     * Fetches the list of players from the server and displays them.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        if (client != null) {
           /* try {
                client.fetchPlayers();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ArrayList<String> playersList = client.getPlayers();
            for (String player : playersList) {
                System.out.println(player);
            }*/

            Label playerLabel = new Label(client.getUsername());
            playerLabel.setStyle("-fx-font-weight: bold;" + "-fx-text-fill: #432918;" + "-fx-font-family: Trattatello;" + "-fx-font-size: 30px;");
            listOfPlayers.getChildren().add(playerLabel);

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
                    client.fetchStartingObjectives();
                    client.fetchStartingCard();
                } catch (IOException e) {throw new RuntimeException(e);}

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ChooseObjectiveScene.fxml"));
                    ChooseObjectiveController controller = new ChooseObjectiveController();
                    controller.setClient(client);
                    loader.setController(controller);
                    Scene scene = new Scene(loader.load(), 1600, 900);
                    Stage stage = (Stage) listOfPlayers.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            new Thread(waitUpdateTask).start();
        }
    }

    /**
     * Changes the scene back to the Main Menu.
     * @param event The ActionEvent triggered by the user's interaction.
     */
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