package it.polimi.ingsw.network.rmi;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.helpers.exceptions.model.ElementNotInHand;
import it.polimi.ingsw.helpers.exceptions.model.HandFullException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a client using the RMI protocol.
 * Extends the abstract class Client, which contains the general specifications of a Client.
 * This class connects to the RMI server and provides methods for interacting with the server,
 * such as drawing and placing cards, joining games, and performing other game-related operations.
 *
 * @see Client
 * @see RmiServerInterface
 */

public class RmiClient extends Client implements ClientInterface {

    private final RmiServerInterface remoteObject;

    /**
     * Constructs a new RmiClient object with the specified player username, password, server address, and server port.
     * Connects to the RMI server at the given address and port, and initializes the client.
     * @param serverAddress The address of the server.
     * @param serverPort The port of the server.
     * @throws RuntimeException If an error occurs while connecting to the server.
     */
    public RmiClient(String serverAddress, int serverPort) {
        super(serverAddress, serverPort);

        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            remoteObject = (RmiServerInterface) registry.lookup(RmiServer.getName());
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Requests to create a new game with the specified number of players.
     * @param players The number of players for the new game.
     * @return The UUID of the newly created game.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public UUID newGame(int players) throws RemoteException {
        UUID game = remoteObject.newGame(players);
        if(game != null){
            joinGame(game);
        }
        return game;
    }

    /**
     * Requests to join a game with the specified UUID.
     * @param game The UUID of the game to join.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public boolean joinGame(UUID game) throws RemoteException {
        if(username == null && password == null){
            System.out.println("Please login first");
            return false;
        }
        boolean success = remoteObject.join(game, this.username);

        if (success) {
            this.setGameId(game);
        }
        return success;
    }

    @Override
    public boolean checkCredentials(String username, String password) throws RemoteException {
        if(remoteObject.checkCredentials(username, password)){
            this.username = username;
            this.password = password;
            return true;
        }
        return false;
    }

    /**
     * Draws a card from the server from the deck or the visible card specified
     * by the position and adds it to the client's hand.
     * @param position The index of the deck to draw from.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void drawCard(int position) throws RemoteException {
        drawCardClient(remoteObject.drawCard(this.gameId, username, position));
    }

    /**
     * Places a card from the client's hand onto the board at the specified coordinates.
     * @param coordinates The coordinates on the board where the card will be placed.
     * @param cardId The ID of the card to be placed.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public boolean placeCard(Coordinates coordinates, int cardId) throws RemoteException {
        return placeCardClient(remoteObject.placeCard(this.gameId, username, coordinates, cardId), cardId);
    }

    @Override
    public boolean placeStartingCard(boolean frontSideUp) throws RemoteException{
        return placeStartingCardClient(frontSideUp, remoteObject.setStartingCard(gameId, username, frontSideUp));
    }

    @Override
    public boolean choosePersonalObjective(int objectiveId) throws RemoteException {
        return choosePersonalObjectiveClient(objectiveId , remoteObject.choosePersonalObjective(this.gameId, username, objectiveId));
    }

    @Override
    public boolean fetchAvailableGames() throws RemoteException{
        return fetchAvailableGamesClient(remoteObject.getAvailableGames(username));
    }

    @Override
    public boolean fetchGameState() throws RemoteException {
        return fetchGameStateClient(remoteObject.getGameState(this.gameId, this.username));
    }

    @Override
    public boolean fetchPlayers() throws RemoteException {
        return fetchPlayersClient(remoteObject.getPlayers(this.gameId, this.username));
    }

    @Override
    public boolean fetchCommonObjectives() throws RemoteException {
        return fetchCommonObjectivesClient(remoteObject.getCommonObjectives(this.gameId, this.username));
    }

    @Override
    public boolean fetchPersonalObjective() throws RemoteException {
        return fetchPersonalObjectiveClient(remoteObject.getPersonalObjective(this.gameId, this.username));
    }

    @Override
    public boolean fetchVisibleCardsAndDecks() throws RemoteException {
        return fetchVisibleCardsAndDecksClient(remoteObject.getVisibleCards(this.gameId, this.username), remoteObject.getBackSideDecks(this.gameId, this.username));
    }

    @Override
    public boolean fetchScoreMap() throws RemoteException {
        return fetchScoreMapClient(remoteObject.getScoreMap(this.gameId, this.username));
    }

    @Override
    public boolean fetchPlayersResources() throws RemoteException {
        HashMap<String, HashMap<Resource, Integer>> playersResourcesMap = new HashMap<>();

        for(String player : players){
            HashMap<Resource, Integer> playerResources = remoteObject.getPlayerResources(this.gameId, this.username, player);

            if(playerResources == null){
                return false;
            }

            playersResourcesMap.put(player, playerResources);
        }
        return fetchPlayersResourcesClient(playersResourcesMap);
    }

    @Override
    public boolean fetchPlayersBoards() throws RemoteException {
        HashMap<String, HashMap<Coordinates, Integer>> playersBoardMap = new HashMap<>();

        for(String player : players){
            HashMap<Coordinates, Integer> playerBoardId = remoteObject.getBoard(this.gameId, this.username, player);

            if(playerBoardId == null){
                return false;
            }

            playersBoardMap.put(player, playerBoardId);
        }

        return fetchPlayersBoardsClient(playersBoardMap);
    }

    @Override
    public boolean fetchPlayersPlacingOrder() throws IOException {
        HashMap<String, ArrayList<Card>> placingOrderMap = new HashMap<>();

        for(String player : players){
            Deque<Integer> placingOrderId = remoteObject.getPlacingOrder(this.gameId, this.username, player);

            if(placingOrderId == null){
                return false;
            }
            ArrayList<Card> placingOrder = new ArrayList<>();

            for(int id : placingOrderId){
                placingOrder.add(Game.getCardByID(id));
            }
            placingOrderMap.put(player, placingOrder);
        }

        return fetchPlayersPlacingOrderClient(placingOrderMap);
    }

    @Override
    public boolean fetchValidPlacements() throws RemoteException {
        return fetchValidPlacementsClient(remoteObject.getValidPlacements(this.gameId, this.username));
    }

    @Override
    public boolean fetchClientHand() throws RemoteException {
        return fetchClientHandClient(remoteObject.getHand(this.gameId, this.username));
    }

    @Override
    public boolean fetchOpponentsHandColor() throws RemoteException {
        for(String player : players){
            if(player.equals(this.username)){
                continue;
            }

            ArrayList<Resource> handColor = remoteObject.getHandColor(this.gameId, this.username, player);
            if(handColor == null){
                return false;
            }

            ((OpponentData) playerData.get(player)).setHandColor(handColor);
        }
        return true;
    }

    @Override
    public boolean fetchStartingObjectives() throws RemoteException {
        return fetchStartingObjectivesClient(remoteObject.getStartingObjectives(this.gameId, this.username));
    }

    @Override
    public boolean fetchStartingCard() throws RemoteException {
        return fetchStartingCardClient(remoteObject.getStartingCard(this.gameId, this.username));
    }

    @Override
    public boolean fetchChat() throws RemoteException {
        this.chat = remoteObject.fetchChat(this.gameId);
        return this.chat != null;
    }

    /**
     * Waits for an update from the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public WaitState waitUpdate() throws RemoteException {
        return remoteObject.wait(this.gameId, this.username);
    }

    /**
     * Posts a chat message on the server.
     * @param message The chat message to post.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void postChat(String message) throws RemoteException{
        remoteObject.postChat(this.gameId, username, message);
    }


}