package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a client to request information about a hand color in a game
 * via socket network communication.
 * This class extends the GenericRequestMessage class and contains the username of the client making the request,
 * the UUID of the game, and the username of the player whose hand color information is being requested.
 *
 * @see GenericRequestMessage
 */
public class SocketClientGetHandColorMessage extends GenericRequestMessage {
    private final UUID gameUUID;
    private final String usernameRequiredData;

    /**
     * Constructs a new SocketClientGetHandColorMessage with the specified username, game UUID, and the username of the player
     * whose hand color information is being requested.
     *
     * @param username             The username of the client making the request.
     * @param gameUUID             The UUID of the game for which to retrieve the hand color.
     * @param usernameRequiredData The username of the player whose hand color information is being requested.
     */
    public SocketClientGetHandColorMessage(String username, UUID gameUUID, String usernameRequiredData) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.usernameRequiredData = usernameRequiredData;
    }

    /**
     * Gets the UUID of the game for which to retrieve the hand color.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }

    /**
     * Gets the username of the player whose hand color information is being requested.
     *
     * @return The username of the player.
     */
    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
