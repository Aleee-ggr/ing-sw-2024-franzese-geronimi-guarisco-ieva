package it.polimi.ingsw;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.rmi.RmiServer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args) throws RemoteException, ServerConnectionException {
        List<String> arguments = List.of(args);
        if (arguments.contains("server")) {
            new RmiServer(9090);
        }
        if (arguments.contains("client")) {
            System.out.print("insert connection mode\n1) socket\n2) RMI\n");
            String input = new Scanner(System.in).next();
            int mode = Integer.parseInt(input);
            switch (mode) {
                case 1:
                    break;
                case 2:
                    System.out.print("Insert game uuid: ");
                    input = new Scanner(System.in).next();
                    UUID id = null;
                    if (input.length() > 3) {
                        id = UUID.fromString(input);
                    }
                    System.out.print("Insert server address: ");
                    input = new Scanner(System.in).next();
                    String serverAddress = "localhost";
                    if (input.matches("([0-9]+\\.)+[0-9]+")) {
                        serverAddress = input;
                    }
                    RmiClient client = new RmiClient(
                            String.valueOf(ThreadLocalRandom.current().nextInt(100, 10000)),
                            "password",
                            serverAddress,
                            9090);
                    break;
                default:
                    System.out.println("Unknown option!");
            }

        }
    }
}
