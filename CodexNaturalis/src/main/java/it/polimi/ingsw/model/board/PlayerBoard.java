package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.UnrecognisedCardException;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.*;

/**
 * PlayerBoard has a 2D array that represent the main play board for a Player. <br/>
 * It manages placement of cards and check valid positions for placing cards. <br/>
 * it also checks if a card can be played.
 *
 * @see Player
 * @see Coordinates
 * @see Card
 * @see MockCard
 */
public class PlayerBoard {
    private final HashMap<Coordinates, Card> board = new HashMap<>();
    private final Player boardOwner;
    private final Card notFillable = new MockCard(GameConsts.notFillableId, null);
    private final Deque<Card> lastPlacedCards = new ArrayDeque<>();
    private Coordinates lastPlacedPosition;
    private final Set<Coordinates> validPlacements = new HashSet<>();

    /**
     * Constructor for the PlayerBoard class.
     *
     * @param firstCard first card played.
     */
    public PlayerBoard(Card firstCard, Player player) {
        this.boardOwner = player;
        this.placeCard(firstCard, GameConsts.centralPoint);
    }

    /**
     * Getter for the position of the last placed card on the board.
     *
     * @return the coordinates of the last placed card on the board.
     */
    public Coordinates getLastPlacedPosition() {
        return lastPlacedPosition;
    }

    /**
     * Getter for the HashSet of the valid Placements of Cards.
     *
     * @return the HashSet of validPlacements.
     */
    public Set<Coordinates> getValidPlacements() {
        return new HashSet<>(validPlacements);
    }

    /**
     * Method getCenter, returns the Coordinates of the center of the board.
     *
     * @return Coordinates of the center of the board.
     */
    public Coordinates getCenter() {
        return new Coordinates(0, 0);
    }

    /**
     * Method getCard, returns the Card for specified Coordinate.
     *
     * @param coordinates Coordinates for returning a card.
     * @return Card for the specified coordinates.
     */
    public Card getCard(Coordinates coordinates) {
        return board.get(coordinates);
    }

    /**
     * Method getBoard, returns the HashMap of the Board.
     *
     * @return Hashmap of coordinates and cards.
     */
    public HashMap<Coordinates, Card> getBoard() {
        return new HashMap<>(board);
    }

    /**
     * Method getLastPlacedCards, returns the Deque of the last placed Cards.
     *
     * @return Deque of the last placed Cards.
     */
    public Deque<Card> getLastPlacedCards() {
        return lastPlacedCards;
    }


    /**
     * Private Method dfs, a Recursive Depth First Search algorithm. <br/>
     * after checking if the given coordinates are within the range of the board calling <br/>
     * it visits each cell up, down, right and left from the given coordinates. <br/>
     * if it finds a null pointer it has found a valid position for a card, adding the coordinates to a list and returning.<br/>
     * if it finds a notFillable object it means the position is not valid.<br/>
     *
     * @param cellCoordinates the starting cell from which the depth first search starts.
     * @param visited         a 2D array of boolean used to search every cell once without repetitions.
     * @see MockCard
     */
    private void dfs(Coordinates cellCoordinates, Set<Coordinates> visited) {
        if (!visited.contains(cellCoordinates)) {
            visited.add(cellCoordinates);
            if (board.get(cellCoordinates) == null) {
                validPlacements.add(cellCoordinates);
                return;
            }
            if (board.get(cellCoordinates) == notFillable) {
                return;
            }

            for (Coordinates coordinates : cellCoordinates.getNeighbors()) {
                dfs(coordinates, visited);
            }
        }
    }

