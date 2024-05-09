package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.network.rmi.RmiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;

public class TuiController {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter out = new PrintWriter(System.out, true);
    private final RmiClient client;

    public TuiController(RmiClient client) {
        this.client = client;
    }

    public void start() {
        selectGame();
        lobby();
        setup();
        mainGame();
    }


    private void selectGame() {
        try {
            client.fetchAvailableGames();

            out.println("Available games: ");
            //TODO print available games (indexed starting at 1)
            out.println("Select game to play (0 to create a new game)");
            int selected;
            do {
                selected = select(0, 100); //TODO add actual upper range
                if (selected == 0) {
                    createGame();
                } else {
                    //TODO join game
                }
            } while (selected < 0);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private void lobby() {
        try {
            clear();
            out.println("waiting for players...");
            client.waitUpdate();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setup() {
        fetchSetup();
        boolean done = false;
        while (!done) {
            clear();
            /* TODO print objective view
            System.out.println(new StartingObjectiveView(

            ));
             */
            out.println("Select starting objective: ");
            done = select(1, 2) >= 0;
        }
        //TODO send selection
        done = false;

        while (!done) {
            // TODO Print StartingCardView
            out.println("Select starting card face: ");
            done = select(1, 2) >= 0;
        }
    }

    private void mainGame() {
        //TODO main part of the game
    }

    private int select(int min, int max) {
        int selection;
        try {
            selection = Integer.parseInt(in.readLine());
            if (!(min <= selection && selection <= max)) {
                return selection;
            }
        } catch (NumberFormatException ignored) {
        } catch (IOException e) {throw new RuntimeException(e);}
        return -1;
    }

    private void createGame() {
        try {
            int selection = 0;

            do {
                System.out.println("Insert number of players: ");
                try {
                    selection = Integer.parseInt(in.readLine());
                } catch (NumberFormatException ignored) {
                }
            } while (selection < 2 || selection > 4);

            client.newGame(selection);
        } catch(IOException e) {throw new RuntimeException(e);}
    }

    private void clear() {
        out.print("\033[H\033[2J");
        out.flush();
    }

    private void fetchSetup() {
        //TODO fetch messages
    }
}
