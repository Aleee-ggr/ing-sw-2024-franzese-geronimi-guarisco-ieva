package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Represents a gold card in the game.
 * Extends the ColoredCard class and adds information about requirements and a function to calculate the score.
 */
public class GoldCard extends ColoredCard {
    private final ConcurrentHashMap<Resource, Integer> requirements;
    private final Function<Player, Integer> calculateScore;
    private final int score;
    private final String type;

    /**
     * Constructor for the GoldCard class.
     *
     * @param id             Unique identifier of the card.
     * @param frontCorners   Array of corners on the front side of the card.
     * @param backResource   Resource on the back side of the card.
     * @param requirements   Map of resource requirements for the card.
     * @param calculateScore Function to calculate the score based on the player.
     */
    public GoldCard(int id, Corner[] frontCorners, Resource backResource, Map<Resource, Integer> requirements,
                    Function<Player, Integer> calculateScore, int score, String type) {
        super(id, frontCorners, backResource);
        this.requirements = new ConcurrentHashMap<>(requirements);
        this.calculateScore = calculateScore;
        this.score = score;
        this.type = type;
    }

    /**
     * Getter for the score of the card.
     *
     * @return The score for the card.
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for the type of the card.
     *
     * @return The String type of the card.
     */
    public String getType() {
        return type;
    }

    /**
     * Checks if the specified player meets the requirements to place this gold card.
     * Returns true if the player satisfies all resource requirements specified for this card,
     * otherwise returns false.
     *
     * @param player The player whose resources are being checked against the card's requirements.
     * @return True if the player meets the requirements, false otherwise.
     */
    public boolean checkRequirements(Player player) {

        for (Resource key : requirements.keySet()) {
            if (player.getResources().get(key) < requirements.get(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the score for the player based on the card's requirements using the strategy.
     *
     * @param player The player for whom the score is calculated.
     * @return The calculated score.
     */
    public Integer getScore(Player player) {
        return this.calculateScore.apply(player);
    }

    /**
     * Retrieves the requirements for the card.
     *
     * @return The requirements for the card.
     */
    public ConcurrentHashMap<Resource, Integer> getRequirements() {
        return new ConcurrentHashMap<>(requirements);
    }
}
