package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.HashMap;
import java.util.Map;

public abstract class RotateBoard {
    private static final Integer rotationAngle = -45;
    public static Map<Coordinates, Integer> rotateBoard(Map<Coordinates, Integer> board) {
        Map<Coordinates, Integer> rotatedBoard= new HashMap<>();

        for (Coordinates coordinate : board.keySet()) {
            int x = coordinate.x();
            int y = coordinate.y();
            int value = board.get(coordinate);

            // matrix rotation with dilation
            double radians = Math.toRadians(rotationAngle);
            int rotated_x = (int) Math.round((x * Math.cos(radians) - y * Math.sin(radians)) * Math.sqrt(2));
            int rotated_y = (int) Math.round((x * Math.sin(radians) + y * Math.cos(radians)) * Math.sqrt(2));

            rotatedBoard.put(new Coordinates(rotated_x, rotated_y), value);
        }
        return rotatedBoard;
    }
}
