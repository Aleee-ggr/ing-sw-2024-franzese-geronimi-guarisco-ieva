package it.polimi.ingsw.model.board;

import java.util.ArrayList;
import java.util.List;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Coordinates side(int offset) {
        return new Coordinates(x + offset, y);
    }

    public Coordinates vertical(int offset) {
        return new Coordinates(x, y + offset);
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
