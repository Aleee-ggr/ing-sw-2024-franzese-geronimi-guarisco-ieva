package it.polimi.ingsw.view.TUI.components;

import com.google.common.collect.BiMap;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.RotateBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the board component for displaying card placements in the TUI.
 * Implements the {@link Component} interface.
 */
public class Board implements Component {
    public static final int width = 147;
    public static final int height = 27;
    private final Character[][] board = new Character[height][width];
    private Coordinates center;
    private static final int baseOffset_x = width / 2;
    private static final int baseOffset_y = height / 2;

    private final ClientInterface client;

    private String currentPlayer;

    /**
     * Constructs a Board component with the given client interface.
     *
     * @param client The client interface used to interact with the game server.
     */
    public Board(ClientInterface client) {
        this.client = client;
        currentPlayer = client.getUsername();
        this.center = new Coordinates(0, 0);
    }

    /**
     * Sets the current player's username.
     *
     * @param currentPlayer The username of the current player.
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Sets the center coordinates of the board.
     *
     * @param center The coordinates representing the center of the board.
     */
    public void setCenter(Coordinates center) {
        this.center = center;
    }

    /**
     * Moves the center coordinates of the board by the specified amounts.
     *
     * @param x The horizontal movement amount.
     * @param y The vertical movement amount.
     */
    public void moveCenter(int x, int y) {
        this.center = center
                .horizontal(x)
                .vertical(y);
    }

    /**
     * Computes the layout of the board by placing cards and valid positions.
     */
    public void compute() {
        ;
        clear();
        List<Card> cardPlacementList = client.getOpponentData().get(currentPlayer).getOrder();
        BiMap<Coordinates, Card> cardPlacementMap = client.getOpponentData().get(currentPlayer).getBoard();
        Character[][] drawnCard;

        for (Card card : cardPlacementList) {
            Coordinates current = cardPlacementMap.inverse().get(card);
            Coordinates relativeCoordinates = getOffsetCoordinates(current);
            if (isInView(relativeCoordinates)) {
                if (card instanceof StartingCard starting) {
                    if (!card.isFrontSideUp()) {
                        drawnCard = drawCard(starting.getBackCorners(), starting.getFrontResources().getFirst(), card);
                    } else {
                        drawnCard = drawCard(starting.getFrontCorners(), starting.getFrontResources().getFirst(), card);
                    }
                } else if (card instanceof ColoredCard colored) {
                    if (!card.isFrontSideUp()) {
                        drawnCard = drawCard(colored.getBackCorners(), colored.getBackResource(), card);
                    } else {
                        drawnCard = drawCard(colored.getFrontCorners(), colored.getBackResource(), card);
                    }
                } else {
                    throw new RuntimeException("Unknown card type");
                }

                for (int y = -1; y < 2; y++) {
                    System.arraycopy(drawnCard[y + 1], 0, board[relativeCoordinates.y() + y],
                            relativeCoordinates.x() + -3, 7);
                }
            }

        }

        ArrayList<Coordinates> validPosition = client.getPlayerData().getValidPlacements();

        if (!client.getUsername().equals(currentPlayer)) {
            return;
        }

        for (int i = 0; i < validPosition.size(); i++) {
            Coordinates relativeCoordinates = getOffsetCoordinates(validPosition.get(i));
            if (isInView(relativeCoordinates)) {

                String number = String.valueOf(i);
                String paddedString;
                if (number.length() == 1) {
                    paddedString = " " + number + " ";
                } else {
                    paddedString = String.format("%3s", number);
                }
                for (int x = -1; x < 2; x++) {
                    board[relativeCoordinates.y()][relativeCoordinates.x() + x] = paddedString.charAt(x + 1);
                }
            }
        }
    }

