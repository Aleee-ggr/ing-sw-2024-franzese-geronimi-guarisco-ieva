package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.Shared;
import it.polimi.ingsw.controller.threads.message.ThreadMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Server {
    protected final Map<UUID, Shared<ThreadMessage>> threadMessages = new ConcurrentHashMap<>();
    protected final Set<String> playerList = new HashSet<>();
    protected final Map<UUID, Integer> games = new ConcurrentHashMap<>(); //TODO: remove game while closed

    public UUID createGame(int numberOfPlayers){
        if(numberOfPlayers < 2 || numberOfPlayers > 4){
            UUID id = UUID.randomUUID();
            while(threadMessages.containsKey(id)){
                id = UUID.randomUUID();
            }
            Shared<ThreadMessage> thisGameShared = new Shared<>();
            threadMessages.put(id, thisGameShared);
            new GameThread(thisGameShared, numberOfPlayers).start();
            addGame(id, numberOfPlayers);
            return id;
        } else {
            return null;
        }
    }

    public Map<UUID, Integer> getGames() {
        return Collections.unmodifiableMap(games);
    }

    private void addGame(UUID id, Integer playerNum){
        games.put(id,playerNum);
    }
}
