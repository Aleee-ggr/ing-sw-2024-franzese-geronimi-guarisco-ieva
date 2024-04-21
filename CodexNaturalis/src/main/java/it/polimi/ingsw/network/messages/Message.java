package it.polimi.ingsw.network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected String username;

    public String getUsername() {
        return username;
    }


}
