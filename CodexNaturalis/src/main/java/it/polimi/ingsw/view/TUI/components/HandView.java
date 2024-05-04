package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.player.ClientData;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

import java.util.ArrayList;

//TODO:
public class HandView implements Component{
    public static final int panelHeight = 22;
    public static final int panelWidth = 19;

    private final ClientData clientData = Client.getData();
    private final Card[] hand = new Card[2];
    private final String outputHand;
    private int numHand = 0;


    @Override
    public String toString()  {
        return outputHand;
    }

    public HandView() {
        ArrayList<Integer> intHand = clientData.getHand();
        for(int id : intHand){
            hand[numHand] = Game.getCardByID(id);
            numHand++;
        }
        outputHand = setHand();
    }

    private String setHand() {
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < numHand; i++){
            out.append(numHand+1)
                    .append('.')
                    .append(" ".repeat(panelWidth-2));
            out.append(" ??");
        }
        return out.toString();
    }


}
