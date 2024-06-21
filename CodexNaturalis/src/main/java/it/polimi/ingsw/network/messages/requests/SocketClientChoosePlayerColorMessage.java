package it.polimi.ingsw.network.messages.requests;

import it.polimi.ingsw.model.enums.Color;

import java.util.UUID;

/**
 * Represents a message from a socket client to choose a player color in a game session.
 * Extends GenericRequestMessage to inherit basic message properties like the sender's username.
 */
public class SocketClientChoosePlayerColorMessage extends GenericRequestMessage{
    private final UUID gameUUID;
    private final Color playerColor;

    /**
     * Constructs a SocketClientChoosePlayerColorMessage object.
     *
     * @param username   The username of the client sending the message.
     * @param playerColor The chosen color of the player.
     * @param gameUUID   The UUID of the game session where the color choice is made.
     */
    public SocketClientChoosePlayerColorMessage(String username, Color playerColor, UUID gameUUID){
        this.username = username;
        this.playerColor = playerColor;
        this.gameUUID = gameUUID;
    }

    /**
     * Retrieves the UUID of the game session associated with this color choice message.
     *
     * @return The UUID of the game session.
     */
    public UUID getGameUUID() {
        return gameUUID;
    }

    /**
     * Retrieves the chosen color of the player.
     *
     * @return The chosen Color enum value.
     */
    public Color getPlayerColor() {
        return playerColor;
    }
}
