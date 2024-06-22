package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.enums.Color;

/**
 * This class represents a response message containing the player's color.
 * It extends the GenericResponseMessage class.
 */
public class GetPlayerColorResponseMessage extends GenericResponseMessage{
    private final Color playerColor;
    private final String usernameRequiredData;

    /**
     * Constructs a new GetPlayerColorResponseMessage with the specified player color and username data.
     *
     * @param playerColor the color assigned to the player.
     * @param usernameRequiredData the username of the player for whom the data is required.
     */
    public GetPlayerColorResponseMessage(Color playerColor, String usernameRequiredData) {
        this.playerColor = playerColor;
        this.usernameRequiredData = usernameRequiredData;
    }

    /**
     * Returns the color assigned to the player.
     *
     * @return the color assigned to the player.
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * Retrieves the username of the player for whom the data is required.
     *
     * @return the username of the player for whom the data is required.
     */
    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
