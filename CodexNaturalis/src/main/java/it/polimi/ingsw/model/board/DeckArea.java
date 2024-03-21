package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;

import java.util.Arrays;

public class DeckArea {
    private final Deck<GoldCard> goldDeck;
    private final Deck<StdCard> stdDeck;
    private final GoldCard[] goldCards = new GoldCard[GameConsts.visibleGoldCards];
    private final StdCard[] stdCards = new StdCard[GameConsts.visibleStdCards];

    public DeckArea(Deck<GoldCard> goldDeck, Deck<StdCard> stdDeck) {
        this.goldDeck = goldDeck;
        this.stdDeck = stdDeck;
    }

    public GoldCard[] getGoldCards() {
        return Arrays.copyOf(goldCards, GameConsts.visibleGoldCards);
    }

    public StdCard[] getStdCards() {
        return Arrays.copyOf(stdCards, GameConsts.visibleStdCards);
    }

    public void substituteCard(int position, boolean isGold) {
        if (isGold && !goldDeck.isEmpty()) {
            goldCards[position] = goldDeck.draw();
            return;
        }
        if (!stdDeck.isEmpty()) {
            stdCards[position] = stdDeck.draw();
        }
    }

    public boolean areCardsOver() {
        if (!goldDeck.isEmpty() || !stdDeck.isEmpty()) {
            return false;
        }

        for (var card : goldCards) {
            if (card != null) {
                return false;
            }
        }

        for (var card : stdCards) {
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

    public Card drawVisible(int position, boolean isGold) {
        if (isGold && goldCards[position] != null) {
            return goldCards[position];
        }
        if (stdCards[position] != null) {
            return stdCards[position];
        }
        //TODO check whether the card exists
        return null;
    }
}
