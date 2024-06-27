package it.polimi.ingsw.helpers.exceptions.model;

/**
 * Exception thrown when attempting to add a card to a full Hand.
 */
public class HandFullException extends Exception {
    /**
     * Constructs a new HandFullException with the specified error message.
     *
     * @param message The detail message of the exception.
     */
    public HandFullException(String message) {
        super(message);
    }
}

