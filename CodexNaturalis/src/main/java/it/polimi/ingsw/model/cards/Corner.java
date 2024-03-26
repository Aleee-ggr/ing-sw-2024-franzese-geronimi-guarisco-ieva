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

    public Corner(Resource cornerResource, boolean coverable){
        this.cornerResource = cornerResource;
        this.coverable = coverable;
    }

    public Resource getCornerResource() {
        return cornerResource;
    }

    public boolean isCoverable() {
        return coverable;
    }
}
