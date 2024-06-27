package it.polimi.ingsw.helpers.exceptions.model;

/**
 * Exception thrown when attempting to add more players than allowed.
 */
public class TooManyPlayersException extends Exception {

    /**
     * Constructs a new TooManyPlayersException with the specified error message.
     *
     * @param message The detail message of the exception.
     */
    public TooManyPlayersException(String message) {
        super(message);
    }
}
