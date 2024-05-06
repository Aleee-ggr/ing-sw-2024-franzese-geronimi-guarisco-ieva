package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.FullDeck;
import it.polimi.ingsw.model.cards.StartingCard;
import org.junit.Test;

public class StartingCardViewTest {
    @Test
    public void test() {
        Deck<StartingCard> deck = FullDeck.getFullStartingDeck();

    }
}
