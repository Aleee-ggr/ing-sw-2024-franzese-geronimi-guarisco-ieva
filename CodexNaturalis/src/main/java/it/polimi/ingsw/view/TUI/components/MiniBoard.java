package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.view.TUI.RotateBoard;

import java.util.Map;


public class MiniBoard {
    public static final int boardHeight = 7;
    public static final int boardWidth = 29;
    private int singleBoardWidth = 9; //TODO: refactor to have variable width
    private final Character[][] boardToPrint = new Character[singleBoardWidth][boardHeight];

    public String[] toStringArray() {
        return toString().split("\n");
    }

    public void setBoard(Map<Coordinates, Integer> board) {
        Map<Coordinates, Integer> rotatedBoard = RotateBoard.rotateBoard(board);
        for (int y = 0; y < boardHeight; y++){
            for(int x = 0; x < singleBoardWidth; x++){
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
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for(int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardHeight; x++) {
                out.append(boardToPrint[x][y]);
            }
            out.append('\n');
        }
        return out.toString();
    }

    private Coordinates getBoardCoords(int x, int y){
        int newX = x - (singleBoardWidth - 1) / 2;
        int newY = (boardHeight - 1) / 2 - y;
        return new Coordinates(newX, newY);
    }
}
