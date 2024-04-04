package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.MockCard;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoard {
    private final Card[][] board;
    private final Card notFillable = new MockCard(GameConsts.notFillableId, null);
    private Card lastPlacedCard;
    private int[] lastPlacedPosition;

    public PlayerBoard(Card firstCard) {
        this.board = new Card[GameConsts.totalPlayableCards][GameConsts.totalPlayableCards];
        this.board[GameConsts.centralPoint.getX()][GameConsts.centralPoint.getY()] = firstCard;
        this.lastPlacedCard = firstCard;
        this.lastPlacedPosition = new int[]{GameConsts.centralPoint.getX(), GameConsts.centralPoint.getY()};
    }

    public Card[][] getBoard() {
        return board;
    }

    public Card getLastPlacedCard() {
        return lastPlacedCard;
    }

    public int[] getLastPlacedPosition() {
        return lastPlacedPosition;
    }

    public List<Coordinates> checkPositions(){
        boolean[][] visited = new boolean[GameConsts.totalPlayableCards][GameConsts.totalPlayableCards];
        ArrayList<Coordinates> possiblePlacements = new ArrayList<Coordinates>();
        dfs(GameConsts.centralPoint,possiblePlacements, visited);
        return possiblePlacements;
    }

    private void dfs(Coordinates cellCoordinates, ArrayList<Coordinates> list, boolean[][] visited){
        if(isWithinBounds(cellCoordinates) && !visited[cellCoordinates.getX()][cellCoordinates.getY()]){
            visited[cellCoordinates.getX()][cellCoordinates.getY()] = true;
            if (board[cellCoordinates.getX()][cellCoordinates.getY()] == null) {
                list.add(cellCoordinates);
                return;
            }
            if(board[cellCoordinates.getX()][cellCoordinates.getY()]==notFillable) {
                return;
            }
            dfs(new Coordinates(cellCoordinates.getX(), cellCoordinates.getY()+1), list, visited);
            dfs(new Coordinates(cellCoordinates.getX()+1, cellCoordinates.getY()), list, visited);
            dfs(new Coordinates(cellCoordinates.getX(), cellCoordinates.getY()-1), list, visited);
            dfs(new Coordinates(cellCoordinates.getX()-1, cellCoordinates.getY()), list, visited);
        } else {
            return;
        }

    }

    private boolean isWithinBounds(Coordinates c){
        return c.getX() <= GameConsts.totalPlayableCards && c.getY() <= GameConsts.totalPlayableCards && c.getX() >= 0 && c.getY() >= 0;
    }

    /**TODO:
     * placeCard
     * checkRequirements
     * */
}
