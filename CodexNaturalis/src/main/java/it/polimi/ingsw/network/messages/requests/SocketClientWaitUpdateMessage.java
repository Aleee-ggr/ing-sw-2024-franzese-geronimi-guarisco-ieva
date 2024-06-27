package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message sent by a socket client to wait for game state updates.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 *
 * @see GenericRequestMessage
 */
public class SocketClientWaitUpdateMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a SocketClientWaitUpdateMessage object.
     *
     * @param username The username of the client waiting for updates.
     * @param gameUUID The UUID of the game for which the client is waiting for updates.
     */
    public SocketClientWaitUpdateMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    /**
     * Retrieves the UUID of the game for which the client is waiting for updates.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
