package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * GameThread Class, used to manage the thread-side logic for the controller.
 * Instantiate a new thread for each game to work simultaneously with the same server.
 * @author Daniele Ieva
 * */
public class GameThread extends Thread {
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final Integer playerNum;
    private boolean running = true;
    private final Controller controller;
    private Game game;

    public GameThread(BlockingQueue<ThreadMessage> messageQueue, Integer playerNum) {
        this.messageQueue = messageQueue;
        this.playerNum = playerNum;
        this.controller = new Controller(this, messageQueue);
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
            case "join":
                controller.join(msg.player());
                break;
            case "draw":
                controller.draw(msg.player(), Arrays.stream(msg.args()).mapToInt(Integer::parseInt).findFirst().orElse(-1));
            case "place":
                controller.placeCard(msg.player(), new Coordinates(Integer.valueOf(msg.args()[0]),Integer.valueOf(msg.args()[1])), Integer.valueOf(msg.args()[2]));
            case "create":
                controller.createGame(msg.player(), Integer.valueOf(msg.args()[0]), UUID.fromString(msg.args()[1]));
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
