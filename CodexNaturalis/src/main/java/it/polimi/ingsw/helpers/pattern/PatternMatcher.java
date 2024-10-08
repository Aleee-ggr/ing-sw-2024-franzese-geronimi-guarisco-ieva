package it.polimi.ingsw.helpers.pattern;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.cards.MockCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.Resource;

import java.util.*;

public class PatternMatcher {
    private final Set<Coordinates> visited = new HashSet<>();
    private final Queue<Coordinates> toVisit = new ArrayDeque<>();
    private final Set<Pattern> matched = new HashSet<>();
    private Map<Coordinates, Resource> pattern;
    private PlayerBoard board;
    private Set<Resource> patternResources;

    /**
     * set the pattern to find in the board
     *
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
     *
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
        Coordinates current;

        for (var cards : board.getBoard().entrySet()) {
            if (cards.getValue() instanceof StartingCard || cards.getValue() instanceof MockCard) {
                continue;
            }

            ColoredCard coloredCard = (ColoredCard) cards.getValue();
            if (patternResources.contains(coloredCard.getBackResource())) {
                BFSMatch(cards.getKey());
            }
        }

//        while (!toVisit.isEmpty()) {
//            current = toVisit.remove();
//            if (visited.contains(current)) {
//                continue;
//            }
//
//            visited.add(current);
//
//            if (patternResources.contains(getColor(current))) {
//                BFSMatch(current);
//            }
//
//            for (Coordinates neighbor : current.getNeighbors()) {
//                if (isCard(neighbor) && !visited.contains(neighbor) && !toVisit.contains(neighbor)) {
//                    toVisit.add(neighbor);
//                }
//            }
//        }
        return new PatternDeduplicator(matched).countDistinct();
    }

    /**
     * Find matches on the board against the given pattern using BFS
     *
     * @param coordinates the coordinates from where to find the pattern
     */
    private void BFSMatch(Coordinates coordinates) {
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
                    Set<Coordinates> found_pattern = new HashSet<>();
                    for (Coordinates key : pattern.keySet()) {
                        Coordinates pattern_offset = key.subtract(pattern_coords);
                        Coordinates inPattern = coordinates.add(pattern_offset);
                        found_pattern.add(inPattern);
                    }
                    matched.add(new Pattern(found_pattern));
                }
            }
        }
    }

    /**
     * Check whether the board contains a card at the given coordinates
     *
     * @param coordinates the coordinates where to check
     * @return whether the board contains a card at the given coordinates
     */
    private boolean isCard(Coordinates coordinates) {
        return board.getCard(coordinates) != null;
    }

    /**
     * Check whether a card is an instance of colored card and return its resource type
     *
     * @param coordinates the coordinates where to check
     * @return NONE if the card is not an instance of ColoredCard, is resource type otherwise
     */
    private Resource getColor(Coordinates coordinates) {
        if (isCard(coordinates) && board.getCard(coordinates).isColored()) {
            return ((ColoredCard) board.getCard(coordinates)).getBackResource();
        }
        return Resource.NONE;
    }
}
