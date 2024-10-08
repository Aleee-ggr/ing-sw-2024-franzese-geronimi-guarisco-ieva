package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a generic deck of cards.
 *
 * @param <CardType> The type of cards contained in the deck.
 */

public class Deck<CardType> {
    private final ArrayList<CardType> cards;

    /**
     * Constructor for the Deck class.
     *
     * @param cards The list of cards to initialize the deck with.
     */
    public Deck(ArrayList<CardType> cards) {
        this.cards = cards;
    }

    /**
     * Constructor for the Deck class.
     *
     * @param deck The deck to copy.
     */
    public Deck(Deck<CardType> deck) {
        this.cards = new ArrayList<>(deck.getCards());
    }

    /**
     * Shuffles the cards in the deck.
     */
    public Deck<CardType> shuffle() {
        synchronized (cards) {
            Collections.shuffle(cards);
        }
        return this;
    }

    /**
     * Draws a card from the deck.
     *
     * @return The card drawn from the deck, or null if the deck is empty.
     */
    public CardType draw() {
        synchronized (cards) {
            if (!cards.isEmpty()) {
                return cards.removeFirst();
            }
            return null;
        }
    }

    /**
     * Peeks at the first card in the deck.
     * It's used for getting the back color of the Deck.
     *
     * @return The first card in the deck, or null if the deck is empty.
     */
    public CardType peekFirstCard() {
        synchronized (cards) {
            if (!cards.isEmpty()) {
                return cards.getFirst();
            }
            return null;
        }
    }

    /**
     * Checks if the deck is empty.
     *
     * @return True if the deck is empty, otherwise false.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Retrieves the list of cards in the deck.
     *
     * @return A copy of the list of cards in the deck.
     */
    public ArrayList<CardType> getCards() {
        return new ArrayList<>(cards);
    }
}
