package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.helpers.parser.StdCardParser;

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
            parser.readFile(Path.of("src/main/java/it/polimi/ingsw/data/cards/cards.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        parser.parse();
    }
}
