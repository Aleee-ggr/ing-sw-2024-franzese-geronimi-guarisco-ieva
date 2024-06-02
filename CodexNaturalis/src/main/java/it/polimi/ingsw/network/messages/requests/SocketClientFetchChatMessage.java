package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientFetchChatMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    public SocketClientFetchChatMessage(UUID gameUUID){
        this.gameUUID = gameUUID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}
