package it.polimi.ingsw.network.messages.responses;

import java.util.ArrayList;

/**
 * Represents a response message containing the common objectives' ids.
 * Extends GenericResponseMessage.
 */
public class GetCommonObjectivesResponseMessage extends GenericResponseMessage{
    private final ArrayList<Integer> commonObjectives;

    /**
     * Constructs a GetCommonObjectivesResponseMessage with the specified common objectives ids.
     *
     * @param commonObjectives the list of common objectives
     */
    public GetCommonObjectivesResponseMessage(ArrayList<Integer> commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    /**
     * Retrieves the list of common objectives ids.
     *
     * @return the list of common objectives
     */
    public ArrayList<Integer> getCommonObjectives() {
        return commonObjectives;
    }
}
