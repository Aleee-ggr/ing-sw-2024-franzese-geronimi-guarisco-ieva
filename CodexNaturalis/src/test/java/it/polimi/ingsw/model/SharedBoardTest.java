package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SharedBoardTest {
    private Deck<GoldCard> goldDeck;
    private Deck<StdCard> stdDeck;
    private SharedBoard gameBoard;

    @Before
    public void initializeParsers() {
        DeckFactory.setupParser();
    }

    @Test
    public void areCardsOverEmptyDecksAndEmptyVisibleCards() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertTrue(gameBoard.areCardsOver());
    }

    @Test
    public void areCardsOverFullDecksAndEmptyVisibleCards() {
        goldDeck = DeckFactory.fullGold();
        stdDeck = DeckFactory.fullStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertFalse(gameBoard.areCardsOver());
    }

    @Test
    public void areCardsOverFullDecksAndFullVisibleCards() {
        goldDeck = DeckFactory.fullGold();
        stdDeck = DeckFactory.fullStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        for (int i = 0; i < GameConsts.visibleCards; i++) {
            gameBoard.substituteCard(i, i % 2 == 0);
        }

        Assert.assertFalse(gameBoard.areCardsOver());
    }

    @Test
    public void areCardsOverOneDeckEmptyAndNonEmptyVisibleCards() {
        goldDeck = DeckFactory.fullGold();
        stdDeck = DeckFactory.emptyStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        gameBoard.substituteCard(0, true);

        Assert.assertFalse(gameBoard.areCardsOver());
    }

    @Test
    public void drawDeckFullGoldCard() {
        goldDeck = DeckFactory.fullGold();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertNotNull(gameBoard.drawDeck(true));
    }
    @Test
    public void drawDeckFullStdCard() {
        stdDeck = DeckFactory.fullStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertNotNull(gameBoard.drawDeck(false));
    }
    @Test
    public void drawDeckEmptyGoldCardFullStdCard() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.fullStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertNotNull(gameBoard.drawDeck(true));
    }

    @Test
    public void drawDeckEmptyDecks() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertNull(gameBoard.drawDeck(true));
        Assert.assertNull(gameBoard.drawDeck(false));
    }
}