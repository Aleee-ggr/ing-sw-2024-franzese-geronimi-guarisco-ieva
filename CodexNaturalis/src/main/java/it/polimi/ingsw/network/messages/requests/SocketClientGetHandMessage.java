package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientGetHandMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    public SocketClientGetHandMessage(String username, UUID gameUUID) {
        this.gameUUID = gameUUID;
        this.username = username;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }


}
