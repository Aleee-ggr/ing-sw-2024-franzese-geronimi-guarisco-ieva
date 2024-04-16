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

    /**
     * Starts the server on the specified port.
     *
     * @param port the port number on which the server will listen for incoming connections
     * @throws IOException if an I/O error occurs while creating the server socket
     */
    public void startServer(int port) throws IOException{
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Accepts client connections and creates a new ClientHandler for each accepted connection.
     */
    public void acceptConnection() {
        while(true) {
            try {
                ClientHandler clientHandler = new ClientHandler(server.accept());
            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            }

            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
