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
        if (message.getValue().status() == Status.OK) {
            message.setValue(
                    new ThreadMessage(Status.OK, "TODO")
            );
        }
    }
}
