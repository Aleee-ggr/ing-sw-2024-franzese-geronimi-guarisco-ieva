package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.FullDeck;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.player.ClientData;
import it.polimi.ingsw.network.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewTest {
    @Before
    public void setUp() {
        new Client("usernameTest", "pwd", "", 0);
        final ClientData clientData = Client.getData();
        clientData.setGlobalObjectives(99, 98);
        clientData.setPersonalObjective(99);

        clientData.setClientHand(new ArrayList<>(List.of(1, 2, 3)));

        clientData.setPlayerNum(4);
        clientData.addPlayer("player1");
        clientData.addPlayer("player2");
        clientData.addPlayer("player3");

        HashMap<Coordinates, Integer> map =  new HashMap<>();
        map.put(new Coordinates(0,0), FullDeck.getFullStartingDeck().draw().getId());
        clientData.setPlayerBoard("player1", new HashMap<>(map));
        clientData.setPlayerBoard("player2", new HashMap<>(map));

        HashMap<Coordinates, Integer> map2 = new HashMap<>(map);
        Deck<StdCard> deck = FullDeck.getFullStdDeck();
        deck.shuffle();
        map2.put(new Coordinates(0, 1), deck.draw().getId());
        map2.put(new Coordinates(-1, 0), deck.draw().getId());
        map2.put(new Coordinates(-1, 1), deck.draw().getId());
        map2.put(new Coordinates(0, 2), deck.draw().getId());
        clientData.setPlayerBoard("player3", new HashMap<>(map2));



    }

    @Test
    public void viewTest() {
        Compositor view = new Compositor(new String[]{"player1", "player2", "player3"});
        view.updateView();
    }
}
