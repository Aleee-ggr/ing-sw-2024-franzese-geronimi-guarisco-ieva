package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseObjectiveController implements Initializable {
    private ClientInterface client;

    @FXML
    private ImageView firstObjective;

    @FXML
    private ImageView secondObjective;

    public void setClient(ClientInterface client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PlayerData playerData = client.getPlayerData();
        int firstObjectiveId = playerData.getStartingObjectives().getFirst().getId();
        String imagePath = String.format("GUI/images/cards/%03d.png", firstObjectiveId);
        System.out.println(imagePath);

        Image image = new Image(imagePath);
        firstObjective.setImage(image);

        int secondObjectiveId = playerData.getStartingObjectives().getLast().getId();
        String imagePathSecond = String.format("GUI/images/cards/%03d.png", secondObjectiveId);
        Image imageSecond = new Image(imagePathSecond);
        secondObjective.setImage(imageSecond);
    }

    public void changeChooseStarting(MouseEvent mouseEvent) {
    }
}
