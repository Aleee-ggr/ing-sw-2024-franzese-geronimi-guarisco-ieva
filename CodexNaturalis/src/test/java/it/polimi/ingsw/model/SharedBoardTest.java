package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.FullDeck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import org.junit.Assert;
import org.junit.Test;

public class SharedBoardTest {
    private Deck<GoldCard> goldDeck;
    private Deck<StdCard> stdDeck;
    private SharedBoard gameBoard;

    @Test
    public void areCardsOverEmptyDecksAndEmptyVisibleCards() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertTrue(gameBoard.areCardsOver());
    }

    @Test
    public void areCardsOverFullDecksAndEmptyVisibleCards() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = FullDeck.getFullStdDeck();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertFalse(gameBoard.areCardsOver());
    }

    @Test
    public void areCardsOverFullDecksAndFullVisibleCards() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = FullDeck.getFullStdDeck();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        for (int i = 0; i < GameConsts.visibleCards; i++) {
            gameBoard.substituteCard(i, i % 2 == 0);
        }

        Assert.assertFalse(gameBoard.areCardsOver());
    }

    @Test
    public void areCardsOverOneDeckEmptyAndNonEmptyVisibleCards() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = DeckFactory.emptyStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        gameBoard.substituteCard(0, true);

        Assert.assertFalse(gameBoard.areCardsOver());
    }

    @Test
    public void areCardsOverEmptyDecksAndFullVisibleCards() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = FullDeck.getFullStdDeck();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        for (int i = 0; i < GameConsts.visibleCards; i++) {
            gameBoard.substituteCard(i, i % 2 == 0);
        }

        while(!gameBoard.getGoldDeck().isEmpty()) {
            gameBoard.drawDeck(true);
            gameBoard.drawDeck(false);
        }

        Assert.assertFalse(gameBoard.areCardsOver());
    }

    @Test
    public void drawDeckFullGoldCard() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = FullDeck.getFullStdDeck();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertNotNull(gameBoard.drawDeck(true));
    }
    @Test
    public void drawDeckFullStdCard() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = FullDeck.getFullStdDeck();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertNotNull(gameBoard.drawDeck(false));
    }
    @Test
    public void drawDeckEmptyGoldCardFullStdCard() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = FullDeck.getFullStdDeck();
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

    @Test
    public void substituteCardFullDecks() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = FullDeck.getFullStdDeck();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        gameBoard.substituteCard(0, true);

        Assert.assertNotNull(gameBoard.drawVisible(0));
    }

    @Test
    public void substituteCardEmptyDecks() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        gameBoard.substituteCard(0, true);

        Assert.assertNull(gameBoard.drawVisible(0));
    }

    @Test
    public void substituteCardEmptyGoldDeck() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = FullDeck.getFullStdDeck();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        gameBoard.substituteCard(0, true);

        Assert.assertNotNull(gameBoard.drawVisible(0));
    }

    @Test
    public void substituteCardEmptyStdDeck() {
        goldDeck = FullDeck.getFullGoldDeck();
        stdDeck = DeckFactory.emptyStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        gameBoard.substituteCard(0, false);

        Assert.assertNotNull(gameBoard.drawVisible(0));
    }

}