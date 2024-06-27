package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a socket client requesting to fetch the personal objective for a specific game.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 *
 * @see GenericRequestMessage
 */
public class SocketClientFetchPersonalObjectiveMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a SocketClientFetchPersonalObjectiveMessage object.
     *
     * @param username The username of the client sending the message.
     * @param gameUUID The UUID of the game for which the personal objective is requested.
     */
    public SocketClientFetchPersonalObjectiveMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    /**
     * Gets the UUID of the game for which to retrieve the common objectives.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
