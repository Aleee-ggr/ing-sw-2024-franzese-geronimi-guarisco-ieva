package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.SharedBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Deck;

public class Game {
    private final SharedBoard GameBoard = new SharedBoard();

    public SharedBoard getGameBoard() {
        return GameBoard;
    }
}
