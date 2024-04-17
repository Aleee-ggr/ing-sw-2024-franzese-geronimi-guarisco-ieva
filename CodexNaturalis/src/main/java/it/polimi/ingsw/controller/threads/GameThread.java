package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.controller.threads.message.ThreadMessage;

public class GameThread extends Thread {
    private final Shared<ThreadMessage> message;
    private final Integer playerNum;

    public GameThread(Shared<ThreadMessage> message, Integer playerNum) {
        this.message = message;
        this.playerNum = playerNum;
    }

    @Override
    public void run() {
        //TODO game loop and lobby
        while (true) {
            while (message.getValue().status() != Status.REQUEST) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
            if (message.getValue().status() == Status.OK) {
                message.setValue(
                        new ThreadMessage(Status.OK, "TODO")
                );
            }
        }
    }
}
