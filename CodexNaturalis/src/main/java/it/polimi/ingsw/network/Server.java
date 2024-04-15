package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.Shared;
import it.polimi.ingsw.controller.threads.ThreadMessage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Server {
    protected final Map<UUID, Shared<ThreadMessage>> threadMessages = new ConcurrentHashMap<>();
    protected final Set<String> playerList = new HashSet<>();

    public boolean createGame(int numberOfPlayers){
        if(numberOfPlayers < 2 || numberOfPlayers > 4){
            UUID id = UUID.randomUUID();
            while(threadMessages.containsKey(id)){
                id = UUID.randomUUID();
            }
            Shared<ThreadMessage> thisGameShared = new Shared<>();
            threadMessages.put(id, thisGameShared);
            new GameThread(thisGameShared, numberOfPlayers).start();
            return true;
        } else {
            return false;
        }
    }
}
