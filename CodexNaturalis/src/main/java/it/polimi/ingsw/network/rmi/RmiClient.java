package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.network.Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class RmiClient extends Client{

    private RmiServerInterface server;

    public RmiClient(String playerUsername, String serverAddress, int serverPort) {
        super(playerUsername, serverAddress, serverPort);
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (RmiServerInterface) registry.lookup(RmiServer.getName());
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinGame(UUID game) throws ServerConnectionException, RemoteException {
        server.join(game, this.data.getUsername());
    }

    public UUID newGame(int players) throws ServerConnectionException, RemoteException {
        return server.newGame(players);
    }
}
