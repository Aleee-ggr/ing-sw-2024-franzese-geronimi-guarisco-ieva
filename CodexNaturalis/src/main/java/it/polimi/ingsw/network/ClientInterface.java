package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.WaitState;
import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.model.ChatMessage;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.ClientData;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.model.enums.Color;

import java.io.IOException;
import java.util.*;

/**
 * This interface defines the methods for client-server communication and the getters and setters for the client.
 * Clients can interact with the server by creating and joining games, managing credentials,
 * exchanging messages, drawing cards, placing cards on the board, and fetching various game
 * state information.
 * It also provides methods to access and modify client-specific data such as visible cards, game state,
 * player number, opponent data, and more.
 */
public interface ClientInterface {

    //Server communication methods

    /**
     * Method to create a new game.
     *
     * @param players The number of players in the game.
     * @return The UUID of the game created.
     * @throws IOException If an I/O error occurs.
     */
    UUID newGame(int players, String gameName) throws IOException;

    /**
     * Method to join an existing game.
     *
     * @param game The UUID of the game to join.
     * @return True if the game was joined successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean joinGame(UUID game) throws IOException;

    /**
     * Method to ping the server.
     *
     * @throws IOException If an I/O error occurs.
     */
    void pingServer() throws IOException;

    /**
     * Method to check the credentials of a user.
     * This method is needed every time a user create a new username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the credentials are valid, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean checkCredentials(String username, String password) throws IOException;

    /**
     * Method to wait for an update from the server.
     *
     * @throws IOException If an I/O error occurs.
     */
    WaitState waitUpdate() throws IOException;

    /**
     * Method to send a chat message to the server.
     *
     * @param message The message to send.
     * @throws IOException If an I/O error occurs.
     */
    void postChat(String message, String receiver) throws IOException;

    /**
     * Method to ask for a drawn card from the server.
     *
     * @param position The index of the deck or visible card to draw.
     * @throws IOException If an I/O error occurs.
     */
    void drawCard(int position) throws IOException;

    /**
     * Method to ask the server to place a card on the board.
     *
     * @param coordinates The coordinates where to place the card.
     * @param CardId      The ID of the card to place.
     * @return True if the card was placed successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean placeCard(Coordinates coordinates, int CardId) throws IOException;

    /**
     * Method to ask the server to place a starting card on the board.
     *
     * @param frontSideUp True if the front side of the card is up, false otherwise.
     * @return True if the card was placed successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean placeStartingCard(boolean frontSideUp) throws IOException;

    /**
     * Method to ask the server to choose a starting objective.
     *
     * @param objectiveId The ID of the chosen objective.
     * @return True if the objective was chosen successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean choosePersonalObjective(int objectiveId) throws IOException;

    /**
     * Method to ask the server to choose a player color.
     *
     * @param playerColor The color chosen by the player.
     * @return True if the player color was chosen successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean choosePlayerColor(Color playerColor) throws IOException;

    /**
     * Method to ask the server to fetch player colors.
     *
     * @return True if the player colors were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchPlayersColors() throws IOException;

    /**
     * Method to ask the server to fetch available colors.
     *
     * @return True if the available colors were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchAvailableColors() throws IOException;

    /**
     * Method to fetch the available games from the server.
     *
     * @return True if the available games were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchAvailableGames() throws IOException;

    /**
     * Method to fetch the game state from the server.
     *
     * @return True if the game state was fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchGameState() throws IOException;

    /**
     * Method to fetch the players from the server.
     *
     * @return True if the players were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchPlayers() throws IOException;

    /**
     * Method to fetch the common objectives from the server.
     *
     * @return True if the common objectives were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchCommonObjectives() throws IOException;

    /**
     * Method to fetch the personal objectives from the server.
     *
     * @return True if the objective was fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchPersonalObjective() throws IOException;

    /**
     * Method to fetch the visible cards and decks from the server.
     * It's used to form a drawing view for the Player.
     *
     * @return True if the visible cards and decks were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchVisibleCardsAndDecks() throws IOException;

    /**
     * Method to fetch the score map from the server.
     *
     * @return True if the score map was fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchScoreMap() throws IOException;

    /**
     * Method to fetch all the players resources from the server.
     *
     * @return True if the players resources were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchPlayersResources() throws IOException;

    /**
     * Method to fetch all the players boards from the server.
     *
     * @return True if the players boards were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchPlayersBoards() throws IOException;

    /**
     * Method to fetch all the players placing order from the server.
     *
     * @return True if the players placing order was fetched successfully, false otherwise.
     */
    boolean fetchPlayersPlacingOrder() throws IOException;

    /**
     * Method to fetch the valid placements of the Player from the server.
     *
     * @return True if the valid placements were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchValidPlacements() throws IOException;

    /**
     * Method to fetch the hand of the client from the server.
     *
     * @return True if the hand was fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchClientHand() throws IOException;

    /**
     * Method to fetch the opponents hand color from the server.
     *
     * @return True if the opponents hand color was fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchOpponentsHandColor() throws IOException;

    /**
     * Method to fetch the opponent's hand type from the server.
     *
     * @return True if the opponent's hand type was fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchOpponentsHandType() throws IOException;

    /**
     * Method to fetch the starting objectives from the server.
     * The starting objectives are the ones the player needs to choose from at the start of the game.
     *
     * @return True if the starting objectives were fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchStartingObjectives() throws IOException;

    /**
     * Method to fetch the starting card from the server.
     * The starting card is the one the player needs to place at the start of the game.
     *
     * @return True if the starting card was fetched successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    boolean fetchStartingCard() throws IOException;

    /**
     * Method to fetch the current turn player from the server.
     *
     * @throws IOException If an I/O error occurs.
     */
    void fetchTurnPlayer() throws IOException;

    /**
     * Method to get the visible cards from the client.
     *
     * @return The ArrayList of visible cards.
     */
    ArrayList<Card> getVisibleCards();

    /**
     * Method to get the decks backs from the client.
     *
     * @return The ArrayList of decks backs.
     */
    ArrayList<Card> getDecksBacks();

    /**
     * Method to get the game state from the client.
     *
     * @return the GameState of the client.
     */
    GameState getGameState();
    
    /**
     * Method to get the player number from the client.
     *
     * @return The player number of the client.
     */
    int getPlayerNum();

    /**
     * Method to get the opponent data from the client.
     *
     * @return The HashMap of opponent data.
     */
    HashMap<String, ClientData> getOpponentData();

    /**
     * Method to get the player data from the client.
     *
     * @return The PlayerData of the client.
     */
    PlayerData getPlayerData();

    /**
     * Method to get the score map from the client.
     *
     * @return The HashMap of the score map.
     */
    HashMap<String, Integer> getScoreMap();

    /**
     * Method to get the available games from the client.
     *
     * @return The ArrayList of available games.
     */
    Map<UUID, String> getAvailableGames();

    /**
     * Method to get the players usernames from the client.
     *
     * @return The ArrayList of players usernames.
     */
    ArrayList<String> getPlayers();

    /**
     * Method to get the username of the client.
     *
     * @return The username of the client.
     */
    String getUsername();

    /**
     * Method to set the credentials of the client.
     *
     * @param username The username of the client.
     * @param password The password of the client.
     */
    void setCredentials(String username, String password);

    /**
     * Fetch the chat logs from the server
     *
     * @return whether the fetch was successful
     */
    boolean fetchChat() throws IOException;

    /**
     * @return a list containing the chat messages saved in the client
     */
    List<ChatMessage> getChat();
}
