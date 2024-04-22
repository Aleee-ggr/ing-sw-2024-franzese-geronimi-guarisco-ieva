package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.controller.threads.message.ThreadMessage;

import java.util.concurrent.BlockingQueue;

public class GameThread extends Thread {
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final Integer playerNum;

    public GameThread(BlockingQueue<ThreadMessage> messageQueue, Integer playerNum) {
        this.messageQueue = messageQueue;
        this.playerNum = playerNum;
    }

    @Override
    public void run() {
        // TODO game loop and lobby
        while (true) {

            ThreadMessage msg;
            do {
                msg = messageQueue.peek();
            } while (msg == null || msg.status() != Status.REQUEST);

            msg = messageQueue.remove();

            if (msg.status() == Status.REQUEST) {
                messageQueue.add(
                        new ThreadMessage(Status.OK, msg.player(), "ok", null)
                );
            }
        }
    }
}
