package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;

import java.io.IOException;

public class ClientUpdateThread extends Thread {
    private final ClientInterface client;
    private final SharedUpdate updater;
    private final Compositor compositor;

    public ClientUpdateThread(ClientInterface client, SharedUpdate updater, Compositor compositor) {
        this.client = client;
        this.updater = updater;
        this.compositor = compositor;
    }

    /**
     * Wait for updates from the server, using the function waitUpdate and waiting for a return from the call.
     * This function will set the state to either TURN or WAIT, and then wait for the server to change it to UPDATE
     * or TURN_UPDATE, if an update is received, fetch necessary data from the server and update the view using the
     * updater
     *
     * @see SharedUpdate
     * @see WaitState
     * @see ClientUpdateThread#fetchData()
     * @see GameThread#sendUpdate()
     */
    @Override
    public void run() {
        boolean running;
        WaitState state = null, oldState;
        synchronized (client) {
            running = client.getGameState() != GameState.STOP;
        }
        while (running) {
            try {
                oldState = state;
                state = client.waitUpdate();
                if (state == WaitState.UPDATE || state == WaitState.TURN_UPDATE) {
                    fetchData();
                    updater.update();
                }
                if (oldState != WaitState.TURN && state == WaitState.TURN) {
                    fetchData();
                    compositor.switchView(View.BOARD);
                    compositor.setTopBar("Your Turn: Place a Card!");
                    updater.update();
                }
                sleep(500);

            } catch (IOException | InterruptedException e) {
                System.exit(1);
            }
            synchronized (client) {
                running = client.getGameState() != GameState.STOP;
            }
        }
    }
    
    /**
     * Fetch all the required data from the server in order to render the tui interface,
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
            client.fetchChat();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
