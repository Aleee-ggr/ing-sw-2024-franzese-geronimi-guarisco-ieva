package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.Compositor;

import java.io.PrintWriter;

public class RenderThread extends Thread {
    private final PrintWriter out = new PrintWriter(System.out, true);
    private final ClientInterface client;
    private final Compositor compositor;
    private final SharedUpdate updater;

    public RenderThread(ClientInterface client, SharedUpdate updater, Compositor compositor) {
        this.client = client;
        this.updater = updater;
        this.compositor = compositor;
    }

    @Override
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
            clear();
            out.print(compositor);
            out.flush();
            synchronized (client) {
                running = client.getGameState() != GameState.STOP;
            }
        }
    }


    private void clear() {
        out.print("\033[H\033[2J");
        out.flush();
    }
}
