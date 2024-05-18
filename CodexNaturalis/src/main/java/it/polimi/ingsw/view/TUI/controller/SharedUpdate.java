package it.polimi.ingsw.view.TUI.controller;


import static java.lang.Thread.sleep;

public class SharedUpdate {
    private volatile Boolean update = false;
    private final Object lock = new Object();

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
