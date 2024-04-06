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
    public PatternMatcher setPattern(Map<Coordinates, Resource> pattern) {
        this.pattern = pattern;
        patternResources = new HashSet<>(pattern.values());
        return this;
    }

    public PatternMatcher setBoard(PlayerBoard board) {
        this.board = board;
        return this;
    }

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

    private boolean isCard(Coordinates coordinates) {
        return board.isWithinBounds(coordinates) && board.getCard(coordinates) != null;
    }

    private Resource getColor(Coordinates coordinates) {
        if (isCard(coordinates) && board.getCard(coordinates).isColored()) {
            return ((ColoredCard)board.getCard(coordinates)).getBackResource();
        }
        return Resource.NONE;
    }
}
