package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to CODEX NATURALIS!");
        System.out.println("Please, insert your username and password to register.");
        System.out.println("Username: ");
        String username = br.readLine();
        System.out.println("Password: ");
        String password = br.readLine();
        System.out.println("Do you have any preferences for technology?(1 -> RMI,  else -> SOCKET)");
        int tech = 0;
        try {
            tech = Integer.parseInt(br.readLine());
        }catch (NumberFormatException e){
            System.out.println("Invalid input, using SOCKET as default.");
        }
        System.out.println("Please, insert the server IP address and port.");
        System.out.println("IP address: ");
        String ip = br.readLine();
        System.out.println("Port: (default 9090)");
        int port = 9090;
        try {
            port = Integer.parseInt(br.readLine());
        }catch (NumberFormatException e){
            System.out.println("Invalid input, using 9090 as default port.");
        }

        if (tech == 1) {
            try {
                RmiClient client = new RmiClient(ip, port);
            } catch (Exception e) {
                System.out.println("Error with the connection:" + e.getMessage());
            }
        } else {
            try {
                SocketClient client = new SocketClient(ip, port);
                client.startConnection(ip, port);
            } catch (Exception e) {
                System.out.println("Error with the connection:" + e.getMessage());
            }
        }
    }

}
