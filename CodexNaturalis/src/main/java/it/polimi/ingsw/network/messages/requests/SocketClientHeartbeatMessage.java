package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a heartbeat message sent from a socket client to indicate its presence and status.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 */
public class SocketClientHeartbeatMessage extends GenericRequestMessage {
    private final String username;
    private final UUID gameUUID;

    /**
     * Constructs a SocketClientHeartbeatMessage object.
     *
     * @param username The username of the client sending the heartbeat message.
     * @param gameUUID The UUID of the game associated with the heartbeat message.
     */
    public SocketClientHeartbeatMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    /**
     * Retrieves the username of the client sending the heartbeat message.
     *
     * @return The username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the UUID of the game associated with the heartbeat message.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
