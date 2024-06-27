package it.polimi.ingsw;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.rmi.RmiServer;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.network.socket.SocketServer;
import it.polimi.ingsw.view.GUI.Gui;
import it.polimi.ingsw.view.TUI.controller.TuiController;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 */

public class App {
    public static void main(String[] args) throws RemoteException, ServerConnectionException {
        List<String> arguments = List.of(args);
        if (arguments.contains("server")) {
            new RmiServer(9000);
            new SocketServer(8000);
        }
        if (arguments.contains("client")) {
            System.out.println("Do you want to launch the GUI [default: N]? [Y/N]:");
            Scanner scanner = new Scanner(System.in);
            String useGui = scanner.nextLine();
            if (useGui.equalsIgnoreCase("Y")) {
                Gui.start();
                scanner.close();
                return;
            }
            System.out.print("insert connection mode [Default: RMI]\n1) socket\n2) RMI\n");
            String input = scanner.next();
            int mode;
            try {
                mode = Integer.parseInt(input);
            } catch (NumberFormatException ignored) {
                mode = 2;
            }
            if (mode != 1 && mode != 2) {
                mode = 2;
            }

            System.out.print("Insert server address [Default: 127.0.0.1]: ");
            input = scanner.next();
            String serverAddress = "127.0.0.1";
            if (input.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
                serverAddress = input;
            }

            switch (mode) {
                case 1:
                    System.setProperty("java.socket.server.hostname", serverAddress);
                    SocketClient clientSocket = new SocketClient(serverAddress, 8000);
                    new TuiController(clientSocket).start();
                    break;
                case 2:
                    System.setProperty("java.rmi.server.hostname", serverAddress);
                    RmiClient clientRmi = new RmiClient(serverAddress, 9000);
                    new TuiController(clientRmi).start();
                    break;
                default:
                    System.out.println("Unknown option!");
            }
            scanner.close();
        }
    }
}
