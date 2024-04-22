package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.network.Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

/**
 * Represents a client using the RMI protocol.
 * Extends the abstract class Client that contains the specs of the general Client.
 * @author Alessio Guarisco
 */

public class RmiClient extends Client{

    private RmiServerInterface server;

    /**
     * Constructs a new RmiClient object with the specified player username, server address, and server port.
     * @param playerUsername The username of the player.
     * @param serverAddress  The address of the server.
     * @param serverPort     The port of the server.
     */
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

    /**
     * Requests to join a game with the specified UUID.
     * @param game The UUID of the game to join.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException          If a remote communication error occurs.
     */
    public void joinGame(UUID game) throws ServerConnectionException, RemoteException {
        server.join(game, this.data.getUsername());
    }

    /**
     * Requests to create a new game with the specified number of players.
     * @param players The number of players for the new game.
     * @return The UUID of the newly created game.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException          If a remote communication error occurs.
     */
    public UUID newGame(int players) throws ServerConnectionException, RemoteException {
        return server.newGame(players);
    }
}
