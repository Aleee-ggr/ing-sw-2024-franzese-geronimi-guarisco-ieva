package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The SocketServer class extends the Server class and represents a server that uses sockets
 * for communication with clients.
 * @author Samuele Franzese
 */
public class SocketServer extends Server {
    private ServerSocket server;
    private Socket socket;

    public SocketServer(int port) {
        System.out.println("Starting server...");
        try {
            server = new ServerSocket(port);
            System.out.println("Started server on port " + port);
            new Thread(this::acceptConnection).start();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Starts the server on the specified port.
     *
     * @param port the port number on which the server will listen for incoming connections
     * @throws IOException if an I/O error occurs while creating the server socket
     */
    public void startServer(int port) throws IOException{

    }

    /**
     * Accepts client connections and creates a new ClientHandler for each accepted connection.
     */
    public void acceptConnection() {
        while(true) {
            try {
                socket = server.accept();
                System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());
                ClientHandler clientHandler = new ClientHandler(socket, threadMessages);
                clientHandler.start();
            } catch (IOException e) {
                System.out.println("Connection error accept connection: " + e.getMessage());
            }
        }
    }

    public void stopServer() {
        try {
            server.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

