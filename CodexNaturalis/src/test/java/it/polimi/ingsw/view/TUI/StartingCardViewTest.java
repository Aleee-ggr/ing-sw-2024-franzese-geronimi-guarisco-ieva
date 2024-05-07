package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.view.TUI.components.StartingCardView;
import org.junit.Test;

public class StartingCardViewTest {
    @Test
    public void test() {
        StartingCardView startingCardView = new StartingCardView((StartingCard) Game.getCardByID(86));
        System.out.println(startingCardView);
    }
}
