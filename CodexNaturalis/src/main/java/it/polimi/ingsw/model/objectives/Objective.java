package it.polimi.ingsw.model.objectives;

import it.polimi.ingsw.model.Game;

import java.util.function.Function;

public class Objective {
    private final Function<Game, Integer> pointCalculator;

    public Objective(Function<Game, Integer> pointCalculator) {
        this.pointCalculator = pointCalculator;
    }

    public Integer getPoints(Game game) {
        return this.pointCalculator.apply(game);
    }

}