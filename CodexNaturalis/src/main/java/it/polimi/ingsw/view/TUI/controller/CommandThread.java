package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static java.lang.Math.abs;

public class CommandThread extends Thread {
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private final ClientInterface client;
    private final SharedUpdate updater;
    private final Compositor compositor;

    boolean placed = false;

    public CommandThread(ClientInterface client, SharedUpdate updater, Compositor compositor) {
        this.client = client;
        this.updater = updater;
        this.compositor = compositor;
    }

    @Override
    public void run() {
        boolean running;
        synchronized (client) {
            running = client.getGameState() != GameState.STOP;
        }
        while (running) {
            try {
                String command = in.readLine();
                handleCommand(command);
                fetchData();
                updater.update();
            } catch (IOException e) {throw new RuntimeException(e);}
            synchronized (client) {
                running = client.getGameState() != GameState.STOP;
            }
        }
    }

    private boolean place(int card, int position) {
        int id = 1;
        if (card < 0) {
            id = -1;
            card = abs(card) - 2;
        }
        id *= client.getPlayerData().getClientHand().get(card).getId();
        Coordinates coordinates = client.getPlayerData().getValidPlacements().get(position);
        try {
            return client.placeCard(coordinates, id);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * List of available commands:
     * <ul>
     *  <li> place [card] [position]: place the card in the given position</li>
     *  <li> view [deck|objectives|board]: show the selected element </li>
     *  <li> switch [player]: show the view from the given player side </li>
     *  <li> draw [int index]: draw the card at the given position
     *  0 to 3 are visible cards, 4 & 5 are respectively gold and std deck </li>
     *  <li> w / a / s / d [int distance]: move the view of the board for the given distance</li>
     *  <li> h: show this list </li>
     *  </ul>
     * @param command the command to execute
     */
    private void handleCommand(String command) {
        String[] cmd = command.split(" ");
        int position, id;
        int offset;
        try {
            switch (cmd[0].toLowerCase()) {
                case "place":
                    try {
                        id = Integer.parseInt(cmd[1]) - 1;
                        position = Integer.parseInt(cmd[2]);
                    } catch (Exception e) {
                        defaultCommand();
                        break;
                    }
                    if (!placed) {
                        placed = place(id, position);
                        if (placed) {
                            fetchData();
                            updater.update();
                            sleep(1000);
                            compositor.setTopBar("Your Turn: Draw a Card!");
                            compositor.switchView(View.DECK);
                        }
                    }
                    break;
                case "view":
                    try {
                        compositor.switchView(View.getView(cmd[1]));
                    } catch(IllegalStateException e){
                        defaultCommand();
                    }
                        break;
                case "chat":
                    postChat(Arrays.copyOfRange(cmd, 1, cmd.length));
                case "switch":
                    compositor.setViewPlayer(cmd[1]);
                    updater.update();
                    break;
                case "draw":
                    position = Integer.parseInt(cmd[1]) -1;
                    if (placed) {
                        client.drawCard(position);
                        placed = false;
                        compositor.setTopBar("Waiting for your Turn...");
                    }
                    break;
                case "w":
                    offset = 1;
                    if (cmd.length == 2) {
                        offset = getOffset(cmd[1]);
                    }
                    compositor.getBoard().moveCenter(0, 2 * offset);
                    break;
                case "a":
                    offset = 1;
                    if (cmd.length == 2) {
                        offset = getOffset(cmd[1]);
                    }
                    compositor.getBoard().moveCenter(4 * offset, 0);
                    break;
                case "s":
                    offset = 1;
                    if (cmd.length == 2) {
                        offset = getOffset(cmd[1]);
                    }
                    compositor.getBoard().moveCenter(0, -2 * offset);
                    break;
                case "d":
                    offset = 1;
                    if (cmd.length == 2) {
                        offset = getOffset(cmd[1]);
                    }
                    compositor.getBoard().moveCenter(-2 * offset, 0);
                    break;
                case "h", "help":
                    System.out.println("""
                            List of available commands:
                              - place [card] [position]: place the card in the given position
                              - view [deck|objectives|board]: show the selected element
                              - switch [player]: show the view from the given player side
                              - draw [int index]: draw the card at the given position, 0 to 3 are visible cards, 4 & 5 are respectively gold and std deck
                              - w / a / s / d [int distance]: move the view of the board for the given distance
                              - h: show this list
                              Press ENTER to continue""");
                    in.readLine();
                    break;
                default:
                    defaultCommand();
            }
        } catch (IOException | NumberFormatException | InterruptedException e) {throw new RuntimeException(e);}
    }

    private void defaultCommand() {
        System.out.println("Unknown command\nPress ENTER to continue");
        try {
            in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getOffset(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {return 0;}
    }

    private void postChat(String[] message) {
        String msg = String.join(" ", message);

        msg = client.getUsername() + ": " + msg;
        compositor.addToChat(msg);
        try {
            client.postChat(msg);
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
        } catch (IOException e) {throw new RuntimeException(e);}
    }
}
