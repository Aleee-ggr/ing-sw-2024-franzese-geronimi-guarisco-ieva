package it.polimi.ingsw.network.socket;


import it.polimi.ingsw.network.Client;

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
    public SocketClient(String playerUsername, String serverAddress, int serverPort) {
        super(playerUsername, serverAddress, serverPort);
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
        //TODO method to read network and do the request
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

    /**
     * Send server the message to create a game.
     */
    public void createGame() {}

    /**
     * Send server the message to join a game.
     * @param id The UUID of the game to join.
     */
    public void joinGame(UUID id) {}

    /**
     * Send server the message to reconnect to a game.
     * @param id The UUID of the game to reconnect to.
     */
    public void reconnect(UUID id) {}

}
