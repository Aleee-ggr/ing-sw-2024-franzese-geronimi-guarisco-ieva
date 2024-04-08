package it.polimi.ingsw.helpers.exceptions;

/**
 * Exception thrown when attempting to add more players than allowed.
 * @author Alessio Guarisco
 */
public class TooManyPlayersException extends Exception {

    /**
     * Constructs a new TooManyPlayersException with the specified error message.
     * @param message The detail message of the exception.
     */
    public TooManyPlayersException(String message) {
        super(message);
    }
}
