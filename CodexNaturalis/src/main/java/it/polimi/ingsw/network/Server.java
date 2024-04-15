package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.threads.Shared;
import it.polimi.ingsw.controller.threads.ThreadMessage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Server {
    protected final Map<UUID, Shared<ThreadMessage>> threadMessages = new ConcurrentHashMap<>();
}
