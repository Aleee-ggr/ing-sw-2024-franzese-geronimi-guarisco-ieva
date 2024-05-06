package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.FullDeck;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.view.TUI.components.StartingObjectiveView;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;
import org.junit.Test;

public class StartingObjectiveViewTest {
    @Test
    public void test() {
        Deck<Objective> deck = FullDeck.getFullObjDeck();
        ObjectiveCard[] cards = new ObjectiveCard[] {
          new ObjectiveCard(deck.draw()),
          new ObjectiveCard(deck.draw())
        };

        System.out.println(new StartingObjectiveView(cards));
    }
}
