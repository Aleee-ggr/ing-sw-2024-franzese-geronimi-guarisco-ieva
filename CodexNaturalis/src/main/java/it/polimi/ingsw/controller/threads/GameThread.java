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
    private final Integer maxPlayers;
    private boolean running = true;
    private final Controller controller;
    private Game game;

    public GameThread(BlockingQueue<ThreadMessage> messageQueue, Integer maxPlayers) {
        this.messageQueue = messageQueue;
        this.maxPlayers = maxPlayers;
        this.controller = new Controller(this, messageQueue, maxPlayers);
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
                controller.join(msg.player(), msg.messageUUID());
                break;
            case "draw":
                controller.draw(
                        msg.player(),
                        Arrays.stream(msg.args()).mapToInt(Integer::parseInt).findFirst().orElse(-1),
                        msg.messageUUID()
                );
            case "place":
                controller.placeCard(
                        msg.player(),
                        new Coordinates(Integer.valueOf(msg.args()[0]),Integer.valueOf(msg.args()[1])),
                        Integer.valueOf(msg.args()[2]),
                        msg.messageUUID()
                );
            case "create":
                controller.createGame(
                        msg.player(),
                        Integer.valueOf(msg.args()[0]),
                        UUID.fromString(msg.args()[1]),
                        msg.messageUUID()
                );
            case "kill":
                running = false;
                break;
            default:
                messageQueue.add(
                    new ThreadMessage(Status.ERROR, msg.player(), "unknown", null, msg.messageUUID())
                );
        }
    }
}
