package it.polimi.ingsw.controller.threads;

import java.util.concurrent.BlockingQueue;

public class GameThread extends Thread {
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final Integer playerNum;
    private boolean running = true;

    public GameThread(BlockingQueue<ThreadMessage> messageQueue, Integer playerNum) {
        this.messageQueue = messageQueue;
        this.playerNum = playerNum;
    }

    @Override
    public void run() {
        // TODO game loop and lobby
        while (running) {
            ThreadMessage msg = getMessage();
            if (msg.status() == Status.REQUEST) {
                respond(msg);
            }
        }
    }

    private ThreadMessage getMessage() {
        ThreadMessage msg;
        do {
            msg = messageQueue.peek();
        } while (msg == null || msg.status() != Status.REQUEST);

        return messageQueue.remove();
    }

    private void respond(ThreadMessage msg) {
        switch (msg.type()) {
            case "kill":
                running = false;
                break;
            default:
                messageQueue.add(
                    new ThreadMessage(Status.OK, msg.player(), "ok", null)
                );
        }
    }
}
