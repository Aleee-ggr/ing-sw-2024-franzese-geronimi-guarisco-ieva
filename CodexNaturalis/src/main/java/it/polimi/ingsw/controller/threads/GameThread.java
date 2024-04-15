package it.polimi.ingsw.controller.threads;

public class GameThread extends Thread {
    private final Shared<ThreadMessage> message;
    private final Integer playerNum;

    public GameThread(Shared<ThreadMessage> message, Integer playerNum) {
        this.message = message;
        this.playerNum = playerNum;
    }

    @Override
    public void run() {
        super.run();
    }
}
