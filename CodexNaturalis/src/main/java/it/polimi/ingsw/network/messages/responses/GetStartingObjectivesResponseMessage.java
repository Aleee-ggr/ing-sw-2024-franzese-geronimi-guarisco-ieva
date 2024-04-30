package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

public class GetStartingObjectivesResponseMessage extends GenericResponseMessage{
    private final ArrayList<Integer> startingObjectives;

    public GetStartingObjectivesResponseMessage(ArrayList<Integer> startingObjectives) {
        this.startingObjectives = startingObjectives;
    }

    public ArrayList<Integer> getStartingObjectives() {
        return startingObjectives;
    }
}
