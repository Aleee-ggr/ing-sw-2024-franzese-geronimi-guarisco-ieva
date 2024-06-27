package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;

/**
 * Represents a response message containing the hand color's information of a specified player.
 * Extends GenericResponseMessage.
 */
public class GetHandColorResponseMessage extends GenericResponseMessage {
    private final ArrayList<Resource> handColor;
    private final String usernameRequiredData;

    /**
     * Constructs a GetHandColorResponseMessage with the specified hand color and the username of the player.
     *
     * @param handColor            the list of resources representing the hand color
     * @param usernameRequiredData the username of the list of resources
     */
    public GetHandColorResponseMessage(ArrayList<Resource> handColor, String usernameRequiredData) {
        this.handColor = handColor;
        this.usernameRequiredData = usernameRequiredData;
    }

    /**
     * Retrieves the list of resources representing the hand color.
     *
     * @return the list of resources representing the hand color
     */
    public ArrayList<Resource> getHandColor() {
        return handColor;
    }

    /**
     * Retrieves the username of the required data.
     *
     * @return the required username data
     */
    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
