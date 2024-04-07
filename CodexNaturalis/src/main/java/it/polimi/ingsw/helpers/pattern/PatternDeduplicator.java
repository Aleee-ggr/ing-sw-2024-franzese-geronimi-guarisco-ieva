package it.polimi.ingsw.helpers.pattern;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.*;

public class PatternDeduplicator {
    private final Set<Pattern> patterns;
    private Set<Pattern> output = new HashSet<>();
    private final Set<Pattern> checked = new HashSet<>();

    public PatternDeduplicator(Set<Pattern> patterns) {
        this.patterns = patterns;
    }

    public PatternDeduplicator removeDuplicates() {
        for (Pattern pattern : patterns) {
            if (checked.contains(pattern)) {continue;}
            Set<Pattern> conflicts = getConflictGroup(pattern);
            deduplicate(conflicts);
            Set<Pattern> newOutput = new HashSet<>(output);
            for (Pattern p1: output) {
                for (Pattern p2 : output) {
                    if (!newOutput.contains(p1) || !newOutput.contains(p2)) {continue;}
                    if (!p1.equals(p2) && p1.getOverlap(p2) > 0) {
                        newOutput.remove(p2);
                    }
                }
            }
            output = newOutput;
        }
        return this;
    }

    public int countDistinct() {
        if (output.isEmpty() && !patterns.isEmpty()) {
            removeDuplicates();
        }
        return output.size();
    }

    private Set<Pattern> getConflictGroup(Pattern pattern) {
        Set<Pattern> conflicts = new HashSet<>();
        Queue<Pattern> queue = new ArrayDeque<>();
        conflicts.add(pattern);
        queue.add(pattern);
        while (!queue.isEmpty()) {
            Pattern cur = queue.remove();
            checked.add(cur);
            for (Pattern other : patterns) {
                if (!checked.contains(other) && !other.equals(cur) && cur.getOverlap(other) > 0) {
                    checked.add(other);
                    conflicts.add(other);
                    queue.add(other);
                }
            }
        }
        return conflicts;
    }

    private Set<Coordinates> getConflictingCoordinates(Set<Pattern> patterns) {
        Set<Coordinates> coordinates = new HashSet<>();
        for (Pattern pattern : patterns) {

            coordinates.addAll(pattern.getCoordinates());
        }

        return coordinates;
    }

    /**
     *
     * @param patterns a set of conflicting patterns with some overlap
     * @return a map that associates a set of coordinates to how many patterns contains them
     */
    private Map<Coordinates, Integer> countPatternPerCoordinates(Set<Pattern> patterns) {
        Set<Coordinates> coordinates = getConflictingCoordinates(patterns);
        Map<Coordinates, Integer> result = new HashMap<>();
        for (Coordinates current : coordinates) {
            int count = 0;
            for (Pattern pattern : patterns) {
                if (pattern.contains(current)) {
                    count++;
                }
            }
            result.put(current, count);
        }
        return result;
    }

    /**
     * Recursively store necessary patterns for coverage based on the coordinateCounter
     * @param patterns a set of conflicting patterns with some overlap
     */
    private void deduplicate(Set<Pattern> patterns) {
        Map<Coordinates, Integer> coordinateCounter = countPatternPerCoordinates(patterns);
        saveUnique(patterns, coordinateCounter);
    }

    /**
     * Recursively store necessary patterns for coverage based on the coordinateCounter
     * @param patterns a set of conflicting patterns with some overlap
     * @param coordinateCounter a map that associates a set of coordinates to how many patterns contains them
     */
    private void saveUnique(Set<Pattern> patterns, Map<Coordinates, Integer> coordinateCounter) {
        Set<Pattern> toAdd = new HashSet<>();
        Set<Pattern> toRemove = new HashSet<>();

        for (Coordinates coordinates : coordinateCounter.keySet()) {
            if (coordinateCounter.get(coordinates) == 1) {
                for (Pattern pattern : patterns) {
                    if (pattern.contains(coordinates)) {
                        toAdd.add(pattern);
                    }
                }
            }
        }
        for (Pattern current : toAdd) {
            for (Pattern other : patterns) {
                if (current.getOverlap(other) > 0) {
                    toRemove.add(other);
                }
            }
        }

        output.addAll(toAdd);
        patterns.removeAll(toRemove);
        if (!patterns.isEmpty()) {
            saveUnique(patterns, countPatternPerCoordinates(patterns));
        }
    }
}
