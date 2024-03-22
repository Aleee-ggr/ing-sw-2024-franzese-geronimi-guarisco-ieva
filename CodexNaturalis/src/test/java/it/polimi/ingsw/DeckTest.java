package it.polimi.ingsw;

import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import org.junit.Assert;
import org.junit.Test;

public class DeckTest {
    private Deck<GoldCard> goldDeck;
    private Deck<StdCard> stdDeck;

    @Test
    public void isEmpty() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();

        Assert.assertTrue(goldDeck.isEmpty());
        Assert.assertTrue(stdDeck.isEmpty());
    }
}
