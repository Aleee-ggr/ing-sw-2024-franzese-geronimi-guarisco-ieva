package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientGetBackSideDecksMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    public SocketClientGetBackSideDecksMessage(String username, UUID gameUUID){
        this.username = username;
        this.gameUUID = gameUUID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}
