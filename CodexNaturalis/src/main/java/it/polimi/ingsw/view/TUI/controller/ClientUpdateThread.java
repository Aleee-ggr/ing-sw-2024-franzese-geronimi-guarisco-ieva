package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;

import java.io.IOException;

import static it.polimi.ingsw.controller.WaitState.*;

/**
 * This class represents a thread that listens for updates from the game server
 * and updates the TUI accordingly.
 */
public class ClientUpdateThread extends Thread {
    private final ClientInterface client;
    private final SharedUpdate updater;
    private final Compositor compositor;

    /**
     * Constructs a new ClientUpdateThread.
     *
     * @param client     The client interface to communicate with the game server.
     * @param updater    The shared updater instance to synchronize updates with the TUI.
     * @param compositor The compositor instance to update the TUI components.
     */
    public ClientUpdateThread(ClientInterface client, SharedUpdate updater, Compositor compositor) {
        this.client = client;
        this.updater = updater;
        this.compositor = compositor;
    }

    /**
     * Wait for updates from the server, using the function waitUpdate and waiting
     * for a return from the call.
     * This function will set the state to either TURN or WAIT, and then wait for
     * the server to change it to UPDATE
     * or TURN_UPDATE, if an update is received, fetch necessary data from the
     * server and update the view using the
     * updater
     *
     * @see SharedUpdate
     * @see WaitState
     * @see ClientUpdateThread#fetchData()
     * @see GameThread#sendUpdate()
     */
    @Override
    public void run() {
        WaitState state = null;
        WaitState oldState;
        while (state != ENDGAME) {
            try {
                oldState = state;
                state = client.waitUpdate();
                if (state == UPDATE || state == TURN_UPDATE) {
                    fetchData();
                    updater.update();
                }
                if (state == TURN || state == TURN_UPDATE) {
                    fetchData();
                    compositor.setTopBar("Your Turn!");
                    updater.update();

                    if (oldState == UPDATE || oldState == WAIT) {
                        compositor.switchView(View.BOARD);
                    }
                }

                sleep(500);

            } catch (IOException | InterruptedException e) {
                System.exit(1);
            }
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
            client.fetchChat();
            client.fetchPlayersColors();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
