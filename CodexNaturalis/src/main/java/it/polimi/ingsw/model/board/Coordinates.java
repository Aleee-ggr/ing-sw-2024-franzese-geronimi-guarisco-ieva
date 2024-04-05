package it.polimi.ingsw.model.board;

import java.util.ArrayList;
import java.util.List;

public record Coordinates(int x, int y) {
    public Coordinates side(int offset) {
        return new Coordinates(x + offset, y);
    }

    public Coordinates vertical(int offset) {
        return new Coordinates(x, y + offset);
    }

    public Coordinates subtract(Coordinates c2) {
        return new Coordinates(
                x - c2.x(),
                y - c2.y()
        );
    }

    public Coordinates add(Coordinates c2) {
        return new Coordinates(
                x + c2.x(),
                y + c2.y()
        );
    }

    public List<Coordinates> getNeighbors() {
        List<Coordinates> out =  new ArrayList<>();
        out.add(side(-1));
        out.add(side(1));
        out.add(vertical(-1));
        out.add(vertical(1));
        return out;
    }
}
