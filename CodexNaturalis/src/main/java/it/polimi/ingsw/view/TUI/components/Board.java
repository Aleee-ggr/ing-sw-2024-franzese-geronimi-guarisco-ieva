package it.polimi.ingsw.view.TUI.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.network.Client;

import java.util.List;


public class Board implements Component {
    public static final int width = 147;
    public static final int height = 27;
    private final Character[][] board = new Character[height][width];
    private Coordinates center;
    private static final int baseOffset_x = width / 2;
    private static final int baseOffset_y = height / 2;


    public Board() {
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
        List<Integer> cardPlacementList = Client.getData().getClientPlacingOrder();
        BiMap<Coordinates, Integer> cardPlacementMap = HashBiMap.create(Client.getData().getClientBoard());

        for (Integer cardId: cardPlacementList) {
            Coordinates current = cardPlacementMap.inverse().get(cardId);
            Coordinates relativeCoordinates  = getOffsetCoordinates(current);
            if (isInView(relativeCoordinates)) {
                Card card = Game.getCardByID(cardId);
                if (card instanceof StartingCard starting) {
                    board[relativeCoordinates.y()][relativeCoordinates.x()] = 'S';
                    if (cardId < 0) {
                        setCorners(relativeCoordinates, starting.getBackCorners());
                    }
                    continue;
                } 
                if (card instanceof ColoredCard colored) {
                    board[relativeCoordinates.y()][relativeCoordinates.x()] = colored.getBackResource().toChar();
                    if (cardId < 0) {
                        for (Coordinates neighbor : relativeCoordinates.getNeighbors()) {
                            board[neighbor.y()][neighbor.x()] = 'N';
                        }
                        continue;
                    }

                    setCorners(relativeCoordinates, colored.getFrontCorners());

                    continue;
                }

                throw new RuntimeException("Unknown card type");
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
        coordinates = doubleCoordinates(coordinates);
        return new Coordinates(
                coordinates.x() +  baseOffset_x - center.x(),
                coordinates.y() + baseOffset_y - center.y());
    }

    private boolean isInView(Coordinates coordinates) {
        Coordinates relativeCoordinates = getOffsetCoordinates(coordinates);
        return  (relativeCoordinates.x() >= 0 &&
                relativeCoordinates.y() >= 0 &&
                relativeCoordinates.x() < width &&
                relativeCoordinates.y() >= height
        );
    }
}
