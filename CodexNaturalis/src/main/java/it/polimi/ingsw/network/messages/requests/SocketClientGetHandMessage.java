package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a client to request information about a hand in a game
 * via socket network communication.
 * This class extends the GenericRequestMessage class and contains the username of the client making the request
 * and the UUID of the game.
 */
public class SocketClientGetHandMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a new SocketClientGetHandMessage with the specified username and game UUID.
     * @param username The username of the client making the request.
     * @param gameUUID The UUID of the game from which to retrieve the hand information.
     */
    public SocketClientGetHandMessage(String username, UUID gameUUID) {
        this.gameUUID = gameUUID;
        this.username = username;
    }

    /**
     * Gets the UUID of the game from which to retrieve the hand information.
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }


}
