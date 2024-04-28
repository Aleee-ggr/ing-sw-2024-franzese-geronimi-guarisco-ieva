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
 * @author Daniele Ieva
 */
public class Objective {
    private final Function<Player, Integer> pointCalculator;
    private final int id;
    private final String type;
    private Resource[][] pattern = null;
    private Map<Resource, Integer> resource = null;


    /**
     * Constructs a new Objective with the specified point calculator function.
     * @param pointCalculator The function to calculate points for the objective based on the player.
     */
    public Objective(Function<Player, Integer> pointCalculator, int id, String type, JsonElement requirements) {
        this.pointCalculator = pointCalculator;
        this.id = id;
        this.type = type;
        setPatternAndResources(type, requirements);
    }

    public int getId() {
        return id;
    }

    public void setPatternAndResources(String type, JsonElement requirements) {
        switch (type) {
            case "pattern":
                this.pattern = ObjectiveParser.getPattern(requirements);
                break;
            case "resource":
                this.resource = ObjectiveParser.getResources(requirements);
                break;
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
