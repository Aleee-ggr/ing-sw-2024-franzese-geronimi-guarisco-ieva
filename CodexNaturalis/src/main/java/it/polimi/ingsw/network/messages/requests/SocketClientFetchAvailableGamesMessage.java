package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientFetchAvailableGamesMessage extends GenericRequestMessage{
    private final UUID gameUUID;

    public SocketClientFetchAvailableGamesMessage(String username, UUID gameUUID){
        this.username = username;
        this.gameUUID = gameUUID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}