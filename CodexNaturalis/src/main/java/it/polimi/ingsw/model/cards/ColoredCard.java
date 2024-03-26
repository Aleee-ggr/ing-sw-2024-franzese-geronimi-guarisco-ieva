package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Resource;

/**
 * Represents an abstract class for cards with a center back resource (stdCards and goldCards) in the game.
 * Extends the base class Card and adds information about the resource on the back side of the card.
 * @author Samuele Franzese
 */

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
