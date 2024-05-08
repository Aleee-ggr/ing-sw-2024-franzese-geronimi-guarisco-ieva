package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;

public class OpponentData extends ClientData{
    private ArrayList<Resource> handColor;

    public OpponentData() {
        super();
        this.handColor = new ArrayList<>();
    }

    public ArrayList<Resource> getHandColor() {
        return new ArrayList<>(handColor);
    }

    public void setHandColor(ArrayList<Resource> handColor) {
        this.handColor = handColor;
    }
}
