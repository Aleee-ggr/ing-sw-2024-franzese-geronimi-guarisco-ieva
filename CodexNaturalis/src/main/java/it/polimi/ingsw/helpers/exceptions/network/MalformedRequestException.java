package it.polimi.ingsw.helpers.exceptions.network;

public class MalformedRequestException extends ServerConnectionException{
    public MalformedRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
