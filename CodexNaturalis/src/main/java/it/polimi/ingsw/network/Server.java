package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.Shared;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.message.ThreadMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Server {
    protected final Map<UUID, Shared<ThreadMessage>> threadMessages = new ConcurrentHashMap<>();
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
        Shared<ThreadMessage> thisGameShared = new Shared<>();
        thisGameShared.setValue(new ThreadMessage(Status.INIT, ""));
        threadMessages.put(id, thisGameShared);
        new GameThread(thisGameShared, numberOfPlayers).start();
        addGame(id, numberOfPlayers);
        return id;
    }

    public Map<UUID, Integer> getGames() {
        return Collections.unmodifiableMap(games);
    }

    private void addGame(UUID id, Integer playerNum) {
        games.put(id, playerNum);
    }

    protected void sendMessage(UUID game, String message) {
        synchronized (threadMessages) {
            Shared<ThreadMessage> shared = threadMessages.get(game);
            shared.setValue(
                    new ThreadMessage(Status.REQUEST, message)
            );

            ThreadMessage msg;
            do {
                try {
                    Thread.sleep(100);
                } catch (Exception ignored) {
                }
                msg = shared.getValue();
            } while (msg.status() == Status.REQUEST);
        }
    }

}
