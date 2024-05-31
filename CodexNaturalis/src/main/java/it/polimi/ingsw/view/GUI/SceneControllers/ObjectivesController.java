package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ObjectivesController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    StackPane tabPane;

    @FXML
    private ImageView personalObjective;

    @FXML
    private ImageView commonObjective1;

    @FXML
    private ImageView commonObjective2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            client.fetchCommonObjectives();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", playerData.getGlobalObjectives().getFirst().getId());
        Image image = new Image(imagePath);
        commonObjective1.setImage(image);

        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", playerData.getGlobalObjectives().getLast().getId());
        image = new Image(imagePath);
        commonObjective2.setImage(image);

        imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", playerData.getPersonalObjective().getId());
        image = new Image(imagePath);
        personalObjective.setImage(image);
    }

    public void setClient(ClientInterface client) {
        this.client = client;
        this.playerData = client.getPlayerData();
    }

    @FXML
    private void closeTab(ActionEvent event) {
        tabPane.getParent().getParent().setVisible(false);
    }
}

