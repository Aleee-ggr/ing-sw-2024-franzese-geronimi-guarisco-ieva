package it.polimi.ingsw.view.TUI.components;

import com.google.common.collect.BiMap;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.client.PlayerData;
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
    
    public void compute() {
        PlayerData player = (PlayerData) client.getOpponentData().get(client.getUsername());
        List<Card> cardPlacementList = player.getOrder();
        BiMap<Coordinates, Card> cardPlacementMap = player.getBoard();

        for (Card card: cardPlacementList) {
            Coordinates current = cardPlacementMap.inverse().get(card);
            Coordinates relativeCoordinates  = getOffsetCoordinates(current);
            if (isInView(relativeCoordinates)) {
                if (card instanceof StartingCard starting) {
                    board[relativeCoordinates.y()][relativeCoordinates.x()] = 'S';
                    setLines(relativeCoordinates);
                    if (!card.isFrontSideUp()) {
                        setCorners(relativeCoordinates, starting.getBackCorners());
                        setLines(relativeCoordinates);
                    }
                    continue;
                } 
                if (card instanceof ColoredCard colored) {
                    board[relativeCoordinates.y()][relativeCoordinates.x()] = colored.getBackResource().toChar();
                    if (!card.isFrontSideUp()) {
                        for (Coordinates neighbor : relativeCoordinates.getNeighbors()) {
                            board[neighbor.y()][neighbor.x()] = 'N';
                        }
                        continue;
                    }

                    setCorners(relativeCoordinates, colored.getFrontCorners());
                    setLines(relativeCoordinates);

                    continue;
                }

                throw new RuntimeException("Unknown card type");
            }

        }

        ArrayList<Coordinates> validPosition = player.getValidPlacements();

        for (int i = 0; i< validPosition.size(); i++) {
            Coordinates relativeCoordinates = getOffsetCoordinates(validPosition.get(i));
            if (isInView(relativeCoordinates)) {
                board[relativeCoordinates.y()][relativeCoordinates.x()] = Integer.toHexString(i).toCharArray()[0];
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
                coordinates.x() * 2,
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
}
