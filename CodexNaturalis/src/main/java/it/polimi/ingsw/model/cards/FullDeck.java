package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.ObjectiveParser;
import it.polimi.ingsw.helpers.parsers.StartingParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;
import it.polimi.ingsw.model.objectives.Objective;

import java.io.IOException;

import static it.polimi.ingsw.GameConsts.cardJsonPath;

/**
 * The FullDeck class represents a deck that contains all the cards in the game.
 * It provides static methods to access the full deck of different types of cards.
 */
public class FullDeck {

    private static final Deck<GoldCard> FullGoldDeck;
    private static final Deck<StdCard> FullStdDeck;
    private static final Deck<Objective> FullObjDeck;
    private static final Deck<StartingCard> FullStartingDeck;

    static {
        try {
            FullGoldDeck = new GoldCardParser().readFile(cardJsonPath).parse();
            FullStdDeck = new StdCardParser().readFile(cardJsonPath).parse();
            FullObjDeck = new ObjectiveParser().readFile(cardJsonPath).parse();
            FullStartingDeck = new StartingParser().readFile(cardJsonPath).parse();
        } catch (JsonFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a copy of the full deck of gold cards.
     *
     * @return a copy of the full deck of gold cards
     */
    public static Deck<GoldCard> getFullGoldDeck() {
        return new Deck<>(FullGoldDeck);
    }

    /**
     * Returns a copy of the full deck of standard cards.
     *
     * @return a copy of the full deck of standard cards
     */
    public static Deck<StdCard> getFullStdDeck() {
        return new Deck<>(FullStdDeck);
    }

    /**
     * Returns a copy of the full deck of objective cards.
     *
     * @return a copy of the full deck of objective cards
     */
    public static Deck<Objective> getFullObjDeck() {
        return new Deck<>(FullObjDeck);
    }

    /**
     * Returns a copy of the full deck of starting cards.
     *
     * @return a copy of the full deck of starting cards
     */
    public static Deck<StartingCard> getFullStartingDeck() {
        return new Deck<>(FullStartingDeck);
    }
}
