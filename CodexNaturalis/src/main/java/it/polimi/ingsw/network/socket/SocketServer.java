package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The SocketServer class extends the Server class and represents a server that uses sockets
 * for communication with clients.
 *
 * @author Samuele Franzese
 */
public class SocketServer extends Server {
    private ServerSocket server;
    private Socket socket;

    /**
     * Constructs a new SocketServer and starts listening for client connections on the specified port.
     *
     * @param port The port on which the server will listen for client connections.
     */
    public SocketServer(int port) {
        System.out.println("Starting server...");
        try {
            server = new ServerSocket(port);
            System.out.println("Started server on port " + port);
            new Thread(this::acceptConnection).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accepts client connections and creates a new ClientHandler for each accepted connection.
     */
    public void acceptConnection() {
        while (!server.isClosed()) {
            try {
                socket = server.accept();
                new ClientHandler(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the server and closes the server socket.
     */
    public void stopServer() {
        try {
            server.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

