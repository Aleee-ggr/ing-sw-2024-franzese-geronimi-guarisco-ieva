package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a client to request information about the score map in a game
 * via socket network communication.
 * This class extends the GenericRequestMessage class and contains the username of the client making the request.
 *
 * @see GenericRequestMessage
 */
public class SocketClientGetScoreMapMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a new SocketClientGetScoreMapMessage with the specified username.
     *
     * @param username The username of the client making the request.
     * @param gameUUID The UUID of the game from which to retrieve the score map.
     */
    public SocketClientGetScoreMapMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}
