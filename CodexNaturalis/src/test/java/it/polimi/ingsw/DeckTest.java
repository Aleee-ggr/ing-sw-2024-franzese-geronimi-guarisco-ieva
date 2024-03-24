package it.polimi.ingsw;

import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

public class DeckTest {
    private Deck<GoldCard> goldDeck;
    private Deck<StdCard> stdDeck;
    StdCardParser stdParser = new StdCardParser();
    GoldCardParser goldParser = new GoldCardParser();
    Path cardJsonPath = Path.of(GameConsts.cardJsonPath);

    @Test
    public void isEmpty() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();

        Assert.assertTrue(goldDeck.isEmpty());
        Assert.assertTrue(stdDeck.isEmpty());
    }

    @Test
    public void drawFromDeck() {
        try {
            stdParser.readFile(cardJsonPath);
            goldParser.readFile(cardJsonPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Deck<StdCard> stdDeckFull = stdParser.parse();
        Deck<GoldCard> goldDeckFull = goldParser.parse();

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
}
