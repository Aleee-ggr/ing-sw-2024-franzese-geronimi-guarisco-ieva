package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.view.TUI.components.DeckView;
import org.junit.Test;

public class DeckViewTest {
    @Test
    public void testDeckView() {
        Resource[] backs = new Resource[] {
                Resource.FUNGI,
                Resource.INSECT,
        };

        Card[] cards = new Card[] {
                Game.getCardByID(1),
                Game.getCardByID(2),
                Game.getCardByID(3),
                Game.getCardByID(4),
        };

        DeckView deckView = new DeckView(backs, cards);
        System.out.println(deckView);
    }
}
