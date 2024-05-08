package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;

public class OpponentData extends ClientData{
    private ArrayList<Resource> handColor;

    public OpponentData(String username) {
        super(username);
        this.handColor = new ArrayList<>();
    }
}
