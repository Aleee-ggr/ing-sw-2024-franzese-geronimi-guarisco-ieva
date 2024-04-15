package it.polimi.ingsw.helpers;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.StartingParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.cards.StdCard;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public abstract class DeckFactory {
    private static StdCardParser stdParser = new StdCardParser();
    private static GoldCardParser goldParser = new GoldCardParser();

    private static StartingParser startingParser = new StartingParser();

    public static void setupParser() {
        Path cardJsonPath = Path.of(GameConsts.cardJsonPath);

        try {
            stdParser.readFile(cardJsonPath);
            goldParser.readFile(cardJsonPath);
            startingParser.readFile(cardJsonPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static Deck<GoldCard> emptyGold() {
        ArrayList<GoldCard> empty = new ArrayList<>();
        return new Deck<GoldCard>(empty);
    }

    public static Deck<GoldCard> fullGold() {
        try {
            return goldParser.parse();
        } catch (JsonFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static Deck<StdCard> emptyStd() {
        ArrayList<StdCard> empty = new ArrayList<>();
        return new Deck<StdCard>(empty);
    }

    public static Deck<StdCard> fullStd() {
        try {
            return stdParser.parse();
        } catch (JsonFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static Deck<StartingCard> fullStarting() {
        try {
            return startingParser.parse();
        } catch (JsonFormatException e) {
            throw new RuntimeException(e);
        }

    }

}
