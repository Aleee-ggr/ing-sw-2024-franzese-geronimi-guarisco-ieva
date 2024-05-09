package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.RotateBoard;

import java.util.Map;


public class MiniBoard implements Component{
    public static final int boardHeight = 8;
    public static final int boardWidth = 29;
    private int singleBoardWidth;
    private Character[][] boardToPrint;
    private final ClientInterface client;
    private final String username;

    public MiniBoard(String username, ClientInterface client) {
        this.username = username;
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public void setBoard(Map<Coordinates, Integer> board) {
        Map<Coordinates, Integer> rotatedBoard = RotateBoard.rotateBoard(board);
        singleBoardWidth = (boardWidth - (client.getPlayerNum()-2)) / (client.getPlayerNum()-1);
        boardToPrint = new Character[singleBoardWidth][boardHeight];
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
        for(int y = 0; y < boardHeight-1; y++) {
            for (int x = 0; x < singleBoardWidth; x++) {
                out.append(boardToPrint[x][y] != null ? boardToPrint[x][y].toString() : " ");
            }
            out.append('\n');
        }

        if (username.length() > singleBoardWidth-1) {
            out.append(username, 0, singleBoardWidth - 1).append("…");
        } else {
            out.append(String.format("%-" + singleBoardWidth + "s", username));

        }

        out.append('\n');
        return out.toString();
    }

    private Coordinates getBoardCoords(int x, int y){
        int newX = x - (singleBoardWidth - 1) / 2;
        int newY = (boardHeight - 1) / 2 - y;
        return new Coordinates(newX, newY);
    }
}
