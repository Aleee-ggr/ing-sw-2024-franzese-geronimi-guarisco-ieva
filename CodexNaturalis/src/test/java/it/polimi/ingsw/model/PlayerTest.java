package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.MockPlayer;
import it.polimi.ingsw.helpers.exceptions.RequirementsError;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.enums.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class PlayerTest {
    public MockPlayer player;
    private Game game;

    @Before
    public void setUp() {
        game = new Game(4);
        player = new MockPlayer("", game);
        HashMap<Resource, Integer> resourceMap = new HashMap<>();
    }

    @Test
    public void drawFirstHand_EmptyHand_FullHand() {
        int count = 0;
        player.drawFirstHand();

        for (Card card : player.getHand()) {
            if (card != null) {
                count++;
            }
        }

        Assert.assertEquals(GameConsts.firstHandDim, count);
    }
    //TODO: rewrite the test to adapt to changes in Player code. also add test for tempObjectives.
    /*@Test
    public void choosePersonalObjective_validIndex_correctPlayerHiddenObjective() {
        Objective objective = game.getGameObjDeck().getCards().get(0);
        player.choosePersonalObjective(99);

        Assert.assertEquals(objective, player.getHiddenObjective());
    }*/

    @Test
    public void updateResourcesValue_CorrectResourcesAmount() {
        player.updateResourcesValue(Resource.FUNGI, 5);
        player.updateResourcesValue(Resource.FUNGI, 2);
        Assert.assertEquals(7, player.getResources().get(Resource.FUNGI).intValue());
    }

    @Test
    public void drawDecks_testGold() {
        player.drawDecks(true);
        assertSame(player.getHand()[0].getClass(), GoldCard.class);
    }

    @Test
    public void drawDecks_testStd() {
        player.drawDecks(false);
        assertSame(player.getHand()[0].getClass(), StdCard.class);
    }

    @Test
    public void drawVisible_testAll() {
        for (int pos = 0; pos < 4; pos++) {
            player.getHand()[0] = null;
            player.drawVisible(pos);
            if (pos < 2) {
                assertSame(player.getHand()[0].getClass(), GoldCard.class);
            } else {
                assertSame(player.getHand()[0].getClass(), StdCard.class);
            }
        }
    }

    @Test
    public void playCard_cardRemoved() throws RequirementsError {
        player.drawStartingCard();
        player.setFirstCard(true);
        player.drawFirstHand();
        int first_id = player.getHand()[0].getId();
        player.playCard(player.getHand()[0], new Coordinates(0, 1));
        player.drawDecks(false);
        assertNotEquals(first_id, player.getHand()[0].getId());
    }

    @Test
    public void playCard_stdCard_hasPoints() throws RequirementsError {
        player.drawStartingCard();
        player.setFirstCard(true);
        player.drawFirstHand();
        player.getHand()[0] = (ColoredCard) Game.getCardByID(18);
        player.playCard(player.getHand()[0], new Coordinates(0, 1));
        player.drawDecks(false);
        assertNotEquals(0, player.getScore());
    }

    @Test
    public void playCard_playGoldCard_playable() throws RequirementsError {
        player.drawStartingCard();
        player.setFirstCard(true);

        for (Resource res : Resource.values()) {
            player.setResource(res, 999);
        }

        player.drawFirstHand();

        int cardId = 80;
        player.getHand()[2] = (ColoredCard) Game.getCardByID(cardId);
        player.playCard(player.getHand()[2], new Coordinates(0, 1));
    }

    @Test(expected = RequirementsError.class)
    public void playCard_playGoldCard_unplayable() throws RequirementsError {
        player.drawStartingCard();
        player.setFirstCard(true);

        for (Resource res : Resource.values()) {
            player.setResource(res, 0);
        }

        player.drawFirstHand();

        player.getHand()[2] = (ColoredCard) Game.getCardByID(49);
        player.playCard(player.getHand()[2], new Coordinates(0, 1));
    }
}

