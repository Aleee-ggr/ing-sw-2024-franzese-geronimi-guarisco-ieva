package it.polimi.ingsw.network.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHeartbeat implements Runnable{
    private final Socket socket;
    private final PrintWriter output;
    private final BufferedReader input;

    private final String username;
    private final int heartbeatInterval = 2000;
    private final int waitAck = 300;
    private int triesNum = 5;

    public ClientHeartbeat(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException | IllegalThreadStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

        while (!socket.isClosed()) {
            try {
                Thread.sleep(heartbeatInterval);
                output.write(username);

                Thread.sleep(waitAck);
                String message = input.readLine();

                if(message == null || !message.equals("ACK")){
                    triesNum--;
                } else {
                    triesNum = 5;
                }

                if(triesNum == 0){
                    socket.close();
                    System.out.println("Heartbeat failed, Disconnection");
                }

            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
