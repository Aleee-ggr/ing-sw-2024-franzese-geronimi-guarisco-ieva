package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.components.printables.CardBack;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

import java.util.List;
import java.util.Map;


public class HandView implements Component{
    public static final int panelHeight = 22;
    public static final int panelWidth = 19;

    private final ClientInterface client;

    @Override
    public String toString()  {
        return setHand();
    }

    public HandView(ClientInterface client) {
        this.client = client;
    }

    private String setHand() {
        List<Card> cardsToPrint = client.getPlayerData().getClientHand();
        StringBuilder out = new StringBuilder();
        int leftRightPadding = (panelWidth - PrintCards.width) / 2;
        Integer cardNum = 1;

        for (Card card : cardsToPrint) {
            out.append(cardNum)
                    .append(".");
            boolean firstLine = true;

            for (String line : new PrintCards(card).toString().split("\n")) {

                if(firstLine){
                    out.append(line)
                        .append(" ".repeat(leftRightPadding))
                        .append("\n");
                    firstLine = false;
                } else {
                    out.append(" ".repeat(leftRightPadding))
                            .append(line)
                            .append(" ".repeat(leftRightPadding))
                            .append("\n");
                }
            }

            cardNum++;
        }

        for (int i = 0; i < cardsToPrint.size() - GameConsts.firstHandDim; i++) {
            for (int j = 0; j < PrintCards.height; j++) {
                out.append(" ".repeat(panelWidth))
                        .append("\n");
            }
        }
        for (int i = 0; i < panelHeight - cardsToPrint.size() * (PrintCards.height); i++) {
            out.append(" ".repeat(panelWidth))
                    .append("\n");
        }
        return out.toString();
    }
}
