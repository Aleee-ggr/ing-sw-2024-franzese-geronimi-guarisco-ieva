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

    public Card(int id, Corner[] frontCorners){
        this.id = id;
        this.frontCorners = frontCorners;
    }

    public int getId(){
        return id;
    }

    public boolean isFrontSideUp(){
        return frontSideUp;
    }

    public Corner[] getFrontCorners() {
        return Arrays.copyOf(frontCorners, GameConsts.numCorners);
    }
}
