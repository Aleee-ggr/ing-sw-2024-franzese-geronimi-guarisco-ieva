package it.polimi.ingsw.network;

import it.polimi.ingsw.helpers.RmiClientFactory;
import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.rmi.RmiServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

public class RmiTest {
    private static RmiServer server;

    @BeforeClass
    public static void setup() {
        try {
            server = new RmiServer(9091);
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
        RmiClient c2 = RmiClientFactory.getClient();
        assertTrue(c2.joinGame(game));
    }

    @Test
    public void testInvalidJoin_usernameExists() throws ServerConnectionException, RemoteException {
        RmiClient c1 = RmiClientFactory.getClient();
        RmiClient c2 = new RmiClient("localhost", 9091);
        try {
            c2.checkCredentials(c1.username, "differentPassword");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        UUID game = c1.newGame(3);
        assertFalse(c2.joinGame(game));
    }

    @Test
    public void testInvalidJoin_playerCount() throws ServerConnectionException, RemoteException {
        RmiClient c1 = RmiClientFactory.getClient();
        UUID game = c1.newGame(2);
        RmiClient c2 = RmiClientFactory.getClient();
        assertTrue(c2.joinGame(game));
        RmiClient c4 = RmiClientFactory.getClient();
        assertFalse(c4.joinGame(game));
    }

    @Test
    public void turnTest() throws IOException {
        ArrayList<ClientInterface> clients = new ArrayList<>();
        clients.add(new RmiClient("localhost", 9091));
        clients.add(new RmiClient("localhost", 9091));

        String name = "turnTest_";

        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).checkCredentials(name + i, name);
        }

        UUID game = clients.getFirst().newGame(2);
        for (int i = 1; i < clients.size(); i++) {
            clients.get(i).joinGame(game);
        }

        for (ClientInterface client : clients) {
            fetchSetup(client);
            client.placeStartingCard(true);
            int objectiveId = client.getPlayerData().getStartingObjectives().getFirst().getId();
            client.choosePersonalObjective(objectiveId);
            client.choosePlayerColor(client.getPlayerData().getAvailableColors().getFirst());
        }

        for (ClientInterface client : clients) {
            fetchData(client);

            assertFalse(client.getPlayerData().getValidPlacements().isEmpty());
            assertFalse(client.getPlayerData().getClientHand().isEmpty());
            assertFalse(client.getPlayerData().getBoard().isEmpty());
            assertFalse(client.getDecksBacks().isEmpty());
            assertFalse(client.getVisibleCards().isEmpty());
            assertFalse(client.getPlayerData().getGlobalObjectives().isEmpty());


            Coordinates placementCoords = client.getPlayerData().getValidPlacements().getFirst();
            int cardId = client.getPlayerData().getClientHand().getFirst().getId();
            assertTrue(client.placeCard(placementCoords, cardId));
            int toDraw = 1;
            client.drawCard(toDraw);
        }

        for (ClientInterface client : clients) {
            client.postChat("hello, World!", null);
            client.fetchChat();
        }
    }

    private void fetchSetup(ClientInterface client) {
        try {
            client.fetchPlayers();
            client.fetchStartingObjectives();
            client.fetchStartingCard();
            client.fetchAvailableColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchData(ClientInterface client) {
        try {
            client.fetchClientHand();
            client.fetchCommonObjectives();
            client.fetchValidPlacements();
            client.fetchPlayersBoards();
            client.fetchPlayersPlacingOrder();
            client.fetchPlayersResources();
            client.fetchScoreMap();
            client.fetchGameState();
            client.fetchVisibleCardsAndDecks();
            client.fetchOpponentsHandColor();
            client.fetchPlayersColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
