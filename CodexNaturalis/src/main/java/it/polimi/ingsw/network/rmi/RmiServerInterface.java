package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.helpers.exceptions.network.ServerConnectionException;
import it.polimi.ingsw.model.cards.Card;

import java.rmi.Remote;

public interface RmiServerInterface extends Remote {
    public Card drawCard() throws ServerConnectionException;

    public void placeCard() throws ServerConnectionException;
}
