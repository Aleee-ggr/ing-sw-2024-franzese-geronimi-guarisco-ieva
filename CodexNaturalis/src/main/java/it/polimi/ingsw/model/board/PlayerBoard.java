package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.MockCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.exceptions.UnrecognisedCardException;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PlayerBoard has a 2D array that represent the main play board for a Player.
 * It manages placement of cards and check valid positions for placing cards.
 * it also checks if a card can be played.
 * @see it.polimi.ingsw.model.player.Player
 * @see Coordinates
 * @see Card
 * @see MockCard
 * @author Alessio Guarisco
 * */
public class PlayerBoard {
    private final Card[][] board;
    private final Player boardOwner;
    private final Card notFillable = new MockCard(GameConsts.notFillableId, null);
    private Card lastPlacedCard;
    private Coordinates lastPlacedPosition;
    private final Set<Coordinates> validPlacements = new HashSet<>();

    /**
     * Constructor for the PlayerBoard class.
     * @param firstCard first card played.
     */
    public PlayerBoard(Card firstCard, Player player) {
        this.board = new Card[GameConsts.totalPlayableCards][GameConsts.totalPlayableCards];
        this.board[GameConsts.centralPoint.x()][GameConsts.centralPoint.y()] = firstCard;
        this.boardOwner = player;
        this.lastPlacedCard = firstCard;
        this.lastPlacedPosition = new Coordinates(GameConsts.centralPoint.x(), GameConsts.centralPoint.y());
    }

    /**
     * Getter for the entire 2D array of the board.
     * @return a 2D array of cards.
     */
    public Card[][] getBoard() {
        return board;
    }

    /**
     * Getter for the last placed card on the board.
     * @return the last placed Card obj.
     */
    public Card getLastPlacedCard() {
        return lastPlacedCard;
    }

    /**
     * Getter for the position of the last placed card on the board.
     * @return the coordinates of the last placed card on the board.
     */
    public Coordinates getLastPlacedPosition() {
        return lastPlacedPosition;
    }

    /**
     * Method getCenter, returns the Coordinates of the center of the board.
     * @return Coordinates of the center of the board.
     */
    public Coordinates getCenter() {
        return new Coordinates(GameConsts.centralPoint.x(), GameConsts.centralPoint.y());
    }

    /**
     * Method getCard, returns the Card for specified Coordinate.
     * @param coordinates Coordinates for returning a card.
     * @return Card for the specified coordinates.
     */
    public Card getCard(Coordinates coordinates) {
        return board[coordinates.x()][coordinates.y()];
    }


    /**
     * Public Method isWithinBounds,
     * called by {@link #dfs(Coordinates, boolean[][]) dfs} and  {@link #placeCard(Card, Coordinates) placeCard},
     * it uses the GameConsts class to check the limits of the board.
     * @param c Coordinates to check
     * @see Coordinates
     * @see GameConsts
     */
    public boolean isWithinBounds(Coordinates c){
        return c.x() <= GameConsts.totalPlayableCards && c.y() <= GameConsts.totalPlayableCards && c.x() >= 0 && c.y() >= 0;
    }

    /**
     * Method that returns the possible positions for placing a Card on the board.
     * it's used if a player is disconnected.
     * in normal execution of the game the validPlacements hashset is updated while placing a card.
     * it calls the private method {@link #dfs(Coordinates, boolean[][]) dfs}.
     * @see Coordinates
     */
    public void checkPositionsIfDisconnected(){
        boolean[][] visited = new boolean[GameConsts.totalPlayableCards][GameConsts.totalPlayableCards];
        dfs(GameConsts.centralPoint, visited);
    }

