package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable, TabController {
    private ClientInterface client;
    private PlayerData playerData;
    private GameController gameController;
    private SharedUpdate updater;

    @FXML
    VBox chatContainer;

    @FXML
    StackPane tabPane;

    @FXML
    TextField messageInput;

    @FXML
    Button sendButton;

    @FXML
    ChoiceBox<String> playerChoiceChat;

    public void setClient(ClientInterface client, GameController gameController, SharedUpdate updater) {
        this.client = client;
        this.playerData = client.getPlayerData();
        this.gameController = gameController;
        this.updater = updater;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setChat();
        setChoiceChat();
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        if (!messageInput.getText().isEmpty()) {
            try {
                client.postChat(messageInput.getText(), !playerChoiceChat.getValue().equals("General") ? playerChoiceChat.getValue() : null);
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

        String selectedPlayer = playerChoiceChat.getValue();

        for (ChatMessage chatMessage : client.getChat()) {
            if (selectedPlayer.equals("General")) {
                if (chatMessage.receiver() == null) {
                    addMessageToChat(chatMessage);
                }
            } else {
                if ((chatMessage.sender().equals(selectedPlayer) && chatMessage.receiver() != null && chatMessage.receiver().equals(client.getUsername())) || (chatMessage.receiver() != null && chatMessage.receiver().equals(selectedPlayer) && chatMessage.sender().equals(client.getUsername()))) {
                    addMessageToChat(chatMessage);
                }
            }
        }
    }

    private void addMessageToChat(ChatMessage chatMessage) {
        HBox messageHBox = new HBox();
        VBox messageVBox = new VBox();

        Text message = new Text(chatMessage.sender() + ":\n" + chatMessage.message());
        message.setStyle("-fx-font-family: Trattatello;" +
                "-fx-font-size: 30;" +
                "-fx-text-fill: #432918;"
        );

        if (!chatMessage.sender().equals(client.getUsername())) {
            message.setTextAlignment(TextAlignment.LEFT);
        } else {
            message.setTextAlignment(TextAlignment.RIGHT);
        }
        messageVBox.getChildren().add(message);


        if (chatMessage.sender().equals(client.getUsername())) {
            messageHBox.setAlignment(Pos.CENTER_RIGHT);
            messageHBox.setPadding(new Insets(0, 7, 0, 0));
        } else {
            messageHBox.setAlignment(Pos.CENTER_LEFT);
            messageHBox.setPadding(new Insets(0, 0, 0, 5));
        }

        messageHBox.getChildren().add(messageVBox);
        chatContainer.getChildren().add(messageHBox);
    }


    @FXML
    private void setChoiceChat() {
        playerChoiceChat.getItems().clear();
        playerChoiceChat.getItems().add("General");
        playerChoiceChat.setValue("General");
        playerChoiceChat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setChat());

        try {
            client.fetchPlayers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String player : client.getPlayers()) {
            if (!player.equals(client.getUsername())) {
                playerChoiceChat.getItems().add(player);
            }
        }
    }

    @FXML
    private void closeTab(ActionEvent event) {
        tabPane.getParent().getParent().setVisible(false);
        gameController.setActiveTab(null);
    }

    @Override
    public void update() {
        Platform.runLater(this::setChat);
    }
}