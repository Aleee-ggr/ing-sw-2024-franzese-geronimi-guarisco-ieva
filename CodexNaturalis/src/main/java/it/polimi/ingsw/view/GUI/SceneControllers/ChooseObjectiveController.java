package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseObjectiveController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @FXML
    private ImageView firstObjective;

    @FXML
    private ImageView secondObjective;

    public void setClient(ClientInterface client) {
        this.client = client;
        playerData = client.getPlayerData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
        int firstObjectiveId = playerData.getStartingObjectives().getFirst().getId();
        String imagePath = String.format("GUI/images/cards.nogit/front/%03d.png", firstObjectiveId);
        System.out.println(imagePath);

        Image image = new Image(imagePath);
        firstObjective.setImage(image);

        int secondObjectiveId = playerData.getStartingObjectives().getLast().getId();
        String imagePathSecond = String.format("GUI/images/cards.nogit/front/%03d.png", secondObjectiveId);
        Image imageSecond = new Image(imagePathSecond);
        secondObjective.setImage(imageSecond);
    }

    public void changeChooseStarting(MouseEvent mouseEvent) {
        try {
            if (mouseEvent.getSource() == firstObjective) {
                client.choosePersonalObjective(playerData.getStartingObjectives().getFirst().getId());
            } else if (mouseEvent.getSource() == secondObjective) {
                client.choosePersonalObjective(playerData.getStartingObjectives().getLast().getId());
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ChooseStartingCardSideScene.fxml"));
            ChooseStartingCardSideController controller = new ChooseStartingCardSideController();
            controller.setClient(client);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
