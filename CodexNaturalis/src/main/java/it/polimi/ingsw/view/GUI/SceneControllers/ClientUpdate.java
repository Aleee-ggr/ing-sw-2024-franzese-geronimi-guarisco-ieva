package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;

import java.io.IOException;

import static it.polimi.ingsw.controller.WaitState.*;

/**
 * The ClientUpdate class is a thread that waits for updates from the server.
 * When updates are received, it fetches the necessary data from the server
 * and updates a shared variable to notify ClientRenderUpdateThread to update the view.
 */
public class ClientUpdate extends Thread {
    private final ClientInterface client;
    private final SharedUpdate updater;
    private final GameController gameController;

    /**
     * Constructs a new ClientUpdate thread.
     *
     * @param client        the client interface to communicate with the server
     * @param gameController the game controller that manages the game view
     * @param updater       the shared updater that triggers view updates
     */
    public ClientUpdate(ClientInterface client, GameController gameController, SharedUpdate updater) {
        this.client = client;
        this.updater = updater;
        this.gameController = gameController;
    }

    /**
     * Wait for updates from the server, using the function waitUpdate and waiting for a return from the call.
     * This function will set the state to either TURN or WAIT, and then wait for the server to change it to UPDATE
     * or TURN_UPDATE, if an update is received,
     * fetch necessary data from the server and update the view using the updater.
     */
    public void run() {
        boolean running;
        WaitState state = null;
        synchronized (client) {
            running = client.getGameState() != GameState.STOP;
        }
        while (running) {
            try {
                state = client.waitUpdate();
                if (state == UPDATE || state == TURN_UPDATE) {
                    fetchData();
                    updater.update();
                }
                if (state == TURN || state == TURN_UPDATE) {
                    fetchData();
                    gameController.setTurn();
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
     * Fetches all necessary game data from the server.
     * This includes hand data, common objectives, valid placements, player boards,
     * player order, resources, score map, game state, visible cards and decks,
     * opponent hand color and type, player colors, and chat messages.
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
            client.fetchChat();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

