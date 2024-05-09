package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;

/**
 * The OpponentData class extends the ClientData class and contains the data specific to the opponents in the game.
 * The client must have the opponents' hand color.
 * */
public class OpponentData extends ClientData{
    private ArrayList<Resource> handColor;

    /**
     * Constructor for the OpponentData class.
     * Initializes the hand color.
     * */
    public OpponentData() {
        super();
        this.handColor = new ArrayList<>();
    }

    /**
     * Getter for the hand color.
     * @return the ArrayList of the hand color
     * */
    public ArrayList<Resource> getHandColor() {
        return new ArrayList<>(handColor);
    }

    /**
     * Setter for the hand color.
     * @param handColor the ArrayList of the hand color
     * */
    public void setHandColor(ArrayList<Resource> handColor) {
        this.handColor = handColor;
    }
}
