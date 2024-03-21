package it.polimi.ingsw.model.player;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.DeckArea;
import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Player {
    private final String username;
    private int score;
    private int[][] board;
    private Card[] hand;
    private Objective hiddenObjective;
    private ConcurrentHashMap<Resource, Integer> playerResources = new ConcurrentHashMap<Resource, Integer>();
    private final Game game;


    public Player(String username, Game currentGame) {
        this.username = username;
        this.game = currentGame;
    }

    public String getUsername() {
        return username;
    }
    public Card[] getHand(){
        return hand;
    }
    public Objective getHiddenObjective(){
        return hiddenObjective;
    }
    public void drawFirstHand(){
        this.hand = new Card[GameConsts.firstHandDim];
        for (int i = 0; i < GameConsts.fistHandStdNum; i++){
            this.hand[i] = game.getGameBoard().getDeckArea().drawDeck(false);
        }
        for (int i = GameConsts.fistHandStdNum; i <= GameConsts.firstHandDim; i++){
            this.hand[i] = game.getGameBoard().getDeckArea().drawDeck(true);
        }
    }
    public void drawDecks(int cardId){

    }
    public void drawVisible(int cardId){

    }
    public void playCard(Card playedCard){

    }
    /**TODO: map not copied**/
    public ConcurrentHashMap<Resource, Integer> getResources(){
        return playerResources;
    }
    /** TODO: Game class implementation for joinGame **/
    public void joinGame(UUID gameId){

    }
}
