package it.polimi.ingsw.model.client;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.ElementNotInHand;
import it.polimi.ingsw.helpers.exceptions.model.HandFullException;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.objectives.Objective;

import java.util.ArrayList;

public class PlayerData extends ClientData {

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

    /**
     * Adds a card to the client's hand if the hand is not already full.
     * @param card The card to be added to the hand.
     * @throws HandFullException If the client's hand is already full.
     */
    public void addToHand(Card card) throws HandFullException {
        if(clientHand.size() > GameConsts.firstHandDim){
            throw new HandFullException("client hand full");
        } else {
            clientHand.add(card);
        }
    }

    /**
     * Removes a card from the client's hand if it exists.
     * @param card The card to be removed from the hand.
     * @throws ElementNotInHand If the specified card is not in the client's hand.
     */
    public void removeFromHand(Card card) throws ElementNotInHand {
        if(!clientHand.contains(card)){
            throw new ElementNotInHand("the client does not have this card!");
        } else {
            clientHand.remove(card);
        }
    }


}
