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
    public void drawFromDeck_FullDecks_NotNull() {
        stdDeckFull = FullDeck.getFullStdDeck();
        goldDeckFull = FullDeck.getFullGoldDeck();

        Assert.assertNotNull(stdDeckFull.draw());
        Assert.assertNotNull(goldDeckFull.draw());
    }
    @Test
    public void drawFromDeck_EmptyDecks_Null() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();

        Assert.assertNull(goldDeck.draw());
        Assert.assertNull(stdDeck.draw());
    }

    @Test
    public void drawFromDeck_FullDecksUntilEmpty_Null() {
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
    public void shuffle_FullDecks_NotEquals(){
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