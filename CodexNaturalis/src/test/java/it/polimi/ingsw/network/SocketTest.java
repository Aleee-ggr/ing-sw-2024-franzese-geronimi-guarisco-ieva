package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.network.socket.SocketServer;
import org.junit.AfterClass;
import org.junit.Assert;
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
        Thread.sleep(1000);

        try {
            client.stopConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameTest() throws InterruptedException {
        SocketClient client = new SocketClient("prova", "pippo", "localhost", 9090);

        try {
            client.createGame("prova", 3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread.sleep(1000);
        Assert.assertNotNull(client.gameId);

        try {
            client.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void joinGameTest() throws InterruptedException {
        SocketClient client = new SocketClient("prova", "pippo", "localhost", 9090);
        SocketClient client2 = new SocketClient("prova_2", "pippo", "localhost", 9090);

        try {
            client.createGame("prova", 3);
            Thread.sleep(1000);
            client2.joinGame("prova_2", client.gameId);
            Thread.sleep(1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Assert.assertEquals(client.gameId, client2.gameId);

        try {
            client.stopConnection();
            client2.stopConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}