package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;

public class GetPlayerResourcesResponseMessage extends GenericResponseMessage{
    private final HashMap<Resource, Integer> playerResources;
    private final String usernameRequiredData;

    public GetPlayerResourcesResponseMessage(HashMap<Resource, Integer> playerResources, String usernameRequiredData) {
        this.playerResources = playerResources;
        this.usernameRequiredData = usernameRequiredData;
    }

    public HashMap<Resource, Integer> getPlayerResources() {
        return playerResources;
    }

    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
