package it.polimi.ingsw.view.TUI.controller;

public class Shared<E> {
    private volatile E element;

    public Shared(E element) {
        this.element = element;
    }

    public synchronized E getElement() {
        return element;
    }

    public synchronized void setElement(E element) {
        this.element = element;
    }
}
