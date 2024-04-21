package it.polimi.ingsw.network.messages;

import java.util.UUID;

/**
 * Class representing a message from a client to reconnect to a game in network communication via sockets.
 * This class extends the Message class, inheriting the username field.
 * It is used when a client wants to reconnect to an existing game specified by a game UUID.
 */
public class SocketClientReconnectMessage extends Message{
    private UUID gameUUID;

    /**
     * Constructs for the SocketClientReconnectMessage class.
     *
     * @param username The username of the client sending the message.
     * @param gameUUID The UUID of the game the client wants to reconnect to.
     */
    public SocketClientReconnectMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }
}
