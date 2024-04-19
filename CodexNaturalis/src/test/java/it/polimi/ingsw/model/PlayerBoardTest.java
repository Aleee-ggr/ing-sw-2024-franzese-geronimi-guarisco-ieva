package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.MockPlayer;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.FullDeck;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

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
        Set<Coordinates> expected = new HashSet<>(GameConsts.centralPoint.getNeighbors());
        Set<Coordinates> actual = board.getValidPlacements();
        assertEquals(expected, actual);

    }
}