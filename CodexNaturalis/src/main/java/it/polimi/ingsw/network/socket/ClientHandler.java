package it.polimi.ingsw.network.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
    }
}
