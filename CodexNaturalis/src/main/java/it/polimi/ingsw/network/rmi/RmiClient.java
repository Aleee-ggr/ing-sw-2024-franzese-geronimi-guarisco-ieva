package it.polimi.ingsw.network.rmi;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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
     * Draws a card from the server from the deck or the visible card specified
     * by the position and adds it to the client's hand.
     * @param position The index of the deck to draw from.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void drawCard(int position) throws RemoteException {
        Integer id = remoteObject.drawCard(this.gameId, username, position);

        if (id != null) {
            try {
                ((PlayerData) playerData.get(username)).addToHand(Game.getCardByID(id));
            } catch (HandFullException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean placeStartingCard(boolean frontSideUp) throws RemoteException{
        getPlayerData().setStartingCard((StartingCard) getPlayerData().getStartingCard().setFrontSideUp(frontSideUp));
        return remoteObject.setStartingCard(gameId, username, frontSideUp);
    }

    @Override
    public boolean choosePersonalObjective(int objectiveId) throws RemoteException {
        boolean success = remoteObject.choosePersonalObjective(this.gameId, username, objectiveId);
        if (success) {
            getPlayerData().setPersonalObjective(Game.getObjectiveByID(objectiveId));
        }
        return success;
    }

    /**
     * Places a card from the client's hand onto the board at the specified coordinates.
     * @param coordinates The coordinates on the board where the card will be placed.
     * @param cardId The ID of the card to be placed.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public boolean placeCard(Coordinates coordinates, int cardId) throws RemoteException {
        boolean success = remoteObject.placeCard(this.gameId, username, coordinates, cardId);
        if(success){
            try {
                ((PlayerData) playerData.get(username)).removeFromHand(Game.getCardByID(cardId));
            } catch (ElementNotInHand e) {
                throw new RuntimeException(e);
            }

            return true;
        }
        return false;
    }

    /**
     * Requests to create a new game with the specified number of players.
     * @param players The number of players for the new game.
     * @return The UUID of the newly created game.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public UUID newGame(int players) throws RemoteException {
        UUID game = null;
        game = remoteObject.newGame(players);
        joinGame(game);
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

    @Override
    public boolean fetchAvailableGames() throws RemoteException{
        this.availableGames = remoteObject.getAvailableGames(username);
        return availableGames != null && !availableGames.isEmpty();
    }

    @Override
    public boolean fetchGameState() throws RemoteException {
        GameState gameState = remoteObject.getGameState(this.gameId, this.username);
        if (gameState == null){
            return false;
        }
        this.gameState = gameState;
        return true;
    }

    @Override
    public boolean fetchPlayers() throws RemoteException {
        ArrayList<String> players = remoteObject.getPlayers(this.gameId, this.username);
        if (players == null){
            return false;
        }

        createPlayerData(players);
        return true;
    }

    @Override
    public boolean fetchCommonObjectives() throws RemoteException {
        ArrayList<Integer> commonObjectivesId = remoteObject.getCommonObjectives(this.gameId, this.username);

        if(commonObjectivesId == null) {
            return false;
        }

        ArrayList<Objective> commonObjectivesList = new ArrayList<>();

        for (Integer id : commonObjectivesId) {
            commonObjectivesList.add(Game.getObjectiveByID(id));
        }

        ((PlayerData) playerData.get(username)).setGlobalObjectives(commonObjectivesList);
        return true;
    }

    @Override
    public boolean fetchVisibleCardsAndDecks() throws RemoteException {
        ArrayList<Integer> visibleCards = remoteObject.getVisibleCards(this.gameId, this.username);
        ArrayList<Integer> backSideDecks = remoteObject.getBackSideDecks(this.gameId, this.username);

        if (visibleCards == null || backSideDecks == null) {
            return false;
        }

        ArrayList<Card> visibleCardsList = new ArrayList<>();
        ArrayList<Card> backSideDecksList = new ArrayList<>();

        for (Integer id : visibleCards) {
            visibleCardsList.add(Game.getCardByID(id));
        }
        for (Integer id : backSideDecks) {
            backSideDecksList.add(Game.getCardByID(id));
        }

        this.visibleCards = visibleCardsList;
        this.backSideDecks = backSideDecksList;
        return true;
    }

    @Override
    public boolean fetchScoreMap() throws RemoteException {
        HashMap<String, Integer> scoreMap = remoteObject.getScoreMap(this.gameId, this.username);

        if (scoreMap == null) {
            return false;
        }
        for (String player : players) {
            if (!scoreMap.containsKey(player)) {
                scoreMap.put(player, 0);
            }
        }

        this.scoreMap = scoreMap;
        return true;
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

        for(String player : players){
            playerData.get(player).setResources(playersResourcesMap.get(player));
        }

        return true;
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

        for(String player : players){
            BiMap<Coordinates, Card> playerBoard = HashBiMap.create();

            for(Coordinates c : playersBoardMap.get(player).keySet()){
                playerBoard.put(c, Game.getCardByID(playersBoardMap.get(player).get(c)));
            }

            playerData.get(player).setBoard(playerBoard);
        }

        return true;
    }

    @Override
    public boolean fetchPlayersPlacingOrder() throws RemoteException {
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

        for(String player : players){
            playerData.get(player).setOrder(placingOrderMap.get(player));
        }

        return true;
    }

    @Override
    public boolean fetchValidPlacements() throws RemoteException {
        ArrayList<Coordinates> validPlacements = remoteObject.getValidPlacements(this.gameId, this.username);

        if (validPlacements == null) {
            return false;
        }

        ((PlayerData) playerData.get(username)).setValidPlacements(validPlacements);
        return true;
    }

    @Override
    public boolean fetchClientHand() throws RemoteException {
        ArrayList<Integer> handIds = remoteObject.getHand(this.gameId, this.username);

        if(handIds == null){
            return false;
        }

        ArrayList<Card> hand = new ArrayList<>();

        for(int id : handIds){
            hand.add(Game.getCardByID(id));
        }

        ((PlayerData) playerData.get(username)).setClientHand(hand);
        return true;
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
        ArrayList<Integer> startingObjectives = remoteObject.getStartingObjectives(this.gameId, this.username);
        if(startingObjectives == null){
            return false;
        }

        ArrayList<Objective> startingObjectivesList = new ArrayList<>();
        for(int id : startingObjectives){
            startingObjectivesList.add(Game.getObjectiveByID(id));
        }
        System.out.println(username);
        System.out.println(playerData.keySet());
        getPlayerData().setStartingObjectives(startingObjectivesList);

        return true;
    }

    @Override
    public boolean fetchStartingCard() throws RemoteException {
        Integer startingCardId = remoteObject.getStartingCard(this.gameId, this.username);
        if(startingCardId == null){
            return false;
        }

        ((PlayerData)playerData.get(username)).setStartingCard((StartingCard) Game.getCardByID(startingCardId));
        return true;
    }

    /**
     * Waits for an update from the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void waitUpdate() throws RemoteException {
        remoteObject.wait(this.gameId, this.username);
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