package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class GetCommonObjectivesResponseMessage extends GenericResponseMessage{
    private final ArrayList<Integer> commonObjectives;

    public GetCommonObjectivesResponseMessage(ArrayList<Integer> commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    public ArrayList<Integer> getCommonObjectives() {
        return commonObjectives;
    }
}
