package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

public class SocketClientFetchPersonalObjectiveMessage extends GenericRequestMessage{
    private final UUID gameUUID;


    public SocketClientFetchPersonalObjectiveMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }


    /**
     * Gets the UUID of the game for which to retrieve the common objectives.
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
