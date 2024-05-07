package it.polimi.ingsw.model.player;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: DOCUMENTATION AND CONSTRUCTOR

public class PlayersDataLight {
    private BiMap<Coordinates, Integer> board = HashBiMap.create();
    private List<Integer> order = new ArrayList<>();
    private Map<Resource, Integer> resources = new HashMap<>();
    private List<Resource> handColor = new ArrayList<>(GameConsts.firstHandDim);
    private Integer score = 0;
    private Integer startingCard;

    /*
    public PlayersDataLight(HashMap<Coordinates, Card> board, List<Integer> order, Map<Resource, Integer> resources, List<Resource> handColor, Integer score) {
        this.board = board;
        this.order = order;
        this.resources = resources;
        this.handColor = handColor;
        this.score = score;
    }
    */

    public Map<Coordinates, Integer> getBoard() {
        return board;
    }

    public Integer getStartingCard() {
        return startingCard;
    }

    public List<Integer> getOrder() {
        return order;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public List<Resource> getHandColor() {
        return handColor;
    }

    public Integer getScore() {
        return score;
    }

    public void setBoard(HashMap<Coordinates, Integer> board) {
        this.board = HashBiMap.create(board);
    }

    public void setStartingCard(Integer startingCard) {
        this.startingCard = startingCard;
    }

    public void setOrder(List<Integer> order) {
        this.order = order;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void updateResources(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public void updateHand (List<Resource> handColor) {
        this.handColor = handColor;
    }

    public void addCard (Coordinates coordinates, Integer cardId) {
        board.put(coordinates, cardId);
        order.add(cardId);
    }
}

