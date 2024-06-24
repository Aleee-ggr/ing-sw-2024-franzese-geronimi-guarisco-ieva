package it.polimi.ingsw.helpers.builders;

import it.polimi.ingsw.helpers.exceptions.model.InvalidTypeException;
import it.polimi.ingsw.helpers.pattern.PatternMatcher;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.*;
import java.util.function.Function;

public class FunctionBuilder {
    public static final String[] validTypes = {"none", "pattern", "resource", "resources", "cover",};
    private final Map<Coordinates, Resource> pattern = new HashMap<>(3);
    private int points;
    private String type;
    private Map<Resource, Integer> resources;
    private Resource resource;

    /**
     * @return a list of valid types for the method {@link #setType(String) setType}
     */
    public static List<String> getValidTypes() {
        return Collections.unmodifiableList(Arrays.asList(validTypes));
    }

    /**
     * Rotate a set of coordinates with value between -1 and 1 (inclusive)
     *
     * @param coordinates an offset of coordinates in range (-1, 1) inclusive
     * @return a new set of coordinates, rotated clockwise 45deg
     */
    private static Coordinates rotate(Coordinates coordinates) {
        Coordinates result;
        int x = coordinates.x();
        int y = coordinates.y();
        
        if (x == 1 && y != 1) {
            y++;
        } else if (y == 1 && x != -1) {
            x--;
        } else if (x == -1 && y != -1) {
            y--;
        } else if (y == -1) {
            x++;
        }


        result = new Coordinates(y, x);
        return result;
    }

    /**
     * Set the resources and their amount to use to calculate the score when paired
     * with the type "resources"
     *
     * @param resources the resource the function will use to calculate the score
     * @return this object
     * @see #setType(String)
     * @see #setPoints(int)
     * @see #build()
     */
    public FunctionBuilder setResources(Map<Resource, Integer> resources) {
        this.resources = resources;
        return this;
    }

    /**
     * A multiplier of the points a certain type yields
     *
     * @param points the multiplier the function will use to calculate the score
     * @return this object
     */
    public FunctionBuilder setPoints(int points) {
        this.points = points;
        return this;
    }

    /**
     * A pattern that will yield points if it is matched on the game grid when the
     * type is "pattern"
     *
     * @param pattern the pattern to use
     * @return this object
     * @see #setType(String)
     * @see #setPoints(int)
     * @see #build()
     */
    public FunctionBuilder setPattern(Resource[][] pattern) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (pattern[y][x] != Resource.NONE) {
                    this.pattern.put(rotate(new Coordinates(x - 1, y - 1)), pattern[y][x]);
                }
            }
        }
        return this;
    }

    /**
     * Set the type of the function, listed in {@link #getValidTypes()},
     * to choose the strategy used to calculate the player score.
     *
     * @param type the type of the function calculation
     * @return this object
     * @throws InvalidTypeException when the type is not included in
     *                              {@link #getValidTypes()}
     */
    public FunctionBuilder setType(String type) throws InvalidTypeException {
        if (!Arrays.asList(validTypes).contains(type)) {
            throw new InvalidTypeException(type + " is not a valid type");
        }
        this.type = type;
        return this;
    }

    /**
     * Set the resource to use to calculate the score when paired with the type
     * "resource"
     *
     * @param resource the resource the function will use to calculate the score
     * @return this object
     * @see #setType(String)
     * @see #setPoints(int)
     * @see #build()
     */
    public FunctionBuilder setResource(Resource resource) {
        this.resource = resource;
        return this;
    }

    /**
     * returns a function of the given type to calculate a player score, using the
     * given type and score
     *
     * @return a function of the given type to calculate a player score
     * @throws InvalidTypeException when the type is not included in
     *                              {@link #getValidTypes()}
     * @see #setType(String)
     * @see Player
     */
    public Function<Player, Integer> build() throws InvalidTypeException {
        return switch (type) {
            case "none" -> (Player player) -> points;

            case "pattern" -> (Player player) -> {
                PlayerBoard board = player.getPlayerBoard();
                int matches_found = new PatternMatcher().setPattern(pattern).setBoard(board).matches_found();
                return matches_found * points;
            };

            case "cover" -> (Player player) -> {
                PlayerBoard board = player.getPlayerBoard();
                Coordinates lastCoordinates = board.getLastPlacedPosition();
                int neighbor_count = 0;
                for (Coordinates neighbor : lastCoordinates.getNeighbors()) {
                    if (board.getCard(neighbor) != null && board.getCard(neighbor).getId() != 0) {
                        neighbor_count++;
                    }
                }
                return neighbor_count * points;
            };

            case "resources" -> (Player player) -> {
                Map<Resource, Integer> playerResources = player.getResources();
                int resourcePoints;
                Map<Resource, Integer> result = new HashMap<>();
                for (Resource res : resources.keySet()) {
                    resourcePoints = (playerResources.get(res) / resources.get(res)) * points;
                    result.put(res, resourcePoints);
                }
                return Collections.min(result.values());
            };

            case "resource" -> (Player player) -> {
                Map<Resource, Integer> playerResources = player.getResources();
                return playerResources.get(resource) * points;
            };

            default -> throw new InvalidTypeException(type + "is invalid");
        };
    }
}
