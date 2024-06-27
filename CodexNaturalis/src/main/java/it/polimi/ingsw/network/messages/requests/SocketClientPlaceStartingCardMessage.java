package it.polimi.ingsw.network.messages.requests;

import java.util.UUID;

/**
 * Represents a message sent by a socket client to place a starting card in a game.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 *
 * @see GenericRequestMessage
 */
public class SocketClientPlaceStartingCardMessage extends GenericRequestMessage {
    private final UUID gameUUID;
    private final boolean frontSideUp;

    /**
     * Constructs a SocketClientPlaceStartingCardMessage object.
     *
     * @param username    The username of the client sending the place starting card message.
     * @param frontSideUp Whether the starting card should be placed front side up.
     * @param gameUUID    The UUID of the game in which the starting card is being placed.
     */
    public SocketClientPlaceStartingCardMessage(String username, boolean frontSideUp, UUID gameUUID) {
        this.username = username;
        this.gameUUID = gameUUID;
        this.frontSideUp = frontSideUp;
    }

    /**
     * Retrieves the UUID of the game in which the starting card is being placed.
     *
     * @return The UUID of the game.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }

    /**
     * Checks whether the starting card should be placed front side up.
     *
     * @return true if the starting card is placed front side up, false otherwise.
     */
    public boolean isFrontSideUp() {
        return frontSideUp;
    }
}
