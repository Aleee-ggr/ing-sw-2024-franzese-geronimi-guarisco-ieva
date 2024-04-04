package it.polimi.ingsw.helpers.builders;

import it.polimi.ingsw.helpers.exceptions.InvalidTypeException;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.*;
import java.util.function.Function;

public class FunctionBuilder {
    public static final String[] validTypes = {
            "none",
            "pattern",
            "resource",
            "resources",
            "cover",
    };
    private int points;
    private int cardId;
    private String type;
    private  Map<Resource, Integer> resources;
    private Resource[][] pattern;
    private Resource resource;


    /**
     * @return a list of valid types for the method {@link #setType(String) setType}
     */
    public static List<String> getValidTypes() {
        return Collections.unmodifiableList(Arrays.asList(validTypes));
    }

    /**
     * The id of the card this function is used in, to use with the type "cover"
     * @param cardId the id of the card that will use this function
     * @return this object
     */
    public FunctionBuilder setCardId(int cardId) {
        this.cardId = cardId;
        return this;
    }

    /**
     * Set the resources and their amount to use to calculate the score  when paired with the type "resources"
     * @see #setType(String)
     * @see #setPoints(int)
     * @see #build()
     * @param resources the resource the function will use to calculate the score
     * @return this object
     */
    public FunctionBuilder setResources(Map<Resource, Integer> resources) {
        this.resources = resources;
        return this;
    }

    /**
     * A multiplier of the points a certain type yields
     * @param points the multiplier the function will use to calculate the score
     * @return this object
     */
    public FunctionBuilder setPoints(int points) {
        this.points = points;
        return this;
    }

    /**
     * A pattern that will yield points if it is matched on the game grid when the type is "pattern"
     * @see #setType(String)  
     * @see #setPoints(int)
     * @see #build()
     * @param pattern the pattern to use
     * @return this object
     */
    public FunctionBuilder setPattern(Resource[][] pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * Set the type of the function, listed in {@link #getValidTypes()},
     * to choose the strategy used to calculate the player score.
     * @param type the type of the function calculation
     * @return this object
     * @throws InvalidTypeException when the type is not included in {@link #getValidTypes()}
     */
    public FunctionBuilder setType(String type) throws InvalidTypeException {
        if (!Arrays.asList(validTypes).contains(type)) {
            throw new InvalidTypeException(type + " is not a valid type");
        }
        this.type = type;
        return this;
    }

    /**
     * Set the resource to use to calculate the score  when paired with the type "resource"
     * @see #setType(String)
     * @see #setPoints(int)
     * @see #build()
     * @param resource the resource the function will use to calculate the score
     * @return this object
     */
    public FunctionBuilder setResource(Resource resource) {
        this.resource = resource;
        return this;
    }

    /**
     * returns a function of the given type to calculate a player score, using the given type and score
     * @see #setType(String)
     * @see Player
     * @return a function of the given type to calculate a player score
     * @throws InvalidTypeException when the type is not included in {@link #getValidTypes()}
     */
    public Function<Player, Integer> build() throws InvalidTypeException {
        return switch (type) {
            case "none" ->
                    (Player player) -> points;

            case "pattern" ->
                    (Player player) -> {
                        //TODO implement the actual function
                        return points;
                    };

            case "cover" ->
                    (Player player) -> {
                        PlayerBoard board = player.getPlayerBoard();
                        Coordinates lastCoordinates = board.getLastPlacedPosition();
                        int neighbor_count = 0;
                        for (Coordinates neighbor : lastCoordinates.getNeighbors()) {
                            if (board.getBoard()[neighbor.getX()][neighbor.getY()] != null) {
                                neighbor_count++;
                            }
                        }
                        return neighbor_count * points;
                    };

            case "resources" ->
                    (Player player) -> {
                        Map<Resource, Integer> playerResources = player.getResources();
                        int resourcePoints;
                        Map<Resource, Integer> result = new HashMap<>();
                        for (Resource res : resources.keySet()) {
                            resourcePoints = (playerResources.get(res) / resources.get(res)) * points;
                            result.put(res, resourcePoints);
                        }
                        return Collections.min(result.values());
                    };
            
            case "resource" ->
                    (Player player) -> {
                        Map<Resource, Integer> playerResources = player.getResources();
                        return playerResources.get(resource) * points;
                    };

            default -> throw new InvalidTypeException(type + "is invalid");
        };
    }

}
