package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Resource;

/**
 * Represents an abstract class for cards with a center back resource (stdCards and goldCards) in the game.
 * Extends the base class Card and adds information about the resource on the back side of the card.
 * @author Samuele Franzese
 */

public abstract class ColoredCard extends Card{
    private final Resource backResource;

    /**
     * Constructor for the ColoredCard class.
     * @param id Unique identifier of the card.
     * @param frontCorners Array of corners on the front side of the card.
     * @param backResource Resource on the back side of the card.
     */
    public ColoredCard(int id, Corner[] frontCorners, Resource backResource) {
        super(id, frontCorners, true);
        this.backResource = backResource;
    }

    /**
     * Retrieves the resource on the back side of the card.
     * @return The resource on the back side of the card.
     */
    public Resource getBackResource() {
        return backResource;
    }
}
