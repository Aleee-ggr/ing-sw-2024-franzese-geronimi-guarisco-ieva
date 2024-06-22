package it.polimi.ingsw.view.GUI.SceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ErrorMessageController {
    @FXML
    private Text messageText;

    private StackPane overlayPane;
    private StackPane parentStackPane;

    public void setParentStackPane(StackPane parentStackPane) {
        this.parentStackPane = parentStackPane;
    }

    public void setOverlayPane(StackPane overlayPane) {
        this.overlayPane = overlayPane;
    }

    public void setErrorMessage(String message) {
        messageText.setText(message);
    }

    @FXML
    private void closeMessageWindow(ActionEvent event) {
        parentStackPane.getChildren().remove(overlayPane);
    }

    public static void showErrorMessage(String message, StackPane parentStackPane) {
        try {
            FXMLLoader loader = new FXMLLoader(ErrorMessageController.class.getResource("/GUI/fxml/ErrorMessage.fxml"));
            Parent errorMessageRoot = loader.load();

            ErrorMessageController controller = loader.getController();
            controller.setErrorMessage(message);

            StackPane overlayPane = new StackPane();
            overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
            overlayPane.setPrefSize(parentStackPane.getWidth(), parentStackPane.getHeight());

            StackPane.setAlignment(errorMessageRoot, javafx.geometry.Pos.CENTER);
            overlayPane.getChildren().add(errorMessageRoot);
            controller.setParentStackPane(parentStackPane);
            controller.setOverlayPane(overlayPane);

            parentStackPane.getChildren().add(overlayPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}