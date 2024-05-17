package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the shared board in the game.
 * The shared board contains information about objectives, scores, visible cards, and decks of cards.
 * @author Daniele Ieva
 */

public class SharedBoard {
    private final Objective[] objectives = new Objective[GameConsts.globalObjectives];
    private final ConcurrentHashMap<Player, Integer> scoreMap = new ConcurrentHashMap<>();
    private final Deck<GoldCard> goldDeck;
    private final Deck<StdCard> stdDeck;
    private final ColoredCard[] visibleCards = new ColoredCard[4];

    /**
     * Constructor for the SharedBoard class.
     * @param goldDeck The deck of gold cards.
     * @param stdDeck The deck of standard cards.
     */
    public SharedBoard(Deck<GoldCard> goldDeck, Deck<StdCard> stdDeck) {
        this.goldDeck = goldDeck;
        this.stdDeck = stdDeck;
        visibleCards[0] = goldDeck.draw();
        visibleCards[1] = goldDeck.draw();
        visibleCards[2] = stdDeck.draw();
        visibleCards[3] = stdDeck.draw();
    }

    /**
     * Retrieves the deck of gold cards from the shared board.
     * @return The deck of gold cards.
     */
    public Deck<GoldCard> getGoldDeck() {
        return goldDeck;
    }

    /**
     * Retrieves the deck of standard cards from the shared board.
     * @return The deck of standard cards.
     */
    public Deck<StdCard> getStdDeck() {
        return stdDeck;
    }

    /**
     * Retrieves the visible cards on the shared board.
     * @return An array containing the visible cards.
     */
    public Card[] getVisibleCards() {
        return Arrays.copyOf(visibleCards, GameConsts.visibleCards);
    }

    public void setObjectives(Objective[] obj){
        try {
            System.arraycopy(obj, 0, this.objectives, 0, objectives.length);
        } catch (IndexOutOfBoundsException e){
            System.out.println("not enough obj");
        }
    }

    /**
     * Substitutes a card on the shared board at the specified position with a new card from the deck.
     * @param position The position of the card to be substituted.
     * @param isGold Indicates whether the card to be substituted is a gold card.
     */
    public void substituteCard(int position, boolean isGold) {
        if (isGold) {
            if (!goldDeck.isEmpty()){
                visibleCards[position] = goldDeck.draw();
                return;
            }
            if (!stdDeck.isEmpty()) {
                visibleCards[position] = stdDeck.draw();
                return;
            }
        } else {
            if (!stdDeck.isEmpty()){
                visibleCards[position] = stdDeck.draw();
                return;
            }
            if (!goldDeck.isEmpty()) {
                visibleCards[position] = goldDeck.draw();
            }
        }
    }

    /**
     * Checks if there are no cards left in the decks and on the shared board.
     * @return True if there are no cards left, otherwise false.
     */
    public boolean areCardsOver() {
        if (!goldDeck.isEmpty() || !stdDeck.isEmpty()) {
            return false;
        }

        for (var card : visibleCards) {
            if (card != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Draws a card from the specified deck.
     * @param isGold Indicates whether the card should be drawn from the gold card deck.
     * @return The drawn card, or null if the deck is empty.
     */
    public ColoredCard drawDeck(boolean isGold) {
        if (isGold && !goldDeck.isEmpty()) {
            return goldDeck.draw();
        }
        if (!stdDeck.isEmpty()) {
            return stdDeck.draw();
        }
        return null;
    }

    /**
     * Retrieves the card at the specified position from the visible cards.
     * @param position The position of the card to retrieve.
     * @return The card at the specified position, or null if no card is present.
     */
    public ColoredCard drawVisible(int position) {
        if (visibleCards[position] != null) {
            ColoredCard card = visibleCards[position];
            if (position == 0 || position == 1) {
                substituteCard(position, true);
            } else if (position == 2 || position == 3) {
                substituteCard(position, false);
            }
            return card;
        }
        return null;
    }

    /**
     * Checks if the game is over by verifying if any player has reached the ending score.
     * @return True if the game is over, otherwise false.
     */
    public boolean isOver() {
        for (int score : scoreMap.values()) {
            if (score >= GameConsts.endingScore) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a copy of the score map.
     * @return A copy of the score map.
     */
    public ConcurrentHashMap<Player, Integer> getScore() {
        return new ConcurrentHashMap<>(scoreMap); // Copy the map instead of returning the object itself
    }

    /**
     * Updates the score of the specified player by the given value.
     *
     * @param player the player whose score needs to be updated
     * @param value the value by which the score should be updated
     */
    public void updateScore(Player player, int value) {
        if (!scoreMap.containsKey(player)) {
            scoreMap.put(player, value);
            return;
        }
        scoreMap.put(player, scoreMap.get(player) + value);
    }

    /**
     * Retrieves the global objectives on the shared board.
     * @return An array containing the global objectives.
     */
    public Objective[] getGlobalObjectives() {
        return Arrays.copyOf(objectives, GameConsts.globalObjectives);
    }

    /**
     * Draw a card in the given position, if available, else null
     * @param position the position of the card (0-3: visible, 4: stdDeck, 5: goldDeck)
     * @return the drawn card
     */
    public Card draw(Integer position) {
        if (position == 4) {
            return drawDeck(true);
        }
        if (position == 5) {
            return drawDeck(false);
        }
        return drawVisible(position);
    }
}
