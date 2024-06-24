package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Resource;

/**
 * Represents a standard card in the game.
 * Extends the ColoredCard class and adds information about whether the card has points or not.
 */
public class StdCard extends ColoredCard {
    private final boolean point;

    /**
     * Constructor for the StdCard class.
     *
     * @param id           Unique identifier of the card.
     * @param frontCorners Array of corners on the front side of the card.
     * @param backResource Resource on the back side of the card.
     * @param point        Indicates whether the card has one point or not.
     */
    public StdCard(int id, Corner[] frontCorners, Resource backResource, boolean point) {
        super(id, frontCorners, backResource);
        this.point = point;
    }

    /**
     * Checks whether the card has points.
     * StdCard can only have one point or zero.
     *
     * @return True if the card has points, otherwise false.
     */
    public boolean isPoint() {
        return point;
    }
}
