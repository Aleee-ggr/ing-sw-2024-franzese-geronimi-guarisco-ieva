package it.polimi.ingsw.network.messages;

import java.util.UUID;

public class SocketClientLeaveGame extends Message{
    private UUID gameUUID;
    public SocketClientLeaveGame(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }
}