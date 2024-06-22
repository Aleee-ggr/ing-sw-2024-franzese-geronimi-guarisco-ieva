package it.polimi.ingsw.view.TUI.controller;

/**
 * The SharedUpdate class is a thread-safe utility for managing update notifications.
 * It allows multiple threads to communicate about updates, ensuring synchronized access to the update state.
 */
public class SharedUpdate {
    private final Object lock = new Object();
    private volatile Boolean update = false;

    private boolean locked = false;

    /**
     * Sets the update state to true, indicating that an update has occurred.
     * This method is synchronized to ensure thread safety.
     */
    public void update() {
        synchronized (lock) {
            update = true;
        }
    }

    /**
     * Retrieves the current update state.
     * If the instance is locked, it always returns false.
     * Otherwise, it returns the current update state and resets it to false.
     *
     * @return The current update state, or false if locked.
     */
    public Boolean getUpdate() {
        if (locked) {
            return false;
        }
        synchronized (lock) {
            boolean old = update;
            update = false;
            return old;
        }
    }

    /**
     * Locks the instance, preventing updates from being retrieved.
     */
    public void lock() {
        this.locked = true;
    }

    /**
     * Unlocks the instance, allowing updates to be retrieved again.
     */
    public void unlock() {
        this.locked = false;
    }
}