    /**
     * Converts the board layout into a string representation for display in the TUI.
     *
     * @return A string representation of the board layout.
     */
    @Override
    public String toString() {
        compute();
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(board[y][x] != null ? board[y][x] : " ");
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Draws a card's ASCII art representation based on its corners, resource, and type.
     *
     * @param corners  Array of Corner objects representing the card's corners.
     * @param resource The resource associated with the card.
     * @param card     The Card object to draw.
     * @return A 2D array of characters representing the ASCII art of the card.
     */
    private Character[][] drawCard(Corner[] corners, Resource resource, Card card) {

        StringBuilder cardBuilder = new StringBuilder();

        if (!card.isFrontSideUp() && card instanceof ColoredCard coloredCard) {
            cardBuilder.append(Resource.NONE.toChar())
                    .append("▔▔▔▔▔")
                    .append(Resource.NONE.toChar())
                    .append("\n")
                    .append("▏")
                    .append("  ")
                    .append(coloredCard.getBackResource().toChar())
                    .append("  ")
                    .append("▕\n")
                    .append(Resource.NONE.toChar())
                    .append("▁▁▁▁▁")
                    .append(Resource.NONE.toChar());
        } else {
            cardBuilder.append(corners[0].getCornerResource().toChar())
                    .append("▔▔▔▔▔")
                    .append(corners[1].getCornerResource().toChar())
                    .append("\n");

            cardBuilder.append("▏");

            if (card instanceof StartingCard) {
                if (!((StartingCard) card).isFrontSideUp()) {
                    cardBuilder.append("  ")
                            .append("❖")
                            .append("  ");
                } else {
                    int resourceNum = ((StartingCard) card).getFrontResources().size();
                    switch (resourceNum) {
                        case 1:
                            cardBuilder.append("  ")
                                    .append(((StartingCard) card).getFrontResources().getFirst().toChar())
                                    .append("  ");
                            break;
                        case 2:
                            cardBuilder.append(" ")
                                    .append(((StartingCard) card).getFrontResources().getFirst().toChar())
                                    .append(" ")
                                    .append(((StartingCard) card).getFrontResources().getLast().toChar())
                                    .append(" ");
                            break;
                        case 3:
                            cardBuilder.append(" ")
                                    .append(((StartingCard) card).getFrontResources().getFirst().toChar())
                                    .append(((StartingCard) card).getFrontResources().get(1).toChar())
                                    .append(((StartingCard) card).getFrontResources().getLast().toChar())
                                    .append(" ");
                            break;
                        default:
                            throw new RuntimeException("Invalid number of resources");
                    }
                }
            } else if (card instanceof ColoredCard) {
                if (card.isFrontSideUp()) {
                    cardBuilder.append("  ")
                            .append(((ColoredCard) card).getBackResource().toCharCenter())
                            .append("  ");
                } else {
                    cardBuilder.append("  ")
                            .append(((ColoredCard) card).getBackResource().toChar())
                            .append("  ");
                }
            }

            cardBuilder.append("▕")
                    .append("\n");
            cardBuilder.append(corners[2].getCornerResource().toChar())
                    .append("▁▁▁▁▁")
                    .append(corners[3].getCornerResource().toChar())
                    .append("\n");
        }

        String[] lines = cardBuilder.toString().split("\n");

        Character[][] characterMatrix = new Character[lines.length][lines[0].length()];

        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                characterMatrix[i][j] = lines[i].charAt(j);
            }
        }

        return characterMatrix;
    }

    /**
     * Doubles the coordinates for a more accurate board placement calculation.
     *
     * @param coordinates The original coordinates to be doubled.
     * @return Doubled coordinates for precise placement.
     */
    private Coordinates doubleCoordinates(Coordinates coordinates) {
        return new Coordinates(
                coordinates.x() * 6,
                coordinates.y() * 2);
    }

    /**
     * Adjusts the given coordinates with rotation and scaling for board layout.
     *
     * @param coordinates The original coordinates to be adjusted.
     * @return Adjusted coordinates based on board center and orientation.
     */
    private Coordinates getOffsetCoordinates(Coordinates coordinates) {
        coordinates = RotateBoard.rotateCoordinates(coordinates, -45);
        coordinates = doubleCoordinates(coordinates);
        return new Coordinates(
                coordinates.x() + baseOffset_x - center.x(),
                -coordinates.y() + baseOffset_y - center.y());
    }

    /**
     * Checks if the given coordinates are within the visible board area.
     *
     * @param coordinates The coordinates to check.
     * @return True if the coordinates are within the visible board area, false otherwise.
     */
    private boolean isInView(Coordinates coordinates) {
        return (coordinates.x() >= 0 &&
                coordinates.y() >= 0 &&
                coordinates.x() < width &&
                coordinates.y() < height);
    }

    /**
     * Clears the board by filling it with empty spaces (' ').
     */
    private void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = ' ';
            }
        }
    }
}
