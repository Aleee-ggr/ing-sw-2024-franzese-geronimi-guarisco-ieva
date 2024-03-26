package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a starting card in the game.
 * Extends the base class Card and contains additional information about back corners and front resources.
 * @author Samuele Franzese
 */

public class StartingCard extends Card{
    private final Corner[] backCorners;
    private final ArrayList<Resource> frontResources;
    public StartingCard(int id, Corner[] frontCorners, Corner[] backCorners, ArrayList<Resource> frontResources) {
        super(id, frontCorners);
        this.backCorners = backCorners;
        this.frontResources = frontResources;
    }

    public Corner[] getBackCorners() {
        return Arrays.copyOf(backCorners, GameConsts.numCorners);
    }

    public ArrayList<Resource> getFrontResources() {
        return frontResources;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        out.append("id: ").append(getId())
                .append("\nresources:");
        for (var resource : getFrontResources()) {
            out.append("\n\t").append(resource);
        }

        out.append("\nfront corners:");
        for (var corner : this.getFrontCorners()) {
            out.append("\n\tcorner: ")
                    .append(corner.isCoverable())
                    .append(" ")
                    .append(corner.getCornerResource());
        }

        out.append("\nback corners:");
        for (var corner : this.getBackCorners()) {
            out.append("\n\tcorner: ")
                    .append(corner.isCoverable())
                    .append(" ")
                    .append(corner.getCornerResource());
        }

        return out.toString();
    }
}
