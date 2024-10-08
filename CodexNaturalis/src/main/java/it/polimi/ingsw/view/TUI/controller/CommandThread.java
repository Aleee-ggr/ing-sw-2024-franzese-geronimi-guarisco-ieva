package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.Fetch;
import it.polimi.ingsw.view.TUI.Compositor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.abs;

/**
 * The CommandThread class handles user input commands and interacts with the server.
 * Some commands are visual, while others affect the server and require updates.
 */
public class CommandThread extends Thread {
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private final ClientInterface client;
    private final SharedUpdate updater;
    private final Compositor compositor;
    private final AtomicBoolean running;

    boolean placed = false;

    /**
     * Constructs a CommandThread with the specified client, updater, and compositor.
     *
     * @param client     The client interface to interact with the server.
     * @param updater    The SharedUpdate instance to check for updates.
     * @param compositor The Compositor instance to render the TUI.
     */
    public CommandThread(ClientInterface client, SharedUpdate updater, Compositor compositor, AtomicBoolean running) {
        this.client = client;
        this.updater = updater;
        this.compositor = compositor;
        this.running = running;
    }

    /**
     * Parses the offset from a string argument.
     *
     * @param arg The string argument.
     * @return The parsed offset, or 0 if parsing fails.
     */
    private static int getOffset(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    /**
     * Starts a thread reading user commands, some are only visual, while "chat",
     * "whisper", "place", and "draw" also
     * have effects on the server, and require a server-side update, which will be
     * detected by
     * {@link ClientUpdateThread}.
     *
     * @see #handleCommand(String) handleCommand("h") for a list of available
     * commands.
     */
    @Override
    public void run() {
        while (running.get()) {
            try {
                String command = in.readLine();
                if (running.get()) {
                    handleCommand(command);
                    fetchData();
                    updater.update();
                }
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }

    /**
     * List of available commands:
     * <ul>
     * <li>place [card] [position]: place the card in the given position</li>
     * <li>view [deck|objectives|board]: show the selected element</li>
     * <li>switch [player]: show the view from the given player side</li>
     * <li>draw [int index]: draw the card at the given position
     * 0 to 3 are visible cards, 4 & 5 are respectively gold and std deck</li>
     * <li>center: center the board</li>
     * <li>chat [message]: send a global message</li>
     * <li>whisper [player]: send a private message to a player</li>
     * <li>w / a / s / d [int distance]: move the view of the board for the given
     * distance</li>
     * <li>h: show this list</li>
     * </ul>
     * The command executed is the first word of the user input.<br/>
     * if the command given is not within the list, call {@link #defaultCommand()}
     * to warn the user.
     *
     * @param command the command to execute
     * @see #defaultCommand()
     * @see #postChat(String[], String)
     */
    private void handleCommand(String command) {
        if (command == null || command.strip().equals("")) {
            return;
        }
        String[] cmd = command.split(" ");
        int position, id;
        int offset;
        try {
            switch (cmd[0].toLowerCase()) {
                case "place":
                    if (cmd.length == 3) {
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
                                Fetch.fetchPlace(client);
                                updater.update();
                                sleep(1000);
                                compositor.setTopBar("Your Turn: Draw a Card!");
                                compositor.switchView(View.DECK);
                            } else {
                                printError("Could not place card!");
                            }
                        }
                    } else {
                        defaultCommand();
                    }
                    break;

                case "view":
                    try {
                        compositor.switchView(View.getView(cmd[1]));
                    } catch (IllegalStateException e) {
                        defaultCommand();
                    }
                    break;

                case "chat":
                    if (cmd.length > 1) {
                        postChat(Arrays.copyOfRange(cmd, 1, cmd.length), null);
                    } else {
                        defaultCommand();
                    }
                    break;

                case "whisper":
                    if (cmd.length > 2) {
                        if (client.getPlayers().contains(cmd[1])) {
                            postChat(Arrays.copyOfRange(cmd, 2, cmd.length), cmd[1]);
                        } else {
                            printError("Unknown Player %s".formatted(cmd[1]));
                        }
                    } else {
                        defaultCommand();
                    }
                    break;

                case "center":
                    compositor.getBoard().setCenter(new Coordinates(0, 0));
                    break;

                case "switch":
                    if (cmd.length == 2 && client.getPlayers().contains(cmd[1])) {
                        Fetch.fetchSwitch(client);
                        compositor.setViewPlayer(cmd[1]);
                    } else {
                        defaultCommand();
                    }
                    break;

                case "draw":
                    if (cmd.length == 2) {
                        try {
                            position = Integer.parseInt(cmd[1]) - 1;
                        } catch (NumberFormatException ignored) {
                            printError("Invalid position");
                            break;
                        }
                        if (placed) {
                            if (!client.getDecksBacks().isEmpty()) {
                                if (position < 0 || position > 5) {
                                    defaultCommand();
                                    break;
                                } else {
                                    client.drawCard(position);
                                    Fetch.fetchDraw(client);
                                }
                            } else {
                                int availableCount = 0;
                                int drawPosition = -1;
                                for (int i = 0; i < client.getVisibleCards().size(); i++) {
                                    if (client.getVisibleCards().get(i) != null) {
                                        availableCount++;
                                        if (availableCount == position + 1) {
                                            drawPosition = i;
                                            break;
                                        }
                                    }
                                }
                                if (position < 0 || position >= availableCount) {
                                    defaultCommand();
                                    break;
                                } else {
                                    client.drawCard(drawPosition);
                                }
                            }
                            placed = false;
                            compositor.setTopBar("Waiting for your Turn...");
                        } else {
                            printError("You must place a card first!");
                        }
                    } else {
                        defaultCommand();
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
                    updater.lock();
                    System.out.println("""
                            List of available commands:
                              - place [card] [position]: place the card in the given position
                              - view [deck|objectives|board]: show the selected element
                              - switch [player]: show the view from the given player side
                              - draw [int index]: draw the card at the given position, 1 to 4 are visible cards, 5 & 6 are respectively gold and std deck
                              - w / a / s / d [int distance]: move the view of the board for the given distance
                              - center: center the board
                              - chat [message]: send a global message
                              - whisper [player]: send a private message to a player
                              - h: show this list
                              Press ENTER to continue""");
                    in.readLine();
                    updater.unlock();
                    break;

                default:
                    defaultCommand();
            }
        } catch (IOException | NumberFormatException |

                 InterruptedException e) {
            System.exit(1);
        }
    }

    /**
     * Send a request to place a card to the server, and return whether the card was
     * placed successfully
     *
     * @param card     the index of the card in the
     *                 {@link it.polimi.ingsw.view.TUI.components.HandView hand} to
     *                 play
     *                 (base 1)
     * @param position the index of the card placement, as shown in the
     *                 {@link it.polimi.ingsw.view.TUI.components.Board} rendering
     * @return true if the card was placed successfully, else false
     * @see ClientInterface#placeCard(Coordinates, int)
     */
    private boolean place(int card, int position) {
        int id = 1;
        if (card < 0) {
            id = -1;
            card = abs(card) - 2;
        }

        try {
            id *= client.getPlayerData().getClientHand().get(card).getId();
            Coordinates coordinates = client.getPlayerData().getValidPlacements().get(position);
            return client.placeCard(coordinates, id);
        } catch (IndexOutOfBoundsException | IOException e) {
            return false;
        }
    }

    /**
     * Prints a warning if the given command was not found.
     *
     * @see #handleCommand(String)
     */
    private void defaultCommand() {
        System.out.println("Unknown command\nPress ENTER to continue");
        try {
            in.readLine();
        } catch (IOException e) {
            System.exit(1);
        }
    }

    /**
     * Prints an error message to the console and waits for the user to press Enter.
     *
     * @param error the error message to print
     */
    private void printError(String error) {
        System.out.printf("Invalid input| %s%n", error);
        try {
            in.readLine();
        } catch (IOException e) {
            System.exit(1);
        }
    }

    /**
     * Sends a message to the server chat, to the given receiver, if any.
     *
     * @param message  the message to send
     * @param receiver either the username of a player in case of private message,
     *                 or null for a global message
     * @see it.polimi.ingsw.model.ChatMessage#ChatMessage(String, String, String)
     * ChatMessage
     * @see it.polimi.ingsw.model.ChatMessage#filterPlayer(String)
     * @see #handleCommand(String)
     */
    private void postChat(String[] message, String receiver) {
        String msg = String.join(" ", message);
        try {
            client.postChat(msg, receiver);
        } catch (IOException e) {
            System.exit(1);
        }
    }

    /**
     * Fetch all the required data from the server in order to render the tui
     * interface,
     *
     * @see ClientInterface
     * @see Compositor
     */
    private void fetchData() {
        try {
            Fetch.fetchData(client);
            client.fetchChat();
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
