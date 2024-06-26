package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.network.messages.requests.*;
import it.polimi.ingsw.network.messages.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.UUID;

/**
 * The SocketClient class represents a client that uses sockets for network communication.
 * It extends the Client class and manages a socket connection with a server,
 * allowing the client to create games, join games, and reconnect to games.
 */
public class SocketClient extends Client implements ClientInterface {

    private Socket client;
    private Socket waitUpdateSocket;
    private Socket heartbeatSocket;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private ObjectInputStream waitInput;
    private ObjectOutputStream waitOutput;

    private ObjectInputStream heartbeatInput;
    private ObjectOutputStream heartbeatOutput;

    //the following are attributes used as tmp values to store the response from the server ONLY!
    private WaitState waitState;
    private HashMap<String, HashMap<Coordinates, Integer>> playersBoardMap;
    private HashMap<String, ArrayList<Card>> placingOrderMap;
    private HashMap<String, HashMap<Resource, Integer>> playersResourcesMap;
    private ArrayList<Integer> visibleCardsIds;
    private ArrayList<Integer> visibleDecksIds;

    /**
     * Constructor for SocketClient.
     *
     * @param serverAddress The address of the server.
     * @param serverPort    The port of the server.
     */
    public SocketClient(String serverAddress, int serverPort) {
        super(serverAddress, serverPort);

        if (startConnection(serverAddress, serverPort)) {
            System.out.println("Connected to the socket server.");
        } else {
            System.out.println("Error while connecting to the socket server.");
            System.exit(1);
        }
    }

