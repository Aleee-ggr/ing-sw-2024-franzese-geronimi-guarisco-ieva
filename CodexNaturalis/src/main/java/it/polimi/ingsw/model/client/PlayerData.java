package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.objectives.Objective;

import java.util.ArrayList;

public class PlayerData extends ClientData {
    private String password;

    private ArrayList<Coordinates> validPlacements;
    private ArrayList<Card> clientHand;

    private ArrayList<Objective> globalObjectives;
    private ArrayList<Objective> startingObjectives;
    private Objective personalObjective;
    private StartingCard startingCard;

    public PlayerData(String password, String username) {
        super(username);
        this.password = password;
        this.validPlacements = new ArrayList<>();
        this.clientHand = new ArrayList<>();
        this.globalObjectives = new ArrayList<>();
        this.startingObjectives = new ArrayList<>();
    }
}
