package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedBoard {
    private final Objective[] objectives = new Objective[GameConsts.globalObjectives];
    private final ConcurrentHashMap<Player, Integer> scoreMap = new ConcurrentHashMap<>();
    private final Deck<GoldCard> goldDeck;
    private final Deck<StdCard> stdDeck;
    private final Card[] visibleCards = new Card[4];

    public SharedBoard(Deck<GoldCard> goldDeck, Deck<StdCard> stdDeck) {
        this.goldDeck = goldDeck;
        this.stdDeck = stdDeck;
    }

    public Card[] getVisibleCards() {
        return Arrays.copyOf(visibleCards, GameConsts.visibleCards);
    }

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
        }

        if (!isGold && !stdDeck.isEmpty()) {
            if (!stdDeck.isEmpty()){
                visibleCards[position] = goldDeck.draw();
                return;
            }
            if (!goldDeck.isEmpty()) {
                visibleCards[position] = stdDeck.draw();
            }
        }
    }

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

    public Card drawDeck(boolean isGold) {
        if (isGold && !goldDeck.isEmpty()) {
            return goldDeck.draw();
        }
        if (!stdDeck.isEmpty()) {
            return stdDeck.draw();
        }
        //TODO check whether the card exists
        return null;
    }

    public Card drawVisible(int position) {
        if (visibleCards[position] != null) {
            return visibleCards[position];
        }
        //TODO check whether the card exists
        return null;
    }

    public Map<Player, Integer> getScore() {
        return new ConcurrentHashMap<>(scoreMap); // Copy the map instead of returning the object itself
    }

    public Objective[] getGlobalObjectives() {
        return Arrays.copyOf(objectives, GameConsts.globalObjectives);
    }
}
