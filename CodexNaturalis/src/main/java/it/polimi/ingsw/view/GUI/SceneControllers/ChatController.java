package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Color;
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

/**
 * The ChatController class manages the chat functionality of the GUI.
 * It initializes the chat interface, handles sending and receiving messages,
 * and updates the chat view accordingly.
 */
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

    /**
     * Sets the client, game controller, and shared updater for this chat controller.
     *
     * @param client         the client interface to communicate with the server
     * @param gameController the game controller to manage game state
     * @param updater        the shared updater to synchronize updates
     */
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

    /**
     * Sends a message when the send button is clicked.
     *
     * @param event the action event triggered by clicking the send button
     */
    @FXML
    private void sendMessage(ActionEvent event) {
        if (!messageInput.getText().isEmpty()) {
            try {
                client.postChat(messageInput.getText(), !playerChoiceChat.getValue().equals("General") ? playerChoiceChat.getValue() : null);
                client.fetchChat();
                gameController.messages = client.getChat();
            } catch (IOException e) {
                ErrorMessageController.showErrorMessage("Impossible to fetch data from server!", gameController.root);
            }
        }
        setChat();
        messageInput.clear();
    }

    /**
     * Updates the chat view with the latest messages. Fetches messages from the server and displays them.
     */
    @FXML
    private void setChat() {
        chatContainer.getChildren().clear();
        try {
            client.fetchChat();
            client.fetchPlayersColors();
        } catch (IOException e) {
            ErrorMessageController.showErrorMessage("Impossible to fetch data from server!", gameController.root);
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

    /**
     * Adds a message to the chat container.
     *
     * @param chatMessage the chat message to add to the chat container
     * @param isGeneral   whether the message is a general message (true) or a private message (false)
     */
    private void addMessageToChat(ChatMessage chatMessage, boolean isGeneral) {
        HBox messageHBox = new HBox();
        VBox messageVBox = new VBox();
        messageVBox.setPadding(new Insets(10, 0, 15, 0));
        messageVBox.setSpacing(10);

        Color playerColor;
        if (chatMessage.sender().equals(client.getUsername())) {
            playerColor = playerData.getPlayerColor();
        } else {
            playerColor = ((OpponentData) client.getOpponentData().get(chatMessage.sender())).getPlayerColor();
        }

        Text sender = new Text(chatMessage.sender() + ":");
        sender.setStyle(String.format("-fx-font-family: Trattatello;" +
                "-fx-font-size: 20;" +
                "-fx-fill: %s;", !playerColor.equals(Color.YELLOW) ? playerColor : "#d5b343")
        );

        Text message = new Text(chatMessage.message());
        message.setStyle("-fx-font-family: Trattatello;" +
                "-fx-font-size: 20;" +
                "-fx-fill: #432918;"
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
            messageHBox.setPadding(new Insets(0, 15, 0, 0));
        } else {
            messageHBox.setAlignment(Pos.CENTER_LEFT);
            messageHBox.setPadding(new Insets(0, 0, 0, 10));
        }

        messageHBox.getChildren().add(messageVBox);
        chatContainer.getChildren().add(messageHBox);
    }

    /**
     * Sets up the choice box for selecting the chat recipient.
     * Populates the choice box with player names and the "General" option,
     * allowing the user to select between sending a general message or a private message.
     * The choice box is initially set to "General".
     */
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

    /**
     * Closes the chat tab.
     *
     * @param event the action event triggered by clicking the close button
     */
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

