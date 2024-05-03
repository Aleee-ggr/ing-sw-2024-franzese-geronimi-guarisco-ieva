package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int width = 147;
    public static final int height = 27;
    private final Character[][] board = new Character[height][width];
    private Coordinates center;
    private static final int baseOffset_x = width / 2;
    private static final int baseOffset_y = height / 2;

    public Board() {
        this.center = new Coordinates(0, 0);
    }

    public void setCenter(Coordinates center) {
        this.center = center;
    }



    private Coordinates getOffsetCoordinates(Coordinates coordinates) {
        return new Coordinates(
                coordinates.x() +  baseOffset_x - center.x(),
                coordinates.y() + baseOffset_y - center.y());
    }

    private boolean isInView(Coordinates coordinates) {
        Coordinates relativeCoordinates = getOffsetCoordinates(coordinates);
        return  (relativeCoordinates.x() >= 0 &&
                relativeCoordinates.y() >= 0 &&
                relativeCoordinates.x() < width &&
                relativeCoordinates.y() >= height
        );
    }
}
