package it.polimi.ingsw.controller.threads;

public class GameThread extends Thread {
    private final ThreadMessage message;

    public GameThread(ThreadMessage message) {
        this.message = message;
    }

    @Override
    public void run() {
        super.run();
    }
}
