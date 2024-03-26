package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.Resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Represents a gold card in the game.
 * Extends the ColoredCard class and adds information about requirements and a function to calculate the score.
 * @author Samuele Franzese
 */
public class GoldCard extends ColoredCard{
    private final ConcurrentHashMap<Resource, Integer> requirements;
    private final Function<Game, Integer> calculateScore;
    public GoldCard(int id, Corner[] frontCorners, Resource backResource, Map<Resource, Integer> requirements,
                    Function<Game, Integer> calculateScore) {
        super(id, frontCorners, backResource);
        this.requirements = new ConcurrentHashMap<>(requirements);
        this.calculateScore = calculateScore;
    }

    public Integer getCalculateScore(Game game) {
        return this.calculateScore.apply(game);
    }

    public ConcurrentHashMap<Resource, Integer> getRequirements() {
        return new ConcurrentHashMap<>(requirements);
    }
}
