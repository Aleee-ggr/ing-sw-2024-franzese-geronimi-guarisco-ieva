package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The ClientHandler class manages the communication between a client and a server using sockets.
 * It provides methods to send and receive objects over the network.
 * @author Samuele Franzese
 */
public class ClientHandler{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Constructs a new ClientHandler object with the specified socket connection.
     *
     * @param socket the socket connection to the client
     * @throws IOException if an I/O error occurs while initializing the input and output streams
     */
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
    }
}
