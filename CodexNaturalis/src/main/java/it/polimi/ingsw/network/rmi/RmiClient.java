package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.helpers.exceptions.model.ElementNotInHand;
import it.polimi.ingsw.helpers.exceptions.model.HandFullException;
import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
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
    public RmiClient(String playerUsername, String password, String serverAddress, int serverPort) {
        super(playerUsername, password, serverAddress, serverPort);
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (RmiServerInterface) registry.lookup(RmiServer.getName());
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkCredentials() throws RemoteException {
        return server.checkCredentials(data.getUsername(), data.getPassword());
    }
    /**
     * Draws a card from the server and adds it to the client's hand at the specified position.
     * @param position The index of the deck to draw from.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException          If a remote communication error occurs.
     */
    public void drawCard(int position) throws ServerConnectionException, RemoteException {
        int id = server.drawCard(this.gameId, data.getUsername(), position);
        try{
            data.addToHand(id);
        } catch (HandFullException e ){
            throw new RuntimeException(e);
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
        server.placeCard(this.gameId, data.getUsername(), coordinates, cardId);
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
        server.join(game, data.getUsername());
        this.setGameId(game);
    }

    public HashMap<String, Integer> getScoreMap() throws ServerConnectionException, RemoteException {
        return Server.getScoreMap(this.gameId, data.getUsername());
    }

    public ArrayList<Integer> getHand() throws ServerConnectionException, RemoteException {
        return Server.getHand(this.gameId, data.getUsername());
    }

    public ArrayList<Integer> getCommonObjectives() throws ServerConnectionException, RemoteException {
        return Server.getCommonObjectives(this.gameId, data.getUsername());
    }

    public HashMap<Resource, Integer> getPlayerResources(String usernameRequiredData) throws ServerConnectionException, RemoteException {
        return Server.getPlayerResources(this.gameId, data.getUsername(), usernameRequiredData);
    }

    public ArrayList<Integer> getVisibleCards() throws ServerConnectionException, RemoteException {
        return Server.getVisibleCards(this.gameId, data.getUsername());
    }

    public ArrayList<Integer> getBackSideDecks() throws ServerConnectionException, RemoteException {
        return Server.getBackSideDecks(this.gameId, data.getUsername());
    }

    public Set<Coordinates> getValidPlacements() throws ServerConnectionException, RemoteException {
        return Server.getValidPlacements(this.gameId, data.getUsername());
    }

    public ArrayList<Resource> getPlayerHandColor(String usernameRequiredData) throws ServerConnectionException, RemoteException {
        return Server.getHandColor(this.gameId, data.getUsername(), usernameRequiredData);
    }

    public void setClientHand() throws ServerConnectionException, RemoteException {
        data.setClientHand(Server.getHand(this.gameId, data.getUsername()));
    }

    public void setValidPlacements() throws ServerConnectionException, RemoteException {
        data.setValidPlacements(Server.getValidPlacements(this.gameId, data.getUsername()));
    }

    public void updateScore(String username) throws ServerConnectionException, RemoteException {
        data.updateScore(username, Server.getScoreMap(this.gameId, data.getUsername()).get(username));
    }

    public void setClientBoard() throws ServerConnectionException, RemoteException {
        data.setClientBoard(Server.getBoard(this.gameId, data.getUsername(), data.getUsername()));
    }

    public void updatePlayerResources(String username) throws ServerConnectionException, RemoteException {
        data.updatePlayerResources(username, Server.getPlayerResources(this.gameId, data.getUsername(), username));
    }

    public void setPlayerBoard(String username) throws ServerConnectionException, RemoteException {
        data.setPlayerBoard(username, Server.getBoard(this.gameId, data.getUsername(), username));
    }

    public void setPlayerHandColor(String username) throws ServerConnectionException, RemoteException {
        data.setPlayerHandColor(username, Server.getHandColor(this.gameId, data.getUsername(), username));
    }

    public boolean chooseStartingObjective(int objectiveId) throws  ServerConnectionException, RemoteException{
        return Server.choosePersonalObjective(this.gameId, data.getUsername(), objectiveId);
    }

    public void getStartingObjectives() throws  ServerConnectionException, RemoteException{
        Server.getStartingObjectives(this.gameId, data.getUsername());
    }

    //TODO: methods to implement
    public void postChat(String message) throws  ServerConnectionException, RemoteException{
        server.postChat(this.gameId, data.getUsername(), message);
    }

    public void waitUpdate() throws RemoteException {
        server.waitUpdate(this.gameId, data.getUsername());
    }
}