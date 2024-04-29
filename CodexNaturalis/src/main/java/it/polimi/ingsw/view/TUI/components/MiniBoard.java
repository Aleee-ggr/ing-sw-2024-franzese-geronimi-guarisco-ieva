package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.view.TUI.RotateBoard;

import java.util.Map;


public class MiniBoard {
    private final int boardHeight = 7;
    private final int boardWidth = 9;
    private final Character[][] boardToPrint = new Character[boardWidth][boardHeight];

    public Character[][] get(Map<Coordinates, Integer> board) {
        Map<Coordinates, Integer> rotatedBoard = RotateBoard.rotateBoard(board);
        for (int y = 0; y < boardHeight; y++){
            for(int x = 0; x < boardWidth; x++){
                Integer cardId = rotatedBoard.get(getBoardCoords(x, y));
                if(cardId == null){
                    boardToPrint[x][y] = ' ';
                } else {
                    Card card = Game.getCardByID(cardId);
                    if (card.isColored()){
                        ColoredCard coloredCard = (ColoredCard) card;
                        boardToPrint[x][y] = coloredCard.getBackResource().toChar();
                    } else {
                        boardToPrint[x][y] = 'S';
                    }
                }
            }
        }
        return boardToPrint;
    }

    private Coordinates getBoardCoords(int x, int y){
        int newX = x - (boardWidth - 1) / 2;
        int newY = (boardHeight - 1) / 2 - y;
        return new Coordinates(newX, newY);
    }
}
