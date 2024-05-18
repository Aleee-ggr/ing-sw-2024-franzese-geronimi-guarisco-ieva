package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        WaitState state;
        synchronized (client) {
            running = client.getGameState() != GameState.STOP;
        }
        while (running) {
            try {
                String command = in.readLine();
                handleCommand(command);
            } catch (IOException e) {throw new RuntimeException(e);}
            synchronized (client) {
                running = client.getGameState() != GameState.STOP;
            }
        }
    }

    private boolean place(int card, int position) {
        int id = client.getPlayerData().getClientHand().get(card).getId();
        Coordinates coordinates = client.getPlayerData().getValidPlacements().get(position);
        try {
            return client.placeCard(coordinates, id);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
     *  <li> w / a / s / d: move the view of the board</li>
     *  <li> h: show this list </li>
     *  </ul>
     * @param command the command to execute
     */
    private void handleCommand(String command) {
        String[] cmd = command.split(" ");
        int position, id;
        try {
            switch (cmd[0]) {
                case "place":
                    id = Integer.parseInt(cmd[1]) - 1;
                    position = Integer.parseInt(cmd[2]);
                    if (!placed) {
                        placed = place(id, position);
                    }
                    break;
                case "view":
                    compositor.switchView(View.getView(cmd[1]));
                    break;
                case "switch":
                    break;
                case "draw":
                    position = Integer.parseInt(cmd[1]) -1;
                    if (placed) {
                        client.drawCard(position);
                        placed = false;
                    }
                    break;
                case "w":
                    compositor.getBoard().moveCenter(0, 2);
                    break;
                case "a":
                    compositor.getBoard().moveCenter(-2, 0);
                    break;
                case "s":
                    compositor.getBoard().moveCenter(0, -2);
                    break;
                case "d":
                    compositor.getBoard().moveCenter(2, 0);
                    break;
                case "h", "help":
                    System.out.println("""
                            List of available commands:
                              - place [card] [position]: place the card in the given position
                              - view [deck|objectives|board]: show the selected element
                              - switch [player]: show the view from the given player side
                              - draw [int index]: draw the card at the given position, 0 to 3 are visible cards, 4 & 5 are respectively gold and std deck
                              - w / a / s / d: move the view of the board
                              - h: show this list
                              Press ENTER to continue""");
                    in.readLine();
                    break;
            }
        } catch (IOException | NumberFormatException e) {throw new RuntimeException(e);}
    }
}
