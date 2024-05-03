package it.polimi.ingsw.network;

import it.polimi.ingsw.helpers.RmiClientFactory;
import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.rmi.RmiServer;
import org.junit.*;

import java.rmi.RemoteException;
import java.util.UUID;

import static org.junit.Assert.*;

public class RmiTest {
    private static RmiServer server;

    @BeforeClass
    public static void setup()  {
        try {
            server = new RmiServer(9090);
            Thread.sleep(2000);
        } catch (RemoteException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void destroyServer() {
        server.stop();
    }

    @Test
    public void testNewGame() throws ServerConnectionException, RemoteException {
        RmiClient c = RmiClientFactory.getClient();

        UUID game = c.newGame(3);
        assertNotNull(game);
    }

    @Test
    public void testJoin() throws ServerConnectionException, RemoteException {
        RmiClient c1 = RmiClientFactory.getClient();
        UUID game = c1.newGame(3);
        RmiClient c2  = RmiClientFactory.getClient();
        assertTrue(c2.joinGame(game));
    }

    @Test
    public void testInvalidJoin_usernameExists() throws ServerConnectionException, RemoteException {
        RmiClient c1 = RmiClientFactory.getClient();
        UUID game = c1.newGame(3);
        assertFalse(c1.joinGame(game));
    }

    @Test
    public void testInvalidJoin_playerCount() throws ServerConnectionException, RemoteException {
        RmiClient c1 = RmiClientFactory.getClient();
        UUID game = c1.newGame(2);
        RmiClient c2 = RmiClientFactory.getClient();
        assertTrue(c2.joinGame(game));
        RmiClient c3 = RmiClientFactory.getClient();
        assertFalse(c3.joinGame(game));
    }
}
