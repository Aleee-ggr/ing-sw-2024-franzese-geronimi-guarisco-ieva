package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class PlayerTest {
    public Player player;
    private Game game;

    @Before
    public void setUp() {
        game = new Game(UUID.randomUUID());
        player = new Player("", game);
    }

    @Test
    public void drawFirstHand_EmptyHand_FullHand() {
        int count = 0;
        player.drawFirstHand();

        for(Card card: player.getHand()) {
            if (card != null) {
                count++;
            }
        }

        Assert.assertEquals(GameConsts.firstHandDim, count);
    }

    @Test
    public void choosePersonalObjective_validIndex_correctPlayerHiddenObjective() {
        Objective objective = game.getGameObjDeck().getCards().get(0);
        player.choosePersonalObjective(0);

        Assert.assertEquals(objective, player.getHiddenObjective());
    }

    @Test
    public void updateResourcesValue__CorrectResourcesAmount() {
        player.updateResourcesValue(Resource.FUNGI, 5);

        Assert.assertEquals(5, player.getResources().get(Resource.FUNGI).intValue());
    }
}

