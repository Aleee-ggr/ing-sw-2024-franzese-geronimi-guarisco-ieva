package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;

import java.nio.file.Path;

public class FullDeck {

    private static Deck<GoldCard> FullGoldDeck;
    private static Deck<StdCard> FullStdDeck;
    private static final Path cardJsonPath = Path.of(GameConsts.cardJsonPath);

    public FullDeck(){
        BuildGoldDeck();
        BuildStdDeck();
    }

    public static Deck<GoldCard> getFullGoldDeck() {
        return FullGoldDeck;
    }

    public static Deck<StdCard> getFullStdDeck() {
        return FullStdDeck;
    }

    //TODO: better error handling, also for parsing
    public static void BuildGoldDeck() {
        GoldCardParser goldParser = new GoldCardParser();
        try {
            goldParser.readFile(cardJsonPath);
        } catch (Exception e) {
            System.out.println("read-file Exception");
        }
        FullGoldDeck = goldParser.parse();
    }

    public static void BuildStdDeck() {
        StdCardParser stdParser = new StdCardParser();
        try {
            stdParser.readFile(cardJsonPath);
        } catch (Exception e) {
            System.out.println("read-file Exception");
        }
        FullStdDeck = stdParser.parse();
    }
}
