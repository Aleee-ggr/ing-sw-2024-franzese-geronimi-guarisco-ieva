package it.polimi.ingsw.network.messages;

import java.util.UUID;

public class SocketClientReconnect extends Message{
    private UUID gameUUID;
    public SocketClientReconnect(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }
}
