package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.ArrayList;
import java.util.Set;

/**
 * Represents a response message containing the valid placements' information.
 * Extends GenericResponseMessage.
 */
public class GetValidPlacementsResponseMessage extends GenericResponseMessage{
    private final ArrayList<Coordinates> validPlacements;

    /**
     * Constructs a GetValidPlacementsResponseMessage with the specified valid placements.
     *
     * @param validPlacements the set of valid placements
     */
    public GetValidPlacementsResponseMessage(ArrayList<Coordinates> validPlacements) {
        this.validPlacements = validPlacements;
    }

    /**
     * Retrieves the set of valid placements.
     *
     * @return the ArrayList of valid placements
     */
    public ArrayList<Coordinates> getValidPlacements() {
        return validPlacements;
    }
}
