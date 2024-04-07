package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class PlayerTest {
    public Player player;
    private Game game;

    @Before
    public void initialize() {
        game = new Game(UUID.randomUUID());
        player = new Player("", game);
    }

    @Test
    public void drawFirstHand() {
        player.drawFirstHand();

        Assert.assertNotNull(player.getHand());
    }
}
