package it.polimi.ingsw.network.socket;


import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.messages.requests.*;
import it.polimi.ingsw.network.messages.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * The SocketClient class represents a client that uses sockets for network communication.
 * It extends the Client class and manages a socket connection with a server,
 * allowing the client to create games, join games, and reconnect to games.
 */
public class SocketClient extends Client {

    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Constructor for SocketClient.
     * @param playerUsername The username of the player.
     * @param serverAddress  The address of the server.
     * @param serverPort     The port of the server.
     */
    public SocketClient(String playerUsername, String password, String serverAddress, int serverPort) {
        super(playerUsername, password, serverAddress, serverPort);
    }

    /**
     * Establishes a connection with the server.
     * @param serverAddress The address of the server.
     * @param serverPort    The port of the server.
     */
    private void startConnection(String serverAddress, int serverPort) {
        try {
            client = new Socket(serverAddress, serverPort);
            input = new ObjectInputStream(client.getInputStream());
            output = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e){
            System.out.println("Error with the connection:" + e.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                GenericResponseMessage response = (GenericResponseMessage) input.readObject();
                handleResponse(response);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Stops the connection with the server.
     * @throws IOException If an I/O error occurs.
     */
    public void stopConnection() throws IOException{
        client.close();
        input.close();
        output.close();
    }

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
    }

    /**
     * Send server the message to create a game.
     */
    public void createGame(String username, int numPlayers) throws IOException {
        output.writeObject(new SocketClientCreateGameMessage(username, numPlayers));
    }

    /**
     * Send server the message to join a game.
     * @param gameUUID The UUID of the game to join.
     */
    public void joinGame(String username, UUID gameUUID) throws IOException {
        output.writeObject(new SocketClientJoinGameMessage(username, gameUUID));
        this.setGameId(gameUUID);
    }

    /**
     * Send server the message to reconnect to a game.
     * @param gameUUID The UUID of the game to reconnect to.
     */
    public void reconnect(String username,UUID gameUUID) throws IOException {
        output.writeObject(new SocketClientReconnectMessage(username, gameUUID));
    }

    /**
     * Check credentials validity
     * @param username the username of the player
     * @param password the password of the player
     */
    public void checkCredentials(String username, String password) throws IOException {
        output.writeObject(new SocketValidateCredentialsMessage(username, password));
    }

    public void drawCard(int position, UUID gameUUID) throws IOException {
        output.writeObject(new SocketClientDrawCardMessage(data.getUsername(), position, gameUUID));
    }

    public void getScoreMap() throws IOException {
        output.writeObject(new SocketClientGetScoreMapMessage(data.getUsername()));
    }

    public void getHand() throws IOException {
        output.writeObject(new SocketClientGetHandMessage(data.getUsername(), this.gameId));
    }

    public void getCommonObjectives() throws IOException {
        output.writeObject(new SocketClientGetCommonObjectivesMessage(data.getUsername(), this.gameId));
    }

    public void getPlayerResources(String username) throws IOException {
        output.writeObject(new SocketClientGetPlayerResourcesMessage(data.getUsername(), this.gameId, username));
    }

    public void getVisibleCards() throws IOException {
        output.writeObject(new SocketClientGetVisibleCardsMessage(data.getUsername(), this.gameId));
    }

    public void getBackSideDecks() throws IOException {
        output.writeObject(new SocketClientGetBackSideDecksMessage(data.getUsername(), this.gameId));
    }

    public void getValidPlacements() throws IOException {
        output.writeObject(new SocketClientGetValidPlacementsMessage(data.getUsername(), this.gameId));
    }

    public void getPlayerBoard(String username) throws IOException {
        output.writeObject(new SocketClientGetPlayerBoard(data.getUsername(), this.gameId, username));
    }

    public void getHandColor(String username) throws IOException {
        output.writeObject(new SocketClientGetHandColorMessage(data.getUsername(), this.gameId, username));
    }
}