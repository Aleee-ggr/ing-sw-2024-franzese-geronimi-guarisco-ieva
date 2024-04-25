package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.board.Coordinates;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface RmiServerInterface extends Remote {
    /**
     * Make the given player in the game with the provided UUID draw a card based on the position: <br/>
     * 0-3: visible cards
     * 4: stdCard
     * 5: goldCard
     * @see it.polimi.ingsw.model.board.SharedBoard#draw(Integer)
     * @param game the id of the game
     * @param player the name of the player
     * @param position the position of the card
     * @return the id of the drawn card
     * @throws RemoteException rmi exception
     */
    Integer drawCard(UUID game, String player, Integer position) throws RemoteException;

    /**
     * Place a card from the players hand at the given coordinates
     * @param game the id of the game
     * @param player the name of the player
     * @param coordinates the coordinates where to place the card
     * @param cardID the id of the placed card
     * @return true if the operation was successful, false otherwise
     * @throws RemoteException rmi exception
     */
    boolean placeCard(UUID game, String player, Coordinates coordinates, Integer cardID) throws RemoteException;

    /**
     * Create a new game with the given capacity, returning the game uuid
     * @param player_count the number of players
     * @return the uuid of the game if the operation was successful, null otherwise
     * @throws RemoteException rmi exception
     */
    UUID newGame(Integer player_count) throws RemoteException;

    boolean join(UUID game, String name) throws RemoteException;

    boolean chooseStartingObjective(UUID game, String name, Integer objectiveId) throws RemoteException;

    String postChat(UUID game, String name, String message) throws RemoteException;

    void waitUpdate(UUID game, String name) throws RemoteException;

}
