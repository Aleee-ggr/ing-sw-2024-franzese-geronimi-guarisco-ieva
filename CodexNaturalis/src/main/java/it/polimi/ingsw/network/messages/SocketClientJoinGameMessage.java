package it.polimi.ingsw.network.messages;

import java.util.UUID;

/**
 * Class representing a message from a client to join an existing game in network communication via sockets.
 * This class extends the `Message` class, inheriting the username field.
 * It is used when a client wants to join an existing game specified by a game UUID.
 *
 * @author gloriageronimi
 */
public class SocketClientJoinGameMessage extends Message{
    private UUID gameUUID;

    /**
     * Constructs for SocketClientJoinGameMessage class.
     *
     * @param username The username of the client sending the message.
     * @param gameUUID The UUID of the game the client wants to join.
     */
    public SocketClientJoinGameMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }
}
