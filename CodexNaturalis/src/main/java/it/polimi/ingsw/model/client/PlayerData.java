package it.polimi.ingsw.model.client;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.ElementNotInHand;
import it.polimi.ingsw.helpers.exceptions.model.HandFullException;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.objectives.Objective;

import java.util.ArrayList;

/**
 * The PlayerData class extends the ClientData class and contains the data specific to the player in the game.
 * The client must have the valid placements, the hand, the global objectives, the starting objectives, the personal objective, and the starting card.
 * */
public class PlayerData extends ClientData {

    private ArrayList<Coordinates> validPlacements;
    private ArrayList<Card> clientHand;

    private ArrayList<Objective> globalObjectives;
    private ArrayList<Objective> startingObjectives;
    private Objective personalObjective;
    private StartingCard startingCard;

    /**
     * Constructor for the PlayerData class.
     * Initializes the valid placements, hand, global objectives, starting objectives, personal objective, and starting card.
     * */
    public PlayerData() {
        super();
        this.validPlacements = new ArrayList<>();
        this.clientHand = new ArrayList<>();
        this.globalObjectives = new ArrayList<>();
        this.startingObjectives = new ArrayList<>();
    }

    /**
     * Getter for the valid placements of cards on the board.
     * @return the ArrayList of Coordinates of valid placements
     * */
    public ArrayList<Coordinates> getValidPlacements() {
        return new ArrayList<>(validPlacements);
    }

    /**
     * Getter for the client's hand.
     * @return the ArrayList of Cards in the hand
     * */
    public ArrayList<Card> getClientHand() {
        return new ArrayList<>(clientHand);
    }

    /**
     * Getter for the global objectives.
     * @return the ArrayList of global objectives
     * */
    public ArrayList<Objective> getGlobalObjectives() {
        return new ArrayList<>(globalObjectives);
    }

    /**
     * Getter for the starting objectives.
     * @return the ArrayList of starting objectives
     * */
    public ArrayList<Objective> getStartingObjectives() {
        return new ArrayList<>(startingObjectives);
    }

    /**
     * Getter for the personal objective.
     * @return the personal objective
     * */
    public Objective getPersonalObjective() {
        return personalObjective;
    }

    /**
     * Getter for the starting card.
     * @return the starting card
     * */
    public StartingCard getStartingCard() {
        return startingCard;
    }

    /**
     * Setter for the valid placements.
     * @param validPlacements the ArrayList of Coordinates of valid placements
     * */
    public void setValidPlacements(ArrayList<Coordinates> validPlacements) {
        this.validPlacements = validPlacements;
    }

    /**
     * Setter for the client's hand.
     * @param clientHand the ArrayList of Cards in the hand
     * */
    public void setClientHand(ArrayList<Card> clientHand) {
        this.clientHand = clientHand;
    }

    /**
     * Setter for the global objectives.
     * @param globalObjectives the ArrayList of global objectives
     * */
    public void setGlobalObjectives(ArrayList<Objective> globalObjectives) {
        this.globalObjectives = globalObjectives;
    }

    /**
     * Setter for the starting objectives.
     * @param startingObjectives the ArrayList of starting objectives
     * */
    public void setStartingObjectives(ArrayList<Objective> startingObjectives) {
        this.startingObjectives = startingObjectives;
    }

    /**
     * Setter for the personal objective.
     * @param personalObjective the personal objective
     * */
    public void setPersonalObjective(Objective personalObjective) {
        this.personalObjective = personalObjective;
    }

    /**
     * Setter for the starting card.
     * @param startingCard the starting card
     * */
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

    /**
     * Places the starting card on the board.
     * @param card The starting card to be placed on the board.
     */
    public void placeStartingCard(Card card){
        this.board.put(new Coordinates(0, 0), card);
        this.order.add(card);
        this.setStartingCard((StartingCard) card);
    }


}
