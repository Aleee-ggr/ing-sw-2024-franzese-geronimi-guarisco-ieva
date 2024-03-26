package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.exceptions.ExistingUsernameException;
import it.polimi.ingsw.model.exceptions.TooManyPlayersException;
import it.polimi.ingsw.model.player.Player;

import java.sql.ShardingKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Game Class
 * @author Alessio Guarisco
 * */
public class Game {
    private final UUID id;
    private int numPlayers;
    private String firstPlayer;
    private List<Player> players = new ArrayList<>();
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

    public List<Player> getPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public SharedBoard getGameBoard() {
        return GameBoard;
    }

    /**
     * Method used to add a Player in Game
     * @param playerUsername is the player username as a String
     * @throws TooManyPlayersException while trying to add a player when the Game is full
     * @throws ExistingUsernameException while there is already a player with the same username in game
     * the exceptions are managed by the caller!
     * */
    public void addPlayer(String playerUsername) throws TooManyPlayersException, ExistingUsernameException{ /*TODO: the caller needs to manage these exception*/
        if(players.size() >= GameConsts.maxPlayersNum) {
            throw new TooManyPlayersException("Too Many Players");
        }
        Player toAdd = new Player(playerUsername, this);
        for(Player p : players){
            if(Objects.equals(p.getUsername(), playerUsername)){
                throw new ExistingUsernameException("Username Already Exists in this game");
            }
        }
        players.add(toAdd);
    }

    /**
     * TODO: methods to implements:
     */
    public void startGame(){
    }
    public boolean resetBoard(/*SharedBoard*/){
        return true;
    }
    public void endGame(){
    }
}
