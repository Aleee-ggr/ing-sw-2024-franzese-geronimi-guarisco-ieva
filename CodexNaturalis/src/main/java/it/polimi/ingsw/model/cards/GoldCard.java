package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.Resource;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class GoldCard extends ColoredCard{
    private final ConcurrentHashMap<Resource, Integer> requirements = new ConcurrentHashMap<>();
    private final Function<Game, Integer> calculateScore;
    public GoldCard(int id, Corner[] frontCorners, Resource backResource, Function<Game, Integer> calculateScore) {
        super(id, frontCorners, backResource);
        this.calculateScore = calculateScore;
    }

    public Integer getCalculateScore(Game game) {
        return this.calculateScore.apply(game);
    }

    public ConcurrentHashMap<Resource, Integer> getRequirements() {
        return new ConcurrentHashMap<>(requirements);
    }
}
