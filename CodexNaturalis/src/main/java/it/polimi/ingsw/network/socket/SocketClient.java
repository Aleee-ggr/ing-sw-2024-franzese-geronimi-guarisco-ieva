package it.polimi.ingsw.network.socket;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.network.messages.requests.*;
import it.polimi.ingsw.network.messages.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

//TODO: lots of methods are not implemented

/**
 * The SocketClient class represents a client that uses sockets for network communication.
 * It extends the Client class and manages a socket connection with a server,
 * allowing the client to create games, join games, and reconnect to games.
 */
public class SocketClient extends Client implements ClientInterface {

    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Constructor for SocketClient.
     * @param serverAddress The address of the server.
     * @param serverPort The port of the server.
     */
    public SocketClient(String serverAddress, int serverPort) {
        super(serverAddress, serverPort);
        startConnection(serverAddress, serverPort);
    }

    /**
     * Establishes a connection with the server.
     * @param serverAddress The address of the server.
     * @param serverPort The port of the server.
     */
    public boolean startConnection(String serverAddress, int serverPort) {
        try {
            client = new Socket(serverAddress, serverPort);
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());

            return true;
        } catch (IOException e){
            System.out.println("Error with the connection:" + e.getMessage());
            return false;
        }
    }

    /**
     * Stops the connection with the server.
     * @throws IOException If an I/O error occurs while closing the connection.
     */
    public void stopConnection() throws IOException{
        output.writeObject(new SocketClientCloseConnection(username));
        input.close();
        output.close();
        client.close();
    }

    /**
     * Handles the server's response message.
     */
    public boolean handleResponse() throws IOException { //TODO: exception handling
        GenericResponseMessage response;

        do {
            try {
                response = (GenericResponseMessage) input.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Error while reading the response from the server.");
                throw new RuntimeException(e);
            }
        } while(response == null);


        if (response instanceof CreateGameResponseMessage) {
            UUID id = ((CreateGameResponseMessage) response).getGameUUID();
            if(id == null){
                return false;
            }
            setGameId(((CreateGameResponseMessage) response).getGameUUID());
            return true;
        }

        //Sets the opponent HandColor.
        if (response instanceof GetHandColorResponseMessage) {
            OpponentData player = (OpponentData) playerData.get(((GetHandColorResponseMessage) response).getUsernameRequiredData());
            if(player == null){
                return false;
            }
            player.setHandColor(((GetHandColorResponseMessage) response).getHandColor());
            return true;
        }

        //Sets the player (client) hand of cards.
        if (response instanceof GetHandResponseMessage) {
            ArrayList<Integer> handIds = ((GetHandResponseMessage) response).getHandIds();
            ArrayList<Card> hand = new ArrayList<>();

            for(int id : handIds){
                hand.add(Game.getCardByID(id));
            }

            ((PlayerData)playerData.get(username)).setClientHand(hand);
        }

        //Sets the Board of a specified player.
        if (response instanceof GetPlayerBoardResponseMessage) {
            HashMap<Coordinates, Integer> playersBoardMap = ((GetPlayerBoardResponseMessage) response).getPlayerBoard();
            BiMap<Coordinates, Card> playerBoard = HashBiMap.create();
            String playerUsername = ((GetPlayerBoardResponseMessage) response).getUsernameRequiredData();

            for(Coordinates c : playersBoardMap.keySet()){
                playerBoard.put(c, Game.getCardByID(playersBoardMap.get(c)));
            }

            playerData.get(playerUsername).setBoard(playerBoard);
        }

        //Sets the player resources.
        if (response instanceof GetPlayerResourcesResponseMessage) {
            String playerUsername = ((GetPlayerResourcesResponseMessage) response).getUsernameRequiredData();
            playerData.get(playerUsername).setResources(((GetPlayerResourcesResponseMessage) response).getPlayerResources());
        }

        //Sets the valid placements Coordinates of the Player.
        if (response instanceof GetValidPlacementsResponseMessage) {
            ((PlayerData)playerData.get(username)).setValidPlacements(((GetValidPlacementsResponseMessage) response).getValidPlacements());
        }

        if (response instanceof JoinGameResponseMessage) {
            if (((JoinGameResponseMessage) response).isJoinedGame()) {
                setGameId(((JoinGameResponseMessage) response).getGameUUID());
            }
        }
        return false;
    }

    /**
     * Sends the server the message to create a game.
     * @param numPlayers The number of players in the game.
     * @throws IOException If an I/O error occurs while sending the message.
     * @return The UUID of the game created.
     */
    @Override
    public UUID newGame(int numPlayers) throws IOException {
        output.writeObject(new SocketClientCreateGameMessage(numPlayers));
        if(!handleResponse()){
            throw new IOException("Error while creating the game.");
        }
        return gameId;
    }

    /**
     * Sends the server the message to join a game.
     * @param gameUUID The UUID of the game to join.
     * @throws IOException If an I/O error occurs while sending the message.
     * @return true if the player joined the game, false otherwise.
     */
    @Override
    public boolean joinGame(UUID gameUUID) throws IOException {
        output.writeObject(new SocketClientJoinGameMessage(username, gameUUID));
        return handleResponse();
    }


    //TODO: there isn't any response message for checking credentials
    /**
     * Checks the validity of player credentials.
     * @param username The username of the player.
     * @param password The password of the player.
     * @throws IOException If an I/O error occurs while sending the message.
     * @return true if the credentials are valid, false otherwise.
     */
    @Override
    public boolean checkCredentials(String username, String password) throws IOException {
        output.writeObject(new SocketValidateCredentialsMessage(username, password));
        return handleResponse();
    }

    //TODO: not implemented in handleResponse();
    @Override
    public void waitUpdate() throws IOException {
        output.writeObject(new SocketClientWaitUpdateMessage(username, gameId));
        handleResponse();
    }

    //TODO: not implemented -> there is no socket message for this
    @Override
    public void postChat(String message) throws IOException {

    }

    //TODO: not implemented in handleResponse();
    /**
     * Sends a message to draw a card from a specified position.
     * @param position The position of the card to draw.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public void drawCard(int position) throws IOException {
        output.writeObject(new SocketClientDrawCardMessage(username, position, gameId));
        handleResponse();
    }

    //TODO: no implementation
    @Override
    public boolean placeCard(Coordinates coordinates, int CardId) throws IOException {
        return false;
    }

    //TODO: no implementation
    @Override
    public boolean placeStartingCard(boolean frontSideUp) throws IOException {
        return false;
    }

    //TODO: no implementation
    @Override
    public boolean choosePersonalObjective(int objectiveId) throws IOException {
        return false;
    }

    //TODO: not implemented -> there is no socket message for this
    @Override
    public boolean fetchAvailableGames() throws IOException {
        return false;
    }

    //TODO: not implemented -> there is no socket message for this
    @Override
    public boolean fetchGameState() throws IOException {
        return false;
    }

    //TODO: not implemented in handleResponse();
    /**
     * Sends a request message to the server to retrieve the list of players in the game.
     * @throws IOException if there is an error during communication with the server.
     */
    @Override
    public boolean fetchPlayers() throws IOException {
        output.writeObject(new SocketClientGetPlayersMessage(username, gameId));
        return handleResponse();
    }

    //TODO: not implemented in handleResponse();
    @Override
    public boolean fetchCommonObjectives() throws IOException {
        output.writeObject(new SocketClientGetCommonObjectivesMessage(username, gameId));
        return handleResponse();
    }

    //TODO: not implemented in handleResponse();
    @Override
    public boolean fetchStartingCard() throws IOException {
        output.writeObject(new SocketClientGetStartingCardMessage(username, gameId));
        return handleResponse();
    }

    //TODO: no implementation
    @Override
    public boolean fetchStartingObjectives() throws IOException {
        return false;
    }

    /**
     * Sends a message to get all the opponents hand color.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public boolean fetchOpponentsHandColor() throws IOException {
        for(String player : players){
            if(player.equals(username)){
                continue;
            }

            output.writeObject(new SocketClientGetHandColorMessage(username, this.gameId, player));

            if(!handleResponse()){
                return false;
            }
        }
        return true;
    }

    /**
     * Sends a message to get the Player hand.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public boolean fetchClientHand() throws IOException {
        output.writeObject(new SocketClientGetHandMessage(username, this.gameId));
        return handleResponse();
    }

    @Override
    public boolean fetchValidPlacements() throws IOException {
        output.writeObject(new SocketClientGetValidPlacementsMessage(username, this.gameId));
        return handleResponse();
    }

    @Override
    public boolean fetchPlayersBoards() throws IOException {
        for(String player : players){
            output.writeObject(new SocketClientGetPlayerBoard(username, this.gameId, player));

            if(!handleResponse()){
                return false;
            }
        }
        return true;
    }

    //TODO: not implemented
    @Override
    public boolean fetchPlayersPlacingOrder() throws RemoteException {
        return false;
    }

    @Override
    public boolean fetchPlayersResources() throws IOException {
        for(String player : players){
            output.writeObject(new SocketClientGetPlayerResourcesMessage(username, this.gameId, player));

            if(!handleResponse()){
                return false;
            }
        }
        return true;
    }

    //TODO: not implemented in handleResponse();
    /**
     * Sends a message to get the score map.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    @Override
    public boolean fetchScoreMap() throws IOException {
        output.writeObject(new SocketClientGetScoreMapMessage(username));
        return handleResponse();
    }

    //TODO: not implemented in handleResponse();
    @Override
    public boolean fetchVisibleCardsAndDecks() throws IOException {
        output.writeObject(new SocketClientGetVisibleCardsMessage(username, this.gameId));
        if(!handleResponse()){
            return false;
        }
        output.writeObject(new SocketClientGetBackSideDecksMessage(username, this.gameId));

        return handleResponse();
    }

}
