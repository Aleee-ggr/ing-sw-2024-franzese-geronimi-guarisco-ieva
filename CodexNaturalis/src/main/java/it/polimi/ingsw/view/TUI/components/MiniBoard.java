package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.RotateBoard;

import java.util.Map;


public class MiniBoard implements Component{
    public static final int boardHeight = 8;
    public static final int boardWidth = 29;
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
        boardToPrint = new Character[boardWidth][boardHeight];
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
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for(int y = 0; y < boardHeight-1; y++) {
            for (int x = 0; x < boardWidth; x++) {
                out.append(boardToPrint[x][y] != null ? boardToPrint[x][y].toString() : " ");
            }
            out.append('\n');
        }

        Color playerColor = ((OpponentData) client.getOpponentData().get(username)).getPlayerColor();

        String colorCode = getColorCode(playerColor);
        String coloredPlayer = " " + colorCode + username + "\u001b[0m";

        if (username.length() > boardWidth -1) {
            out.append(coloredPlayer, 0, boardWidth - 1)
                    .append("\u001b[0m")
                    .append("…");
        } else {
            String spaces = " ".repeat(boardWidth - username.length() - 1);
            out.append(String.format("%s%s", coloredPlayer, spaces));
        }

        out.append('\n');
        return out.toString();
    }

    private Coordinates getBoardCoords(int x, int y){
        int newX = x - (boardWidth - 1) / 2;
        int newY = (boardHeight - 1) / 2 - y;
        return new Coordinates(newX, newY);
    }

    private String getColorCode(Color color) {
        return switch (color) {
            case RED -> "\u001b[1;31m"; // Rosso
            case BLUE -> "\u001b[1;34m"; // Blu
            case GREEN -> "\u001b[1;32m"; // Verde
            case YELLOW -> "\u001b[1;33m"; // Giallo
            case null -> "\u001b[0m"; // Reset
        };
    }
}
