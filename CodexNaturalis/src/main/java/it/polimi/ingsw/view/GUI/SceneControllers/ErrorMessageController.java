package it.polimi.ingsw.view.GUI.SceneControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Controller class for displaying error messages in a GUI.
 * This class manages the display of error messages in an overlay pane
 * within a parent stack pane. The error message is shown with a semi-transparent
 * background to focus user attention on the message.
 */
public class ErrorMessageController {
    private StackPane overlayPane;
    private StackPane parentStackPane;

    @FXML
    private Text messageText;

    /**
     * Sets the parent stack pane where the error message overlay will be added.
     *
     * @param parentStackPane the parent stack pane
     */
    public void setParentStackPane(StackPane parentStackPane) {
        this.parentStackPane = parentStackPane;
    }

    /**
     * Sets the overlay pane that contains the error message.
     *
     * @param overlayPane the overlay pane
     */
    public void setOverlayPane(StackPane overlayPane) {
        this.overlayPane = overlayPane;
    }

    /**
     * Sets the error message to be displayed.
     *
     * @param message the error message to be displayed
     */
    public void setErrorMessage(String message) {
        messageText.setText(message);
    }

    /**
     * Closes the error message window by removing the overlay pane from the parent stack pane.
     *
     * @param event the action event triggering the close operation
     */
    @FXML
    private void closeMessageWindow(ActionEvent event) {
        parentStackPane.getChildren().remove(overlayPane);
    }

    /**
     * Static method to display an error message in a specified parent stack pane.
     * Loads the error message FXML layout, sets the error message, and displays
     * the overlay pane with the error message.
     *
     * @param message the error message to be displayed
     * @param parentStackPane the parent stack pane where the error message overlay will be added
     */
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
            System.out.println("Error in ErrorMessageController");
        }
    }
}