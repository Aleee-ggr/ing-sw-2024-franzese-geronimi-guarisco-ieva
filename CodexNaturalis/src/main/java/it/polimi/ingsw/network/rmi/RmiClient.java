package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.controller.threads.GameState;
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
 * Extends the abstract class Client, which contains the general specifications of a Client.
 * This class connects to the RMI server and provides methods for interacting with the server,
 * such as drawing and placing cards, joining games, and performing other game-related operations.
 *
 * @see Client
 * @see RmiServerInterface
 * @author Alessio Guarisco
 * @author Gloria Geronimi
 */

public class RmiClient extends Client{

    private RmiServerInterface remoteObject;

    /**
     * Constructs a new RmiClient object with the specified player username, password, server address, and server port.
     * Connects to the RMI server at the given address and port, and initializes the client.
     * @param playerUsername The username of the player.
     * @param password The password of the player.
     * @param serverAddress The address of the server.
     * @param serverPort The port of the server.
     * @throws RuntimeException If an error occurs while connecting to the server.
     */
    public RmiClient(String playerUsername, String password, String serverAddress, int serverPort) {
        super(playerUsername, password, serverAddress, serverPort);

        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress, 1099);
            remoteObject = (RmiServerInterface) registry.lookup(RmiServer.getName());
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the credentials of the client with the server.
     * @return True if the credentials are valid, false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean checkCredentials() throws RemoteException {
        return remoteObject.checkCredentials(data.getUsername(), data.getPassword());
    }

    /**
     * Draws a card from the server and adds it to the client's hand at the specified position.
     * @param position The index of the deck to draw from.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void drawCard(int position) throws ServerConnectionException, RemoteException {
        int id = remoteObject.drawCard(this.gameId, data.getUsername(), position);
        try{
            data.addToHand(id);
        } catch (HandFullException e ){
            throw new RuntimeException(e);
        }
    }

    /**
     * Places a card from the client's hand onto the board at the specified coordinates.
     * @param coordinates The coordinates on the board where the card will be placed.
     * @param cardId The ID of the card to be placed.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void placeCard(Coordinates coordinates, Integer cardId) throws ServerConnectionException, RemoteException {
        try{
            data.removeFromHand(cardId);
        } catch (ElementNotInHand e ){
            e.printStackTrace();
        }
        remoteObject.placeCard(this.gameId, data.getUsername(), coordinates, cardId);
    }

    /**
     * Requests to create a new game with the specified number of players.
     * @param players The number of players for the new game.
     * @return The UUID of the newly created game.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public UUID newGame(int players) throws ServerConnectionException, RemoteException {
        boolean validCredentials = remoteObject.checkCredentials(data.getUsername(), data.getPassword());
        UUID game = null;
        if (validCredentials) {
            game = remoteObject.newGame(players);
            joinGame(game);
        }
        return game;
    }

    /**
     * Requests to join a game with the specified UUID.
     * @param game The UUID of the game to join.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean joinGame(UUID game) throws ServerConnectionException, RemoteException {
        boolean success = remoteObject.join(game, data.getUsername());
        if (success) {
            this.setGameId(game);
        }
        return success;
    }

    /**
     * Retrieves a map of player scores from the server.
     * @return a map of player's usernames with theirs scores.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getScoreMap() throws ServerConnectionException, RemoteException {
        HashMap<String, Integer> scoreMap = remoteObject.getScoreMap(this.gameId, data.getUsername());

        if (scoreMap != null) {
            data.setScoreMap(scoreMap);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the client's hand from the server.
     * @return A list of card IDs in the client's hand.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getHand() throws ServerConnectionException, RemoteException {
        ArrayList<Integer> handIds = remoteObject.getHand(this.gameId, data.getUsername());

        if (handIds != null) {
            data.setClientHand(handIds);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the common objectives from the server.
     * @return A list of common objective IDs.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getCommonObjectives() throws ServerConnectionException, RemoteException {
        ArrayList<Integer> commonObjectives = remoteObject.getCommonObjectives(this.gameId, data.getUsername());

        if (commonObjectives != null) {
            data.setGlobalObjectives(commonObjectives.get(0), commonObjectives.get(1));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the resources of a player from the server.
     * @param usernameRequiredData The username of the player whose resources are to be retrieved.
     * @return A map of resource types to quantities.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getPlayerResources(String usernameRequiredData) throws ServerConnectionException, RemoteException {
        HashMap<Resource, Integer> playerResources = remoteObject.getPlayerResources(this.gameId, data.getUsername(), usernameRequiredData);

        if (playerResources != null) {
            data.updatePlayerResources(usernameRequiredData, playerResources);
            return true;
        } else {
            return false;
        }
    }

    public boolean getPlayers() throws ServerConnectionException, RemoteException {
        ArrayList<String> players = remoteObject.getPlayers(this.gameId, data.getUsername());

        if (players != null) {
            data.setPlayers(players);
            return true;
        } else {
            return false;
        }
    }

    public boolean getGameState() throws ServerConnectionException, RemoteException {
        GameState gameState = remoteObject.getGameState(this.gameId, data.getUsername());

        if (gameState != null) {
            data.setGameState(gameState);
            return true;
        } else {
            return false;
        }
    }

    public boolean getAvailableGames() throws ServerConnectionException, RemoteException {
        ArrayList<UUID> availableGames = Server.getAvailableGames(this.gameId, data.getUsername());
        return availableGames != null && !availableGames.isEmpty();
    }

    /**
     * Retrieves the visible cards from the server.
     * @return A list of visible card IDs.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getVisibleCards() throws ServerConnectionException, RemoteException {
        ArrayList<Integer> visibleCards = remoteObject.getVisibleCards(this.gameId, data.getUsername());

        if (visibleCards != null) {
            data.setVisibleCards(visibleCards);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the back side decks from the server.
     * @return A list of back side deck IDs.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getBackSideDecks() throws ServerConnectionException, RemoteException {
        ArrayList<Integer> backSideDecks = remoteObject.getBackSideDecks(this.gameId, data.getUsername());

        if (backSideDecks != null) {
            data.setBackSideDecks(backSideDecks);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the valid placements from the server.
     * @return A set of coordinates representing valid placements.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getValidPlacements() throws ServerConnectionException, RemoteException {
        Set<Coordinates> validPlacements = remoteObject.getValidPlacements(this.gameId, data.getUsername());

        if (validPlacements != null) {
            data.setValidPlacements(validPlacements);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the hand color of a player from the server.
     * @param usernameRequiredData The username of the player whose hand color is to be retrieved.
     * @return A list of resources representing the hand color.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getPlayerHandColor(String usernameRequiredData) throws ServerConnectionException, RemoteException {
        ArrayList<Resource> handColor = remoteObject.getHandColor(this.gameId, data.getUsername(), usernameRequiredData);

        if (handColor != null) {
            data.setPlayerHandColor(usernameRequiredData, handColor);
            return true;
        } else {
            return false;
        }
    }

    public boolean getClientBoard() throws ServerConnectionException, RemoteException {
        HashMap<Coordinates, Integer> clientBoard = remoteObject.getBoard(this.gameId, data.getUsername(), data.getUsername());

        if (clientBoard != null) {
            data.setClientBoard(clientBoard);
            return true;
        } else {
            return false;
        }
    }

    public boolean getPlayerBoard(String usernameRequiredData) throws ServerConnectionException, RemoteException {
        HashMap<Coordinates, Integer> clientBoard = remoteObject.getBoard(this.gameId, data.getUsername(), usernameRequiredData);

        if (clientBoard != null) {
            data.setPlayerBoard(usernameRequiredData, clientBoard);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the client's hand with data from the server.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void setClientHand() throws ServerConnectionException, RemoteException {
        data.setClientHand(remoteObject.getHand(this.gameId, data.getUsername()));
    }

    /**
     * Sets the client's valid placements with data from the server.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void setValidPlacements() throws ServerConnectionException, RemoteException {
        data.setValidPlacements(remoteObject.getValidPlacements(this.gameId, data.getUsername()));
    }

    /**
     * Updates the score of the specified player.
     * @param username The username of the player whose score is to be updated.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    /*public void updateScore(String username) throws ServerConnectionException, RemoteException {
        data.updateScore(username, Server.getScoreMap(this.gameId, data.getUsername()).get(username));
    }*/

