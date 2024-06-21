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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

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
    ScrollPane chatScrollPane;

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
        setChoiceChat();
        setChat();
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        if (!messageInput.getText().isEmpty()) {
            try {
                client.postChat(messageInput.getText(), !playerChoiceChat.getValue().equals("General") ? playerChoiceChat.getValue() : null);
                client.fetchChat();
                gameController.messages = client.getChat();
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
                    addMessageToChat(chatMessage, true);
                }
            } else {
                if ((chatMessage.sender().equals(selectedPlayer) && chatMessage.receiver() != null && chatMessage.receiver().equals(client.getUsername())) || (chatMessage.receiver() != null && chatMessage.receiver().equals(selectedPlayer) && chatMessage.sender().equals(client.getUsername()))) {
                    addMessageToChat(chatMessage, false);
                }
            }
        }

        Platform.runLater(() -> {
            chatContainer.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                chatScrollPane.setVvalue(1.0);
            });
        });
    }

    private void addMessageToChat(ChatMessage chatMessage, boolean isGeneral) {
        HBox messageHBox = new HBox();
        VBox messageVBox = new VBox();
        messageVBox.setPadding(new Insets(10, 0, 10, 0));
        messageVBox.setSpacing(10);

        Text sender = new Text(chatMessage.sender() + ":");
        sender.setStyle("-fx-font-family: Trattatello;" +
                "-fx-font-size: 20;" +
                "-fx-text-fill: #432918;"
        );
        Text message = new Text(chatMessage.message());
        message.setStyle("-fx-font-family: Trattatello;" +
                "-fx-font-size: 20;" +
                "-fx-text-fill: #432918;"
        );
        sender.setBoundsType(TextBoundsType.VISUAL);
        message.setBoundsType(TextBoundsType.VISUAL);
        message.setWrappingWidth(900 / 1.5);
        message.setLineSpacing(-10);

        if (!chatMessage.sender().equals(client.getUsername())) {
            sender.setTextAlignment(TextAlignment.LEFT);
            message.setTextAlignment(TextAlignment.LEFT);
            messageVBox.setAlignment(Pos.CENTER_LEFT);
        } else {
            sender.setTextAlignment(TextAlignment.RIGHT);
            message.setTextAlignment(TextAlignment.RIGHT);
            messageVBox.setAlignment(Pos.CENTER_RIGHT);
        }

        if (isGeneral) {
            messageVBox.getChildren().addAll(sender, message);
        } else {
            messageVBox.getChildren().add(message);
        }


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

