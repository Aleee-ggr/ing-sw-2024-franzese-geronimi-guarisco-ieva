package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.message.ThreadMessage;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class Server {
    protected final Map<UUID, BlockingQueue<ThreadMessage>> threadMessages = new ConcurrentHashMap<>();
    protected final Set<String> playerList = new HashSet<>();
    protected final Map<UUID, Integer> games = new ConcurrentHashMap<>(); // TODO: remove game while closed

    public UUID createGame(int numberOfPlayers) {
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

    public Map<UUID, Integer> getGames() {
        return Collections.unmodifiableMap(games);
    }

    private void addGame(UUID id, Integer playerNum) {
        games.put(id, playerNum);
    }

    protected void sendMessage(UUID game, String message, String player) {
        synchronized (threadMessages) {
            BlockingQueue<ThreadMessage> queue = threadMessages.get(game);
            queue.add(
                    new ThreadMessage(Status.REQUEST, message, player)
            );

            ThreadMessage response;
            boolean responded = false;
            do {
                response = queue.peek();
                if (response != null && response.status() != Status.REQUEST) {
                    String sender = response.player();
                    responded = sender.equals(player);
                }
            } while (!responded);
            System.out.println(response);
        }
    }

}
