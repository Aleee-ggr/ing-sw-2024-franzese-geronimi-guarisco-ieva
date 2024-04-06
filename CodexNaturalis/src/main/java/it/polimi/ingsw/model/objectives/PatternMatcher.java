package it.polimi.ingsw.model.objectives;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.enums.Resource;

import java.util.*;

public class PatternMatcher {
    private Map<Coordinates, Resource> pattern;
    private PlayerBoard board;
    private Set<Coordinates> visited = new HashSet<>();
    private Set<Coordinates> matched = new HashSet<>();
    private Queue<Coordinates> toVisit = new ArrayDeque<>();
    private Set<Resource> patternResources;

    /**
     * set the pattern to find in the board
     * @param pattern the pattern the object will attempt to match
     * @return this
     */
    public PatternMatcher setPattern(Map<Coordinates, Resource> pattern) {
        this.pattern = pattern;
        patternResources = new HashSet<>(pattern.values());
        return this;
    }

    /**
     * set the board in which to find in the pattern
     * @param board the board the object will attempt to match the pattern on
     * @return this
     */
    public PatternMatcher setBoard(PlayerBoard board) {
        this.board = board;
        return this;
    }

    /**
     * @return the number of matches the board contains with the given pattern
     */
    public int matches_found() {
        toVisit.add(board.getCenter());
        Coordinates current;
        int matches = 0;
        while (!toVisit.isEmpty()) {
            current = toVisit.remove();
            if (visited.contains(current) || matched.contains(current)) {
                continue;
            }

            visited.add(current);

            if (patternResources.contains(getColor(current))) {
                if (BFSMatch(current)) {
                    matches += 1;
                }
            }

            for (Coordinates neighbor : current.getNeighbors()) {
                if (isCard(neighbor) && !visited.contains(neighbor) && !toVisit.contains(neighbor)) {
                    toVisit.add(neighbor);
                }
            }
        }
        return matches;
    }

    private boolean BFSMatch(Coordinates coordinates) {
        for (Coordinates pattern_coords : pattern.keySet()) {
            if (pattern.get(pattern_coords) == getColor(coordinates)) {
                boolean found = true;
                for (Coordinates other_resources : pattern.keySet()) {
                    if (other_resources != pattern_coords) {
                        Coordinates pattern_offset = other_resources.subtract(pattern_coords);
                        Coordinates toCheck = coordinates.add(pattern_offset);
                        if (getColor(toCheck) != pattern.get(other_resources)) {
                            found = false;
                            break;
                        }
                    }
                }
                if (found) {
                    for (Coordinates key : pattern.keySet()) {
                        Coordinates pattern_offset = key.subtract(pattern_coords);
                        Coordinates toCheck = coordinates.add(pattern_offset);
                        matched.add(toCheck);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check wether the board contains a card at the given coordinates
     * @param coordinates the coordinates where to check
     * @return whether the board contains a card at the given coordinates
     */
    private boolean isCard(Coordinates coordinates) {
        return board.isWithinBounds(coordinates) && board.getCard(coordinates) != null;
    }

    /**
     * Check whether a card is an instance of colored card and return its resource type
     * @param coordinates the coordinates where to check
     * @return NONE if the card is not an instance of ColoredCard, is resource type otherwise
     */
    private Resource getColor(Coordinates coordinates) {
        if (isCard(coordinates) && board.getCard(coordinates).isColored()) {
            return ((ColoredCard)board.getCard(coordinates)).getBackResource();
        }
        return Resource.NONE;
    }
}
