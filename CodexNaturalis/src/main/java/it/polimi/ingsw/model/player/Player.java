package it.polimi.ingsw.model.player;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Class
 * @author Alessio Guarisco
 * */
public final class Player {
    private final String username;
    private int score;
    private int[][] board;
    private Card[] hand;
    private Objective hiddenObjective;
    private ConcurrentHashMap<Resource, Integer> playerResources = new ConcurrentHashMap<Resource, Integer>();
    private Game game;
    /**
     * @param username it is the unique identifier of the player.
     * @param currentGame pointer to the instance of game the player is playing.
     * */
    public Player(String username, Game currentGame) {
        this.username = username;
        joinGame(currentGame);
    }

    public String getUsername() {
        return username;
    }

    public int getScore(){
        return score;
    }

    public int[][] getBoard() {
        return board;
    }

    public Card[] getHand(){
        return hand;
    }

    public Objective getHiddenObjective(){
        return hiddenObjective;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHiddenObjective(Objective hiddenObjective) {
        this.hiddenObjective = hiddenObjective;
    }

    /**
     * This method is used while the game is starting to draw the first hand.
     * It takes consts values from:
     * @see it.polimi.ingsw.GameConsts
     * */
    public void drawFirstHand(){
        this.hand = new Card[GameConsts.firstHandDim];
        for (int i = 0; i < GameConsts.fistHandStdNum; i++){
            this.hand[i] = game.getGameBoard().drawDeck(false);
        }
        for (int i = GameConsts.fistHandStdNum; i <= GameConsts.firstHandDim; i++){
            this.hand[i] = game.getGameBoard().drawDeck(true);
        }
    }

    public ConcurrentHashMap<Resource, Integer> getResources() {
        return (ConcurrentHashMap<Resource, Integer>) Collections.unmodifiableMap(playerResources);
    }

    /**
     * Method to draw from one of the two decks in the SharedBoard of the Game
     * @param isGold is used to identify if the card is drawn to the gold card deck or the std deck
     * */
    public void drawDecks(boolean isGold){
        Card drawnCard = game.getGameBoard().drawDeck(isGold);
        toHand(drawnCard);
    }

    /**
     * Method to draw from one of the four visible cards in the SharedBoard of the Game
     * @param numVisible is used to choose the card from the board
     * */
    public void drawVisible(int numVisible){
        Card drawnCard = game.getGameBoard().drawVisible(numVisible);
        toHand(drawnCard);
    }

    /**
     * Private general method to add a drawn card to the hand of the player
     * */
    private void toHand(Card drawnCard){
        for(int i = 0; i < GameConsts.firstHandDim; i++){
            if (hand[i]==null){
                hand[i]=drawnCard;
                return;
            }
        }
        System.out.println("too many cards");
    }

    /**
     * TODO: methods to implement
     */
    private void joinGame(Game currentGame){
        this.game = currentGame;
        this.score = 0;
        for (Resource r : Resource.values()){
            playerResources.put(r, 0);
        }
    }

    public void playCard(Card playedCard){

    }
}
