package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.view.TUI.components.printables.CardBack;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

public class DeckView implements Component {
    private static final int width = 146;
    private static final int height = 27;

    private static final int hSpacing = 4;
    private static final int vSpacing = 1;
    private static final int contentWidth = hSpacing * 4 + CardBack.width * 4;
    private static final int contentHeight = vSpacing * 3 + CardBack.height * 2;

    private static final int paddingLeft = (width - contentHeight - 2) / 2;
    private static final int paddingTop = (height - contentHeight - 4) / 2;
    private static final String header = "DRAW YOUR CARD";

    private Resource[] backs = new Resource[2];
    private Card[] visibleCards = new Card[GameConsts.visibleCards];


    public DeckView(Resource[] backs, Card[] visibleCards) {
        this.backs = backs;
        this.visibleCards = visibleCards;
    }

    public void replaceVisibleCard(int index, Card newCard) {
        this.visibleCards[index] = newCard;
    }

    public void replaceDeckCard(int index, Resource newResource) {
        this.backs[index] = newResource;
    }

    @Override
    public String toString() {
        PrintCards[] printCards = new PrintCards[visibleCards.length];
        for (int i = 0; i < visibleCards.length; i++) {
            printCards[i] = new PrintCards(visibleCards[i]);
        }
        StringBuilder out = new StringBuilder()
                .append("\n".repeat(paddingTop))
                .append(" ".repeat(paddingLeft))
                .append("╭")
                .append("─".repeat(contentWidth))
                .append("╮\n")
                .append(" ".repeat(paddingLeft))
                .append("│")
                .append(" ".repeat((contentWidth - header.length())/2))
                .append(header)
                .append(" ".repeat((contentWidth - header.length())/2))
                .append("│\n")
                .append(" ".repeat(paddingLeft))
                .append("├")
                .append("─".repeat(contentWidth))
                .append("┤\n");
        for (int i = 0; i < vSpacing; i++) {
            out.append(" ".repeat(paddingLeft))
                    .append("│")
                    .append(" ".repeat(contentWidth))
                    .append("│\n");
        }

        for (int i = 0; i < CardBack.height; i++) {
            out.append(" ".repeat(paddingLeft))
                    .append("│")
                    .append(" ".repeat(hSpacing/2))
                    .append(CardBack.resources.get(backs[0]).split("\n")[i])
                    .append(" ".repeat(CardBack.width * 2 + hSpacing * 3))
                    .append(CardBack.resources.get(backs[1]).split("\n")[i])
                    .append(" ".repeat(hSpacing/2))
                    .append("│\n");
        }

        for (int i = 0; i < vSpacing; i++) {
            out.append(" ".repeat(paddingLeft))
                    .append("│")
                    .append(" ".repeat(contentWidth))
                    .append("│\n");
        }

        for (int i = 0; i < CardBack.height; i++) {
            out.append(" ".repeat(paddingLeft))
                    .append("│")
                    .append(" ".repeat(hSpacing/2));
                    for (int j = 0; j < visibleCards.length - 1; j++) {
                        out.append(printCards[j].toStringArray()[i])
                                .append(" ".repeat(hSpacing));
                    }
                    out.append(printCards[visibleCards.length - 1].toStringArray()[i]);
                    out.append(" ".repeat(hSpacing/2))
                    .append("│\n");
        }

        for (int i = 0; i < vSpacing; i++) {
            out.append(" ".repeat(paddingLeft))
                    .append("│")
                    .append(" ".repeat(contentWidth))
                    .append("│\n");
        }

        out.append(" ".repeat(paddingLeft))
                .append("╰")
                .append("─".repeat(contentWidth))
                .append("╯\n");


        return out.toString();
    }
}
