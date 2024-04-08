package it.polimi.ingsw;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args) {
        List<String> arguments = List.of(args);
        if (arguments.contains("server")) {
            System.out.println("server");
        }
        if (arguments.contains("client")) {
            System.out.println("client");
        }
    }
}
