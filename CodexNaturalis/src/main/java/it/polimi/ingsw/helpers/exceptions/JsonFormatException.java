package it.polimi.ingsw.helpers.exceptions;

public class JsonFormatException extends Exception {
    /**
     * An exception thrown by implementations of {@link it.polimi.ingsw.helpers.parsers.JsonParser}
     * when they find an error in the format or values of the json
     */
    public JsonFormatException() {
        super();
    }
    /**
     * An exception thrown by implementations of {@link it.polimi.ingsw.helpers.parsers.JsonParser}
     * when they find an error in the format or values of the json
     * @param e a throwable object to append to this call
     */
    public JsonFormatException(Throwable e) {
        super(e);
    }
    /**
     * An exception thrown by implementations of {@link it.polimi.ingsw.helpers.parsers.JsonParser}
     * when they find an error in the format or values of the json
     * @param msg a message to describe the error
     */
    public JsonFormatException(String msg) {
        super(msg);
    }
}
