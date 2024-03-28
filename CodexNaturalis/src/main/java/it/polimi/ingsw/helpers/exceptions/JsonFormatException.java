package it.polimi.ingsw.helpers.exceptions;

public class JsonFormatException extends Exception {
    public JsonFormatException() {
        super();
    }
    public JsonFormatException(Throwable e) {
        super(e);
    }

    public JsonFormatException(String msg) {
        super(msg);
    }
}
