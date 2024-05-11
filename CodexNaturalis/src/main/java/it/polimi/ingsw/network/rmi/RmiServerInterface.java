package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 * The RmiServerInterface defines methods for interacting with a remote game server using RMI.
 * This interface includes various operations related to managing and participating in a game, such as drawing and placing cards,
 * creating and joining games, choosing starting objectives, posting chat messages, and verifying player credentials.
 *
 * The interface extends the Remote class, which means that all of its methods can be invoked by remote clients via RMI.
 * As a result, each method throws a RemoteException in case of communication errors or other issues related to RMI.
 *
 */
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
     * @throws RemoteException If an RMI error occurs.
     */
    Integer drawCard(UUID game, String player, Integer position) throws RemoteException;

    /**
     * Place a card from the players hand at the given coordinates
     * @param game the id of the game
     * @param player the name of the player
     * @param coordinates the coordinates where to place the card
     * @param cardID the id of the placed card
     * @return true if the operation was successful, false otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    boolean placeCard(UUID game, String player, Coordinates coordinates, Integer cardID) throws RemoteException;

    /**
     * Create a new game with the given capacity, returning the game uuid
     * @param player_count the number of players
     * @return the uuid of the game if the operation was successful, null otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    UUID newGame(Integer player_count) throws RemoteException;

    Integer getStartingCard(UUID game, String name) throws RemoteException;

    HashMap<String, Integer> getScoreMap(UUID game, String name) throws RemoteException;

    ArrayList<Integer> getHand(UUID game, String name) throws RemoteException;

    ArrayList<Integer> getCommonObjectives(UUID game, String name) throws RemoteException;

    HashMap<Resource, Integer> getPlayerResources(UUID game, String name, String nameRequiredData) throws RemoteException;

    ArrayList<String> getPlayers(UUID game, String name) throws RemoteException;

    GameState getGameState(UUID game, String name) throws RemoteException;

    ArrayList<Integer> getVisibleCards(UUID game, String name) throws RemoteException;

    ArrayList<Integer> getBackSideDecks(UUID game, String name) throws RemoteException;

    ArrayList<Coordinates> getValidPlacements(UUID game, String name) throws RemoteException;

    HashMap<Coordinates, Integer> getBoard(UUID game, String name, String nameRequiredData) throws RemoteException;

    ArrayList<Resource> getHandColor(UUID game, String name, String nameRequiredData) throws RemoteException;

    boolean choosePersonalObjective(UUID game, String username, Integer objectiveId) throws RemoteException;

    ArrayList<Integer> getStartingObjectives(UUID game, String username) throws RemoteException;

    Deque<Integer> getPlacingOrder(UUID game, String name, String nameRequiredData) throws RemoteException;

    boolean setStartingCard(UUID game, String username, boolean frontSideUp) throws RemoteException;

    /**
     * Allows a player to join an existing game.
     * @param game The UUID of the game.
     * @param name The name of the player joining the game.
     * @return True if the player successfully joins the game, false otherwise.
     * @throws RemoteException If an RMI error occurs.
     */
    boolean join(UUID game, String name) throws RemoteException;

    /**
     * Allows a player to choose a starting objective.
     * @param game The UUID of the game.
     * @param name The name of the player choosing the objective.
     * @param objectiveId The ID of the chosen objective.
     * @return True if the choice is successful, false otherwise.
     * @throws RemoteException If an RMI error occurs.
     */
    boolean chooseStartingObjective(UUID game, String name, Integer objectiveId) throws RemoteException;

    /**
     * Allows a player to post a chat message in the game.
     * @param game The UUID of the game.
     * @param name The name of the player posting the message.
     * @param message The chat message to post.
     * @return The posted message.
     * @throws RemoteException If an RMI error occurs.
     */
    String postChat(UUID game, String name, String message) throws RemoteException;

    void wait(UUID game, String name) throws RemoteException;

    /**
     * Check whether the given credentials are valid (size < 16 and username is not reused)
     * @param username the username of the player
     * @param password the password of the player
     * @return true if the given credentials are valid, false otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    boolean checkCredentials(String username, String password) throws RemoteException;

    ArrayList<UUID> getAvailableGames(String username) throws RemoteException;
}
