package it.polimi.ingsw.helpers.pattern;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.HashSet;
import java.util.Set;

public record Pattern(Set<Coordinates> p) {
    public boolean contains(Coordinates coordinates) {
        return p.contains(coordinates);
    }

    /**
     * Check how many sets of coordinates are in common between two patterns
     * @param other the pattern to match against
     * @return the number of shared tiles
     */
    public int getOverlap(Pattern other) {
        int overlap = 0;
        for (Coordinates coord : p) {
            if (other.contains(coord)) {
                overlap++;
            }
        }
        return overlap;
    }

    /**
     * @return a set of coordinates the pattern is made of
     */
    public Set<Coordinates> getCoordinates() {
        return new HashSet<>(p);
    }
}
