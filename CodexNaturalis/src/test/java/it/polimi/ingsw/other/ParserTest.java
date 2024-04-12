package it.polimi.ingsw.other;

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
                            "type": resources,
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

        int score = goldCard.getCalculateScore(dummy);

        assertEquals(score, 10);
    }
}
