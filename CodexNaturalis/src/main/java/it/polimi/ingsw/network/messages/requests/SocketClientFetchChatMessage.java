package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientFetchChatMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    public SocketClientFetchChatMessage(String username, UUID gameUUID){
        this.gameUUID = gameUUID;
        this.username = username;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}