    /**
     * Private Method dfs, a Recursive Depth First Search algorithm.
     * after checking if the given coordinates are within the range of the board calling
     * the private {@link #isWithinBounds(Coordinates) isWithinBounds method},
     * it visits each cell up, down, right and left from the given coordinates.
     * if it finds a null pointer it has found a valid position for a card, adding the coordinates to a list and returning.
     * if it finds a notFillable object it means the position is not valid.
     * @see MockCard
     * @param cellCoordinates the starting cell from which the depth first search starts.
     * @param visited a 2D array of boolean used to search every cell once without repetitions.
     */
    private void dfs(Coordinates cellCoordinates, boolean[][] visited){
        if(isWithinBounds(cellCoordinates) && !visited[cellCoordinates.x()][cellCoordinates.y()]){
            visited[cellCoordinates.x()][cellCoordinates.y()] = true;
            if (board[cellCoordinates.x()][cellCoordinates.y()] == null) {
                validPlacements.add(cellCoordinates);
                return;
            }
            if(board[cellCoordinates.x()][cellCoordinates.y()]==notFillable) {
                return;
            }
            dfs(new Coordinates(cellCoordinates.x(), cellCoordinates.y()+1), visited);
            dfs(new Coordinates(cellCoordinates.x()+1, cellCoordinates.y()), visited);
            dfs(new Coordinates(cellCoordinates.x(), cellCoordinates.y()-1), visited);
            dfs(new Coordinates(cellCoordinates.x()-1, cellCoordinates.y()), visited);
        }
    }

    /**
     * Method placeCard, used to place a given card on the board at specified Coordinates.
     * it calls the private method {@link #markNotCoverable(Coordinates, Corner[]) markNotCoverable} for checks and placing notFillable.
     * @param card Card obj to add
     * @param coordinates Coordinates for adding the Card
     * @throws IndexOutOfBoundsException when trying to place a card out of bound
     * @throws UnrecognisedCardException when a card is not recognised
     * @see Coordinates
     */
    public void placeCard(Card card, Coordinates coordinates) throws IndexOutOfBoundsException, UnrecognisedCardException{
        if (!isWithinBounds(coordinates)) {
            throw new IndexOutOfBoundsException("error placing card");
        }

        board[coordinates.x()][coordinates.y()] = card;
        lastPlacedPosition = coordinates;
        lastPlacedCard = card;

        if(card.isFrontSideUp()){
            Corner[] c = card.getFrontCorners();
            markNotCoverable(coordinates, c);
        } else if(!card.isFrontSideUp() && card.getClass() == StartingCard.class){
            Corner[] c = ((StartingCard) card).getBackCorners();
            markNotCoverable(coordinates, c);
        } else {
            throw new UnrecognisedCardException("unrecognised card"); /**TODO: fix this bug and implement back of colored*/
        }

    }

    /**
     * Private Method markNotCoverable,
     * called by {@link #placeCard(Card, Coordinates) placeCard}, it checks the corners of the placed Card and if it finds
     * a {@link Resource#NONCOVERABLE  NONCOVERABLE} Resource marks the cell with a notFIllable MockCard Obj, if it's a valid
     * resource it adds to the player Resource hashmap and to the Set of validPlacements for cards.
     * @param coordinates Coordinates of the placed Card
     * @param c Array of Corner of Resources of the card
     * @see Coordinates
     * @see Resource
     * @see Corner
     */
    private void markNotCoverable(Coordinates coordinates, Corner[] c){ /**TODO: refactor*/
        try{
            for (int i = 0; i<c.length; i++){
                if (c[i].getCornerResource() == Resource.NONCOVERABLE){
                    switch (i){
                        case 0 -> {
                            board[coordinates.x()][coordinates.y()+1] = notFillable;
                        }
                        case 1 -> {
                            board[coordinates.x()+1][coordinates.y()] = notFillable;
                        }
                        case 2 -> {
                            board[coordinates.x()][coordinates.y()-1] = notFillable;
                        }
                        case 3 -> {
                            board[coordinates.x()-1][coordinates.y()] = notFillable;
                        }
                    }
                } else {
                    if(c[i].getCornerResource() != Resource.NONE){
                        boardOwner.updateResourcesValue(c[i].getCornerResource(), GameConsts.numberOfResourcesPerCorner);
                    }
                    switch (i){
                        case 0 -> {
                            validPlacements.add(new Coordinates(coordinates.x(),coordinates.y() + 1));
                        }
                        case 1 -> {
                            validPlacements.add(new Coordinates(coordinates.x() + 1,coordinates.y()));
                        }
                        case 2 -> {
                            validPlacements.add(new Coordinates(coordinates.x(), coordinates.y() - 1));
                        }
                        case 3 -> {
                            validPlacements.add(new Coordinates(coordinates.x() - 1, coordinates.y()));
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e){
            System.out.println("player board out of bound");
        }
    }
}