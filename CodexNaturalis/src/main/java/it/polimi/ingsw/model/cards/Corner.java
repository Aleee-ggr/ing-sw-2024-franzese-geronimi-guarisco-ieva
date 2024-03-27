package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.Resource;

/**
 * Represents a corner of a card.
 * Corners of cards can hold resources and can be covered or not.
 * @author Gloria Geronimi
 */

public class Corner {
    private final Resource cornerResource;
    private final boolean coverable;

    /**
     * Constructor for the Corner class.
     * @param cornerResource The resource located in the corner.
     * @param coverable Indicates whether the corner can be covered.
     */
    public Corner(Resource cornerResource, boolean coverable){
        this.cornerResource = cornerResource;
        this.coverable = coverable;
    }

    /**
     * Retrieves the resource located in the corner.
     * @return The resource located in the corner.
     */
    public Resource getCornerResource() {
        return cornerResource;
    }

    /**
     * Checks whether the corner can be covered.
     * @return True if the corner can be covered, false otherwise.
     */
    public boolean isCoverable() {
        return coverable;
    }
}
