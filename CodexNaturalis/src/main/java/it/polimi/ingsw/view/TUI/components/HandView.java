package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

import java.util.ArrayList;


public class HandView implements Component{
    public static final int panelHeight = 22;
    public static final int panelWidth = 19;

    private final ClientInterface client;
    private final ArrayList<PrintCards> cardsToPrint = new ArrayList<>();
    private final String outputHand;


    @Override
    public String toString()  {
        return outputHand;
    }

    public HandView(ClientInterface client) {
        this.client = client;
        ArrayList<Card> intHand = ((PlayerData)client).getClientHand();
        for(Card card : intHand){
            cardsToPrint.add(new PrintCards(card));
        }
        outputHand = setHand();
    }

    private String setHand() {
        StringBuilder out = new StringBuilder();
        int leftRightPadding = (panelWidth - PrintCards.width) / 2;
        Integer cardNum = 1;

        for (PrintCards card : cardsToPrint) {
            out.append(cardNum)
                    .append(".")
                    .append(" ".repeat(panelWidth - (Integer.toString(cardNum).length()) - 1))
                    .append("\n");
            for (String line : card.toString().split("\n")) {
                out.append(" ".repeat(leftRightPadding))
                        .append(line)
                        .append(" ".repeat(leftRightPadding))
                        .append("\n");
            }
            out.append(" ".repeat(panelWidth))
                    .append("\n");
            cardNum++;
        }
        for (int i = 0; i < panelHeight - cardsToPrint.size() * (PrintCards.height + 2); i++) {
            out.append(" ".repeat(panelWidth))
                    .append("\n");
        }
        return out.toString();
    }
}
