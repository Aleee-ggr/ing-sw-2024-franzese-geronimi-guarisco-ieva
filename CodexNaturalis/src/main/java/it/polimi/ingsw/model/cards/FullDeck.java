package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.ObjectiveParser;
import it.polimi.ingsw.helpers.parsers.StartingParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;
import it.polimi.ingsw.model.objectives.Objective;

import java.io.IOException;
import java.nio.file.Path;

public class FullDeck {
    private static final Path cardJsonPath = Path.of(GameConsts.cardJsonPath);

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



    public static Deck<GoldCard> getFullGoldDeck() {
        return new Deck<>(FullGoldDeck);
    }

    public static Deck<StdCard> getFullStdDeck() {
        return new Deck<>(FullStdDeck);
    }

    public static Deck<Objective> getFullObjDeck() {
        return new Deck<>(FullObjDeck);
    }

    public static Deck<StartingCard> getFullStartingDeck() {
        return new Deck<>(FullStartingDeck);
    }
}
