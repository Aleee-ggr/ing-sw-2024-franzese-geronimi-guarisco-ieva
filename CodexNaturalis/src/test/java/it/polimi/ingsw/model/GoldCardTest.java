package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GoldCardTest {
    private Player player;

    @Before
    public void setUp() {
        player = new Player("test", new Game());
        player.updateResourcesValue(Resource.FUNGI, 4);
        player.updateResourcesValue(Resource.ANIMAL, 9);
    }
    
    @Test
    public void checkRequirements_ValidRequirements_True() {
        Map<Resource, Integer> requirements = new HashMap<>();
        requirements.put(Resource.FUNGI, 2);
        requirements.put(Resource.ANIMAL, 1);

        GoldCard goldCard = new GoldCard(1, null, null, requirements, null);

        Assert.assertTrue(goldCard.checkRequirements(player));
    }

    @Test
    public void checkRequirements_InvalidRequirements_False() {
        Map<Resource, Integer> requirements = new HashMap<>();
        requirements.put(Resource.FUNGI, 2);
        requirements.put(Resource.PLANT, 1);

        GoldCard goldCard = new GoldCard(1, null, null, requirements, null);

        Assert.assertFalse(goldCard.checkRequirements(player));
    }
}
