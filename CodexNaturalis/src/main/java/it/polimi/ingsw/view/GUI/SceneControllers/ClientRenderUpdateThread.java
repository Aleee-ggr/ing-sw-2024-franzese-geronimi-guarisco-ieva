package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;

/**
 * The ClientRenderUpdateThread class is a thread that continuously checks for updates
 * from the shared variable and updates the client's view when notified.
 */
public class ClientRenderUpdateThread extends Thread {
    private final ClientInterface client;
    private final SharedUpdate updater;
    private final GameController gameController;

    /**
     * Constructs a new ClientRenderUpdateThread.
     *
     * @param client        the client interface to communicate with the server
     * @param gameController the game controller that manages the game view
     * @param updater       the shared updater that checks for updates from the server
     */
    public ClientRenderUpdateThread(ClientInterface client, GameController gameController, SharedUpdate updater) {
        this.client = client;
        this.updater = updater;
        this.gameController = gameController;
    }

    /**
     * Continuously waits for updates from the shared variable.
     * When an update is received, it calls the method to update the client view.
     * It then returns to wait for another update.
     * The thread will keep running as long as the game's state is not STOP.
     */
    public void run() {
        boolean running;
        synchronized (client) {
            running = client.getGameState() != GameState.STOP;
        }
        while (running) {
            boolean update = false;
            while (!update) {
                update = updater.getUpdate();
            }
            gameController.updateView();
            synchronized (client) {
                running = client.getGameState() != GameState.STOP;
            }

            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}