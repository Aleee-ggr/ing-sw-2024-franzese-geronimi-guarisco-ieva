package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.enums.Resource;

import java.util.Arrays;

public class StartingCard extends Card{
    private Corner[] backCorners = new Corner[GameConsts.numCorners];
    private Resource[] frontResources = new Resource[GameConsts.numCorners];
    public StartingCard(int id, Corner[] frontCorners, Corner[] backCorners, Resource[] frontResources) {
        super(id, frontCorners);
        this.backCorners = backCorners;
        this.frontResources = frontResources;
    }

    public Corner[] getBackCorners() {
        return Arrays.copyOf(backCorners, GameConsts.numCorners);
    }

    public Resource[] getFrontResources() {
        return Arrays.copyOf(frontResources, GameConsts.numCorners);
    }
}
