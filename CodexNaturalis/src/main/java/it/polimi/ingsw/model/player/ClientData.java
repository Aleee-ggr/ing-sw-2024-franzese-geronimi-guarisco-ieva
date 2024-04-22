package it.polimi.ingsw.model.player;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.board.PlayerBoard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the data associated with a client in the game, including username, hand, player number, player boards, and scores.
 * @author Alessio Guarisco
 */
public class ClientData {
    private final String username;
    private int[] hand = new int[GameConsts.firstHandDim];
    private int playerNum;
    private final Map<String, PlayerBoard> playerBoardMap;
    private final ConcurrentHashMap<String, Integer> scoreMap;

    /**
     * Constructs a new ClientData object with the specified username.
     * @param username The username of the client.
     */
    public ClientData(String username) {
        this.username = username;
        playerBoardMap = new HashMap<>();
        scoreMap = new ConcurrentHashMap<>();
    }

    /**
     * Getter for the hand of the client.
     * @return An array representing the hand of the client.
     */
    public int[] getHand() {
        return hand;
    }

    /**
     * Getter for the username of the client.
     * @return The username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the player number of the client.
     * @return The player number of the client.
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * Getter for an unmodifiable map of the players boards.
     * @return An unmodifiable map of the players boards.
     */
    public Map<String, PlayerBoard> getPlayerBoard() {
        return Collections.unmodifiableMap(playerBoardMap);
    }

    /**
     * Getter for a copy of the players score map.
     * @return A copy of the players score map.
     */
    public ConcurrentHashMap<String, Integer> getScoreMap() {
        return new ConcurrentHashMap<>(scoreMap);
    }

    /**
     * Setter for the number of players in the game.
     * @param playerNum The player number to set.
     */
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * Setter for a new hand of id of cards for the client.
     * @param newHand The new hand to set.
     */
    public void setNewHand(int[] newHand) {
        this.hand = newHand;
    }

    /**
     * Updates the player board associated with the specified username.
     * @param username    The username of the player.
     * @param playerBoard The player board to update.
     */
    public void updatePlayerBoard(String username, PlayerBoard playerBoard) {
        playerBoardMap.put(username, playerBoard);
    }

    /**
     * Updates the score of the client associated with the specified username.
     * @param username The username of the client.
     * @param newScore The new score to set.
     */
    public void updateScoreMap(String username, Integer newScore) {
        scoreMap.put(username, newScore);
    }

}
