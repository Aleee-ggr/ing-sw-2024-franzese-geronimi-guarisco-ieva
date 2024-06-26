package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;
import it.polimi.ingsw.view.TUI.components.ColorView;
import it.polimi.ingsw.view.TUI.components.EndGameTUI;
import it.polimi.ingsw.view.TUI.components.StartingCardView;
import it.polimi.ingsw.view.TUI.components.StartingObjectiveView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * The TuiController class handles the flow of the game using the TUI.
 * It interacts with the client and manages the different stages of the game
 * including login, game selection, setup, and the main game loop.
 */
public class TuiController {
    private final ClientInterface client;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter out = new PrintWriter(System.out, true);
    Compositor compositor = null;

    /**
     * Constructs a TuiController with the specified client interface.
     *
     * @param client The client interface to interact with.
     */
    public TuiController(ClientInterface client) {
        this.client = client;
    }

    /**
     * Starts the TUI controller, managing the flow from login to the main game
     * loop.
     */
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
            case MAIN, ENDGAME:
                mainGame();
            case STOP:
                stop();
        }
    }

    /**
     * Handles the user login process.
     */
    private void login() {
        try {
            boolean valid = false;
            do {
                String username;
                do {
                    out.println("Insert username: ");
                    username = in.readLine();
                } while (!username.matches("^[\\w\\d]{1,16}$"));
                String password;
                do {
                    out.println("Insert password: ");
                    password = in.readLine();
                } while (!password.matches("^" + "\\S{1,16}$"));

                valid = client.checkCredentials(username, password);
            } while (!valid);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles game selection, allowing the user to join an existing game or create
     * a new one.
     */
    private void selectGame() {
        try {
            final AtomicBoolean selected = new AtomicBoolean(false);
            final AtomicInteger gameCount = new AtomicInteger(0);
            new Thread(() -> {
                while (!selected.get()) {
                    try {
                        client.fetchAvailableGames();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    }
                    List<UUID> games = client.getAvailableGames().keySet().stream().toList();
                    clear();
                    for (int i = 0; i < games.size(); i++) {
                        out.printf("%d.\t%s\n", i + 1, client.getAvailableGames().get(games.get(i)));
                    }
                    out.println("Available games: ");
                    out.println("Select game to play (0 to create a new game)");
                    try {
                        sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    gameCount.set(games.size());
                }
            }).start();
            int selectedGame;
            do {

                selectedGame = atomicSelect(0, gameCount);
            } while (selectedGame < 0);

            selected.set(true);

            if (selectedGame == 0) {
                createGame();
            } else {
                List<UUID> games = client.getAvailableGames().keySet().stream().toList();
                UUID choice = games.get(selectedGame - 1);
                if (!client.joinGame(choice)) {
                    out.println("Invalid Join! \n" + "Are you sure you don't already have a game open?\n" + "Are you sure you are not trying to join a different game than the one you are already in?");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Waits in the lobby for other players to join.
     */
    private void lobby() {
        clear();
        out.println("waiting for players...");
        waitUpdate();
    }

    /**
     * Handles the setup phase of the game, allowing the user to select starting
     * cards, colors, and objectives.
     */
    private void setup() {
        fetchSetup();
        boolean done = false;
        PlayerData playerData = client.getPlayerData();
        int sel = 1;

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
            sel = select(1, playerData.getAvailableColors().size());
            done = sel >= 0;
        }

        try {
            client.choosePlayerColor(playerData.getAvailableColors().get(sel - 1));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        done = false;

        while (!done) {
            clear();
            out.println(new StartingObjectiveView(playerData));
            out.println("Select starting objective: ");
            sel = select(1, 2);
            done = sel >= 0;
        }

        try {
            client.choosePersonalObjective(playerData.getStartingObjectives().get(sel - 1).getId());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Handles the main game loop.
     */
    private void mainGame() {
        clear();
        fetchData();
        compositor = new Compositor(client);
        AtomicBoolean running = new AtomicBoolean(true);
        SharedUpdate updater = new SharedUpdate();
        Thread commands = new CommandThread(client, updater, compositor, running);
        commands.start();
        Thread update = new ClientUpdateThread(client, updater, compositor, running);
        update.start();
        Thread render = new RenderThread(updater, compositor, running);
        render.start();
        try {
            update.join();
            client.fetchGameState();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Handles the end of the game, displaying the final scores.
     */
    private void stop() {
        try {
            client.fetchScoreMap();
            clear();
            System.out.println(new EndGameTUI(client.getScoreMap()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

    /**
     * Prompts the user to make a selection within a specified range.
     *
     * @param min The minimum valid selection.
     * @param max The maximum valid selection.
     * @return The selected value, or -1 if the selection is invalid.
     */
    private int select(int min, int max) {
        int selection;
        try {
            selection = Integer.parseInt(in.readLine());
            if (min <= selection && selection <= max) {
                return selection;
            }
        } catch (NumberFormatException ignored) {
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return -1;
    }

    private int atomicSelect(int min, final AtomicInteger max) {
        int selection;
        try {
            selection = Integer.parseInt(in.readLine());
            if (min <= selection && selection <= max.get()) {
                return selection;
            }
        } catch (NumberFormatException ignored) {
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return -1;
    }

    /**
     * Creates a new game with the specified number of players and game name.
     */
    private void createGame() {
        try {
            int selection = 0;
            final Set<String> existingGames = new HashSet<>(client.getAvailableGames().values());
            do {
                out.println("Insert number of players: ");
                try {
                    selection = Integer.parseInt(in.readLine());
                } catch (NumberFormatException ignored) {
                }
            } while (selection < 2 || selection > 4);

            String gameName;
            do {
                out.println("Insert game name: ");
                gameName = in.readLine();
            } while (gameName.isEmpty() || existingGames.contains(gameName));

            client.newGame(selection, gameName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Fetches the initial setup data required for the game.
     */
    private void fetchSetup() {
        try {
            client.fetchPlayers();
            client.fetchStartingObjectives();
            client.fetchStartingCard();
            client.fetchAvailableColors();
            client.fetchClientHand();
            client.fetchCommonObjectives();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Fetches the data required for the main game loop.
     */
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
            client.fetchOpponentsHandType();
            client.fetchPlayersColors();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Waits for an update from the client.
     */
    private void waitUpdate() {
        try {
            client.waitUpdate();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Clears the console screen using ansi escape characters.
     */
    private void clear() {
        out.print("\033[H\033[2J");
        out.flush();
    }
}
