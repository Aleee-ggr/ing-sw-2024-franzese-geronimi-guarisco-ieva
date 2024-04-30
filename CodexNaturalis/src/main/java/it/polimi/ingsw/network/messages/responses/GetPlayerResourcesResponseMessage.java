package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;

/**
 * Represents a response message containing the player resources' information.
 * Extends GenericResponseMessage.
 */
public class GetPlayerResourcesResponseMessage extends GenericResponseMessage{
    private final HashMap<Resource, Integer> playerResources;
    private final String usernameRequiredData;

    /**
     * Constructs a GetPlayerResourcesResponseMessage with the specified player resources and required username data.
     *
     * @param playerResources the player resources mapping resources to quantities
     * @param usernameRequiredData the username of the required data
     */
    public GetPlayerResourcesResponseMessage(HashMap<Resource, Integer> playerResources, String usernameRequiredData) {
        this.playerResources = playerResources;
        this.usernameRequiredData = usernameRequiredData;
    }

    /**
     * Retrieves the player resources mapping resources to quantities.
     *
     * @return the player resources mapping resources to quantities
     */
    public HashMap<Resource, Integer> getPlayerResources() {
        return playerResources;
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
