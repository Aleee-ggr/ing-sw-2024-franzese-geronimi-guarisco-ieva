package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.player.Player;

import java.sql.ShardingKey;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Game Class
 * @author Alessio Guarisco
 * */
public class Game {
    private final UUID id;
    private int numPlayers;
    private String firstPlayer;
    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    private GameState gameState;
    private final SharedBoard GameBoard = new SharedBoard(null, null); //TODO add actual decks

    public Game(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public ConcurrentHashMap<String, Player> getPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public SharedBoard getGameBoard() {
        return GameBoard;
    }

    public boolean addPlayer(String playerUsername){ /**TODO: implement toomanyplayer except and samename except*/
        Player checkPlayer = new Player(playerUsername, this);
        try{
            checkPlayer = players.putIfAbsent(playerUsername, checkPlayer);
        } catch (Exception e){
            System.out.println("tempException");
            return false;
        }
        return true;
    }

    /**
     * TODO: methods to implements:
     */
    public void startGame(){
    }
    public boolean checkUsername(/*String*/){
        return true;
    }
    public boolean resetBoard(/*SharedBoard*/){
        return true;
    }
    public void endGame(){
    }
}
