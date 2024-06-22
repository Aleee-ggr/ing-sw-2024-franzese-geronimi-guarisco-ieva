package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.HashMap;
import java.util.Map;

/**
 * The RotateBoard class provides static methods to rotate a board represented by a map of coordinates and values.
 * The rotation is performed based on a specified angle, with a default angle of -45 degrees.
 */
public abstract class RotateBoard {
    private static final Integer rotationAngle = -45;

    /**
     * Rotates the board by the specified default rotation angle.
     *
     * @param board The original board represented as a map of coordinates and their corresponding values.
     * @return A new map with coordinates rotated by the default angle and their corresponding values.
     */
    public static Map<Coordinates, Integer> rotateBoard(Map<Coordinates, Integer> board) {
        Map<Coordinates, Integer> rotatedBoard= new HashMap<>();

        for (Coordinates coordinate : board.keySet()) {
            int x = coordinate.x();
            int y = coordinate.y();
            int value = board.get(coordinate);

            Coordinates rotatedCoordinates = rotationMatrix(x, y, rotationAngle);
            rotatedBoard.put(rotatedCoordinates, value);
        }
        return rotatedBoard;
    }

    /**
     * Rotates a given set of coordinates by the specified rotation angle.
     *
     * @param coordinates   The original coordinates to be rotated.
     * @param rotationAngle The angle by which to rotate the coordinates.
     * @return The new coordinates after rotation.
     */
    public static Coordinates rotateCoordinates(Coordinates coordinates, int rotationAngle) {
        int x = coordinates.x();
        int y = coordinates.y();

        return rotationMatrix(x, y, rotationAngle);
    }

    /**
     * Applies a rotation matrix to a pair of coordinates and returns the new coordinates.
     *
     * @param x             The x-coordinate to be rotated.
     * @param y             The y-coordinate to be rotated.
     * @param rotationAngle The angle by which to rotate the coordinates.
     * @return The new coordinates after rotation.
     */
    private static Coordinates rotationMatrix(int x, int y, int rotationAngle) {
        // matrix rotation with dilation
        double radians = Math.toRadians(rotationAngle);
        int rotated_x = (int) Math.round((x * Math.cos(radians) - y * Math.sin(radians)) * Math.sqrt(2));
        int rotated_y = (int) Math.round((x * Math.sin(radians) + y * Math.cos(radians)) * Math.sqrt(2));
        return new Coordinates(rotated_x, rotated_y);
    }
}
