package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a socket client requesting the player color information for a game.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 */
public class SocketClientGetPlayerColorMessage extends GenericRequestMessage{
    private final UUID gameUUID;
    private final String usernameRequiredData;

    /**
     * Constructs a SocketClientGetPlayerColorMessage object.
     *
     * @param username The username of the client sending the message.
     * @param gameUUID The UUID of the game for which the player color is requested.
     * @param usernameRequiredData The username for which player color data is requested.
     */
    public SocketClientGetPlayerColorMessage(String username, UUID gameUUID, String usernameRequiredData) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.usernameRequiredData = usernameRequiredData;
    }

    /**
     * Retrieves the UUID of the game for which the player color is requested.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }

    /**
     * Retrieves the username for which player color data is requested.
     *
     * @return The username for which player color data is requested.
     */
    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}