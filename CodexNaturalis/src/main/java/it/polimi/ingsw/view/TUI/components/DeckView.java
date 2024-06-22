package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.components.printables.CardBack;
import it.polimi.ingsw.view.TUI.components.printables.PrintCards;

import java.util.ArrayList;

/**
 * Represents a component for displaying the deck of cards and visible cards in the TUI.
 * Implements the {@link Component} interface.
 */
public class DeckView implements Component {
    private static final int width = 146;
    private static final int height = 27;

    private static final int hSpacing = 4;
    private static final int vSpacing = 1;
    private static final int contentWidth = hSpacing * 4 + CardBack.width * 4;
    private static final int contentHeight = vSpacing * 3 + CardBack.height + PrintCards.height;

    private static final int paddingLeft = (width - contentWidth - 2) / 2;
    private static final int paddingTop = (height - contentHeight - 4) / 2;
    private static final String header = "DRAW YOUR CARD";

    private Resource[] backs = new Resource[2];
    private Card[] visibleCards = new Card[GameConsts.visibleCards];
    private ClientInterface client;

    /**
     * Constructs a DeckView with the given client interface.
     *
     * @param client The client interface used to retrieve deck and visible cards information.
     */
    public DeckView(ClientInterface client) {
        this.client = client;
    }

    /**
     * Replaces the visible card at the specified index with a new card.
     *
     * @param index   The index of the visible card to replace.
     * @param newCard The new card to be displayed as a visible card.
     */
    public void replaceVisibleCard(int index, Card newCard) {
        this.visibleCards[index] = newCard;
    }

    /**
     * Replaces the deck card at the specified index with a new resource type.
     *
     * @param index       The index of the deck card to replace.
     * @param newResource The new resource type to be displayed as the back of the deck card.
     */
    public void replaceDeckCard(int index, Resource newResource) {
        this.backs[index] = newResource;
    }

    /**
     * Generates a string representation of the deck view, including visible cards and deck backs.
     *
     * @return A string representation of the deck view.
     */
    @Override
    public String toString() {
        this.visibleCards = client.getVisibleCards().toArray(new Card[0]);
        ArrayList<Resource> res = new ArrayList<>();
        for (Card card : client.getDecksBacks()) {
            ColoredCard colored = (ColoredCard) card;
            res.add(colored.getBackResource());
        }
        this.backs = res.toArray(new Resource[0]);

        PrintCards[] printCards = new PrintCards[visibleCards.length];
        for (int i = 0; i < visibleCards.length; i++) {
            printCards[i] = new PrintCards(visibleCards[i]);
        }
        StringBuilder out = new StringBuilder()
                .append(" \n".repeat(paddingTop))
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


        // Print Visible cards
        for (int i = 0; i < PrintCards.height; i++) {
            out.append(" ".repeat(paddingLeft))
                    .append("│");

            if(i == 0){
                out.append("1.");
            } else {
                out.append(" ".repeat(hSpacing/2));
            }

            for (int j = 0; j < visibleCards.length - 1; j++) {
                if(i==0){
                    out.append(printCards[j].toStringArray()[i])
                            .append(" ".repeat(hSpacing-2))
                            .append(j+2)
                            .append(".");
                } else {
                    out.append(printCards[j].toStringArray()[i])
                            .append(" ".repeat(hSpacing));
                }
            }
            out.append(printCards[visibleCards.length - 1].toStringArray()[i]);
            out.append(" ".repeat(hSpacing/2))
            .append("│\n");
        }

        // Print decks
        for (int i = 0; i < CardBack.height; i++) {
            out.append(" ".repeat(paddingLeft))
                    .append("│");

            if(i == 0){
                out.append("5.");
            } else {
                out.append(" ".repeat(hSpacing/2));
            }

            out.append(CardBack.resourcesGold.get(backs[0]).split("\n")[i]);

            if(i == 0){
                out.append(" ".repeat(CardBack.width * 2 + hSpacing * 3 - 2))
                        .append("6.");
            } else {
                out.append(" ".repeat(CardBack.width * 2 + hSpacing * 3));
            }

            out.append(CardBack.resources.get(backs[1]).split("\n")[i])
                .append(" ".repeat(hSpacing/2))
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

        out.append(" \n".repeat(paddingTop+1));
        return out.toString();
    }
}
