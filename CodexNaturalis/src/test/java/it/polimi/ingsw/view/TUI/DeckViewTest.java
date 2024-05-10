package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.helpers.RmiClientFactory;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.rmi.RmiServer;
import it.polimi.ingsw.view.TUI.components.DeckView;
import org.junit.Test;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DeckViewTest {
    @Test
    public void testDeckView() throws RemoteException {
        RmiServer server = new RmiServer(9090);
        RmiClient client = RmiClientFactory.getClient();
        ArrayList<Card> backs = new ArrayList<>(List.of(
                Game.getCardByID(1),
                Game.getCardByID(2),
                Game.getCardByID(3),
                Game.getCardByID(4)
        ));

        ArrayList<Card> cards = new ArrayList<>(List.of(
                Game.getCardByID(5),
                Game.getCardByID(6),
                Game.getCardByID(7),
                Game.getCardByID(8)
        ));
        client.createPlayerData(new ArrayList<>(List.of("player")));
        client.setVisibleCards(cards);
        client.setBackSideDecks(backs);

        DeckView deckView = new DeckView(client);
        System.out.println(deckView);
    }
}
