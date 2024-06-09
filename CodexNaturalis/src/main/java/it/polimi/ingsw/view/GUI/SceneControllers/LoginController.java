package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the login scene in the GUI.
 * This class manages user interactions for the login process and handles scene transitions.
 * It validates user credentials and navigates to the main menu or connection scene based on user actions.
 */
public class LoginController implements Initializable {
    private ClientInterface client;

    @FXML
    StackPane root;

    @FXML
    ImageView backgroundImage;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Text loginError;

    @FXML
    private Text loginErrorMessage;

    /**
     * Changes the scene to the main menu scene when the corresponding button is clicked.
     * Sets the client credentials based on the entered username and password.
     *
     * @param event the action event that triggered the method
     */
    @FXML
    protected void changeMainMenuScene(ActionEvent event) {
        boolean valid = false;
        try {
            valid = client.checkCredentials(usernameField.getText(), passwordField.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (valid) {
            client.setCredentials(usernameField.getText(), passwordField.getText());
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/MainMenu.fxml"));
                MainMenuController controller = new MainMenuController();
                controller.setClient(client);
                loader.setController(controller);
                Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loginError.setVisible(true);
            loginErrorMessage.setVisible(true);
        }
    }

    /**
     * Changes the scene to the connection scene.
     *
     * @param event the action event that triggered the method
     */
    @FXML
    protected void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/ConnectionScene.fxml"));
            Scene scene = new Scene(loader.load(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the client for the controller.
     *
     * @param client the client interface to set
     */
    protected void setClient(ClientInterface client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());
    }
}

