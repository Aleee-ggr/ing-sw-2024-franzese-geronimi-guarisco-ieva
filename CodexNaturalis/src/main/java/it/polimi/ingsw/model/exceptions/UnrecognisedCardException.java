package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when attempting to read an unrecognised card's corners.
 * @author Alessio Guarisco
 */
public class UnrecognisedCardException extends RuntimeException{

    /**
     * Constructs a new UnrecognisedCardException with the specified error message.
     * @param message The detail message of the exception.
     */
    public UnrecognisedCardException(String message) {
        super(message);
    }
}

