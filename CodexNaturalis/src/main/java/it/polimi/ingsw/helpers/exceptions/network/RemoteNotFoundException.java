package it.polimi.ingsw.helpers.exceptions.network;

public class RemoteNotFoundException extends ServerConnectionException{
    public RemoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
