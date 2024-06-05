package it.polimi.ingsw.view.TUI.controller;


public class SharedUpdate {
    private final Object lock = new Object();
    private volatile Boolean update = false;

    public void update() {
        synchronized (lock) {
            update = true;
        }
    }

    public Boolean getUpdate() {
        synchronized (lock) {
            boolean old = update;
            update = false;
            return old;
        }
    }
}
