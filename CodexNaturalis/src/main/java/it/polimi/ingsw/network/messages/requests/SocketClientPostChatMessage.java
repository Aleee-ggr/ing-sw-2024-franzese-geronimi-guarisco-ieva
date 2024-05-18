package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientPostChatMessage extends GenericRequestMessage{
    private final UUID gameUUID;
    private final String message;

    public SocketClientPostChatMessage(String username, UUID gameUUID, String message) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.message = message;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public String getMessage() {
        return message;
    }
}
