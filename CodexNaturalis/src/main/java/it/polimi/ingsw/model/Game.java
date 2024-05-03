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
    static final HashSet<Player> playersConnected  = new HashSet<>(); //TODO: not the same username inter-game
    static final ConcurrentHashMap<Integer, Card> cardID = new ConcurrentHashMap<>() {{
        for (Card gold : FullDeck.getFullGoldDeck().getCards()) {
            put(gold.getId(), gold);
        }
        for (Card std : FullDeck.getFullStdDeck().getCards()) {
            put(std.getId(), std);
        }
        for (Card start : FullDeck.getFullStartingDeck().getCards()) {
            put(start.getId(), start);
        }
    }};
    static final ConcurrentHashMap<Integer, Objective> objectiveID = new ConcurrentHashMap<>() {{
        for (Objective objective : FullDeck.getFullObjDeck().getCards()) {
            put(objective.getId(), objective);
        }
    }};
    /*Game-Specific Decks: not static decks for the instance of Game*/
    Deck<GoldCard> gameGoldDeck;
    Deck<StdCard> gameStdDeck;
    Deck<Objective> gameObjDeck;
    Deck<StartingCard> gameStartingDeck;
    private int numPlayers = 0;
    private final List<Player> players = new ArrayList<>();
    private SharedBoard gameBoard;

    /**
     * Get the card from the corresponding id
     * @param id the id of the card
     * @return the card corresponding to the id
     */
    public static Card getCardByID(Integer id) {
        return cardID.get(id);
    }

    public static Objective getObjectiveByID(Integer id) {
        return objectiveID.get(id);
    }
    /**
     * Constructor for the Game class.
     */
    public Game() {
        resetBoard();
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
        return gameBoard;
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
            gameGoldDeck = FullDeck.getFullGoldDeck().shuffle();
            gameStdDeck = FullDeck.getFullStdDeck().shuffle();
            gameObjDeck = FullDeck.getFullObjDeck().shuffle();
            gameStartingDeck = FullDeck.getFullStartingDeck().shuffle();

            gameBoard = new SharedBoard(gameGoldDeck, gameStdDeck);
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
        gameBoard.setObjectives(objectiveToAdd);
    }
    
    /**
     * TODO: methods to implement:
     */
    public void startGame(){

    }
    public void endGame(){
    }
}
