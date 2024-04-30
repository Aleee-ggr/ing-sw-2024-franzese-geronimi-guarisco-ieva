package it.polimi.ingsw.network.messages.responses;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.Set;

public class GetValidPlacementsResponseMessage extends GenericResponseMessage{
    private final Set<Coordinates> validPlacements;

    public GetValidPlacementsResponseMessage(Set<Coordinates> validPlacements) {
        this.validPlacements = validPlacements;
    }

    public Set<Coordinates> getValidPlacements() {
        return validPlacements;
    }
}
