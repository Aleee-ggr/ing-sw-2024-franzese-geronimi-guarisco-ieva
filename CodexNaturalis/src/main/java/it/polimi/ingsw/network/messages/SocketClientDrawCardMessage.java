package it.polimi.ingsw.network.messages;

import java.util.UUID;

public class SocketClientDrawCardMessage extends Message{
    private final int position;
    private final UUID gameUUID;

    public SocketClientDrawCardMessage(String username, int position, UUID gameUUID) {
        this.username = username;
        this.position = position;
        this.gameUUID = gameUUID;
    }

    public int getPosition() {
        return position;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}