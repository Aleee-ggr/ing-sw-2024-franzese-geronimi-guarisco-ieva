package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.helpers.exceptions.model.ElementNotInHand;
import it.polimi.ingsw.helpers.exceptions.model.HandFullException;
import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.model.board.Coordinates;
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
     * Draws a card from the server and adds it to the client's hand at the specified position.
     * @param position The index of the deck to draw from.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException          If a remote communication error occurs.
     */
    public void drawCard(int position) throws ServerConnectionException, RemoteException {
        int id = server.drawCard(this.gameId, this.data.getUsername(), position);
        try{
            data.addToHand(id);
        } catch (HandFullException e ){
            e.printStackTrace();
        }
    }

    /**
     * Places a card from the client's hand onto the board at the specified coordinates.
     * @param coordinates The coordinates on the board where the card will be placed.
     * @param cardId      The ID of the card to be placed.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException          If a remote communication error occurs.
     */
    public void placeCard(Coordinates coordinates, Integer cardId) throws ServerConnectionException, RemoteException {
        try{
            data.removeFromHand(cardId);
        } catch (ElementNotInHand e ){
            e.printStackTrace();
        }
        server.placeCard(this.gameId, this.data.getUsername(), coordinates, cardId);
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

    /**
     * Requests to join a game with the specified UUID.
     * @param game The UUID of the game to join.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException          If a remote communication error occurs.
     */
    public void joinGame(UUID game) throws ServerConnectionException, RemoteException {
        server.join(game, this.data.getUsername());
        this.setGameId(game);
    }

    public void postChat(String message) throws  ServerConnectionException, RemoteException{
        server.postChat(this.gameId, this.data.getUsername(), message);
    }

    public void waitUpdate() throws RemoteException {
        server.waitUpdate(this.gameId, this.data.getUsername());
    }

    //TODO: methods to implement

    /*public void getStartingObjectives() throws  ServerConnectionException, RemoteException{
        server.getStartingObjectives(this.gameId, this.data.getUsername());
    }*/

    /*public void getPlacementCoordinates() throws ServerConnectionException, RemoteException{
        data.setValidPlacements(server.getPlacementCoordinates(this.gameId, this.data.getUsername()));
    }*/

    public void chooseStartingObjective(int objectiveId) throws  ServerConnectionException, RemoteException{
        server.chooseStartingObjective(this.gameId, this.data.getUsername(), objectiveId);
    }
}
