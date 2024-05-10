package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.FullDeck;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientInterface;
import org.junit.Before;
import org.junit.Test;

import java.util.*;


//TODO: to refactor
public class ViewTest {
    private final Client clientData = new Client( "", 0);

    @Before
    public void setUp() {
        String username = "usernameTest";
        clientData.setCredentials(username, "passwordTest");
        PlayerData playerData = ((PlayerData)clientData.getOpponentData().get(username));

        playerData.setGlobalObjectives(new ArrayList<>(List.of(Game.getObjectiveByID(98),
                                                               Game.getObjectiveByID(99))));
        playerData.setPersonalObjective(Game.getObjectiveByID(97));

        playerData.setClientHand(new ArrayList<>(List.of(Game.getCardByID(1),
                                                         Game.getCardByID(2),
                                                         Game.getCardByID(3))));

        clientData.setPlayerNum(4);
        /*
        clientData.addPlayer("player1");
        clientData.addPlayer("player2");
        clientData.addPlayer("player3");
        clientData.updateScore("player1", 10);
        clientData.updateScore("player2", 12);
        clientData.updateScore("player3", 13);
        clientData.updateScore("usernameTest", 19);
        */

        Deck<StdCard> deck = FullDeck.getFullStdDeck();
        deck.shuffle();
        HashMap<Coordinates, Integer> map =  new HashMap<>();
        map.put(new Coordinates(0,0), FullDeck.getFullStartingDeck().draw().getId());
        /*
        clientData.setPlayerBoard("player1", new HashMap<>(map));
        clientData.addPlayerCard("player2", new Coordinates(0, 0), FullDeck.getFullStartingDeck().draw().getId());
        clientData.addPlayerCard("player2", new Coordinates(0, 1), deck.draw().getId());
        clientData.setPlayerBoard("player3", new HashMap<>(map));
        */
        //HashMap<Coordinates, Integer> map2 = new HashMap<>(map);
        /*
        clientData.addPlayerCard("usernameTest",new Coordinates(0, 0), FullDeck.getFullStartingDeck().draw().getId());
        clientData.addPlayerCard("usernameTest",new Coordinates(0, 1), deck.draw().getId());
        clientData.addPlayerCard("usernameTest",new Coordinates(-1, 0), deck.draw().getId());
        clientData.addPlayerCard("usernameTest",new Coordinates(-1, 1), deck.draw().getId());
        clientData.addPlayerCard("usernameTest",new Coordinates(0, 2), deck.draw().getId());
        */
        Set<Coordinates> coord = new HashSet<>();
        coord.add(new Coordinates(1, 0));
        coord.add(new Coordinates(-1, 2));
        coord.add(new Coordinates(0, -1));
        coord.add(new Coordinates(1, 2));
        //clientData.setValidPlacements(coord);
        //clientData.setPlayerBoard("usernameTest", new HashMap<>(map2));



    }

    @Test
    public void viewTest() {
        Compositor view = new Compositor((ClientInterface) clientData);
        view.updateView();
    }
}
