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


public class Board implements Component {
    public static final int width = 147;
    public static final int height = 27;
    private final Character[][] board = new Character[height][width];
    private Coordinates center;
    private static final int baseOffset_x = width / 2;
    private static final int baseOffset_y = height / 2;

    private final ClientInterface client;


    public Board(ClientInterface client) {
        this.client = client;
        this.center = new Coordinates(0, 0);
    }

    public void setCenter(Coordinates center) {
        this.center = center;
    }

    public void moveCenter(int x, int y) {
        this.center = center
                .horizontal(x)
                .vertical(y);
    }
    
    public void compute() {;
        clear();
        List<Card> cardPlacementList = client.getPlayerData().getOrder();
        BiMap<Coordinates, Card> cardPlacementMap = client.getPlayerData().getBoard();
        Character[][] drawnCard;

        for (Card card: cardPlacementList) {
            Coordinates current = cardPlacementMap.inverse().get(card);
            Coordinates relativeCoordinates  = getOffsetCoordinates(current);
            if (isInView(relativeCoordinates)) {
                if (card instanceof StartingCard starting) {
                    if (!card.isFrontSideUp()) {
                        drawnCard = drawCard(starting.getBackCorners(), starting.getFrontResources().getFirst()); //TODO: fix this (multiple resources)
                    } else {
                        drawnCard = drawCard(starting.getFrontCorners(), starting.getFrontResources().getFirst());
                    }
                } else if (card instanceof ColoredCard colored) {
                    if (!card.isFrontSideUp()) {
                        drawnCard = drawCard(colored.getBackCorners(), colored.getBackResource());
                    } else {
                        drawnCard = drawCard(colored.getFrontCorners(), colored.getBackResource());
                    }
                } else {
                    throw new RuntimeException("Unknown card type");
                }

                for(int y = -1; y < 2; y++) {
                    System.arraycopy(drawnCard[y + 1], 0, board[relativeCoordinates.y() + y], relativeCoordinates.x() + -3, 7);
                }
            }

        }

        ArrayList<Coordinates> validPosition = client.getPlayerData().getValidPlacements();

        for (int i = 0; i< validPosition.size(); i++) {
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
                    board[relativeCoordinates.y()][relativeCoordinates.x()+x] = paddedString.charAt(x+1);
                }
            }
        }
    }

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
    private void setLines(Coordinates c){
        int i = 0;
        for(Coordinates neighbor : c.getNeighbors()){
            if(isInView(neighbor) && i<2){
                i++;
                board[neighbor.y()][neighbor.x()] = '┃';
            }else if(isInView(neighbor)){
                board[neighbor.y()][neighbor.x()] = '━';
            }
        }
    }

    private Character[][] drawCard(Corner[] corners, Resource resource) {
        String cardBuilder = corners[0].getCornerResource().toChar() +
                    "▔▔▔▔▔" +
                    corners[1].getCornerResource().toChar() +
                    "\n" +
                    "▏" +
                    "     " +
                    "▕" +
                    "\n" +
                    corners[2].getCornerResource().toChar() +
                    "▁▁▁▁▁" +
                    corners[3].getCornerResource().toChar() +
                    "\n";


        String[] lines = cardBuilder.split("\n");

        Character[][] characterMatrix = new Character[lines.length][lines[0].length()];

        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                characterMatrix[i][j] = lines[i].charAt(j);
            }
        }

        return characterMatrix;
    }

    private void setCorners(Coordinates c, Corner[] corners) {
        int off_x = -1, off_y = -1;

        for (Corner corner: corners) {
            Coordinates current = c.horizontal(off_x).vertical(off_y);

            if (isInView(current) && corner.isCoverable()) {
                    board[current.y()][current.x()] = switch (corner.getCornerResource()) {
                        case FUNGI, ANIMAL, PLANT, INSECT -> corner.getCornerResource().toChar();
                        case NONCOVERABLE -> 'X';
                        default -> 'N';
                    };
            }

            if (off_x > 0) {
                off_y = 1;
            }
            off_x = -off_x;
        }
    }

    private Coordinates doubleCoordinates(Coordinates coordinates) {
        return new Coordinates(
                coordinates.x() * 6,
                coordinates.y() * 2
        );
    }

    private Coordinates getOffsetCoordinates(Coordinates coordinates) {
        coordinates = RotateBoard.rotateCoordinates(coordinates, -45);
        coordinates = doubleCoordinates(coordinates);
        return new Coordinates(
                coordinates.x() +  baseOffset_x - center.x(),
                -coordinates.y() + baseOffset_y - center.y());
    }

    private boolean isInView(Coordinates coordinates) {
        return  (coordinates.x() >= 0 &&
                coordinates.y() >= 0 &&
                coordinates.x() < width &&
                coordinates.y() < height
        );
    }

    private void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = ' ';
            }
        }
    }
}
