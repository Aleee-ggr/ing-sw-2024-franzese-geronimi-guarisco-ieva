package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a socket client requesting to fetch the current game state for a specific game.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 *
 * @see GenericRequestMessage
 */
public class SocketClientFetchGameStateMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a SocketClientFetchGameStateMessage object.
     *
     * @param username The username of the client sending the message.
     * @param gameUUID The UUID of the game for which the state is requested.
     */
    public SocketClientFetchGameStateMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    /**
     * Retrieves the UUID of the game for which the state is requested.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}