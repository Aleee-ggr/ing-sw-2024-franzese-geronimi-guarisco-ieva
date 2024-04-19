package it.polimi.ingsw.model;

import it.polimi.ingsw.helpers.MockPlayer;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.FullDeck;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class PlayerBoardTest {
    PlayerBoard board;
    Player p;

    @Before
    public void setup() {
        board = new PlayerBoard(FullDeck.getFullStartingDeck().draw(), p);
        p = new MockPlayer("mock", new Game(UUID.randomUUID()));
    }

    @Test
    public void checkPositionsIfDisconnected_test_start() {
        board.checkPositionsIfDisconnected();
    }
}