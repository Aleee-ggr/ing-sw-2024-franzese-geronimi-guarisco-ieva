package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 * The RmiServerInterface defines methods for interacting with a remote game server using RMI.
 * This interface includes various operations related to managing and participating in a game, such as drawing and placing cards,
 * creating and joining games, choosing starting objectives, posting chat messages, and verifying player credentials.
 * <p>
 * The interface extends the Remote class, which means that all of its methods can be invoked by remote clients via RMI.
 * As a result, each method throws a RemoteException in case of communication errors or other issues related to RMI.
 */
public interface RmiServerInterface extends Remote {
    /**
     * Make the given player in the game with the provided UUID draw a card based on the position:
     * 0-3: visible cards
     * 4: stdCard
     * 5: goldCard
     *
     * @param game     the id of the game
     * @param player   the name of the player
     * @param position the position of the card
     * @return the id of the drawn card
     * @throws RemoteException If an RMI error occurs.
     * @see it.polimi.ingsw.model.board.SharedBoard#draw(Integer)
     */
    Integer drawCard(UUID game, String player, Integer position) throws RemoteException;

    /**
     * Place a card from the players hand at the given coordinates
     *
     * @param game        the id of the game
     * @param player      the name of the player
     * @param coordinates the coordinates where to place the card
     * @param cardID      the id of the placed card
     * @return true if the operation was successful, false otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    Boolean placeCard(UUID game, String player, Coordinates coordinates, Integer cardID) throws RemoteException;

    /**
     * Create a new game with the given capacity, returning the game uuid
     *
     * @param player_count the number of players
     * @return the uuid of the game if the operation was successful, null otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    UUID newGame(Integer player_count, String gameName) throws RemoteException;

    /**
     * Get the color of the player in the specified game.
     *
     * @param game             the ID of the game
     * @param name             the name of the player
     * @param nameRequiredData the name of the player whose color is requested
     * @return the color of the player
     * @throws RemoteException If an RMI error occurs.
     */
    Color getPlayerColor(UUID game, String name, String nameRequiredData) throws RemoteException;

    /**
     * Get the available colors for a player to choose from in the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return a list of available colors
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Color> getAvailableColors(UUID game, String name) throws RemoteException;

    /**
     * Get the starting card ID for the player in the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return the ID of the starting card
     * @throws RemoteException If an RMI error occurs.
     */
    Integer getStartingCard(UUID game, String name) throws RemoteException;

    /**
     * Get the score map for the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return a map of player usernames to their scores
     * @throws RemoteException If an RMI error occurs.
     */
    HashMap<String, Integer> getScoreMap(UUID game, String name) throws RemoteException;

    /**
     * Get the hand of cards for the player in the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return a list of card IDs representing the player's hand
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Integer> getHand(UUID game, String name) throws RemoteException;

    /**
     * Get the common objectives available for the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return a list of common objective IDs
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Integer> getCommonObjectives(UUID game, String name) throws RemoteException;

    /**
     * Get the personal objective chosen by the player in the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return the ID of the personal objective
     * @throws RemoteException If an RMI error occurs.
     */
    Integer getPersonalObjective(UUID game, String name) throws RemoteException;

    /**
     * Get the resources owned by the player in the specified game.
     *
     * @param game             the ID of the game
     * @param name             the name of the player
     * @param nameRequiredData the name of the player whose resources are requested
     * @return a map of resources to their quantities
     * @throws RemoteException If an RMI error occurs.
     */
    HashMap<Resource, Integer> getPlayerResources(UUID game, String name, String nameRequiredData) throws RemoteException;

    /**
     * Get the list of players in the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return a list of player usernames
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<String> getPlayers(UUID game, String name) throws RemoteException;

    /**
     * Get the current state of the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return the current state of the game
     * @throws RemoteException If an RMI error occurs.
     */
    GameState getGameState(UUID game, String name) throws RemoteException;

    /**
     * Get the IDs of the visible cards in the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return a list of visible card IDs
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Integer> getVisibleCards(UUID game, String name) throws RemoteException;

    /**
     * Get the IDs of the back side decks in the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return a list of back side deck IDs
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Integer> getBackSideDecks(UUID game, String name) throws RemoteException;

    /**
     * Get the valid placements for placing a card in the specified game.
     *
     * @param game the ID of the game
     * @param name the name of the player
     * @return a list of valid coordinates for placing a card
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Coordinates> getValidPlacements(UUID game, String name) throws RemoteException;

    /**
     * Get the board state in the specified game.
     *
     * @param game             the ID of the game
     * @param name             the name of the player
     * @param nameRequiredData the name of the player whose board state is requested
     * @return a map of coordinates to card IDs representing the board state
     * @throws RemoteException If an RMI error occurs.
     */
    HashMap<Coordinates, Integer> getBoard(UUID game, String name, String nameRequiredData) throws RemoteException;