    /**
     * Method placeCard, used to place a given card on the board at specified Coordinates. <br/>
     * it calls the private method {@link #markNotCoverable(Coordinates, Corner[]) markNotCoverable} for checks and placing notFillable.
     *
     * @param card        Card obj to add
     * @param coordinates Coordinates for adding the Card
     * @throws IndexOutOfBoundsException when trying to place a card out of bound
     * @throws UnrecognisedCardException when a card is not recognised
     * @see Coordinates
     */
    public void placeCard(Card card, Coordinates coordinates) throws IndexOutOfBoundsException, UnrecognisedCardException {
        board.put(coordinates, card);

        lastPlacedPosition = coordinates;
        lastPlacedCards.add(card);
        validPlacements.remove(coordinates);

        Corner[] corners;

        if (card.isFrontSideUp()) {
            corners = card.getFrontCorners();

            if (card instanceof StartingCard) {
                for (Resource r : ((StartingCard) card).getFrontResources()) {
                    boardOwner.updateResourcesValue(r, GameConsts.numberOfResourcesPerCorner);
                }
            }

            markNotCoverable(coordinates, corners);
            removeResources(coordinates);
        }

        if (!card.isFrontSideUp()) {

            if (card instanceof StartingCard) {
                corners = ((StartingCard) card).getBackCorners();
                markNotCoverable(coordinates, corners);
                removeResources(coordinates);
            } else {
                try {
                    corners = ((ColoredCard) card).getBackCorners();
                    markNotCoverable(coordinates, corners);
                    removeResources(coordinates);
                    boardOwner.updateResourcesValue(((ColoredCard) card).getBackResource(), GameConsts.numberOfResourcesPerCorner);
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
     *
     * @param coordinates Coordinates of the placed Card
     * @param c           Array of Corner of Resources of the card
     * @see Coordinates
     * @see Resource
     * @see Corner
     */
    private void markNotCoverable(Coordinates coordinates, Corner[] c) {
        try {
            for (int i = 0; i < c.length; i++) {
                switch (i) {
                    case 0 -> handleCase(coordinates, c[i], -1, 0);
                    case 1 -> handleCase(coordinates, c[i], 0, 1);
                    case 2 -> handleCase(coordinates, c[i], 0, -1);
                    case 3 -> handleCase(coordinates, c[i], 1, 0);
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
     * mark cells of the board as not Fillable[deprecated]<br/>
     * add values to the player Resource hashmap <br/>
     * add Coordinates to the Set of validPlacements for cards.
     *
     * @param coordinates Coordinates of the placed Card
     * @param corner      specific Corner of the card
     * @param dX          delta x
     * @param dY          delta y
     * @see Coordinates
     * @see Resource
     * @see Corner
     */
    private void handleCase(Coordinates coordinates, Corner corner, int dX, int dY) {
        Coordinates c = new Coordinates(coordinates.x() + dX, coordinates.y() + dY);
        if (corner.getCornerResource() == Resource.NONCOVERABLE || !corner.isCoverable()) {

            if (!board.containsKey(c)) {
                board.put(coordinates.horizontal(dX).vertical(dY), notFillable);
            }
            validPlacements.remove(c);

        } else {

            if (corner.getCornerResource() != Resource.NONE) {
                boardOwner.updateResourcesValue(corner.getCornerResource(), GameConsts.numberOfResourcesPerCorner);
            }
            if (!board.containsKey(c)) {
                validPlacements.add(c);
            }
        }
    }

    /**
     * Removes resources from adjacent cards based on the placement of a new card.
     *
     * @param placedCoordinates the coordinates where the new card is placed
     */
    private void removeResources(Coordinates placedCoordinates) {
        Corner[] corners;
        for (Coordinates c : placedCoordinates.getNeighbors()) {
            if (!board.containsKey(c)) {
                continue;
            }
            if (board.get(c) instanceof MockCard) {
                continue;
            }

            if (board.get(c) instanceof ColoredCard) {
                corners = board.get(c).isFrontSideUp() ? ((ColoredCard) board.get(c)).getFrontCorners() : ((ColoredCard) board.get(c)).getBackCorners();
            } else if (board.get(c) instanceof StartingCard) {
                corners = board.get(c).isFrontSideUp() ? ((StartingCard) board.get(c)).getFrontCorners() : ((StartingCard) board.get(c)).getBackCorners();
            } else {
                return;
            }
            removeCornerResource(placedCoordinates, c, corners);
        }


    }

    /**
     * Adjusts resources for adjacent cards based on the placement of the newly added card.
     *
     * @param placedCoordinates the coordinates where the new card is placed
     * @param c                 the coordinates of an adjacent card
     * @param corners           the corners of the adjacent card
     */
    private void removeCornerResource(Coordinates placedCoordinates, Coordinates c, Corner[] corners) {
        if (board.containsKey(c)) {
            switch (c.x() - placedCoordinates.x()) {
                case -1 ->
                        boardOwner.updateResourcesValue(corners[3].getCornerResource(), -GameConsts.numberOfResourcesPerCorner);
                case 1 ->
                        boardOwner.updateResourcesValue(corners[0].getCornerResource(), -GameConsts.numberOfResourcesPerCorner);
            }
            switch (c.y() - placedCoordinates.y()) {
                case -1 ->
                        boardOwner.updateResourcesValue(corners[1].getCornerResource(), -GameConsts.numberOfResourcesPerCorner);
                case 1 ->
                        boardOwner.updateResourcesValue(corners[2].getCornerResource(), -GameConsts.numberOfResourcesPerCorner);
            }
        }
    }
}