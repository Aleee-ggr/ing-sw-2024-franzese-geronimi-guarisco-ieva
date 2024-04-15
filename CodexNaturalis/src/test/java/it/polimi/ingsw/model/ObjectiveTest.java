package it.polimi.ingsw.model;

import it.polimi.ingsw.helpers.DeckFactory;
import it.polimi.ingsw.helpers.PlayerBuilder;
import it.polimi.ingsw.helpers.builders.FunctionBuilder;
import it.polimi.ingsw.helpers.exceptions.model.InvalidTypeException;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectiveTest {

    @BeforeClass
    public static void setup() {
        DeckFactory.setupParser();
    }
    public static Game game = new Game(new UUID(1,1));
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
        Player player = new Player("", game);

        Function<Player, Integer> score = new FunctionBuilder()
                .setPoints(2)
                .setType("cover")
                .build();

        player.setFirstCard(true);
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
        assertEquals(8, result);
    }

    @Test
    public void testForDiagonalDownPattern() throws InvalidTypeException {
        Deck<StartingCard> startingCards = DeckFactory.fullStarting();

        Resource[][] pattern = {
                {Resource.NONE, Resource.NONE, Resource.FUNGI},
                {Resource.NONE, Resource.FUNGI, Resource.NONE},
                {Resource.FUNGI, Resource.NONE,  Resource.NONE}};
        Function<Player, Integer> score = new FunctionBuilder()
                .setPoints(3)
                .setType("pattern")
                .setPattern(pattern)
                .build();
        Player player = new Player("", game);

        player.setFirstCard(true);
        PlayerBoard board = player.getPlayerBoard();
        Card fungi = new StdCard(0, null, Resource.FUNGI, false);
        Coordinates[] fungi_coord = {
                board.getCenter().horizontal(-1),
                board.getCenter().horizontal(-2),
                board.getCenter().horizontal(-3),
                board.getCenter().horizontal(-4),
                board.getCenter().horizontal(-5),
                board.getCenter().horizontal(-6),
                board.getCenter().horizontal(-7)
        };
        for (Coordinates coord : fungi_coord) {
            try {
                board.placeCard(fungi, coord);
            } catch (Throwable ignored) {}
        }

        int points = score.apply(player);
        assertEquals(6, points);
    }

    @Test
    public void testForLShapeDownPattern() throws InvalidTypeException {
        Deck<StartingCard> startingCards = DeckFactory.fullStarting();

        Resource[][] pattern = {
                {Resource.FUNGI, Resource.NONE, Resource.NONE},
                {Resource.NONE, Resource.FUNGI, Resource.NONE},
                {Resource.NONE, Resource.FUNGI,  Resource.NONE}};
        Function<Player, Integer> score = new FunctionBuilder()
                .setPoints(3)
                .setType("pattern")
                .setPattern(pattern)
                .build();
        Player player = new Player("", game);

        player.setFirstCard(true);
        PlayerBoard board = player.getPlayerBoard();
        Card fungi = new StdCard(0, null, Resource.FUNGI, false);
        Card plant = new StdCard(0, null, Resource.PLANT, false);
        Coordinates fungi1 = board.getCenter().vertical(1);
        Coordinates fungi2 = board.getCenter().vertical(2);
        Coordinates fungi3 = board.getCenter().vertical(3).horizontal(-1);
        try {
            board.placeCard(fungi, fungi1);
        } catch (Exception ignored) {}
        try {
            board.placeCard(fungi, fungi2);
        } catch (Exception ignored) {}
        try {
            board.placeCard(fungi, fungi3);
        } catch (Exception ignored) {}
        int points = score.apply(player);
        assertEquals(3, points);
    }

    @Test
    public void testForMissingPattern() throws InvalidTypeException {
        Deck<StartingCard> startingCards = DeckFactory.fullStarting();

        Resource[][] pattern = {
                {Resource.FUNGI, Resource.NONE, Resource.NONE},
                {Resource.NONE, Resource.FUNGI, Resource.NONE},
                {Resource.NONE, Resource.NONE,  Resource.FUNGI}};

        Function<Player, Integer> score = new FunctionBuilder()
                .setPoints(3)
                .setType("pattern")
                .setPattern(pattern)
                .build();
        Player player = new Player("", game);

        player.setFirstCard(true);
        PlayerBoard board = player.getPlayerBoard();
        Card fungi = new StdCard(0, null, Resource.FUNGI, false);
        Card plant = new StdCard(0, null, Resource.PLANT, false);
        Coordinates fungi1 = board.getCenter().horizontal(-1);
        Coordinates fungi2 = board.getCenter().vertical(-2);
        Coordinates plant1 = board.getCenter().vertical(-3).horizontal(1);

        try {
            board.placeCard(fungi, fungi1);
        } catch (Throwable ignored) {}
        int points = score.apply(player);
        assertEquals(0, points);
    }
}
