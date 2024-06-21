package it.polimi.ingsw.model.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pair of coordinates (x, y) on a 2D plane.
 * Coordinates are immutable and serializable.
 */
public record Coordinates(Integer x, Integer y) implements Serializable {

    /**
     * @param offset how much to shift the coordinates horizontally (same sign as x)
     * @return a new set of coordinates offset by the given amount
     */
    public Coordinates horizontal(int offset) {
        return new Coordinates(x + offset, y);
    }

    /**
     * @param offset how much to shift the coordinates vertically (same sign as x)
     * @return a new set of coordinates offset by the given amount
     */
    public Coordinates vertical(int offset) {
        return new Coordinates(x, y + offset);
    }

    /**
     * get a list of the neighbors of this coordinates
     * @return a list of the neighbors of this coordinates
     */
    public List<Coordinates> getNeighbors() {

        List<Coordinates> out =  new ArrayList<>();
        out.add(horizontal(-1));
        out.add(horizontal(1));
        out.add(vertical(-1));
        out.add(vertical(1));
        return out;
    }

    /**
     * @param c2 the coordinates to subtract from this coordinates
     * @return a new set of coordinates offset by -c2
     */
    public Coordinates subtract(Coordinates c2) {
        return new Coordinates(
                x - c2.x(),
                y - c2.y()
        );
    }

    /**
     * @param c2 the coordinates to subtract from this coordinates
     * @return a new set of coordinates offset by c2
     */
    public Coordinates add(Coordinates c2) {
        return new Coordinates(
                x + c2.x(),
                y + c2.y()
        );
    }
}
