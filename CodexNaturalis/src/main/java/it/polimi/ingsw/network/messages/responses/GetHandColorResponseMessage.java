package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;

public class GetHandColorResponseMessage extends GenericResponseMessage {
    private final ArrayList<Resource> handColor;
    private final String usernameRequiredData;

    public GetHandColorResponseMessage(ArrayList<Resource> handColor, String usernameRequiredData) {
        this.handColor = handColor;
        this.usernameRequiredData = usernameRequiredData;
    }

    public ArrayList<Resource> getHandColor() {
        return handColor;
    }

    public String getUsernameRequiredData() {
        return usernameRequiredData;
    }
}
