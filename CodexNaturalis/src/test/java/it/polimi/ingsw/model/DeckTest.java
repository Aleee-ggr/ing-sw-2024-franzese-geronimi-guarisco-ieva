package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

public class DeckTest {
    private Deck<GoldCard> goldDeck;
    private Deck<StdCard> stdDeck;
    private StdCardParser stdParser;
    private GoldCardParser goldParser;
    private Deck<StdCard> stdDeckFull;
    private Deck<GoldCard> goldDeckFull;

    @Before
    public void initializeParsers() {
        stdParser = new StdCardParser();
        goldParser = new GoldCardParser();

        Path cardJsonPath = Path.of(GameConsts.cardJsonPath);

        try {
            stdParser.readFile(cardJsonPath);
            goldParser.readFile(cardJsonPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void isEmpty() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();

        Assert.assertTrue(goldDeck.isEmpty());
        Assert.assertTrue(stdDeck.isEmpty());
    }

    @Test
    public void drawFromDeck() {
        stdDeckFull = stdParser.parse();
        goldDeckFull = goldParser.parse();

        Assert.assertNotNull(stdDeckFull.draw());
        Assert.assertNotNull(goldDeckFull.draw());
    }
    @Test
    public void drawFromEmptyDeck() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();

        Assert.assertNull(goldDeck.draw());
        Assert.assertNull(stdDeck.draw());
    }

    @Test
    public void drawUntilEmpty() {
        stdDeckFull = stdParser.parse();
        goldDeckFull = goldParser.parse();

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
    public void shuffle(){

        Deck<StdCard> stdDeckFull = stdParser.parse();
        Deck<GoldCard> goldDeckFull = goldParser.parse();
        Deck<StdCard> stdDeckOriginal = stdParser.parse();
        Deck<GoldCard> goldDeckOriginal = goldParser.parse();

        stdDeckFull.shuffle();
        goldDeckFull.shuffle();

        Assert.assertNotEquals(stdDeckFull.getCards(), stdDeckOriginal.getCards());
        Assert.assertNotEquals(goldDeckFull.getCards(), goldDeckOriginal.getCards());
    }
}