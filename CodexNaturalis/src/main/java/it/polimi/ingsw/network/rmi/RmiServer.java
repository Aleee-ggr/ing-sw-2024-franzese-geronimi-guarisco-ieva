package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.threads.Shared;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.network.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RmiServer implements Server, RmiServerInterface {
    private static final String name="rmiServer";
    private final Registry registry;
    private final Map<UUID, Shared<ThreadMessage>> threadMessages = new HashMap<>();

    public RmiServer(int port) throws RemoteException {
        System.out.println("Starting server...");
        RmiServerInterface stub = (RmiServerInterface) UnicastRemoteObject.exportObject(this, port);
        registry = LocateRegistry.createRegistry(1099);
        registry.rebind(name, stub);
        System.out.println("Started!");
    }

    public void stop() {
        try {
            registry.unbind(name);
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
