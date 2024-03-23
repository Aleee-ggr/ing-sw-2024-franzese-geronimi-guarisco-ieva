package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Collections;

public class Deck<CardType> {
    private final ArrayList<CardType> cards;

    public Deck(ArrayList<CardType> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        synchronized (cards) {

            Collections.shuffle(cards);
        }
    }

    public CardType draw() {
        synchronized (cards) {
            if (!cards.isEmpty()) {
                return cards.removeFirst();
            }
            return null;
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

}
