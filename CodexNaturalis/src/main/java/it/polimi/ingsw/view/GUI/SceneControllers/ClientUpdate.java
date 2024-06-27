package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;
import it.polimi.ingsw.view.TUI.controller.TuiController;

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
    private volatile WaitState state;

    /**
     * Constructs a new ClientUpdate thread.
     *
     * @param client         the client interface to communicate with the server
     * @param gameController the game controller that manages the game view
     * @param updater        the shared updater that triggers view updates
     */
    public ClientUpdate(ClientInterface client, GameController gameController, SharedUpdate updater) {
        this.client = client;
        this.updater = updater;
        this.gameController = gameController;
    }

    public WaitState getWaitState() {
        return state;
    }

    /**
     * Wait for updates from the server, using the function waitUpdate and waiting for a return from the call.
     * This function will set the state to either TURN or WAIT, and then wait for the server to change it to UPDATE
     * or TURN_UPDATE, if an update is received,
     * fetch necessary data from the server and update the view using the updater.
     */
    @Override
    public void run() {
        state = null;
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
                    sleep(500);
                    fetchData();
                    if (!gameController.myTurn) {
                        gameController.setTurn();
                    }
                    updater.update();
                }

                if (oldState != STANDBY && state == STANDBY) {
                    gameController.setStandby();
                    updater.update();
                }

                sleep(500);
            } catch (IOException | InterruptedException e) {
                System.exit(1);
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
            TuiController.fetchData(client);
            client.fetchChat();
        } catch (IOException e) {
            System.out.println("Impossible to fetch data from server!");
            System.exit(1);
        }
    }
}

