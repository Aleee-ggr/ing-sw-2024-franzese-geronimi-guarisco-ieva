package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a client to request information about valid placements in a game
 * via socket network communication.
 * This class extends the GenericRequestMessage class and contains the username of the client making the request
 * and the UUID of the game.
 */
public class SocketClientGetValidPlacementsMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a new SocketClientGetValidPlacementsMessage with the specified username and game UUID.
     * @param username The username of the client making the request.
     * @param gameUUID The UUID of the game from which to retrieve the valid placements.
     */
    public SocketClientGetValidPlacementsMessage(String username, UUID gameUUID){
        this.username = username;
        this.gameUUID = gameUUID;
    }

    /**
     * Gets the UUID of the game from which to retrieve the valid placements.
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
