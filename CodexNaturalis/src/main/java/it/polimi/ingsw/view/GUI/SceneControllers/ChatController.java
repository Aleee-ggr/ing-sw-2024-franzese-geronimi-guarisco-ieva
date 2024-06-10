package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private ClientInterface client;
    private PlayerData playerData;

    @FXML
    VBox chatContainer;

    @FXML
    StackPane tabPane;

    @FXML
    TextField messageInput;

    @FXML
    Button sendButton;

    public void setClient(ClientInterface client) {
        this.client = client;
        this.playerData = client.getPlayerData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setChat();
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        if (!messageInput.getText().isEmpty()) {
            try {
                client.postChat(messageInput.getText(), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        setChat();
        messageInput.clear();
    }

    @FXML
    private void setChat() {
        chatContainer.getChildren().clear();
        try {
            client.fetchChat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (ChatMessage chatMessage: client.getChat()) {
            HBox messageHBox = new HBox();
            Text message = new Text(chatMessage.sender() + (chatMessage.receiver()!=null ? " to " + chatMessage.receiver() : "") + ": " + chatMessage.message());
            message.setStyle("-fx-font-family: Trattatello;" +
                             "-fx-font-size: 30;" +
                             "-fx-text-fill: #432918;"
            );

            if (chatMessage.sender().equals(client.getUsername())) {
                messageHBox.setAlignment(Pos.CENTER_RIGHT);
                messageHBox.setPadding(new Insets(0, 7, 0, 0));
            } else {
                messageHBox.setAlignment(Pos.CENTER_LEFT);
                messageHBox.setPadding(new Insets(0, 0, 0, 5));
            }

            messageHBox.getChildren().add(message);
            chatContainer.getChildren().add(messageHBox);
        }
    }

    @FXML
    private void closeTab(ActionEvent event) {
        tabPane.getParent().getParent().setVisible(false);
    }
}
