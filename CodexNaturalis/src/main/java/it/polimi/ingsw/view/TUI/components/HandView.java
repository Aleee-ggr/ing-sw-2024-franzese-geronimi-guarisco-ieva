package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.OpponentData;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.components.printables.CardBack;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

import java.util.List;

/**
 * Represents a component for displaying the hand of cards for either the current player or opponents in the TUI.
 * Implements the {@link Component} interface.
 */
public class HandView implements Component {
    public static final int panelHeight = 22;
    public static final int panelWidth = 19;

    private final ClientInterface client;

    private String currentPlayer;

    /**
     * Constructs a HandView object initialized with the provided client interface.
     *
     * @param client The client interface used to retrieve player and opponent data.
     */
    public HandView(ClientInterface client) {
        this.client = client;
        currentPlayer = client.getUsername();
    }

    @Override
    public String toString() {
        return setHand();
    }

    /**
     * Sets the current player associated with the hand view.
     *
     * @param currentPlayer The username of the current player or opponent.
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Generates the formatted string representation of the hand of cards for the current player or opponent.
     *
     * @return The formatted string representing the hand of cards.
     */
    private String setHand() {
        StringBuilder out = new StringBuilder();
        int leftRightPadding = (panelWidth - PrintCards.width) / 2;
        Integer cardNum = 1;

        if (currentPlayer.equals(client.getUsername())) {
            List<Card> cardsToPrint = client.getPlayerData().getClientHand();

            for (Card card : cardsToPrint) {
                out.append(cardNum)
                        .append(".");
                boolean firstLine = true;

                for (String line : new PrintCards(card).toString().split("\n")) {

                    if (firstLine) {
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
        }

        else {
            OpponentData data = (OpponentData) client.getOpponentData().get(currentPlayer);
            List<Resource> cardsToPrint = data.getHandColor();
            for (int i = 0; i<cardsToPrint.size(); i++) {
                out.append(cardNum)
                        .append(".");
                out.append(" ".repeat(panelWidth - (Integer.toString(cardNum).length()) - 1));
                out.append("\n");
                String[] cardBack;

                if(data.getHandIsGold().get(i)){
                    cardBack = CardBack.resourcesGold.get(cardsToPrint.get(i)).split("\n");
                } else {
                    cardBack = CardBack.resources.get(cardsToPrint.get(i)).split("\n");
                }

                for (String line : cardBack) {
                    out.append(" ".repeat(leftRightPadding))
                            .append(line)
                            .append(" ".repeat(leftRightPadding))
                            .append("\n");
                }
                out.append(" ".repeat(panelWidth));
                out.append("\n");
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
        }
        return out.toString();
    }
}
