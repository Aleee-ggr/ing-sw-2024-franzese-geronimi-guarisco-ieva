package it.polimi.ingsw;

import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;

import java.nio.file.Path;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Path cardJsonPath = Path.of(GameConsts.cardJsonPath);
        
        StdCardParser stdParser = new StdCardParser();
        GoldCardParser goldParser = new GoldCardParser();

        try {
            stdParser.readFile(cardJsonPath);
            goldParser.readFile(cardJsonPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Deck<StdCard> stdDeck = stdParser.parse();
        Deck<GoldCard> goldDeck = goldParser.parse();

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
    }
}
