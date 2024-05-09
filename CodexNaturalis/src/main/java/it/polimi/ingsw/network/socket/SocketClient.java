package it.polimi.ingsw.network.socket;


import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.messages.requests.*;
import it.polimi.ingsw.network.messages.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

//TODO: fix with new refactor

/**
 * The SocketClient class represents a client that uses sockets for network communication.
 * It extends the Client class and manages a socket connection with a server,
 * allowing the client to create games, join games, and reconnect to games.
 */
public class SocketClient extends Client implements Runnable {

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
        new Thread(this).start();
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
     * This method is the main run loop for the client, which listens for responses from the server.
     * It continuously waits for a response message from the server, handling each response with the handleResponse method.
     */
    public void run() {
        while (true) {
            try {
                GenericResponseMessage response = (GenericResponseMessage) input.readObject();
                handleResponse(response);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Connection with server lost");
                break;
            }
        }
    }

    /**
     * Stops the connection with the server.
     * @throws IOException If an I/O error occurs while closing the connection.
     */
    public void stopConnection() throws IOException{
        output.writeObject(new SocketClientCloseConnection(data.getUsername()));
        input.close();
        output.close();
        client.close();
    }

    /**
     * Handles the server's response message.
     * @param response The response message from the server.
     */
    public void handleResponse(GenericResponseMessage response) {
        if (response instanceof GetHandColorResponseMessage) {
            data.setPlayerHandColor(((GetHandColorResponseMessage) response).getUsernameRequiredData(), ((GetHandColorResponseMessage) response).getHandColor());
        }

        if (response instanceof GetHandResponseMessage) {
            data.setClientHand(((GetHandResponseMessage) response).getHandIds());
        }

        if (response instanceof GetPlayerBoardResponseMessage) {
            if (((GetPlayerBoardResponseMessage) response).getUsernameRequiredData().equals(data.getUsername())) {
                data.setClientBoard(((GetPlayerBoardResponseMessage) response).getPlayerBoard());
            } else {
                data.setPlayerBoard(((GetPlayerBoardResponseMessage) response).getUsernameRequiredData(), ((GetPlayerBoardResponseMessage) response).getPlayerBoard());
            }
        }

        if (response instanceof GetPlayerResourcesResponseMessage) {
            data.updatePlayerResources(((GetPlayerResourcesResponseMessage) response).getUsernameRequiredData(), ((GetPlayerResourcesResponseMessage) response).getPlayerResources());
        }

        if (response instanceof GetValidPlacementsResponseMessage) {
            data.setValidPlacements(((GetValidPlacementsResponseMessage) response).getValidPlacements());
        }

        if (response instanceof CreateGameResponseMessage) {
            setGameId(((CreateGameResponseMessage) response).getGameUUID());
        }

        if (response instanceof JoinGameResponseMessage) {
            if (((JoinGameResponseMessage) response).isJoinedGame()) {
                this.setGameId(((JoinGameResponseMessage) response).getGameUUID());
            }
        }
    }

    /**
     * Sends the server the message to create a game.
     * @param username The username of the player.
     * @param numPlayers The number of players in the game.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void createGame(String username, int numPlayers) throws IOException {
        output.writeObject(new SocketClientCreateGameMessage(username, numPlayers));
    }

    /**
     * Sends the server the message to join a game.
     * @param username The username of the player.
     * @param gameUUID The UUID of the game to join.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void joinGame(String username, UUID gameUUID) throws IOException {
        output.writeObject(new SocketClientJoinGameMessage(username, gameUUID));
    }

    /**
     * Sends the server the message to reconnect to a game.
     * @param username The username of the player.
     * @param gameUUID The UUID of the game to reconnect to.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void reconnect(String username,UUID gameUUID) throws IOException {
        output.writeObject(new SocketClientReconnectMessage(username, gameUUID));
    }

    /**
     * Checks the validity of player credentials.
     * @param username The username of the player.
     * @param password The password of the player.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void checkCredentials(String username, String password) throws IOException {
        output.writeObject(new SocketValidateCredentialsMessage(username, password));
    }

    /**
     * Sends a message to draw a card from a specified position.
     * @param position The position of the card to draw.
     * @param gameUUID The UUID of the game in which to draw the card.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void drawCard(int position, UUID gameUUID) throws IOException {
        output.writeObject(new SocketClientDrawCardMessage(data.getUsername(), position, gameUUID));
    }

    /**
     * Sends a message to get the score map.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getScoreMap() throws IOException {
        output.writeObject(new SocketClientGetScoreMapMessage(data.getUsername()));
    }

    /**
     * Sends a message to get the hand.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getHand() throws IOException {
        output.writeObject(new SocketClientGetHandMessage(data.getUsername(), this.gameId));
    }

    /**
     * Sends a message to get the common objectives.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getCommonObjectives() throws IOException {
        output.writeObject(new SocketClientGetCommonObjectivesMessage(data.getUsername(), this.gameId));
    }

    /**
     * Sends a message to get player resources.
     * @param username The username of the player whose resources to get.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getPlayerResources(String username) throws IOException {
        output.writeObject(new SocketClientGetPlayerResourcesMessage(data.getUsername(), this.gameId, username));
    }

    /**
     * Sends a message to get the visible cards.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getVisibleCards() throws IOException {
        output.writeObject(new SocketClientGetVisibleCardsMessage(data.getUsername(), this.gameId));
    }

    /**
     * Sends a message to get the back side decks.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getBackSideDecks() throws IOException {
        output.writeObject(new SocketClientGetBackSideDecksMessage(data.getUsername(), this.gameId));
    }

    /**
     * Sends a message to get the valid placements.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getValidPlacements() throws IOException {
        output.writeObject(new SocketClientGetValidPlacementsMessage(data.getUsername(), this.gameId));
    }

    /**
     * Sends a message to get the player's board.
     * @param username The username of the player whose board to get.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getPlayerBoard(String username) throws IOException {
        output.writeObject(new SocketClientGetPlayerBoard(data.getUsername(), this.gameId, username));
    }

    /**
     * Sends a message to get the player's hand color.
     * @param username The username of the player whose hand color to get.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void getHandColor(String username) throws IOException {
        output.writeObject(new SocketClientGetHandColorMessage(data.getUsername(), this.gameId, username));
    }

    /**
     * Sends a request message to the server to retrieve the list of players in the game.
     * @param username the username of the client making the request.
     * @throws IOException if there is an error during communication with the server.
     */
    public void getPlayers(String username) throws IOException {
        output.writeObject(new SocketClientGetPlayersMessage(data.getUsername(), this.gameId));
    }

    /**
     * Sends a request message to the server to retrieve the starting card of the player.
     * @param username the username of the client making the request.
     * @throws IOException if there is an error during communication with the server.
     */
    public void getStartingCard(String username) throws IOException {
        output.writeObject(new SocketClientGetStartingCardMessage(data.getUsername(), this.gameId));
    }

    /**
     * Sends a request message to the server to notify when it's this player's turn.
     * @param username the username of the client making the request.
     * @throws IOException if there is an error during communication with the server.
     */
    public void waitUpdate(String username) throws IOException {
        output.writeObject(new SocketClientWaitUpdateMessage(data.getUsername(), this.gameId));
    }
    */
}
