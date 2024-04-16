package it.polimi.ingsw.network.socket;


import it.polimi.ingsw.network.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class SocketClient extends Client {

    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    public SocketClient(UUID gameId, String playerUsername, String serverAddress, int serverPort) {
        super(gameId, playerUsername, serverAddress, serverPort);
    }

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
    public void stopConnection() throws IOException{
        client.close();
        input.close();
        output.close();
    }

    public void createGame() {}

    public void joinGame(UUID id) {}

    public void reconnect(UUID id) {}

}
