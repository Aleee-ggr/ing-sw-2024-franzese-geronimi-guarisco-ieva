package it.polimi.ingsw.network;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.player.ClientData;

import java.util.UUID;

/**
 * Abstract class, baseline for building a client that can connect to a server to participate in a game.
 * @author Alessio Guarisco
 * @author Daniele Ieva
 */
public class Client {
    protected UUID gameId;
    protected final String serverAddress;
    protected final int serverPort;
    protected static ClientData data = null;

    /**
     * Constructs a new Client object with the specified player username, server address, and server port.
     * @param playerUsername The username of the player.
     * @param serverAddress  The address of the server.
     * @param serverPort     The port of the server.
     */
    public Client(String playerUsername, String password, String serverAddress, int serverPort) {
        data = new ClientData(playerUsername, password);
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public static ClientData getData() {
        return data;
    }

    public void setUsername(String username) {
        data.setUsername(username);
    }

    public void setPassword(String password) {
        data.setPassword(password);
    }
    /**
     * Sets the game ID associated with the client.
     * @param gameId The game ID to set.
     */
    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    /**
     * Getter for the score of the client.
     * @return The score of the client.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     */
    public Integer getScore() throws ServerConnectionException{
        return null;
    }

    /**
     * Getter for the player board of the client.
     * @return The player board of the client.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     */
    public String getPlayerBoard() throws ServerConnectionException{
        return null;
    }

    /**
     * Getter for the shared board.
     * @return The shared board from the server.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     */
    public String getSharedBoard() throws ServerConnectionException{
        return null;
    }

    /**
     * Draws a card from the server.
     * @return The card drawn from the server.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     */
    public Card drawCard() throws ServerConnectionException{
        return null;
    }

    /**
     * Places a card on the board.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     */
    public void placeCard() throws ServerConnectionException{
    }

    /**
     * Join a game.
     * @throws ServerConnectionException If there is an issue connecting to the server.
     */
    public void joinGame() throws ServerConnectionException{
    }
}
