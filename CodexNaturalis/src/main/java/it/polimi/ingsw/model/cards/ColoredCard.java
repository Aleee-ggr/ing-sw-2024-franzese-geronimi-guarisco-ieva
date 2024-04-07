package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.Resource;

import java.util.Arrays;

/**
 * Represents an abstract class for cards with a center back resource (stdCards and goldCards) in the game.
 * Extends the base class Card and adds information about the resource on the back side of the card.
 * @author Samuele Franzese
 */

public abstract class ColoredCard extends Card{
    private final Resource backResource;
    private static final Corner[] backCorners = {
            new Corner(Resource.NONE, true),
            new Corner(Resource.NONE, true),
            new Corner(Resource.NONE, true),
            new Corner(Resource.NONE, true)
    };

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

    /**
     * Retrieves the back corners of the card.
     * @return Array of back corners of the card.
     */
    public static Corner[] getBackCorners() {
        return Arrays.copyOf(backCorners, GameConsts.numCorners);
    }
}
