package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a socket client requesting to fetch chat messages for a specific game.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 *
 * @see GenericRequestMessage
 */
public class SocketClientFetchChatMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a SocketClientFetchChatMessage object.
     *
     * @param username The username of the client sending the message.
     * @param gameUUID The UUID of the game for which chat messages are requested.
     */
    public SocketClientFetchChatMessage(String username, UUID gameUUID) {
        this.gameUUID = gameUUID;
        this.username = username;
    }

    /**
     * Retrieves the UUID of the game for which chat messages are requested.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
