package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a request message sent by a client to retrieve the list of players in a game.
 */
public class SocketClientGetPlayersMessage extends GenericRequestMessage{
    private final UUID gameUUID;

    /**
     * Constructs a new SocketClientGetPlayersMessage.
     * @param username the username of the client sending the request.
     * @param gameUUID the unique identifier of the game for which the list of players is requested.
     */
    public SocketClientGetPlayersMessage(String username, UUID gameUUID){
        this.username = username;
        this.gameUUID = gameUUID;
    }

    /**
     * Gets the unique identifier of the game for which the list of players is requested.
     * @return the UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
