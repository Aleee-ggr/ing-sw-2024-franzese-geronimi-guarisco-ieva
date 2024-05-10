package it.polimi.ingsw.model.client;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The ClientData class contains the common data for all the players in the game.
 * The client must have every player's board, order of the cards, and resources.
 * The class is abstract and is extended by the PlayerData class and the OpponentData class.
 * */
public abstract class ClientData {
    protected BiMap<Coordinates, Card> board;
    protected ArrayList<Card> order;
    protected HashMap<Resource, Integer> resources;

    /**
     * Constructor for the ClientData class.
     * Initializes the board, order, and resources.
     * */
    public ClientData() {
        this.board = HashBiMap.create();
        this.order = new ArrayList<>();
        this.resources = new HashMap<>() {{
            for (Resource res : Resource.values()) {
                put(res, 0);
            }
        }};
    }

    /**
     * Getter for the board.
     * @return the BiMap of the board
     * */
    public BiMap<Coordinates, Card> getBoard() {
        return HashBiMap.create(board);
    }

    /**
     * Getter for the placing order.
     * @return the ArrayList of the placing order
     * */
    public ArrayList<Card> getOrder() {
        return new ArrayList<>(order);
    }

    /**
     * Getter for the resources.
     * @return the HashMap of the resources
     * */
    public HashMap<Resource, Integer> getResources() {
        return new HashMap<>(resources);
    }

    /**
     * Setter for the board.
     * @param board the BiMap of the board
     * */
    public void setBoard(BiMap<Coordinates, Card> board) {
        this.board = board;
    }

    /**
     * Setter for the placing order.
     * @param order the ArrayList of the placing order
     * */
    public void setOrder(ArrayList<Card> order) {
        this.order = order;
    }

    /**
     * Setter for the resources.
     * @param resources the HashMap of the resources
     * */
    public void setResources(HashMap<Resource, Integer> resources) {
        this.resources = resources;
    }

}
