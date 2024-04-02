package it.polimi.ingsw.model;

import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SharedBoardTest {
    private Deck<GoldCard> goldDeck;
    private Deck<StdCard> stdDeck;
    private Card[] visibleCards;
    private SharedBoard gameBoard;

    @Before
    public void initializeParsers() {
        DeckFactory.setupParser();
    }
    @Test
    public void areCardsOverEmptyDecks() {
        goldDeck = DeckFactory.emptyGold();
        stdDeck = DeckFactory.emptyStd();
        gameBoard = new SharedBoard(goldDeck, stdDeck);

        Assert.assertTrue(gameBoard.areCardsOver());
    }
}