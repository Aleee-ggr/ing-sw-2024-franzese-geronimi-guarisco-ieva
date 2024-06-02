package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientHeartbeatMessage extends GenericRequestMessage {
    private final String username;
    private final UUID gameUUID;

    public SocketClientHeartbeatMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    public String getUsername() {
        return username;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}
