package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.ConnectionStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerHeartbeatReceiver implements Runnable {
    private final Socket socket;
    private final PrintWriter output;
    private final BufferedReader input;

    private Set<String> players = new HashSet<>();
    private Map<String, ConnectionStatus> clientStatus= new HashMap<>();

    public ServerHeartbeatReceiver(Socket socket, Set<String> players) {
        this.socket = socket;
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        firstConnection();
        while (!socket.isClosed()) {
            try {
                String message = input.readLine(); //TODO: differentiate responses
                output.write("ACK");
                //TODO: handle message
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void firstConnection(){
        while(clientStatus.size() < players.size()){
            try {
                String message = input.readLine();
                if(message != null){
                    clientStatus.put(message, ConnectionStatus.ALIVE);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
