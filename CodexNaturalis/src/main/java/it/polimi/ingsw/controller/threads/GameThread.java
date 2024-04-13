package it.polimi.ingsw.controller.threads;

public class GameThread extends Thread {
    private final Shared<ThreadMessage> message;

    public GameThread(Shared<ThreadMessage> message) {
        this.message = message;
    }

    @Override
    public void run() {
        super.run();
    }
}
