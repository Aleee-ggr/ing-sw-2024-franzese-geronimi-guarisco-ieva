package it.polimi.ingsw.model.objectives;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.helpers.parsers.ObjectiveParser;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.Map;
import java.util.function.Function;

/**
 * Represents an objective in the game.
 * Objectives define criteria for scoring points for players.
 */
public class Objective {
    private final Function<Player, Integer> pointCalculator;
    private final int id;
    private final String type;
    private Resource[][] pattern = null;
    private Map<Resource, Integer> resources = null;

    /**
     * Constructs a new Objective with the specified point calculator function.
     * @param pointCalculator The function to calculate points for the objective based on the player.
     * @param id The unique identifier of the objective.
     * @param type The type of the objective.
     * @param requirements The requirements for the objective in JSON format.
     */
    public Objective(Function<Player, Integer> pointCalculator, int id, String type, JsonElement requirements) {
        this.pointCalculator = pointCalculator;
        this.id = id;
        this.type = type;
        setPatternAndResources(type, requirements);
    }

    /**
     * Gets the unique identifier of the objective.
     * @return The unique identifier of the objective.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the type of the objective.
     * @return The type of the objective.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the pattern associated with the objective.
     * @return The pattern associated with the objective.
     */
    public Resource[][] getPattern() {
        return pattern;
    }

    /**
     * Gets the resources associated with the objective.
     * @return The resources associated with the objective.
     */
    public Map<Resource, Integer> getResource() {
        return resources;
    }

    /**
     * Sets the pattern and resources for the objective based on the type and requirements.
     * @param type The type of the objective.
     * @param requirements The requirements for the objective in JSON format.
     */
    public void setPatternAndResources(String type, JsonElement requirements) {
        switch (type) {
            case "pattern":
                this.pattern = ObjectiveParser.getPattern(requirements);
                break;
            case "resources":
                this.resources = ObjectiveParser.getResources(requirements);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    /**
     * Calculates the points for the objective based on the player.
     * @param player The player for whom the points are calculated.
     * @return The calculated points for the objective.
     */
    public Integer getPoints(Player player) {
        return this.pointCalculator.apply(player);
    }
}
