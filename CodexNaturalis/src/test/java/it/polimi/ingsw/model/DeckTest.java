package it.polimi.ingsw.model;

import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.FullDeck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import org.junit.Assert;
import org.junit.Test;

public class DeckTest {
    private Deck<GoldCard> goldDeck;
    private Deck<StdCard> stdDeck;

    private Deck<StdCard> stdDeckFull;
    private Deck<GoldCard> goldDeckFull;

    @Test
    public void isEmpty_EmptyDecks_True() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();

        Assert.assertTrue(goldDeck.isEmpty());
        Assert.assertTrue(stdDeck.isEmpty());
    }

    @Test
    public void isEmpty_FullDecks_False() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = FullDeck.getFullStdDeck();

        Assert.assertFalse(goldDeck.isEmpty());
        Assert.assertFalse(stdDeck.isEmpty());
    }

    @Test
    public void drawFromDeck_FullDecks_NotNullCardObject() {
        stdDeckFull = FullDeck.getFullStdDeck();
        goldDeckFull = FullDeck.getFullGoldDeck();

        Assert.assertNotNull(stdDeckFull.draw());
        Assert.assertNotNull(goldDeckFull.draw());
    }
    @Test
    public void drawFromDeck_EmptyDecks_NullCardObject() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();

        Assert.assertNull(goldDeck.draw());
        Assert.assertNull(stdDeck.draw());
    }

    @Test
    public void drawFromDeck_FullDecksUntilEmpty_NullCardObjectWhenFullAndNotNullCardObjectWhenEmpty() {
        stdDeckFull = FullDeck.getFullStdDeck();
        goldDeckFull = FullDeck.getFullGoldDeck();

        while(!stdDeckFull.isEmpty()){
            Assert.assertNotNull(stdDeckFull.draw());
        }
        Assert.assertNull(stdDeckFull.draw());

        while(!goldDeckFull.isEmpty()){
            Assert.assertNotNull(goldDeckFull.draw());
        }
        Assert.assertNull(goldDeckFull.draw());
    }

    @Test
    public void peekFirstCard_FullDeck_FirstCardObject() {
        stdDeckFull = FullDeck.getFullStdDeck();
        goldDeckFull = FullDeck.getFullGoldDeck();

        Assert.assertEquals(stdDeckFull.peekFirstCard(), stdDeckFull.getCards().getFirst());
        Assert.assertEquals(goldDeckFull.peekFirstCard(), goldDeckFull.getCards().getFirst());
    }

    @Test
    public void peekFirstCard_EmptyDeck_NullCardObject() {
        stdDeck = DeckFactory.emptyStd();
        goldDeck = DeckFactory.emptyGold();

        Assert.assertNull(goldDeck.peekFirstCard());
        Assert.assertNull(stdDeck.peekFirstCard());
    }

    @Test
    public void shuffle_FullDecks_NotEqualsDecks(){
        Deck<StdCard> stdDeckFull = FullDeck.getFullStdDeck();
        Deck<GoldCard> goldDeckFull = FullDeck.getFullGoldDeck();
        Deck<StdCard> stdDeckOriginal = FullDeck.getFullStdDeck();
        Deck<GoldCard> goldDeckOriginal = FullDeck.getFullGoldDeck();

        stdDeckFull.shuffle();
        goldDeckFull.shuffle();

        Assert.assertNotEquals(stdDeckFull.getCards(), stdDeckOriginal.getCards());
        Assert.assertNotEquals(goldDeckFull.getCards(), goldDeckOriginal.getCards());
    }

    @Test
    public void nonIdentityTest() {
        Deck<StdCard> fullDeck1 = FullDeck.getFullStdDeck();
        Deck<StdCard> fullDeck2 = FullDeck.getFullStdDeck();
    }
}