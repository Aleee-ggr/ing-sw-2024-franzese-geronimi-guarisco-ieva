package it.polimi.ingsw.view.TUI.controller;

import it.polimi.ingsw.view.TUI.Compositor;

import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

public class RenderThread extends Thread {
    private final PrintWriter out = new PrintWriter(System.out, true);
    private final Compositor compositor;
    private final SharedUpdate updater;
    private final AtomicBoolean running;

    /**
     * The RenderThread class handles the rendering of the TUI.
     * It listens for updates and redraws the TUI accordingly.
     */
    public RenderThread(SharedUpdate updater, Compositor compositor, AtomicBoolean running) {
        this.updater = updater;
        this.compositor = compositor;
        this.running = running;
    }

    /**
     * Clears the screen and print the tui whenever an update is received from the given {@link #updater},
     * which will be called from
     * {@link ClientUpdateThread} and {@link CommandThread}.
     */
    @Override
    public void run() {
        while (running.get()) {
            boolean update = false;
            while (!update) {
                update = updater.getUpdate();
            }
            clear();
            out.print(compositor);
            out.flush();
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
