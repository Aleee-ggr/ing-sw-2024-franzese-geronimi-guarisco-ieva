package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class GetHandResponseMessage extends GenericResponseMessage{
    private final ArrayList<Integer> handIds;

    public GetHandResponseMessage(ArrayList<Integer> handIds) {
        this.handIds = handIds;
    }

    public ArrayList<Integer> getHandIds() {
        return handIds;
    }

}
