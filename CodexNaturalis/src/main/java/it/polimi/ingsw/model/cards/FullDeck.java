package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.ObjectiveParser;
import it.polimi.ingsw.helpers.parsers.StartingParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;

import it.polimi.ingsw.model.objectives.Objective;


import java.nio.file.Path;

public class FullDeck {

    private static Deck<GoldCard> FullGoldDeck;
    private static Deck<StdCard> FullStdDeck;
    private static Deck<Objective> FullObjDeck;
    private static Deck<StartingCard> FullStartingDeck;
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

    public static Deck<Objective> getFullObjDeck() {
        return FullObjDeck;
    }

    public static Deck<StartingCard> getFullStartingDeck() {
        return FullStartingDeck;
    }


    public static void BuildGoldDeck() {
        GoldCardParser goldParser = new GoldCardParser();
        try {
            goldParser.readFile(cardJsonPath);
            FullGoldDeck = goldParser.parse();
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    public static void BuildStdDeck() {
        StdCardParser stdParser = new StdCardParser();
        try {
            stdParser.readFile(cardJsonPath);
            FullStdDeck = stdParser.parse();
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    public static void BuildObjDeck() {
        ObjectiveParser objParser = new ObjectiveParser();
        try {
            objParser.readFile(cardJsonPath);
            FullObjDeck = objParser.parse();
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    public static void BuildStartingDeck() {
        StartingParser startingParser = new StartingParser();
        try {
            startingParser.readFile(cardJsonPath);
            FullStartingDeck = startingParser.parse();
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }
}
