package it.polimi.ingsw.model.objectives;

import it.polimi.ingsw.model.player.Player;

import java.util.function.Function;

/**
 * Represents an objective in the game.
 * Objectives define criteria for scoring points for players.
 * @author Daniele Ieva
 */
public class Objective {
    private final Function<Player, Integer> pointCalculator;

    /**
     * Constructs a new Objective with the specified point calculator function.
     * @param pointCalculator The function to calculate points for the objective based on the player.
     */
    public Objective(Function<Player, Integer> pointCalculator) {
        this.pointCalculator = pointCalculator;
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
