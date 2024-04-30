package it.polimi.ingsw.network.messages.requests;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.UUID;

/**
 * Represents a message from a client to place a card in a game via socket network communication.
 * This class extends the GenericRequestMessage class and contains the username of the client making the request,
 * the coordinates where the card will be placed, the ID of the card, and the UUID of the game.
 */
public class SocketClientPlaceCardMessage extends GenericRequestMessage {
    private final Coordinates coordinates;
    private final int cardId;
    private final UUID gameUUID;

    /**
     * Constructs a new SocketClientPlaceCardMessage with the specified username, coordinates, card ID, and game UUID.
     * @param username The username of the client making the request.
     * @param coordinates The coordinates where the card will be placed.
     * @param cardId The ID of the card to be placed.
     * @param gameUUID The UUID of the game in which the card will be placed.
     */
    public SocketClientPlaceCardMessage(String username, Coordinates coordinates, int cardId, UUID gameUUID) {
        this.username = username;
        this.coordinates = coordinates;
        this.cardId = cardId;
        this.gameUUID = gameUUID;
    }

    /**
     * Gets the coordinates where the card will be placed.
     * @return The coordinates where the card will be placed.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gets the ID of the card to be placed.
     * @return The ID of the card.
     */
    public int getCardId() {
        return cardId;
    }

    /**
     * Gets the UUID of the game in which the card will be placed.
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }
}