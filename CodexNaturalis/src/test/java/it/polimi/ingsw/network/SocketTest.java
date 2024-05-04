package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.network.socket.SocketServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class SocketTest {
    private static SocketServer server;

    @BeforeClass
    public static void setup() {
        server = new SocketServer(9090);
    }

    @AfterClass
    public static void tearDown() {
        server.stopServer();
    }

    @Test
    public void connectionTest() throws InterruptedException {
        SocketClient client = new SocketClient("prova", "pippo", "localhost", 9090);
        client.startConnection("localhost", 9090);
        Thread.sleep(1000);
        try {
            client.stopConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}