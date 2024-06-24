package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a starting card in the game.
 * Extends the base class Card and contains additional information about back corners and front resources.
 */

public class StartingCard extends Card {
    private final Corner[] backCorners;
    private final ArrayList<Resource> frontResources;

    /**
     * Constructor for the StartingCard class.
     *
     * @param id             Unique identifier of the card.
     * @param frontCorners   Array of corners on the front side of the card.
     * @param backCorners    Array of corners on the back side of the card.
     * @param frontResources List of resources in the middle of the card's front side.
     */
    public StartingCard(int id, Corner[] frontCorners, Corner[] backCorners, ArrayList<Resource> frontResources) {
        super(id, frontCorners, false);
        this.backCorners = backCorners;
        this.frontResources = frontResources;
    }

    /**
     * Retrieves the back corners of the card.
     *
     * @return Array of back corners of the card.
     */
    public Corner[] getBackCorners() {
        return Arrays.copyOf(backCorners, GameConsts.numCorners);
    }

    /**
     * Retrieves the front resources in the middle of the card.
     *
     * @return List of front resources in the middle of the card.
     */
    public ArrayList<Resource> getFrontResources() {
        return frontResources;
    }
}
