package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message from a client to request drawing a card in a game via socket network communication.
 * This class extends the `GenericRequestMessage` class and contains the username of the client making the request,
 * the position of the card to be drawn, and the UUID of the game.
 *
 * @see GenericRequestMessage
 */
public class SocketClientDrawCardMessage extends GenericRequestMessage {
    private final int position;
    private final UUID gameUUID;

    /**
     * Constructs a new SocketClientDrawCardMessage with the specified username, position, and game UUID.
     *
     * @param username The username of the client making the request.
     * @param position The position of the card to be drawn (e.g., index in the deck).
     * @param gameUUID The UUID of the game in which the card will be drawn.
     */

    public SocketClientDrawCardMessage(String username, int position, UUID gameUUID) {
        this.username = username;
        this.position = position;
        this.gameUUID = gameUUID;
    }

    /**
     * Gets the position of the card to be drawn.
     *
     * @return The position of the card.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Gets the UUID of the game in which the card will be drawn.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}
