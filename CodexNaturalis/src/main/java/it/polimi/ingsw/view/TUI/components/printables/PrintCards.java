package it.polimi.ingsw.view.TUI.components.printables;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.GoldCard;

public class PrintCards {
    private final ColoredCard card;

    private final String cardString;

    public PrintCards(ColoredCard card) {
        this.card = card;
        cardString = setCard(card);
    }

    public String[] toStringArray() {
        return this.toString().split("\n");
    }

    @Override
    public String toString()  {
        return cardString;
    }

    private static String setCard(ColoredCard card) {
        Corner[] corners;
        StringBuilder out = new StringBuilder();

        if (card.isFrontSideUp()) {
            corners = card.getFrontCorners();
        } else {
            corners = card.getBackCorners();
        }

        //first and second row
        if(corners[0].isCoverable() && corners[1].isCoverable()){
            out.append("┏━┳━━━━━━━━━┳━┓\n");
            out.append("┃")
                    .append(corners[0].getCornerResource().toChar())
                    .append("┃         ┃")
                    .append(corners[1].getCornerResource().toChar())
                    .append("┃\n");
        } else if (corners[0].isCoverable() && !corners[1].isCoverable()){
            out.append("┏━┳━━━━━━━━━━━┓\n");
            out.append("┃")
                    .append(corners[0].getCornerResource().toChar())
                    .append("┃           ")
                    .append("┃\n");
        } else if (!corners[0].isCoverable() && corners[1].isCoverable()){
            out.append("┏━━━━━━━━━━━┳━┓\n");
            out.append("┃")
                    .append("           ")
                    .append('┃')
                    .append(corners[1].getCornerResource().toChar())
                    .append("┃\n");
        } else {
            out.append("┏━━━━━━━━━━━━━┓\n");
            out.append("┃             ┃\n");
        }


        //third row
        if(corners[0].isCoverable() && corners[2].isCoverable()){
            out.append("┣━┫");
        } else if (corners[0].isCoverable() && !corners[2].isCoverable()){
            out.append("┣━┛");
        } else if (!corners[0].isCoverable() && corners[2].isCoverable()){
            out.append("┣━┓");
        } else {
            out.append("┃  ");
        }

        out.append("   ");

        if(card instanceof GoldCard){
            out.append('┃')
                    .append(card.getBackResource().toChar())
                    .append('┃');
        } else {
            out.append(' ')
                    .append(card.getBackResource().toChar())
                    .append(' ');
        }

        out.append("   ");

        if(corners[1].isCoverable() && corners[3].isCoverable()){
            out.append("┣━┫\n");
        } else if (corners[1].isCoverable() && !corners[3].isCoverable()){
            out.append("┗━┫\n");
        } else if (!corners[1].isCoverable() && corners[3].isCoverable()){
            out.append("┏━┫\n");
        } else {
            out.append("  ┃\n");
        }

        //fourth row
        out.append('┃');

        if(corners[2].isCoverable()){
            out.append(corners[2].getCornerResource().toChar());
            out.append('┃');
        } else {
            out.append("  ");
        }

        out.append("         ");

        if(corners[3].isCoverable()){
            out.append('┃');
            out.append(corners[3].getCornerResource().toChar());
        } else {
            out.append("  ");
        }

        out.append("┃\n");

        //fifth row
        if(corners[2].isCoverable() && corners[3].isCoverable()){
            out.append("┗━┻━━━━━━━━━┻━┛");
        } else if (corners[2].isCoverable() && !corners[3].isCoverable()){
            out.append("┗━┻━━━━━━━━━━━┛");
        } else if (!corners[2].isCoverable() && corners[3].isCoverable()){
            out.append("┗━━━━━━━━━━━┻━┛");
        } else {
            out.append("┗━━━━━━━━━━━━━┛");
        }

        return out.toString();
    }


    public static void main(String[] args) {
        Card ncard = Game.getCardByID(15);
        ncard.setFrontSideUp(true);
        PrintCards print = new PrintCards((ColoredCard) ncard);
        System.out.print(print.toString());
    }

}