    /**
     * Get the colors of the hand cards of a player in the specified game.
     *
     * @param game             the ID of the game
     * @param name             the name of the player
     * @param nameRequiredData the name of the player whose hand colors are requested
     * @return a list of resources representing the colors of the hand cards
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Resource> getHandColor(UUID game, String name, String nameRequiredData) throws RemoteException;

    /**
     * Get the types of the hand cards of a player in the specified game.
     *
     * @param game             the ID of the game
     * @param name             the name of the player
     * @param nameRequiredData the name of the player whose hand types are requested
     * @return a list of booleans representing whether the cards are gold
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Boolean> getHandType(UUID game, String name, String nameRequiredData) throws RemoteException;

    /**
     * Choose a personal objective for the player in the specified game.
     *
     * @param game        the ID of the game
     * @param username    the name of the player
     * @param objectiveId the ID of the personal objective to choose
     * @return true if the operation was successful, false otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    boolean choosePersonalObjective(UUID game, String username, Integer objectiveId) throws RemoteException;

    /**
     * Choose a player color for the player in the specified game.
     *
     * @param game        the ID of the game
     * @param username    the name of the player
     * @param playerColor the color to choose for the player
     * @return true if the operation was successful, false otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    boolean choosePlayerColor(UUID game, String username, Color playerColor) throws RemoteException;

    /**
     * Get the starting objectives available for the player in the specified game.
     *
     * @param game     the ID of the game
     * @param username the name of the player
     * @return a list of starting objective IDs
     * @throws RemoteException If an RMI error occurs.
     */
    ArrayList<Integer> getStartingObjectives(UUID game, String username) throws RemoteException;

    /**
     * Get the placing order for the players in the specified game.
     *
     * @param game             the ID of the game
     * @param name             the name of the player
     * @param nameRequiredData the name of the player whose placing order is requested
     * @return a deque of player usernames representing the placing order
     * @throws RemoteException If an RMI error occurs.
     */
    Deque<Integer> getPlacingOrder(UUID game, String name, String nameRequiredData) throws RemoteException;

    /**
     * Set the starting card for the player in the specified game.
     *
     * @param game        the ID of the game
     * @param username    the name of the player
     * @param frontSideUp true if the front side of the card is up, false if the back side is up
     * @return true if the operation was successful, false otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    boolean setStartingCard(UUID game, String username, boolean frontSideUp) throws RemoteException;

    /**
     * Allows a player to join an existing game.
     *
     * @param game The UUID of the game.
     * @param name The name of the player joining the game.
     * @return True if the player successfully joins the game, false otherwise.
     * @throws RemoteException If an RMI error occurs.
     */
    boolean join(UUID game, String name) throws RemoteException;

    /**
     * Allows a player to post a chat message in the game.
     *
     * @param game    The UUID of the game.
     * @param sender  The name of the player posting the message.
     * @param message The chat message to post.
     * @throws RemoteException If an RMI error occurs.
     */
    void postChat(UUID game, String sender, String message, String receiver) throws RemoteException;

    /**
     * Wait for a player's turn in the game.
     *
     * @param game The UUID of the game.
     * @param name The name of the player.
     * @return The current wait state for the player's turn.
     * @throws RemoteException If an RMI error occurs.
     */
    WaitState wait(UUID game, String name) throws RemoteException;

    /**
     * Get the name of the player whose turn it is in the game.
     *
     * @param game The UUID of the game.
     * @param name The name of the player.
     * @return The name of the player whose turn it is.
     * @throws RemoteException If an RMI error occurs.
     */
    String getTurnPlayer(UUID game, String name) throws RemoteException;

    /**
     * Check whether the given credentials are valid (size less than 16 and username is not reused)
     *
     * @param username the username of the player
     * @param password the password of the player
     * @return true if the given credentials are valid, false otherwise
     * @throws RemoteException If an RMI error occurs.
     */
    boolean checkCredentials(String username, String password) throws RemoteException;

    /**
     * Get a map of available games and their UUIDs.
     *
     * @param username the username of the player
     * @return a map of available games and their UUIDs
     * @throws RemoteException If an RMI error occurs.
     */
    Map<UUID, String> getAvailableGames(String username) throws RemoteException;

    /**
     * Fetch the chat messages in the specified game.
     *
     * @param game     the ID of the game
     * @param username the name of the player
     * @return a list of chat messages
     * @throws RemoteException If an RMI error occurs.
     */
    List<ChatMessage> fetchChat(UUID game, String username) throws RemoteException;

    /**
     * Ping the server to check connectivity.
     *
     * @param game     the ID of the game
     * @param username the name of the player
     * @throws RemoteException If an RMI error occurs.
     */
    void ping(UUID game, String username) throws RemoteException;
}
