package it.polimi.ingsw.controller.threads;

public class Shared<T>{
    volatile T value;
    synchronized T getValue() {
        return value;
    }

    synchronized void setValue(T value) {
        this.value = value;
    }
}
