package it.polimi.ingsw.helpers.exceptions.model;

/**
 * Exception thrown when attempting to remove a non-existent card from Hand.
 */
public class ElementNotInHand extends Exception {
    /**
     * Constructs a new ElementNotInHand with the specified error message.
     *
     * @param message The detail message of the exception.
     */
    public ElementNotInHand(String message) {
        super(message);
    }
}

