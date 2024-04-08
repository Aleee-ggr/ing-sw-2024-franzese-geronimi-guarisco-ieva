package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.exceptions.UnrecognisedCardException;
import it.polimi.ingsw.model.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * PlayerBoard has a 2D array that represent the main play board for a Player. <br/>
 * It manages placement of cards and check valid positions for placing cards. <br/>
 * it also checks if a card can be played.
 * @see Player
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
     * Getter for the HashSet of the valid Placements of Cards.
     * @return the HashSet of validPlacements.
     */
    public Set<Coordinates> getValidPlacements() {
        return new HashSet<>(validPlacements);
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
     * Public Method isWithinBounds, <br/>
     * called by {@link #dfs(Coordinates, boolean[][]) dfs} and  {@link #placeCard(Card, Coordinates) placeCard}, <br/>
     * it uses the GameConsts class to check the limits of the board.
     * @param c Coordinates to check
     * @see Coordinates
     * @see GameConsts
     */
    public boolean isWithinBounds(Coordinates c){
        return c.x() <= GameConsts.totalPlayableCards && c.y() <= GameConsts.totalPlayableCards && c.x() >= 0 && c.y() >= 0;
    }

    /**
     * Method that returns the possible positions for placing a Card on the board. <br/>
     * it's used if a player is disconnected. <br/>
     * in normal execution of the game the validPlacements hashset is updated while placing a card.<br/>
     * it calls the private method {@link #dfs(Coordinates, boolean[][]) dfs}.
     * @see Coordinates
     */
    public void checkPositionsIfDisconnected(){
        boolean[][] visited = new boolean[GameConsts.totalPlayableCards][GameConsts.totalPlayableCards];
        dfs(GameConsts.centralPoint, visited);
    }

    /**
     * Private Method dfs, a Recursive Depth First Search algorithm. <br/>
     * after checking if the given coordinates are within the range of the board calling <br/>
     * the private {@link #isWithinBounds(Coordinates) isWithinBounds method}, <br/>
     * it visits each cell up, down, right and left from the given coordinates. <br/>
     * if it finds a null pointer it has found a valid position for a card, adding the coordinates to a list and returning.<br/>
     * if it finds a notFillable object it means the position is not valid.<br/>
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
     * Method placeCard, used to place a given card on the board at specified Coordinates. <br/>
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
        Corner[] c;

        if(card.isFrontSideUp()){
            c = card.getFrontCorners();
            if(card.getClass() == StartingCard.class){
                for (Resource r : ((StartingCard) card).getFrontResources()){
                    boardOwner.updateResourcesValue(r, GameConsts.numberOfResourcesPerCorner);
                }
            }
            markNotCoverable(coordinates, c);
        } else {
            if (card.getClass() == StartingCard.class) {
                c = ((StartingCard) card).getBackCorners();
                markNotCoverable(coordinates, c);
            } else {
                try {
                    c = ((ColoredCard) card).getBackCorners();
                    markNotCoverable(coordinates, c);
                } catch (UnrecognisedCardException e) {
                    System.out.println("unexpected behaviour");
                }
            }
        }
    }

    /**
     * Private Method markNotCoverable, <br/>
     * called by {@link #placeCard(Card, Coordinates) placeCard}, it calls
     * {@link #handleCase(Coordinates, Corner, int, int) handleCase} to handle the four corners of a card.
     * @param coordinates Coordinates of the placed Card
     * @param c Array of Corner of Resources of the card
     * @see Coordinates
     * @see Resource
     * @see Corner
     */
    private void markNotCoverable(Coordinates coordinates, Corner[] c) {
        try {
            for (int i = 0; i < c.length; i++) {
                switch (i) {
                    case 0 -> handleCase(coordinates, c[i], 0, 1);
                    case 1 -> handleCase(coordinates, c[i], 1, 0);
                    case 2 -> handleCase(coordinates, c[i], 0, -1);
                    case 3 -> handleCase(coordinates, c[i], -1, 0);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("player board out of bound");
        }
    }

    /**
     * Private Method handleCase, <br/>
     * called by {@link #markNotCoverable(Coordinates, Corner[]), markNotCoverable}.<br/>
     * Its functions are to:<br/>
     * mark cells of the board as not Fillable<br/>
     * add values to the player Resource hashmap <br/>
     * add Coordinates to the Set of validPlacements for cards.
     * @param coordinates Coordinates of the placed Card
     * @param corner specific Corner of the card
     * @param dX delta x
     * @param dY delta y
     * @see Coordinates
     * @see Resource
     * @see Corner
     */
    private void handleCase(Coordinates coordinates, Corner corner, int dX, int dY) {
        if(corner.getCornerResource() == Resource.NONCOVERABLE) {
            board[coordinates.x() + dX][coordinates.y() + dY] = notFillable;
        } else {
            if(corner.getCornerResource() != Resource.NONE) {
            boardOwner.updateResourcesValue(corner.getCornerResource(), GameConsts.numberOfResourcesPerCorner);
            }
            validPlacements.add(new Coordinates(coordinates.x() + dX, coordinates.y() + dY));
        }
    }
}