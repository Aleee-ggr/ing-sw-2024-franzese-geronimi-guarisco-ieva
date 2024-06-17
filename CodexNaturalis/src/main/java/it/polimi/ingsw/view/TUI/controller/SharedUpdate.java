package it.polimi.ingsw.view.TUI.controller;

public class SharedUpdate {
    private final Object lock = new Object();
    private volatile Boolean update = false;

    private boolean locked = false;

    public void update() {
        synchronized (lock) {
            update = true;
        }
    }

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

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }
}
