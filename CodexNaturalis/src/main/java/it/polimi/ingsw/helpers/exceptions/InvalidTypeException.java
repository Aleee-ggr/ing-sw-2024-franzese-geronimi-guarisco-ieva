package it.polimi.ingsw.helpers.exceptions;

public class InvalidTypeException extends Exception {

    public InvalidTypeException() {
        super();
    }
    public InvalidTypeException(Throwable e) {
        super(e);
    }

    public InvalidTypeException(String msg) {
        super(msg);
    }
}
