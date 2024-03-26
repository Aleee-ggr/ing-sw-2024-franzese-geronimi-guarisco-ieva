package it.polimi.ingsw.model.objectives;

import it.polimi.ingsw.model.player.Player;

import java.util.function.Function;

public class Objective {
    private final Function<Player, Integer> pointCalculator;

    public Objective(Function<Player, Integer> pointCalculator) {
        this.pointCalculator = pointCalculator;
    }

    public Integer getPoints(Player player) {
        return this.pointCalculator.apply(player);
    }

}
