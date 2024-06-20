package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.network.socket.SocketServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SocketTest {
    private static SocketServer server;

    @BeforeClass
    public static void setup() {
        server = new SocketServer(9092);
    }

    @AfterClass
    public static void tearDown() {
        server.stopServer();
    }

    @Test
    public void connectionTest() throws InterruptedException {
        SocketClient client = new SocketClient("localhost", 9092);
        Thread.sleep(1000);

        try {
            client.stopConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameTest() throws InterruptedException {
        SocketClient client = new SocketClient("localhost", 9092);

        try {
            client.checkCredentials("Splayer1", "password");
            client.newGame(3, "createGameTestSocket");
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
    public void turnTest() throws IOException {
        ArrayList<ClientInterface> clients = new ArrayList<>();
        clients.add(new SocketClient("localhost", 9092));
        clients.add(new SocketClient("localhost", 9092));

        String name = "turnTestS_";

        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).checkCredentials(name + i, name);
        }

        UUID game = clients.getFirst().newGame(2, "SocketTurnTest");
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


        for (ClientInterface client : clients) {
            try {
                ((SocketClient) client).stopConnection();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /*
    @Test
    public void joinGameTest() throws InterruptedException {
        SocketClient client = new SocketClient("localhost", 9091);
        SocketClient client2 = new SocketClient("localhost", 9091);

        try {
            client.newGame(3);
            Thread.sleep(1000);
            client2.joinGame( client.gameId);
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

     */

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