package it.polimi.ingsw.model.player;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.board.PlayerBoard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientData {
    private final String username;
    private int[] hand = new int[GameConsts.firstHandDim];
    private int playerNum;
    private final Map<String, PlayerBoard> playerBoardMap;
    private final ConcurrentHashMap<String, Integer> scoreMap;

    public ClientData(String username) {
        this.username = username;
        playerBoardMap = new HashMap<>();
        scoreMap = new ConcurrentHashMap<>();
    }

    public int[] getHand() {
        return hand;
    }

    public String getUsername() {
        return username;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public Map<String, PlayerBoard> getPlayerBoard() {
        return Collections.unmodifiableMap(playerBoardMap);
    }

    public ConcurrentHashMap<String, Integer> getScoreMap() {
        return new ConcurrentHashMap<>(scoreMap);
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public void setNewHand(int[] newHand) {
        this.hand = newHand;
    }


    public void updatePlayerBoard(String username, PlayerBoard playerBoard) {
        playerBoardMap.put(username, playerBoard);
    }

    public void updateScoreMap(String username, Integer newScore) {
        scoreMap.put(username, newScore);
    }

}
