package it.polimi.ingsw.controller.threads;

public class Shared<T>{
    volatile T value;
    public synchronized T getValue() {
        return value;
    }

    public synchronized void setValue(T value) {
        this.value = value;
    }
}
