package it.polimi.ingsw.network.messages;

import java.util.UUID;

public class SocketClientJoinGame extends Message{
    private UUID gameUUID;
    public SocketClientJoinGame(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }
}
