package it.polimi.ingsw.helpers.exceptions.model;

import it.polimi.ingsw.helpers.builders.FunctionBuilder;

public class InvalidTypeException extends Exception {
    /**
     * An exception thrown by the FunctionBuilder whenever the given type is not included in
     * {@link FunctionBuilder#getValidTypes()}
     */
    public InvalidTypeException() {
        super();
    }

    /**
     * An exception thrown by the FunctionBuilder whenever the given type is not included in
     * {@link FunctionBuilder#getValidTypes()}
     *
     * @param e a throwable object to append to this call
     */
    public InvalidTypeException(Throwable e) {
        super(e);
    }

    /**
     * An exception thrown by the FunctionBuilder whenever the given type is not included in
     * {@link FunctionBuilder#getValidTypes()}
     *
     * @param msg a message to describe the error
     */
    public InvalidTypeException(String msg) {
        super(msg);
    }
}
