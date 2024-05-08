package it.polimi.ingsw.model.client;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ClientData {
    private String username;

    private BiMap<Coordinates, Card> board;
    private ArrayList<Card> order;
    private HashMap<Resource, Integer> resources;

    public ClientData() {
        this.board = HashBiMap.create();
        this.order = new ArrayList<>();
        this.resources = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public BiMap<Coordinates, Card> getBoard() {
        return HashBiMap.create(board);
    }

    public ArrayList<Card> getOrder() {
        return new ArrayList<>(order);
    }

    public HashMap<Resource, Integer> getResources() {
        return new HashMap<>(resources);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBoard(BiMap<Coordinates, Card> board) {
        this.board = board;
    }

    public void setOrder(ArrayList<Card> order) {
        this.order = order;
    }

    public void setResources(HashMap<Resource, Integer> resources) {
        this.resources = resources;
    }

}
