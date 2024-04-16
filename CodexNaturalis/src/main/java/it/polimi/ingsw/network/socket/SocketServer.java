package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Server {
    private ServerSocket server;
    private Socket socket;

    public void startServer(int port) throws IOException{
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

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
