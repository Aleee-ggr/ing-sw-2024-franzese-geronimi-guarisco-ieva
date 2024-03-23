package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.helpers.parser.StdCardParser;
import it.polimi.ingsw.model.cards.Deck;
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
        StdCardParser parser = new StdCardParser();
        try {
            parser.readFile(Path.of("src/main/resources/cards.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Deck<StdCard> stdDeck = parser.parse();
        StdCard card = stdDeck.draw();
        while (card != null) {
            System.out.println(card);
            card = stdDeck.draw();
        }
    }
}
