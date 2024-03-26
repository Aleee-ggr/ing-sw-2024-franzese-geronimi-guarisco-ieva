package it.polimi.ingsw;

import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.ObjectiveParser;
import it.polimi.ingsw.helpers.parsers.StartingParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.objectives.Objective;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args) {
        System.out.println( "Hello World!" );
        printCardInfo();
    }

    private static void printCardInfo() {
        Path cardJsonPath = Path.of(GameConsts.cardJsonPath);

        StdCardParser stdParser = new StdCardParser();
        GoldCardParser goldParser = new GoldCardParser();
        StartingParser startingParser = new StartingParser();
        ObjectiveParser objectiveParser = new ObjectiveParser();

        try {
            stdParser.readFile(cardJsonPath);
            goldParser.readFile(cardJsonPath);
            startingParser.readFile(cardJsonPath);
            objectiveParser.readFile(cardJsonPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Deck<StdCard> stdDeck = stdParser.parse();
        Deck<GoldCard> goldDeck = goldParser.parse();
        Deck<StartingCard> startingDeck = startingParser.parse();
        ArrayList<Objective> objectives = objectiveParser.parse();

        StdCard stdCard = stdDeck.draw();
        while (stdCard != null) {
            System.out.println(stdCard);
            stdCard = stdDeck.draw();
        }

        GoldCard goldCard = goldDeck.draw();
        while (goldCard != null) {
            System.out.println(goldCard);
            goldCard = goldDeck.draw();
        }

        StartingCard startingCard = startingDeck.draw();
        while (startingCard != null) {
            System.out.println(startingCard);
            startingCard = startingDeck.draw();
        }

        for (Objective objective : objectives) {
            System.out.println(objective.getPoints(null));
        }
    }
}
