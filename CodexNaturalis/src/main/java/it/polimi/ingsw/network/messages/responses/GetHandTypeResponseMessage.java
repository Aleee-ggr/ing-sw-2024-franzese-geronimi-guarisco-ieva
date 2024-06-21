package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class GetHandTypeResponseMessage extends GenericResponseMessage {
    private final ArrayList<Boolean> isGold;
    private final String usernameRequiredData;

    public GetHandTypeResponseMessage(ArrayList<Boolean> isGold, String usernameRequiredData) {
        this.isGold = isGold;
        this.usernameRequiredData = usernameRequiredData;
    }


    public ArrayList<Boolean> getIsGold() {
        return isGold;
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
