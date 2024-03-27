package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.Resource;

import java.util.Arrays;

/**
 * Represents an abstract card in the game.
 * Cards in the game have unique identifiers and feature a set of corners on their front side
 * Subclasses of Card define specific types of cards with different functionalities.
 * @author Gloria Geronimi
 * */

public abstract class Card {
    private final int id;
    private Corner[] frontCorners = new Corner[GameConsts.numCorners];
    private boolean frontSideUp;

     /**
     * Constructor for the Card class.
     * @param id Unique identifier of the card.
     * @param frontCorners Array of corners on the front side of the card.
     */

    public Card(int id, Corner[] frontCorners){
        this.id = id;
        this.frontCorners = frontCorners;
    }
    /**
     * Returns the identifier of the card.
     * @return id of the card.
     */
    public int getId(){
        return id;
    }

    /**
     * Checks if the front side of the card is facing up.
     * @return True if the front side is facing up, otherwise false.
     */
    public boolean isFrontSideUp(){
        return frontSideUp;
    }

    /**
     * Returns an array of corners on the front side of the card.
     * @return Copy of the array of corners on the front side of the card.
     */
    public Corner[] getFrontCorners() {
        return Arrays.copyOf(frontCorners, GameConsts.numCorners);
    }
}