    /**
     * Establishes a connection with the server.
     *
     * @param serverAddress The address of the server.
     * @param serverPort    The port of the server.
     */
    public synchronized boolean startConnection(String serverAddress, int serverPort) {
        try {
            client = new Socket(serverAddress, serverPort);
            waitUpdateSocket = new Socket(serverAddress, serverPort);
            heartbeatSocket = new Socket(serverAddress, serverPort);

            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());

            waitOutput = new ObjectOutputStream(waitUpdateSocket.getOutputStream());
            waitInput = new ObjectInputStream(waitUpdateSocket.getInputStream());

            heartbeatOutput = new ObjectOutputStream(heartbeatSocket.getOutputStream());
            heartbeatInput = new ObjectInputStream(heartbeatSocket.getInputStream());

            client.setTcpNoDelay(true);
            waitUpdateSocket.setTcpNoDelay(true);

            return true;
        } catch (IOException e) {
            System.out.println("Error with the connection:" + e.getMessage());
            return false;
        }
    }

    /**
     * Stops the connection with the server.
     *
     * @throws IOException If an I/O error occurs while closing the connection.
     */
    public synchronized void stopConnection() throws IOException {
        output.writeObject(new SocketClientCloseConnection(username));
        input.close();
        output.close();
        waitInput.close();
        waitOutput.close();
        client.close();
        waitUpdateSocket.close();
    }

    /**
     * Sends the server the message to create a game.
     *
     * @param numPlayers The number of players in the game.
     * @return The UUID of the game created.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public synchronized UUID newGame(int numPlayers, String gameName) throws IOException {
        output.writeObject(new SocketClientCreateGameMessage(numPlayers, gameName));
        if (!handleResponse()) {
            throw new IOException("Error while creating the game.");
        }
        return gameId;
    }

    /**
     * Sends the server the message to join a game.
     *
     * @param gameUUID The UUID of the game to join.
     * @return true if the player joined the game, false otherwise.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public synchronized boolean joinGame(UUID gameUUID) throws IOException {
        if (username == null && password == null) {
            System.out.println("Please login first");
            return false;
        }
        output.writeObject(new SocketClientJoinGameMessage(username, gameUUID));
        boolean response = handleResponse();

        if (response) {
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(GameConsts.heartbeatInterval);
                        pingServer();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        return response;
    }

    @Override
    public synchronized void pingServer() throws IOException {
        heartbeatOutput.writeObject(new SocketClientHeartbeatMessage(username, gameId));
        try {
            Thread.sleep(200);
            GenericResponseMessage message = (GenericResponseMessage) heartbeatInput.readObject();
        } catch (ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WaitState waitUpdate() throws IOException {
        waitOutput.writeObject(new SocketClientWaitUpdateMessage(username, gameId));
        WaitUpdateResponseMessage message;
        do {
            try {
                message = (WaitUpdateResponseMessage) waitInput.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } while (message == null);

        return message.getWaitState();
    }

    /**
     * Checks the validity of player credentials.
     *
     * @param username The username of the player.
     * @param password The password of the player.
     * @return true if the credentials are valid, false otherwise.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public synchronized boolean checkCredentials(String username, String password) throws IOException {
        output.writeObject(new SocketValidateCredentialsMessage(username, password));
        return handleResponse();
    }

    //TODO: not implemented -> there is no socket message for this
    @Override
    public synchronized void postChat(String message, String receiver) throws IOException {
        output.writeObject(new SocketClientPostChatMessage(username, gameId, message, receiver));
    }

    /**
     * Sends a message to draw a card from a specified position.
     *
     * @param position The position of the card to draw.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public synchronized void drawCard(int position) throws IOException {
        output.writeObject(new SocketClientDrawCardMessage(username, position, gameId));
        handleResponse();
    }

    @Override
    public synchronized boolean placeCard(Coordinates coordinates, int cardId) throws IOException {
        output.writeObject(new SocketClientPlaceCardMessage(username, coordinates, cardId, gameId));
        return placeCardClient(handleResponse(), cardId);
    }

    @Override
    public synchronized boolean placeStartingCard(boolean frontSideUp) throws IOException {
        output.writeObject(new SocketClientPlaceStartingCardMessage(username, frontSideUp, gameId));
        return placeStartingCardClient(frontSideUp, handleResponse());
    }

    @Override
    public synchronized boolean choosePersonalObjective(int objectiveId) throws IOException {
        output.writeObject(new SocketClientChoosePersonalObjectiveMessage(username, objectiveId, gameId));
        return choosePersonalObjectiveClient(objectiveId, handleResponse());
    }

    @Override
    public synchronized boolean choosePlayerColor(Color playerColor) throws IOException {
        output.writeObject(new SocketClientChoosePlayerColorMessage(username, playerColor, gameId));
        return choosePlayerColorClient(playerColor, handleResponse());
    }

    @Override
    public synchronized boolean fetchAvailableColors() throws IOException {
        output.writeObject(new SocketClientGetAvailableColorsMessage(username, gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchAvailableGames() throws IOException {
        output.writeObject(new SocketClientFetchAvailableGamesMessage(username));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchGameState() throws IOException {
        output.writeObject(new SocketClientFetchGameStateMessage(username, gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchPlayers() throws IOException {
        output.writeObject(new SocketClientGetPlayersMessage(username, gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchCommonObjectives() throws IOException {
        output.writeObject(new SocketClientGetCommonObjectivesMessage(username, gameId));
        return handleResponse();
    }

    @Override
    public boolean fetchPersonalObjective() throws IOException {
        output.writeObject(new SocketClientFetchPersonalObjectiveMessage(username, gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchStartingCard() throws IOException {
        output.writeObject(new SocketClientGetStartingCardMessage(username, gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchChat() throws IOException {
        output.writeObject(new SocketClientFetchChatMessage(username, gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchStartingObjectives() throws IOException {
        output.writeObject(new SocketClientGetStartingObjectivesMessage(username, gameId));
        return handleResponse();
    }

    /**
     * Sends a message to get all the opponents hand color.
     *
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public synchronized boolean fetchOpponentsHandColor() throws IOException {
        for (String player : players) {
            if (player.equals(username)) {
                continue;
            }

            output.writeObject(new SocketClientGetHandColorMessage(username, this.gameId, player));

            if (!handleResponse()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized boolean fetchOpponentsHandType() throws IOException {
        for (String player : players) {
            if (player.equals(username)) {
                continue;
            }

            output.writeObject(new SocketClientGetHandTypeMessage(username, this.gameId, player));

            if (!handleResponse()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized boolean fetchPlayersColors() throws IOException {
        for (String player : players) {
            if (player.equals(username)) {
                continue;
            }

            output.writeObject(new SocketClientGetPlayerColorMessage(username, this.gameId, player));

            if (!handleResponse()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sends a message to get the Player hand.
     *
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public synchronized boolean fetchClientHand() throws IOException {
        output.writeObject(new SocketClientGetHandMessage(username, this.gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchValidPlacements() throws IOException {
        output.writeObject(new SocketClientGetValidPlacementsMessage(username, this.gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchPlayersBoards() throws IOException {
        playersBoardMap = new HashMap<>();
        for (String player : players) {
            output.writeObject(new SocketClientGetPlayerBoard(username, this.gameId, player));

            if (!handleResponse()) {
                return false;
            }
        }
        return fetchPlayersBoardsClient(playersBoardMap);
    }

    @Override
    public synchronized boolean fetchPlayersPlacingOrder() throws IOException {
        placingOrderMap = new HashMap<>();
        for (String player : players) {
            output.writeObject(new SocketClientGetPlacingOrderMessage(username, this.gameId, player));

            if (!handleResponse()) {
                return false;
            }
        }
        return fetchPlayersPlacingOrderClient(placingOrderMap);
    }

    @Override
    public synchronized boolean fetchPlayersResources() throws IOException {
        playersResourcesMap = new HashMap<>();
        for (String player : players) {
            output.writeObject(new SocketClientGetPlayerResourcesMessage(username, this.gameId, player));

            if (!handleResponse()) {
                return false;
            }
        }
        return fetchPlayersResourcesClient(playersResourcesMap);
    }

    @Override
    public synchronized boolean fetchScoreMap() throws IOException {
        output.writeObject(new SocketClientGetScoreMapMessage(username, this.gameId));
        return handleResponse();
    }

    @Override
    public synchronized boolean fetchVisibleCardsAndDecks() throws IOException {

        output.writeObject(new SocketClientGetVisibleCardsMessage(username, this.gameId));
        if (!handleResponse()) {
            return false;
        }

        output.writeObject(new SocketClientGetBackSideDecksMessage(username, this.gameId));
        if (!handleResponse()) {
            return false;
        }
        return fetchVisibleCardsAndDecksClient(visibleCardsIds, visibleDecksIds);
    }

    @Override
    public void fetchTurnPlayer() throws IOException {
        this.turnPlayerName = "";
        //TODO implement socket version
    }

    /**
     * Handles the server's response message.
     *
     * @return true if the response was successfully handled, false otherwise.
     * @throws IOException if an I/O error occurs.
     */
    public synchronized boolean handleResponse() throws IOException {
        GenericResponseMessage response;
        output.flush();

        do {
            try {
                response = (GenericResponseMessage) input.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Error while reading the response from the server.");
                throw new RuntimeException(e);
            }
        } while (response == null);


        switch (response) {

            //Create Game + call to join.
            case CreateGameResponseMessage createGameResponseMessage -> {
                UUID id = createGameResponseMessage.getGameUUID();
                if (id == null) {
                    return false;
                }
                if (!joinGame(id)) {
                    System.out.println("You can't join a game while in another game!");
                    System.exit(0);
                }
                return true;
            }

            //join game and set the gameId.
            case JoinGameResponseMessage joinGameResponseMessage -> {
                if (joinGameResponseMessage.isJoinedGame()) {
                    setGameId(joinGameResponseMessage.getGameUUID());
                }
                return joinGameResponseMessage.isJoinedGame();
            }

            //checkCredentials
            case ValidateCredentialsResponseMessage validateCredentialsResponseMessage -> {
                if (validateCredentialsResponseMessage.isValid()) {
                    this.username = validateCredentialsResponseMessage.getUsername();
                    this.password = validateCredentialsResponseMessage.getPassword();
                    return true;
                }
                return false;
            }

            //wait
            case WaitUpdateResponseMessage waitUpdateResponseMessage -> {
                waitState = waitUpdateResponseMessage.getWaitState();
                return waitState != null;
            }

            //drawCard
            case DrawCardResponseMessage drawCardResponseMessage -> {
                return drawCardClient(drawCardResponseMessage.getCardID());
            }

            //placeCard
            case PlaceCardResponseMessage placeCardResponseMessage -> {
                return placeCardResponseMessage.isPlaced();
            }

            //placeStartingCard
            case PlaceStartingCardResponseMessage placeStartingCardResponseMessage -> {
                return placeStartingCardResponseMessage.isPlaced();
            }

            //choosePersonalObjective
            case ChoosePersonalObjectiveResponseMessage choosePersonalObjectiveResponseMessage -> {
                return choosePersonalObjectiveResponseMessage.isCorrect();
            }

            //fetchAvailableGames
            case FetchAvailableGamesResponseMessage fetchAvailableGamesResponseMessage -> {
                return fetchAvailableGamesClient(fetchAvailableGamesResponseMessage.getAvailableGames());
            }

            //fetchGameState
            case FetchGameStateResponseMessage fetchGameStateResponseMessage -> {
                return fetchGameStateClient(fetchGameStateResponseMessage.getGameState());
            }

            //fetchPlayers
            case GetPlayersResponseMessage getPlayersResponseMessage -> {
                return fetchPlayersClient(getPlayersResponseMessage.getPlayers());
            }

            //fetchCommonObjectives
            case GetCommonObjectivesResponseMessage getCommonObjectivesResponseMessage -> {
                return fetchCommonObjectivesClient(getCommonObjectivesResponseMessage.getCommonObjectives());
            }

            //fetchCommonObjectives
            case FetchPersonalObjectiveResponseMessage fetchPersonalObjectiveResponseMessage -> {
                return fetchPersonalObjectiveClient(fetchPersonalObjectiveResponseMessage.getPersonalObjective());
            }

            //fetchStartingCard
            case GetStartingCardResponseMessage getStartingCardResponseMessage -> {
                return fetchStartingCardClient(getStartingCardResponseMessage.getStartingCardId());
            }

            //fetchStartingObjectives
            case GetStartingObjectivesResponseMessage getStartingObjectivesResponseMessage -> {
                return fetchStartingObjectivesClient(getStartingObjectivesResponseMessage.getStartingObjectives());
            }

            //fetchOpponentsHandColor
            case GetHandColorResponseMessage getHandColorResponseMessage -> {
                String player = getHandColorResponseMessage.getUsernameRequiredData();
                ArrayList<Resource> handColor = getHandColorResponseMessage.getHandColor();
                if (handColor == null) {
                    return false;
                }

                ((OpponentData) playerData.get(player)).setHandColor(handColor);

                return true;
            }

            //fetchOpponentsHandType
            case GetHandTypeResponseMessage getHandTypeResponseMessage -> {
                String player = getHandTypeResponseMessage.getUsernameRequiredData();
                ArrayList<Boolean> isGold = getHandTypeResponseMessage.getIsGold();
                if (isGold == null) {
                    return false;
                }

                ((OpponentData) playerData.get(player)).setHandIsGold(isGold);

                return true;
            }

            case GetPlayerColorResponseMessage getPlayerColorResponseMessage -> {
                String player = getPlayerColorResponseMessage.getUsernameRequiredData();
                Color playerColor = getPlayerColorResponseMessage.getPlayerColor();
                if (playerColor == null) {
                    return false;
                }

                ((OpponentData) playerData.get(player)).setPlayerColor(playerColor);

                return true;
            }

            case GetAvailableColorsResponseMessage getAvailableColorsResponseMessage -> {
                return fetchAvailableColorsClient(getAvailableColorsResponseMessage.getAvailableColors());
            }

            case ChoosePlayerColorResponseMessage choosePlayerColorResponseMessage -> {
                return choosePlayerColorResponseMessage.isCorrect();
            }

            //fetchClientHand
            case GetHandResponseMessage getHandResponseMessage -> {
                return fetchClientHandClient(getHandResponseMessage.getHandIds());
            }

            //fetchValidPlacements
            case GetValidPlacementsResponseMessage getValidPlacementsResponseMessage -> {
                return fetchValidPlacementsClient(getValidPlacementsResponseMessage.getValidPlacements());
            }

            //fetchPlayersBoards
            case GetPlayerBoardResponseMessage getPlayerBoardResponseMessage -> {
                HashMap<Coordinates, Integer> playerBoardId = getPlayerBoardResponseMessage.getPlayerBoard();

                if (playerBoardId == null) {
                    return false;
                }
                playersBoardMap.put(getPlayerBoardResponseMessage.getUsernameRequiredData(), playerBoardId);
                return true;
            }

            //fetchPlayersPlacingOrder
            case GetPlacingOrderResponseMessage getPlacingOrderResponseMessage -> {
                Deque<Integer> placingOrderId = getPlacingOrderResponseMessage.getPlacingOrder();
                if (placingOrderId == null) {
                    return false;
                }
                ArrayList<Card> placingOrder = new ArrayList<>();

                for (int id : placingOrderId) {
                    placingOrder.add(Game.getCardByID(id));
                }
                placingOrderMap.put(getPlacingOrderResponseMessage.getUsernameRequiredData(), placingOrder);
                return true;
            }

            //fetchPlayersResources
            case GetPlayerResourcesResponseMessage getPlayerResourcesResponseMessage -> {
                HashMap<Resource, Integer> playerResources = getPlayerResourcesResponseMessage.getPlayerResources();
                if (playerResources == null) {
                    return false;
                }
                playersResourcesMap.put(getPlayerResourcesResponseMessage.getUsernameRequiredData(), playerResources);
                return true;
            }

            //fetchScoreMap
            case GetScoreMapResponseMessage getScoreMapResponseMessage -> {
                return fetchScoreMapClient(getScoreMapResponseMessage.getScoreMap());
            }

            //fetchVisibleCards
            case GetVisibleCardsResponseMessage getVisibleCardsResponseMessage -> {
                visibleCardsIds = getVisibleCardsResponseMessage.getVisibleCards();
                return visibleCardsIds != null;
            }

            case GetBackSideDecksResponseMessage getBackSideDecksResponseMessage -> {
                visibleDecksIds = getBackSideDecksResponseMessage.getBackSideDecks();
                return visibleDecksIds != null;
            }

            case FetchChatResponseMessage fetchChatResponseMessage -> {
                this.chat = fetchChatResponseMessage.getChat();
                return this.chat != null;
            }

            default -> {
                throw new RuntimeException("Error while handling the response from the server.");
            }
        }
    }

}
