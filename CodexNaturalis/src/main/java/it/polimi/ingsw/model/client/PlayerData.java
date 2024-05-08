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

    public PlayerData() {
        super();
        this.validPlacements = new ArrayList<>();
        this.clientHand = new ArrayList<>();
        this.globalObjectives = new ArrayList<>();
        this.startingObjectives = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Coordinates> getValidPlacements() {
        return new ArrayList<>(validPlacements);
    }

    public ArrayList<Card> getClientHand() {
        return new ArrayList<>(clientHand);
    }

    public ArrayList<Objective> getGlobalObjectives() {
        return new ArrayList<>(globalObjectives);
    }

    public ArrayList<Objective> getStartingObjectives() {
        return new ArrayList<>(startingObjectives);
    }

    public Objective getPersonalObjective() {
        return personalObjective;
    }

    public StartingCard getStartingCard() {
        return startingCard;
    }

    public void setValidPlacements(ArrayList<Coordinates> validPlacements) {
        this.validPlacements = validPlacements;
    }

    public void setClientHand(ArrayList<Card> clientHand) {
        this.clientHand = clientHand;
    }

    public void setGlobalObjectives(ArrayList<Objective> globalObjectives) {
        this.globalObjectives = globalObjectives;
    }

    public void setStartingObjectives(ArrayList<Objective> startingObjectives) {
        this.startingObjectives = startingObjectives;
    }

    public void setPersonalObjective(Objective personalObjective) {
        this.personalObjective = personalObjective;
    }

    public void setStartingCard(StartingCard startingCard) {
        this.startingCard = startingCard;
    }

    public void setCredential(String username, String password) {
        super.setUsername(username);
        this.password = password;
    }
}
