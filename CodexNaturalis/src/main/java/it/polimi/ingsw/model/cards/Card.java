package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.Resource;

import java.util.Arrays;

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
