package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.GameState;
import it.polimi.ingsw.model.exceptions.ExistingUsernameException;
import it.polimi.ingsw.model.exceptions.TooManyPlayersException;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Game Class for creating a game with id, players, numPlayers, gameState and SharedBoard.
 * @author Alessio Guarisco
 * @see Player
 * @see SharedBoard
 * @see GameState
 * */
public class Game {
    static final Deck<GoldCard> fullGoldDeck = FullDeck.getFullGoldDeck();
    static final Deck<StdCard> fullStdDeck = FullDeck.getFullStdDeck();
    static final Deck<StartingCard> fullStartingDeck = FullDeck.getFullStartingDeck();
    static final Deck<Objective> fullObjDeck = FullDeck.getFullObjDeck();

    private final UUID id;
    private int numPlayers = 0;
    private final List<Player> players = new ArrayList<>();
    private GameState gameState;
    private final SharedBoard GameBoard;

    /**
     * Constructor for the Game class.
     * @param id UUID created by the Server.
     */
    public Game(UUID id) {
        this.id = id;
        Deck<GoldCard> gameGoldDeck = new Deck<>(fullGoldDeck.getCards());
        Deck<StdCard> gameStdDeck = new Deck<>(fullStdDeck.getCards());
        gameGoldDeck.shuffle();
        gameStdDeck.shuffle();
        GameBoard = new SharedBoard(gameGoldDeck, gameStdDeck);
    }

    /**
     * Getter for the Game id.
     * @return the id of the Game as UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Getter for numPLayers of Game.
     * @return the number of players in the Game, as int
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Getter for the list of Players in Game.
     * @return the List of Players in the Game
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Getter for the GameState.
     * @return the GameState of the Game
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Getter for the GameBoard.
     * @return the SharedBoard of the Game
     */
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
        this.numPlayers += 1;
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
