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

    /**
     * The RenderThread class handles the rendering of the TUI.
     * It listens for updates and redraws the TUI accordingly.
     */
    public RenderThread(ClientInterface client, SharedUpdate updater, Compositor compositor) {
        this.client = client;
        this.updater = updater;
        this.compositor = compositor;
    }

    /**
     * Clears the screen and print the tui whenever an update is received from the given {@link #updater},
     * which will be called from
     * {@link ClientUpdateThread} and {@link CommandThread}.
     */
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

    /**
     * Clear the screen using ansi escape characters, preparing for the render of the next scene.
     */
    private void clear() {
        out.print("\033[H\033[2J");
        out.flush();
    }
}
