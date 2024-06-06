package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.board.Coordinates;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RotateBoardTest {
    @Test
    public void testRotateBoardCenter() {
        Coordinates origin = new Coordinates(0, 0);
        Coordinates rotated = RotateBoard.rotateCoordinates(origin, 45);

        assertEquals(origin, rotated);
    }

    @Test
    public void testRotateBoardRotateOneZero() {
        Coordinates origin = new Coordinates(1, 0);
        Coordinates expected = new Coordinates(1, 1);
        Coordinates rotated = RotateBoard.rotateCoordinates(origin, 45);

        assertEquals(expected, rotated);
    }

    @Test
    public void testRotateBoardRotateTwoOne() {
        Coordinates origin = new Coordinates(2, 1);
        Coordinates expected = new Coordinates(1, 3);
        Coordinates rotated = RotateBoard.rotateCoordinates(origin, 45);

        assertEquals(expected, rotated);
    }

    @Test
    /**
     *  #         #
     *  ## ==>   #
     *  #       # #
     */ public void testRotateBoardCross() {
        Map<Coordinates, Integer> origin = new HashMap<>() {{
            put(new Coordinates(0, 0), 1);
            put(new Coordinates(1, 0), 1);
            put(new Coordinates(0, 1), 1);
            put(new Coordinates(0, -1), 1);
        }};

        Map<Coordinates, Integer> expected = new HashMap<>() {{
            put(new Coordinates(0, 0), 1);
            put(new Coordinates(1, 1), 1);
            put(new Coordinates(1, -1), 1);
            put(new Coordinates(-1, -1), 1);
        }};

        Map<Coordinates, Integer> rotated = RotateBoard.rotateBoard(origin);

        assertEquals(expected, rotated);
    }
}
