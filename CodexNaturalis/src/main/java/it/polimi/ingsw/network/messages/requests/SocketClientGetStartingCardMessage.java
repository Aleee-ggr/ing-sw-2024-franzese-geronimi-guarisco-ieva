package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a request message sent by a client to retrieve the starting card in a game.
 *
 * @see GenericRequestMessage
 */
public class SocketClientGetStartingCardMessage extends GenericRequestMessage {
    private final UUID gameUUID;

    /**
     * Constructs a new SocketClientGetStartingCardMessage.
     *
     * @param username the username of the client sending the request.
     * @param gameUUID the unique identifier of the game for which the starting card is requested.
     */
    public SocketClientGetStartingCardMessage(String username, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
    }

    /**
     * Gets the unique identifier of the game for which the starting card is requested.
     *
     * @return the UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
