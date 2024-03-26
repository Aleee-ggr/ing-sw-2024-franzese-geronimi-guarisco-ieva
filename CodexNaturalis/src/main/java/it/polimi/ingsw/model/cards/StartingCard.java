package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.Arrays;

public class StartingCard extends Card{
    private Corner[] backCorners = new Corner[GameConsts.numCorners];
    private ArrayList<Resource> frontResources;
    public StartingCard(int id, Corner[] frontCorners, Corner[] backCorners, ArrayList<Resource> frontResources) {
        super(id, frontCorners);
        this.backCorners = backCorners;
        this.frontResources = frontResources;
    }

    public Corner[] getBackCorners() {
        return Arrays.copyOf(backCorners, GameConsts.numCorners);
    }

    public Resource[] getFrontResources() {
        return null;
    }
}
