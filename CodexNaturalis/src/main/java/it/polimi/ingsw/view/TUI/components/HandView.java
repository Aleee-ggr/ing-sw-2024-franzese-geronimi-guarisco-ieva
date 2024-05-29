package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.client.OpponentData;
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

    private String currentPlayer;

    @Override
    public String toString()  {
        return setHand();
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public HandView(ClientInterface client) {
        this.client = client;
        this.currentPlayer = client.getUsername();
    }

    private String setHand() {
        StringBuilder out = new StringBuilder();

        if (currentPlayer.equals(client.getUsername())) {
            List<Card> cardsToPrint = client.getPlayerData().getClientHand();
            int leftRightPadding = (panelWidth - PrintCards.width) / 2;
            Integer cardNum = 1;

            for (Card card : cardsToPrint) {
                out.append(cardNum)
                        .append(".");
                if (card instanceof GoldCard goldCard) {
                    if (goldCard.getType().equals("cover")) {
                        out.append(" cover: ")
                                .append(goldCard.getScore())
                                .append(" ".repeat(panelWidth - " cover: 0".length() - (Integer.toString(cardNum).length()) - 1));
                    } else if (goldCard.getType().equals("none")) {
                        out.append(" ")
                                .append(goldCard.getScore())
                                .append(" ".repeat(panelWidth - " 0".length() - (Integer.toString(cardNum).length()) - 1));
                    } else {
                        out.append(" ")
                                .append(Resource.fromString(goldCard.getType()).toChar())
                                .append(": ")
                                .append(goldCard.getScore())
                                .append(" ".repeat(panelWidth - " R: 0".length() - (Integer.toString(cardNum).length()) - 1));
                    }
                } else {
                    out.append(" ".repeat(panelWidth - (Integer.toString(cardNum).length()) - 1));
                }
                out.append("\n");
                for (String line : new PrintCards(card).toString().split("\n")) {
                    out.append(" ".repeat(leftRightPadding))
                            .append(line)
                            .append(" ".repeat(leftRightPadding))
                            .append("\n");
                }
                if (card instanceof GoldCard goldCard) {
                    out.append(" ");
                    for (Map.Entry<Resource, Integer> e : goldCard.getRequirements().entrySet()) {
                        out.append(" %c:%d".formatted(e.getKey().toChar(), e.getValue()));
                    }
                    out.append(" ".repeat(panelWidth - goldCard.getRequirements().size() * 4 - 1));
                } else {
                    out.append(" ".repeat(panelWidth));
                }
                out.append("\n");
                cardNum++;
            }

            for (int i = 0; i < cardsToPrint.size() - GameConsts.firstHandDim; i++) {
                for (int j = 0; j < CardBack.height + 2; j++) {
                    out.append(" ".repeat(panelWidth))
                            .append("\n");
                }
            }
            for (int i = 0; i < panelHeight - cardsToPrint.size() * (PrintCards.height + 2); i++) {
                out.append(" ".repeat(panelWidth))
                        .append("\n");
            }
        }

        else {
            OpponentData data = (OpponentData) client.getOpponentData().get(currentPlayer);
            List<Resource> cardsToPrint = data.getHandColor();
            int leftRightPadding = (panelWidth - PrintCards.width) / 2;
            Integer cardNum = 1;
            for (Resource r : cardsToPrint) {
                out.append(cardNum)
                        .append(".");
                out.append(" ".repeat(panelWidth - (Integer.toString(cardNum).length()) - 1));
                out.append("\n");
                for (String line : CardBack.resources.get(r).split("\n")) {
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
                for (int j = 0; j < CardBack.height + 2; j++) {
                    out.append(" ".repeat(panelWidth))
                            .append("\n");
                }
            }
            for (int i = 0; i < panelHeight - cardsToPrint.size() * (PrintCards.height + 2); i++) {
                out.append(" ".repeat(panelWidth))
                        .append("\n");
            }
        }



        return out.toString();
    }
}