    /**
     * Sets the client's board with data from the server.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void setClientBoard() throws ServerConnectionException, RemoteException {
        data.setClientBoard(remoteObject.getBoard(this.gameId, data.getUsername(), data.getUsername()));
    }

    /**
     * Updates the resources of the specified player.
     * @param username The username of the player whose resources are to be updated.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void updatePlayerResources(String username) throws ServerConnectionException, RemoteException {
        data.updatePlayerResources(username, remoteObject.getPlayerResources(this.gameId, data.getUsername(), username));
    }

    /**
     * Sets the board of the specified player with data from the server.
     * @param username The username of the player whose board is to be set.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void setPlayerBoard(String username) throws ServerConnectionException, RemoteException {
        data.setPlayerBoard(username, remoteObject.getBoard(this.gameId, data.getUsername(), username));
    }

    /**
     * Sets the hand color of the specified player with data from the server.
     * @param username The username of the player whose hand color is to be set.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void setPlayerHandColor(String username) throws ServerConnectionException, RemoteException {
        data.setPlayerHandColor(username, remoteObject.getHandColor(this.gameId, data.getUsername(), username));
    }

    /**
     * Allows the client to choose a starting objective.
     * @param objectiveId The ID of the chosen objective.
     * @return True if the choice is successful, false otherwise.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean chooseStartingObjective(int objectiveId) throws  ServerConnectionException, RemoteException{
        boolean success = remoteObject.choosePersonalObjective(this.gameId, data.getUsername(), objectiveId);
        if (success) {
            data.setPersonalObjective(objectiveId);
            System.out.println(objectiveId);
        }
        return success;
    }

    /**
     * Retrieves the starting objectives from the server.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public boolean getStartingObjectives() throws  ServerConnectionException, RemoteException {
        //Server.getStartingObjectives(this.gameId, data.getUsername());
        ArrayList<Integer> startingObjectives = remoteObject.getStartingObjectives(this.gameId, data.getUsername());

        if (startingObjectives != null) {
            data.setStartingObjectives(startingObjectives);
            return true;
        } else {
            return false;
        }
    }

    public boolean setStartingCard(boolean frontSideUp) throws ServerConnectionException, RemoteException {
        boolean success = remoteObject.setStartingCard(this.gameId, data.getUsername(), frontSideUp);

        return success;
    }

    /**
     * Waits for an update from the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void waitUpdate() throws RemoteException {
        remoteObject.wait(this.gameId, data.getUsername());
    }

    public boolean getStartingCard() throws ServerConnectionException, RemoteException {
        Integer startingCardId = remoteObject.getStartingCard(this.gameId, data.getUsername());

        if (startingCardId != null) {
            data.setStartingCard(data.getUsername(), startingCardId);
            return true;
        } else {
            return false;
        }
    }
    //TODO: methods to implement

    /**
     * Posts a chat message on the server.
     * @param message The chat message to post.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void postChat(String message) throws  ServerConnectionException, RemoteException{
        remoteObject.postChat(this.gameId, data.getUsername(), message);
    }
}