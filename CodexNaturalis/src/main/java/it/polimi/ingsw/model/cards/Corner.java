package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.Resource;

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
