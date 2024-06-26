package it.polimi.ingsw.network.rmi;


import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;
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
 * Extends the Client class, which contains the general specifications of a Client.
 * This class connects to the RMI server and provides methods for interacting with the server,
 * such as drawing and placing cards, joining games, and performing other game-related operations.
 *
 * @see Client
 * @see RmiServerInterface
 */

public class RmiClient extends Client implements ClientInterface {

    private final RmiServerInterface remoteObject;

    /**
     * Constructs a new RmiClient object with the specified server address, and server port.
     * Connects to the RMI server at the given address and port, and initializes the client.
     *
     * @param serverAddress The address of the server.
     * @param serverPort    The port of the server.
     * @throws RuntimeException If an error occurs while connecting to the server.
     */
    public RmiClient(String serverAddress, int serverPort) {
        super(serverAddress, serverPort);

        RmiServerInterface tmp = null;
        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            tmp = (RmiServerInterface) registry.lookup(RmiServer.getName());
        } catch (RemoteException | NotBoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } finally {
            remoteObject = tmp;
        }
    }

    /**
     * Requests to create a new game with the specified number of players.
     * If the game is successfully created, the client automatically call the joinGame method.
     *
     * @param players The number of players for the new game.
     * @return The UUID of the newly created game.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public UUID newGame(int players, String gameName) throws RemoteException {
        UUID game = remoteObject.newGame(players, gameName);
        if (game != null) {
            if (!joinGame(game)) {
                System.out.println("You can't join a game while in another game!");
                System.exit(0);
            }
        }
        return game;
    }

    /**
     * Requests to join a game with the specified UUID.
     * Checks if the user has already logged in, if not it will return false and offer a prompt.
     * If the client successfully joins the game, it will start a new thread to send heartbeats to the server.
     *
     * @param game The UUID of the game to join.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public boolean joinGame(UUID game) throws RemoteException {
        if (username == null && password == null) {
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
    public void pingServer() throws IOException {
        remoteObject.ping(this.gameId, this.username);
    }

    @Override
    public boolean checkCredentials(String username, String password) throws RemoteException {
        if (remoteObject.checkCredentials(username, password)) {
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(GameConsts.heartbeatInterval);
                        pingServer();
                    } catch (IOException | InterruptedException e) {
                        System.out.println(e.getMessage());
                        System.exit(1);
                    }
                }
            }).start();
            
            this.username = username;
            this.password = password;
            return true;
        }
        return false;
    }

    /**
     * Draws a card from the server from the deck or the visible card specified
     * by the position and adds it to the client's hand.
     *
     * @param position The index of the deck to draw from.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void drawCard(int position) throws RemoteException {
        drawCardClient(remoteObject.drawCard(this.gameId, username, position));
    }

    /**
     * Places a card from the client's hand onto the board at the specified coordinates.
     *
     * @param coordinates The coordinates on the board where the card will be placed.
     * @param cardId      The ID of the card to be placed.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public boolean placeCard(Coordinates coordinates, int cardId) throws RemoteException {
        return placeCardClient(remoteObject.placeCard(this.gameId, username, coordinates, cardId), cardId);
    }

    @Override
    public boolean placeStartingCard(boolean frontSideUp) throws RemoteException {
        return placeStartingCardClient(frontSideUp, remoteObject.setStartingCard(gameId, username, frontSideUp));
    }

    @Override
    public boolean choosePersonalObjective(int objectiveId) throws RemoteException {
        return choosePersonalObjectiveClient(objectiveId, remoteObject.choosePersonalObjective(this.gameId, username, objectiveId));
    }

    public boolean choosePlayerColor(Color playerColor) throws RemoteException {
        return choosePlayerColorClient(playerColor, remoteObject.choosePlayerColor(this.gameId, username, playerColor));
    }

    public boolean fetchPlayersColors() throws RemoteException {
        for (String player : players) {


            Color playerColor = remoteObject.getPlayerColor(this.gameId, this.username, player);
            if (playerColor == null) {
                return false;
            }

            if (player.equals(this.username)) {
                ((PlayerData) playerData.get(player)).setPlayerColor(playerColor);
            } else {
                ((OpponentData) playerData.get(player)).setPlayerColor(playerColor);
            }
        }
        return true;
    }

    public boolean fetchAvailableColors() throws RemoteException {
        return fetchAvailableColorsClient(remoteObject.getAvailableColors(this.gameId, this.username));
    }

    @Override
    public boolean fetchAvailableGames() throws RemoteException {
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

        for (String player : players) {
            HashMap<Resource, Integer> playerResources = remoteObject.getPlayerResources(this.gameId, this.username, player);

            if (playerResources == null) {
                return false;
            }

            playersResourcesMap.put(player, playerResources);
        }
        return fetchPlayersResourcesClient(playersResourcesMap);
    }

    @Override
    public boolean fetchPlayersBoards() throws RemoteException {
        HashMap<String, HashMap<Coordinates, Integer>> playersBoardMap = new HashMap<>();

        for (String player : players) {
            HashMap<Coordinates, Integer> playerBoardId = remoteObject.getBoard(this.gameId, this.username, player);

            if (playerBoardId == null) {
                return false;
            }

            playersBoardMap.put(player, playerBoardId);
        }

        return fetchPlayersBoardsClient(playersBoardMap);
    }

    @Override
    public boolean fetchPlayersPlacingOrder() throws IOException {
        HashMap<String, ArrayList<Card>> placingOrderMap = new HashMap<>();

        for (String player : players) {
            Deque<Integer> placingOrderId = remoteObject.getPlacingOrder(this.gameId, this.username, player);

            if (placingOrderId == null) {
                return false;
            }
            ArrayList<Card> placingOrder = new ArrayList<>();

            for (int id : placingOrderId) {
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
        for (String player : players) {
            if (player.equals(this.username)) {
                continue;
            }

            ArrayList<Resource> handColor = remoteObject.getHandColor(this.gameId, this.username, player);
            if (handColor == null) {
                return false;
            }

            ((OpponentData) playerData.get(player)).setHandColor(handColor);
        }
        return true;
    }

    @Override
    public boolean fetchOpponentsHandType() throws IOException {
        for (String player : players) {
            if (player.equals(this.username)) {
                continue;
            }

            ArrayList<Boolean> isGold = remoteObject.getHandType(this.gameId, this.username, player);
            if (isGold == null) {
                return false;
            }

            ((OpponentData) playerData.get(player)).setHandIsGold(isGold);
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
        this.chat = remoteObject.fetchChat(this.gameId, this.username);
        return this.chat != null;
    }

    @Override
    public void fetchTurnPlayer() throws RemoteException {
        this.turnPlayerName = remoteObject.getTurnPlayer(this.gameId, this.username);
    }

    /**
     * Waits for an update from the server.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public WaitState waitUpdate() throws RemoteException {
        return remoteObject.wait(this.gameId, this.username);
    }

    /**
     * Posts a chat message on the server.
     *
     * @param message The chat message to post.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void postChat(String message, String receiver) throws RemoteException {
        remoteObject.postChat(this.gameId, username, message, receiver);
    }
}