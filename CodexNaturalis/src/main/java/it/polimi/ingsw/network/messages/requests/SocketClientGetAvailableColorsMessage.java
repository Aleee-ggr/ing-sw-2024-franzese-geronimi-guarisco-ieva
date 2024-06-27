package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a socket client requesting available colors for a game.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 *
 * @see GenericRequestMessage
 */
public class SocketClientGetAvailableColorsMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a SocketClientGetAvailableColorsMessage object.
     *
     * @param username The username of the client sending the message.
     * @param gameUUID The UUID of the game for which available colors are requested.
     */
    public SocketClientGetAvailableColorsMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    /**
     * Retrieves the UUID of the game for which available colors are requested.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}