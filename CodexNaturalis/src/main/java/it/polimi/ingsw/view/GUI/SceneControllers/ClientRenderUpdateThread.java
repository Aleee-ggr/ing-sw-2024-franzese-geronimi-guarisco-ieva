package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;

public class ClientRenderUpdateThread extends Thread {
    private ClientInterface client;
    private SharedUpdate updater;
    private GameController gameController;

    public ClientRenderUpdateThread(ClientInterface client, GameController gameController, SharedUpdate updater) {
        this.client = client;
        this.updater = updater;
        this.gameController = gameController;
    }

    public void run() {
        boolean running;
        synchronized (client) {
            running = client.getGameState() != GameState.ENDGAME;
        }
        while (running) {
            boolean update = false;
            while (!update) {
                update = updater.getUpdate();
            }
            gameController.updateView();
            synchronized (client) {
                running = client.getGameState() != GameState.ENDGAME;
            }
        }
    }
}