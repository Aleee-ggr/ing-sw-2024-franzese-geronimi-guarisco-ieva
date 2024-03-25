package it.polimi.ingsw.model.exceptions;

public class ExistingUsernameException extends Exception{
    public ExistingUsernameException(String message) {
        super(message);
    }
}
