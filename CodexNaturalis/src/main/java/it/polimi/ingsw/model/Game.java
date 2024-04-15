package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.helpers.exceptions.model.ExistingUsernameException;
import it.polimi.ingsw.helpers.exceptions.model.TooManyPlayersException;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Game Class for creating a game with id, players, numPlayers and SharedBoard.
 * @author Alessio Guarisco
 * @see Player
 * @see SharedBoard
 * */
public class Game {
    /*static decks: needed not to parse every time*/
    static final Deck<GoldCard> fullGoldDeck = FullDeck.getFullGoldDeck();
    static final Deck<StdCard> fullStdDeck = FullDeck.getFullStdDeck();
    static final Deck<StartingCard> fullStartingDeck = FullDeck.getFullStartingDeck();
    static final Deck<Objective> fullObjDeck = FullDeck.getFullObjDeck();
    static final HashSet<Player> playersConnected  = new HashSet<>(); //TODO: not the same username inter-game
    static final ConcurrentHashMap<Integer, Card> cardid = new ConcurrentHashMap<>() {{
        for (Card gold : fullGoldDeck.getCards()) {
            cardid.put(gold.getId(), gold);
        }
        for (Card std : fullStdDeck.getCards()) {
            cardid.put(std.getId(), std);
        }
        for (Card start : fullStartingDeck.getCards()) {
            cardid.put(start.getId(), start);
        }
    }};
    /*Game-Specific Decks: not static decks for the instance of Game*/
    Deck<GoldCard> gameGoldDeck = new Deck<>(fullGoldDeck);
    Deck<StdCard> gameStdDeck = new Deck<>(fullStdDeck);
    Deck<Objective> gameObjDeck = new Deck<>(fullObjDeck);
    Deck<StartingCard> gameStartingDeck = new Deck<>(fullStartingDeck);
    private final UUID id;
    private int numPlayers = 0;
    private final List<Player> players = new ArrayList<>();
    private SharedBoard GameBoard;

    /**
     * Constructor for the Game class.
     * @param id UUID created by the Server.
     */
    public Game(UUID id) {
        this.id = id;
        resetBoard();
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
     * Getter for the GameBoard.
     * @return the SharedBoard of the Game
     */
    public SharedBoard getGameBoard() {
        return GameBoard;
    }

    /**
     * Getter for the full object deck.
     * @return the gameObjDeck, game specific object deck
     */
    public Deck<Objective> getGameObjDeck() {
        return gameObjDeck;
    }

    /**
     * Getter for the full starting card deck.
     * @return the gameStartingDeck, game specific starting card deck
     */
    public Deck<StartingCard> getGameStartingDeck() {
        return gameStartingDeck;
    }

    /**
     * Method used to add a Player in Game
     * @param playerUsername is the player username as a String
     * @throws TooManyPlayersException while trying to add a player when the Game is full
     * @throws ExistingUsernameException while there is already a player with the same username in game
     * the exceptions are managed by the caller.
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
     * Method used to reset the SharedBoard. <br/>
     * It creates a new board with new shuffled decks.
     * */
    public void resetBoard(){
        try{
            gameGoldDeck = new Deck<>(fullGoldDeck.getCards());
            gameStdDeck = new Deck<>(fullStdDeck.getCards());
            gameGoldDeck.shuffle();
            gameStdDeck.shuffle();
            gameObjDeck.shuffle();
            gameStartingDeck.shuffle();
            GameBoard = new SharedBoard(gameGoldDeck, gameStdDeck);
        } catch (RuntimeException e){
            System.out.println("error while resetting the SharedBoard");
        }
    }

    public void manageObjectives(){ //TODO: try catch if there are obj in sharedboard
        gameObjDeck.shuffle();
        Objective[] objectiveToAdd = new Objective[GameConsts.globalObjectives];
        for(int i = 0; i<GameConsts.globalObjectives; i++){
            objectiveToAdd[i] = gameObjDeck.draw();
        }
        GameBoard.setObjectives(objectiveToAdd);
    }
    
    /**
     * TODO: methods to implement:
     */
    public void startGame(){

    }
    public void endGame(){
    }
}
