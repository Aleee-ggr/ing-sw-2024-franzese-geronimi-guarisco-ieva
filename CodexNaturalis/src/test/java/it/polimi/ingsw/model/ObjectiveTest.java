package it.polimi.ingsw.model;

import it.polimi.ingsw.helpers.PlayerBuilder;
import it.polimi.ingsw.helpers.builders.FunctionBuilder;
import it.polimi.ingsw.helpers.exceptions.InvalidTypeException;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

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
}
