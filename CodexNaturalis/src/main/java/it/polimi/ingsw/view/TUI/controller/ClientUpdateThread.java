package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;
import org.checkerframework.checker.units.qual.N;

import java.io.IOException;

public class ClientUpdateThread extends Thread {
    private final ClientInterface client;
    private final SharedUpdate updater;

    public ClientUpdateThread(ClientInterface client, SharedUpdate updater) {
        this.client = client;
        this.updater = updater;
    }

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
                if (state == WaitState.UPDATE) {
                    fetchData();
                    updater.update();
                }
                if (oldState != WaitState.TURN && state == WaitState.TURN) {
                    fetchData();
                    updater.update();
                }
                sleep(500);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
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
        } catch (IOException e) {throw new RuntimeException(e);}
    }
}
