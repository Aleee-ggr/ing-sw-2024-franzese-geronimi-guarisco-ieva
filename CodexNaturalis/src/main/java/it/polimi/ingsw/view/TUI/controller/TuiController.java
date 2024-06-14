package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;
import it.polimi.ingsw.view.TUI.components.ColorView;
import it.polimi.ingsw.view.TUI.components.StartingCardView;
import it.polimi.ingsw.view.TUI.components.StartingObjectiveView;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TuiController {
    private final ClientInterface client;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter out = new PrintWriter(System.out, true);
    Compositor compositor = null;

    public TuiController(ClientInterface client) {
        this.client = client;
        client.setCredentials(String.valueOf(ThreadLocalRandom.current().nextInt(100, 1000)), "password");
    }

    public void start() {

        login();
        selectGame();
        lobby();

        try {
            client.fetchGameState();
            client.fetchPlayers();
            client.fetchPersonalObjective();
            client.fetchPlayersColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (client.getGameState()) {
            case SETUP:
                setup();
            case MAIN:
                mainGame();
            case ENDGAME:
                endGame();
        }
    }

    private void login() {
        try {
            boolean valid = false;
            do {
                out.println("Insert username: ");
                String username = in.readLine();
                out.println("Insert password: ");
                String password = in.readLine();

                valid = client.checkCredentials(username, password);
            } while (!valid);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectGame() { //TODO: change game selection for duplicate users
        try {
            client.fetchAvailableGames();
            int selected;
            do {
                clear();
                out.println("Available games: ");

                for (int i = 0; i < client.getAvailableGames().size(); i++) {
                    out.printf("%d.\t%s\n", i + 1, client.getAvailableGames().get(i));
                }
                out.println("Select game to play (0 to create a new game)");

                selected = select(0, client.getAvailableGames().size());
            } while (selected < 0);
            if (selected == 0) {
                createGame();
            } else {
                UUID choice = client.getAvailableGames().get(selected - 1);
                client.joinGame(choice);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void lobby() {
        clear();
        out.println("waiting for players...");
        waitUpdate();
    }

    private void setup() {
        fetchSetup();
        boolean done = false;
        PlayerData playerData = client.getPlayerData();
        int sel = 1;
        while (!done) {
            clear();
            out.println(new StartingObjectiveView(playerData.getStartingObjectives().stream().map(ObjectiveCard::new).toArray(ObjectiveCard[]::new)));
            out.println("Select starting objective: ");
            sel = select(1, 2);
            done = sel >= 0;
        }

        try {
            client.choosePersonalObjective(playerData.getStartingObjectives().get(sel - 1).getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        done = false;


        while (!done) {
            clear();
            out.println(new StartingCardView(client.getPlayerData().getStartingCard()));
            out.println("Select starting card face: ");
            sel = select(1, 2);
            done = sel >= 0;
        }

        try {
            client.placeStartingCard(sel == 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        done = false;

        while (!done) {
            clear();
            out.println(new ColorView(playerData.getAvailableColors()));
            out.println("Select color: ");
            sel = select(1, 4);
            done = sel >= 0;
        }

        try {
            client.choosePlayerColor(playerData.getAvailableColors().get(sel - 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void mainGame() {
        clear();
        fetchData();
        compositor = new Compositor(client);
        SharedUpdate updater = new SharedUpdate();
        new CommandThread(client, updater, compositor).start();
        new ClientUpdateThread(client, updater, compositor).start();
        Thread t = new RenderThread(client, updater, compositor);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void endGame() {
        try {
            client.fetchScoreMap();
            List<Map.Entry<String, Integer>> entryList = client.getScoreMap().entrySet().stream().sorted((e1, e2) -> e2.getValue() - e1.getValue()).toList();

            for (int i = 0; i < entryList.size(); i++) {
                out.print(i + 1);
                out.print(": ");
                out.print(entryList.get(i).getKey());
                out.print(": ");
                out.println(entryList.get(i).getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private int select(int min, int max) {
        int selection;
        try {
            selection = Integer.parseInt(in.readLine());
            if (min <= selection && selection <= max) {
                return selection;
            }
        } catch (NumberFormatException ignored) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    private void createGame() {
        try {
            int selection = 0;
            do {
                out.println("Insert number of players: ");
                try {
                    selection = Integer.parseInt(in.readLine());
                } catch (NumberFormatException ignored) {
                }
            } while (selection < 2 || selection > 4);

            client.newGame(selection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchSetup() {
        try {
            client.fetchPlayers();
            client.fetchStartingObjectives();
            client.fetchStartingCard();
            client.fetchAvailableColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchData() {
        try {
            client.fetchClientHand();
            client.fetchCommonObjectives();
            client.fetchValidPlacements();
            client.fetchPlayersBoards();
            client.fetchPlayersPlacingOrder();
            client.fetchPlayersResources();
            client.fetchScoreMap();
            client.fetchGameState();
            client.fetchVisibleCardsAndDecks();
            client.fetchOpponentsHandColor();
            client.fetchPlayersColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitUpdate() {
        try {
            client.waitUpdate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void clear() {
        out.print("\033[H\033[2J");
        out.flush();
    }
}
