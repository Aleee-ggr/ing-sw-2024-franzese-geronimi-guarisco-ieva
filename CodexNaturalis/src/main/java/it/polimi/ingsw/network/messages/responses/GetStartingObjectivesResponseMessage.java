package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

/**
 * Represents a response message containing the starting objectives' information.
 * Extends GenericResponseMessage.
 */
public class GetStartingObjectivesResponseMessage extends GenericResponseMessage {
    private final ArrayList<Integer> startingObjectives;

    /**
     * Constructs a GetStartingObjectivesResponseMessage with the specified starting objectives.
     *
     * @param startingObjectives the list of starting objectives
     */
    public GetStartingObjectivesResponseMessage(ArrayList<Integer> startingObjectives) {
        this.startingObjectives = startingObjectives;
    }

    /**
     * Retrieves the list of starting objectives.
     *
     * @return the list of starting objectives
     */
    public ArrayList<Integer> getStartingObjectives() {
        return startingObjectives;
    }
}
