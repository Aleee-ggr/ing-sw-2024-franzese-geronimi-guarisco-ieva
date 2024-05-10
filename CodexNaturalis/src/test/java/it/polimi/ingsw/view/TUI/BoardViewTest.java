package it.polimi.ingsw.view.TUI;

import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.helpers.RmiClientFactory;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.rmi.RmiServer;
import it.polimi.ingsw.view.TUI.components.Board;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoardViewTest {
    private Board board;
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

    @Test
    public void printTest() throws RemoteException {
        RmiClient client = RmiClientFactory.getClient();
        board = new Board(client);
        UUID game = client.newGame(2);

        RmiClient client2 = RmiClientFactory.getClient();
        client2.joinGame(game);

        HashBiMap<Coordinates, Card> coordinateBoard = HashBiMap.create();
        coordinateBoard.put(new Coordinates(0, 0), Game.getCardByID(1));
        coordinateBoard.put(new Coordinates(0, 1), Game.getCardByID(12));
        ArrayList<Card> order = new ArrayList<>();
        order.add(Game.getCardByID(1));
        order.add(Game.getCardByID(12));


        ArrayList<String> players = new ArrayList<>();
        players.add(client.getUsername());
        players.add(client2.getUsername());

        client.createPlayerData(players);

        client.getPlayerData().setBoard(coordinateBoard);
        client.getPlayerData().setOrder(order);

        for(String s : board.toStringArray()){
            System.out.println(s);
        }
    }


}
