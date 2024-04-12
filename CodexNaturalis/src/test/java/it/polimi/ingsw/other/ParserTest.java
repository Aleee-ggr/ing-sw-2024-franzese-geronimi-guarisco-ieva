package it.polimi.ingsw.other;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.PlayerBuilder;
import it.polimi.ingsw.helpers.exceptions.JsonFormatException;
import it.polimi.ingsw.helpers.parsers.GoldCardParser;
import it.polimi.ingsw.helpers.parsers.ObjectiveParser;
import it.polimi.ingsw.helpers.parsers.StartingParser;
import it.polimi.ingsw.helpers.parsers.StdCardParser;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;


public class ParserTest {

    @Test
    public void testEmptyParser() throws JsonFormatException {
        String json = """
                {
                    "goldcards":[],
                    "stdcards":[],
                    "startingcards":[],
                    "objectives":[]
                }
                """;
        GoldCardParser goldCardParser = new GoldCardParser().readString(json);
        StdCardParser stdCardParser = new StdCardParser().readString(json);
        StartingParser startingParser = new StartingParser().readString(json);
        ObjectiveParser objectiveParser = new ObjectiveParser().readString(json);


        Deck<GoldCard> emptyGoldDeck = goldCardParser.parse();
        Deck<StdCard> stdCardDeck = stdCardParser.parse();
        Deck<StartingCard> startingCardDeck = startingParser.parse();
        Deck<Objective> objectiveDeck = objectiveParser.parse();

        assertTrue(emptyGoldDeck.isEmpty());
        assertTrue(stdCardDeck.isEmpty());
        assertTrue(startingCardDeck.isEmpty());
        assertTrue(objectiveDeck.isEmpty());
    }

    @Test
    public void testObjectiveParser() throws JsonFormatException {
        String json = """
                {
                    "objectives":[
                        {
                            "id": -1,
                            "points": 1,
                            "type": "resources",
                            "requirements": {
                                "PLANT": 3
                            }
                        }
                    ]
                }
                """;
        ObjectiveParser objectiveParser = new ObjectiveParser().readString(json);
        Objective objective = objectiveParser.parse().draw();
        Player dummy = new PlayerBuilder()
                .setUsername("")
                .setResource(Resource.PLANT, 6)
                .build();
        int result = objective.getPoints(dummy);
        assertEquals(result, 2);
    }

    @Test
    public void testGoldCardParser() throws JsonFormatException {
        String json = """
                {
                    "goldcards":[
                        {
                            "id":41,
                            "resource": "FUNGI",
                            "corners": ["NONCOVERABLE", "NONE", "NONE", "QUILL"],
                            "points":{
                            "value": 1,
                            "type": "QUILL"
                            },
                            "requirements": {
                            "FUNGI": 2,
                            "ANIMAL": 1
                            }
                        }
                    ]
                }
                """;
        GoldCardParser goldCardParser = new GoldCardParser().readString(json);
        GoldCard goldCard = goldCardParser.parse().draw();

        assertNotNull(goldCard);

        Player dummy = new PlayerBuilder()
                .setUsername("")
                .setResource(Resource.QUILL, 10)
                .build();

        int score = goldCard.getScore(dummy);

        assertEquals(score, 10);
    }

    @Test
    public void workingParserTest() throws JsonFormatException, IOException {
        Deck<GoldCard> goldCardDeck = new GoldCardParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();
        Deck<StdCard> stdCardDeck = new StdCardParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();
        Deck<StartingCard> startingCardDeck = new StartingParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();
        Deck<Objective> objectiveDeck = new ObjectiveParser().readFile(Path.of(GameConsts.cardJsonPath)).parse();

        assertFalse(goldCardDeck.isEmpty());
        assertFalse(stdCardDeck.isEmpty());
        assertFalse(startingCardDeck.isEmpty());
        assertFalse(objectiveDeck.isEmpty());
    }

    @Test
    public void testStdCardFormatExceptions(){
        String json = """
                {
                    "stdcards": [
                        {}
                    ],
                    "goldcards": [
                        {}
                    ],
                    "startingcards": [
                        {}
                    ],
                    "objectives": [
                        {}
                    ]
                }
                """;

        int exceptionCounter = 0;

        try {
            Deck<StdCard> deck = new StdCardParser().readString(json).parse();
        } catch (JsonFormatException e) {
            exceptionCounter++;
        }
        try {
            Deck<GoldCard> deck = new GoldCardParser().readString(json).parse();
        } catch (JsonFormatException e) {
            exceptionCounter++;
        }
        try {
            Deck<StartingCard> deck = new StartingParser().readString(json).parse();
        } catch (JsonFormatException e) {
            exceptionCounter++;
        }
        try {
            Deck<Objective> deck = new ObjectiveParser().readString(json).parse();
        } catch (JsonFormatException e) {
            exceptionCounter++;
        }

        assertEquals(4, exceptionCounter);
    }

    @Test(expected = JsonFormatException.class)
    public void testInvalidType() throws JsonFormatException {
        String json = """
                {
                    "objectives": [
                        {
                          "id":93,
                          "points": 3,
                          "type": "wrong",
                          "requirements": ["NONE", "NONE", "FUNGI", "NONE", "ANIMAL", "NONE", "NONE", "ANIMAL", "NONE"]
                        }
                    ]
                }
                """;
        new ObjectiveParser().readString(json).parse();
    }
}
