package it.polimi.ingsw.view.GUI.SceneControllers;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.controller.SharedUpdate;

import java.io.IOException;

import static it.polimi.ingsw.controller.WaitState.*;

public class ClientUpdate extends Thread {
    private final ClientInterface client;
    private final SharedUpdate updater;
    private final GameController gameController;

    public ClientUpdate(ClientInterface client, GameController gameController, SharedUpdate updater) {
        this.client = client;
        this.updater = updater;
        this.gameController = gameController;
    }

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

