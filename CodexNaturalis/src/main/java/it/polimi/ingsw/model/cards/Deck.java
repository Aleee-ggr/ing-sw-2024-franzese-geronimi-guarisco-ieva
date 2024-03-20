package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Collections;

public class Deck<CardType> {
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
            return cards.removeFirst();
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    private final ArrayList<CardType> cards;
}
