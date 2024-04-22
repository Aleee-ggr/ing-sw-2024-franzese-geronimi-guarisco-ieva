package it.polimi.ingsw.network;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.rmi.RmiServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class RmiTest {
    private static RmiServer server;
    private static  RmiClient client;

    @BeforeClass
    public static void setup()  {
        try {
            server = new RmiServer(9090);
            Thread.sleep(2000);
            client = new RmiClient("user", "localhost", 9090);
        } catch (RemoteException | InterruptedException ignored) {}
    }
    @AfterClass
    public static void destroyServer() {
        server.stop();
    }

    @Test
    public void testNewGame() throws ServerConnectionException, RemoteException {
        UUID game = client.newGame(3);
        assertNotNull(game);
    }

    @Test
    public void testJoin() throws ServerConnectionException, RemoteException {
        UUID game = client.newGame(3);
        RmiClient client2 = new RmiClient("user2", "localhost", 9090);
        client2.joinGame(game);
    }
}
