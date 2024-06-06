package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientPostChatMessage extends GenericRequestMessage{
    private final UUID gameUUID;
    private final String message;
    private final String receiver;

    public SocketClientPostChatMessage(String username, UUID gameUUID, String message, String receiver) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.message = message;
        this.receiver = receiver;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }
}
