package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.goldCard;
import it.polimi.ingsw.model.cards.stdCard;

import java.util.Arrays;

public class DeckArea {

    public DeckArea(Deck<goldCard> goldDeck, Deck<stdCard> stdDeck) {
        this.goldDeck = goldDeck;
        this.stdDeck = stdDeck;
    }

    public goldCard[] getGoldCards() {
        return Arrays.copyOf(goldCards, GameConsts.visibleGoldCards);
    }

    public stdCard[] getStdCards() {
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

    private final Deck<goldCard> goldDeck;
    private final Deck<stdCard> stdDeck;
    private final goldCard[] goldCards = new goldCard[GameConsts.visibleGoldCards];
    private final stdCard[] stdCards = new stdCard[GameConsts.visibleStdCards];
}
