package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

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
    private final Function<Player, Integer> calculateScore;

    /**
     * Constructor for the GoldCard class.
     * @param id Unique identifier of the card.
     * @param frontCorners Array of corners on the front side of the card.
     * @param backResource Resource on the back side of the card.
     * @param requirements Map of resource requirements for the card.
     * @param calculateScore Function to calculate the score based on the player.
     */
    public GoldCard(int id, Corner[] frontCorners, Resource backResource, Map<Resource, Integer> requirements,
                    Function<Player, Integer> calculateScore) {
        super(id, frontCorners, backResource);
        this.requirements = new ConcurrentHashMap<>(requirements);
        this.calculateScore = calculateScore;
    }

    /**
     * Calculates the score for the player based on the card's requirements using the strategy.
     * @param player The player for whom the score is calculated.
     * @return The calculated score.
     */
    public Integer getCalculateScore(Player player) {
        return this.calculateScore.apply(player);
    }

    /**
     * Retrieves the requirements for the card.
     * @return The requirements for the card.
     */
    public ConcurrentHashMap<Resource, Integer> getRequirements() {
        return new ConcurrentHashMap<>(requirements);
    }
}
