package it.polimi.ingsw.helpers.exceptions;

/**
 * Exception thrown when attempting to create a new player with an existing username.
 * @author Alessio Guarisco
 */
public class ExistingUsernameException extends Exception{

    /**
     * Constructs a new ExistingUsernameException with the specified error message.
     * @param message The detail message of the exception.
     */
    public ExistingUsernameException(String message) {
        super(message);
    }
}
