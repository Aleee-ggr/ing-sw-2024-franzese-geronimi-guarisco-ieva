package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private final ArrayList<Card> cards;
    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        synchronized (cards) {
            Collections.shuffle(cards);
        }
    }

    public Card draw() {
        synchronized (cards) {
            return cards.removeFirst();
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
