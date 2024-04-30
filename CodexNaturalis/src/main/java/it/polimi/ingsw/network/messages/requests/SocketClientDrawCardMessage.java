package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientDrawCardMessage extends GenericRequestMessage {
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