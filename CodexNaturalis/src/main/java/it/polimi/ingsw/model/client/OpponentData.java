package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;

/**
 * The OpponentData class extends the ClientData class and contains the data specific to the opponents in the game.
 * The client must have the opponents' hand color.
 * */
public class OpponentData extends ClientData{
    private ArrayList<Resource> handColor;
    private ArrayList<Boolean> handIsGold;
    private Color playerColor;

    /**
     * Constructor for the OpponentData class.
     * Initializes the hand color.
     * */
    public OpponentData() {
        super();
        this.handColor = new ArrayList<>();
        this.handIsGold = new ArrayList<>();
    }

    /**
     * Getter for the hand color.
     * @return the ArrayList of the hand color
     * */
    public ArrayList<Resource> getHandColor() {
        return new ArrayList<>(handColor);
    }

    public ArrayList<Boolean> getHandIsGold() {
        return handIsGold;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * Setter for the hand color.
     * @param handColor the ArrayList of the hand color
     * */
    public void setHandColor(ArrayList<Resource> handColor) {
        this.handColor = handColor;
    }

    public void setHandIsGold(ArrayList<Boolean> handIsGold) {
        this.handIsGold = handIsGold;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }
}
