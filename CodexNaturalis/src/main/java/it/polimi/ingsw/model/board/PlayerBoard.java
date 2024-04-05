package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.exceptions.UnrecognisedCardException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerBoard {
    private final Card[][] board;
    private final Card notFillable = new MockCard(GameConsts.notFillableId, null);
    private Card lastPlacedCard;
    private Coordinates lastPlacedPosition;

    public PlayerBoard(Card firstCard) {
        this.board = new Card[GameConsts.totalPlayableCards][GameConsts.totalPlayableCards];
        this.board[GameConsts.centralPoint.getX()][GameConsts.centralPoint.getY()] = firstCard;
        this.lastPlacedCard = firstCard;
        this.lastPlacedPosition = new Coordinates(GameConsts.centralPoint.getX(), GameConsts.centralPoint.getY());
    }

    public Coordinates getCenter() {
        return new Coordinates(GameConsts.centralPoint.getX(), GameConsts.centralPoint.getY());
    }

    public Card getCard(Coordinates coordinates) {
        return board[coordinates.getX()][coordinates.getY()];
    }

    public Card[][] getBoard() {
        return board;
    }

    public Card getLastPlacedCard() {
        return lastPlacedCard;
    }

    public Coordinates getLastPlacedPosition() {
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
        }
    }

    public boolean isWithinBounds(Coordinates c){
        return c.getX() <= GameConsts.totalPlayableCards && c.getY() <= GameConsts.totalPlayableCards && c.getX() >= 0 && c.getY() >= 0;
    }
    
    public void placeCard(Card card, Coordinates coordinates) throws IndexOutOfBoundsException, RuntimeException{
        if (!isWithinBounds(coordinates)) {
            throw new IndexOutOfBoundsException("error placing card");
        }

        board[coordinates.getX()][coordinates.getY()] = card;
        lastPlacedPosition = coordinates;
        lastPlacedCard = card;

        if(card.isFrontSideUp()){
            Corner[] c = card.getFrontCorners();
            markNotCoverable(coordinates, c);
        } else if(!card.isFrontSideUp() && card.getClass() == StartingCard.class){
            Corner[] c = ((StartingCard) card).getBackCorners();
            markNotCoverable(coordinates, c);
        } else {
            throw new UnrecognisedCardException("unrecognised card");
        }

    }

    private void markNotCoverable(Coordinates coordinates, Corner[] c){
        try{
            for (int i = 0; i<c.length; i++){
                if (c[i].getCornerResource() == Resource.NONCOVERABLE){
                    switch (i){
                        case 0 -> {
                            board[coordinates.getX()][coordinates.getY()+1] = notFillable;
                        }
                        case 1 -> {
                            board[coordinates.getX()+1][coordinates.getY()] = notFillable;
                        }
                        case 2 -> {
                            board[coordinates.getX()][coordinates.getY()-1] = notFillable;
                        }
                        case 3 -> {
                            board[coordinates.getX()-1][coordinates.getY()] = notFillable;
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e){
            System.out.println("player board out of bound");
        }
    }

    /**
     * TODO:
     * checkRequirements
     * */
}