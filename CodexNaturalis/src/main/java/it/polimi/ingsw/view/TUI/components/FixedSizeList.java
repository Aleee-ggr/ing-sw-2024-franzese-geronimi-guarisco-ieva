package it.polimi.ingsw.view.TUI.components;

import java.util.ArrayList;
import java.util.Iterator;

public class FixedSizeList<T> implements Iterable<T> {
    private final int size;
    private final ArrayList<T> list = new ArrayList<>();

    public FixedSizeList(int size) {
        this.size = size;
    }

    public void add(T value) {
        list.add(value);
        if (list.size() > this.size) {
            list.removeFirst();
        }
    }

    public T get(int index) {
        return list.get(index);
    }

    public int currentSize() {
        return list.size();
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }


}
