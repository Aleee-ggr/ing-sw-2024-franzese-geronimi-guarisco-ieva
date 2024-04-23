package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.ThreadMessage;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The abstract class Server serves as a base class for server implementations.
 * It manages games and their corresponding threads, as well as players and messages.
 * @author Daniele Ieva
 */
public abstract class Server {
    protected static final Map<UUID, BlockingQueue<ThreadMessage>> threadMessages = new ConcurrentHashMap<>();
    protected static final Set<String> playerList = new HashSet<>();
    protected static final Map<UUID, Integer> games = new ConcurrentHashMap<>(); // TODO: remove game while closed

    /**
     * Creates a new game with the specified number of players and starts its thread.
     * @param numberOfPlayers the number of players in the game (between 2 and 4 inclusive)
     * @return the unique ID of the created game, or null if the number of players is out of range
     */
    public static UUID createGame(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) {
            return null;
        }
        UUID id = UUID.randomUUID();
        while (threadMessages.containsKey(id)) {
            id = UUID.randomUUID();
        }
        BlockingQueue<ThreadMessage> messageQueue = new LinkedBlockingDeque<>();
        threadMessages.put(id, messageQueue);
        new GameThread(messageQueue, numberOfPlayers).start();
        addGame(id, numberOfPlayers);
        return id;
    }

    /**
     * Gets an unmodifiable view of the games managed by the server.
     * @return a map of game IDs to the number of players in each game
     */
    public static Map<UUID, Integer> getGames() {
        return Collections.unmodifiableMap(games);
    }

    /**
     * Adds a game to the server's collection of games.
     * @param id the unique ID of the game
     * @param playerNum the number of players in the game
     */
    private static void addGame(UUID id, Integer playerNum) {
        games.put(id, playerNum);
    }

    /**
     * Sends a message to a specified game.
     * The method uses a synchronized block to safely interact with the message queue of the game.
     * @param game the unique ID of the game to send the message to
     * @param message the message to be sent to the game
     */

    public static void sendMessage(UUID game, ThreadMessage message ) {
        synchronized (threadMessages) {
            BlockingQueue<ThreadMessage> queue = threadMessages.get(game);
            queue.add(message);

            ThreadMessage response;
            boolean responded = false;
            do {
                response = queue.peek();
                if (response != null && response.status() != Status.REQUEST) {
                    String sender = response.player();
                    responded = sender.equals(message.player());
                }
            } while (!responded);
            System.out.println(response);
        }
    }

}
