package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Resource;

/**
 * Represents a standard card in the game.
 * Extends the ColoredCard class and adds information about whether the card has points or not.
 * @author Samuele Franzese
 */
public class StdCard extends ColoredCard{
    private final boolean point;
    public StdCard(int id, Corner[] frontCorners, Resource backResource, boolean point) {
        super(id, frontCorners, backResource);
        this.point = point;
    }

    public boolean isPoint() {
        return point;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("id: ")
                .append(getId())
                .append("\nresource: ")
                .append(getBackResource())
                .append("\npoints: ")
                .append(isPoint());
        for (var corner : this.getFrontCorners()) {
            out.append("\ncorner: ")
                    .append(corner.isCoverable())
                    .append(" ")
                    .append(corner.getCornerResource());
        }
        return out.toString();
    }
}
