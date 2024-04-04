package it.polimi.ingsw.model;

import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.helpers.PlayerBuilder;
import it.polimi.ingsw.helpers.builders.FunctionBuilder;
import it.polimi.ingsw.helpers.exceptions.InvalidTypeException;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectiveTest {
    @Test
    public void testResources() throws InvalidTypeException {
        Map<Resource, Integer> requirements = new HashMap<>() {{
                put(Resource.FUNGI, 1);
        }};
        Function<Player, Integer> function = new FunctionBuilder()
                .setPoints(1)
                .setType("resources")
                .setResources(requirements)
                .build();
        Player mockPlayer = new PlayerBuilder()
                .setResource(Resource.FUNGI, 2)
                .build();

        int result = function.apply(mockPlayer);
        assertEquals(result, 2);
    }

    @Test
    public void testMissingResources() throws InvalidTypeException {
        Map<Resource, Integer> requirements = new HashMap<>() {{
            put(Resource.FUNGI, 2);
        }};
        Function<Player, Integer> function = new FunctionBuilder()
                .setPoints(1)
                .setType("resources")
                .setResources(requirements)
                .build();
        Player mockPlayer = new PlayerBuilder()
                .setResource(Resource.FUNGI, 1)
                .build();

        int result = function.apply(mockPlayer);
        assertEquals(result, 0);
    }


    @Test
    public void testCover4Cards() throws InvalidTypeException {

        Deck<StartingCard> startingCards = DeckFactory.fullStarting();
        Player player = new Player("", null);

        Function<Player, Integer> score = new FunctionBuilder()
                .setPoints(2)
                .setType("cover")
                .build();

        player.setFirstCard(startingCards.draw());
        PlayerBoard board = player.getPlayerBoard();
        assertNotNull(board);

        Coordinates scoring_card_pos = new Coordinates(1, 1);
        // Place neighbors of scoring cards
        for (var neighbor : scoring_card_pos.getNeighbors()) {
            board.placeCard(startingCards.draw(), neighbor);
        }
        // Place scoring card
        board.placeCard(startingCards.draw(), scoring_card_pos);
        // Call score function
        int result = score.apply(player);
        assertEquals(result, 8);

    }
}
