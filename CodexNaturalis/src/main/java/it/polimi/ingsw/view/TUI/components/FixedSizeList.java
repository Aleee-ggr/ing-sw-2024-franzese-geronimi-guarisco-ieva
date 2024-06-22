package it.polimi.ingsw.view.TUI.components;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a fixed-size list that stores elements of type T.
 *
 * @param <T> The type of elements stored in the list.
 */
public class FixedSizeList<T> implements Iterable<T> {
    private final int size;
    private final ArrayList<T> list = new ArrayList<>();

    /**
     * Constructs a FixedSizeList with the specified maximum size.
     *
     * @param size The maximum size of the fixed-size list.
     */
    public FixedSizeList(int size) {
        this.size = size;
    }

    /**
     * Adds a new element to the list. If adding the element exceeds the maximum size,
     * the oldest element in the list is removed.
     *
     * @param value The element to add to the list.
     */
    public void add(T value) {
        list.add(value);
        if (list.size() > this.size) {
            list.removeFirst();
        }
    }

    /**
     * Retrieves the element at the specified index in the list.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     */
    public T get(int index) {
        return list.get(index);
    }

    /**
     * Returns the current number of elements in the list.
     *
     * @return The current size of the list.
     */
    public int currentSize() {
        return list.size();
    }

    /**
     * Returns the maximum size of the fixed-size list.
     *
     * @return The maximum size of the list.
     */
    public int size() {
        return size;
    }

    /**
     * Clears all elements from the list, making it empty.
     */
    public void clear() {
        list.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }


}
