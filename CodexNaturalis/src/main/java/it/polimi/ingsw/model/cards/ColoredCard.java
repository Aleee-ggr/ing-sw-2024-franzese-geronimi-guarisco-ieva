package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Resource;

public abstract class ColoredCard extends Card{
    private final Resource backResource;
    public ColoredCard(int id, Corner[] frontCorners, Resource backResource) {
        super(id, frontCorners);
        this.backResource = backResource;
    }

    public Resource getBackResource() {
        return backResource;
    }
}
