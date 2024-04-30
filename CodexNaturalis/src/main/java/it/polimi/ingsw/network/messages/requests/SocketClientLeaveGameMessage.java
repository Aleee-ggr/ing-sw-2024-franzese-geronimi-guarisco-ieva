package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Class representing a message from a client to leave a game in network communication via sockets.
 * This class extends the GenericRequestMessage class, inheriting the username field.
 * It is used when a client leaves an existing game specified by a game UUID.
 */
public class SocketClientLeaveGameMessage extends GenericRequestMessage {
    private UUID gameUUID;

    /**
     * Constructor for the SocketClientLeaveGameMessage class.
     * @param username The username of the client sending the message.
     * @param gameUUID The UUID of the game the client wants to leave.
     */
    public SocketClientLeaveGameMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }
}