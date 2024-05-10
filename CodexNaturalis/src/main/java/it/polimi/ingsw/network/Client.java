package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.threads.GameState;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.ClientData;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.client.PlayerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Abstract class, baseline for building a client that can connect to a server to participate in a game.
 * @author Alessio Guarisco
 * @author Daniele Ieva
 */
public class Client {
    protected final String serverAddress;
    protected final int serverPort;

    protected String username;
    protected String password;

    protected UUID gameId;
    protected GameState gameState;
    protected ArrayList<UUID> availableGames;
    protected int playerNum = 0;
    protected ArrayList<String> players;
    protected HashMap<String, ClientData> playerData;
    protected HashMap<String, Integer> scoreMap;

    protected ArrayList<Card> visibleCards;
    protected ArrayList<Card> backSideDecks;


    /**
     * Constructs a new Client object with the specified player username, server address, and server port.
     * @param serverAddress  The address of the server.
     * @param serverPort     The port of the server.
     */
    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.availableGames = new ArrayList<>();
        this.players = new ArrayList<>();
        this.scoreMap = new HashMap<>();
        this.playerData = new HashMap<>();
        this.gameState = GameState.LOBBY;
    }

    public String getUsername() {
        return username;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<UUID> getAvailableGames() {
        return this.availableGames;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public ArrayList<String> getPlayers() {
        return this.players;
    }

    public HashMap<String, ClientData> getOpponentData() {
        return this.playerData;
    }

    public HashMap<String, Integer> getScoreMap() {
        return this.scoreMap;
    }

    public ArrayList<Card> getVisibleCards() {
        return visibleCards;
    }

    public ArrayList<Card> getDecksBacks() {
        return backSideDecks;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public void createPlayerData(ArrayList<String> players) {
        for(String player : players){
            if(player.equals(this.username)) {
                playerData.put(player, new PlayerData());
            } else {
                playerData.put(player, new OpponentData());
            }
            this.scoreMap.put(player, 0);
        }

        System.out.println(scoreMap);
        this.players = players;
        this.playerNum = players.size();
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public void setPlayerData(HashMap<String, ClientData> playerData) {
        this.playerData = playerData;
    }

    public void setScoreMap(HashMap<String, Integer> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public void setVisibleCards(ArrayList<Card> visibleCards) {
        this.visibleCards = visibleCards;
    }

    public void setBackSideDecks(ArrayList<Card> backSideDecks) {
        this.backSideDecks = backSideDecks;
    }

    public PlayerData getPlayerData() {
        return (PlayerData) playerData.get(username);
    }

    //TODO: implementation at RMI/Socket level
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }




}
