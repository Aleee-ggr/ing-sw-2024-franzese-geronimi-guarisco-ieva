package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

import java.util.ArrayList;
import java.util.List;

//TODO:
public class HandView implements Component{
    public static final int panelHeight = 22;
    public static final int panelWidth = 19;

    private final ClientData clientData = Client.getData();
    private final ArrayList<PrintCards> cardsToPrint = new ArrayList<>();
    private final String outputHand;


    @Override
    public String toString()  {
        return outputHand;
    }

    public HandView() {
        ArrayList<Integer> intHand = clientData.getHand();
        for(int id : intHand){
            cardsToPrint.add(new PrintCards((ColoredCard) Game.getCardByID(id)));
        }
        outputHand = setHand();
    }

    private String setHand() {
        StringBuilder out = new StringBuilder();
        int leftRightPadding = (panelWidth - PrintCards.width) / 2;
        Integer cardNum = 1;

        for(PrintCards card : cardsToPrint){
            out.append(cardNum)
                    .append(".")
                    .append(" ".repeat(panelWidth-(Integer.toString(cardNum).length())-1))
                    .append("\n");
            for(String line : card.toString().split("\n")){
                out.append(" ".repeat(leftRightPadding))
                        .append(line)
                        .append(" ".repeat(leftRightPadding))
                        .append("\n");
            }
            out.append(" ".repeat(panelWidth))
                    .append("\n");
            cardNum++;
        }
        for(int i = 0; i < panelHeight-cardsToPrint.size()*(PrintCards.height+2); i++){
            out.append(" ".repeat(panelWidth))
                    .append("\n");
        }
        return out.toString();
    }

    public static void main(String[] args) {
        new Client("usernameTest", "pwd", "", 0);
        ClientData clientData = Client.getData();
        clientData.setClientHand(new ArrayList<>(List.of(1, 2, 3)));
        HandView handView = new HandView();
        System.out.println(handView);
    }

}
