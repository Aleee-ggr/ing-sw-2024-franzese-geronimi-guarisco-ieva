package it.polimi.ingsw.view.TUI.components.printables;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.TUI.components.Component;

public class PrintCards implements Component {
    private final String cardString;
    public final static int width = 15;
    public final static int height = 5;

    public PrintCards(Card card) {
        cardString = setCard(card);
    }


    @Override
    public String toString()  {
        return cardString;
    }

    private static String setCard(Card inCard) {
        Corner[] corners;
        StringBuilder out = new StringBuilder();
        
        if(inCard instanceof ColoredCard colored){

            if(colored.isFrontSideUp()){
                corners = colored.getFrontCorners();
            } else {
                corners = colored.getBackCorners();
            }
            
        } else if(inCard instanceof StartingCard starting){

            if(starting.isFrontSideUp()){
                corners = starting.getFrontCorners();
            } else {
                corners = starting.getBackCorners();
            }
            
        } else {
            throw new RuntimeException("Card type not supported");
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


        if(inCard instanceof GoldCard){
            GoldCard card = (GoldCard)inCard;
            out.append("   ┃")
                    .append(card.getBackResource().toChar())
                    .append("┃   ");
        } else if(inCard instanceof StdCard){
            StdCard card = (StdCard)inCard;
            out.append("    ")
                    .append(card.getBackResource().toChar())
                    .append("    ");
        } else if(inCard instanceof StartingCard){
            if(!((StartingCard)inCard).isFrontSideUp()){
                out.append("    ")
                        .append("❖")
                        .append("    ");
            } else {
                int resourceNum = ((StartingCard)inCard).getFrontResources().size();
                switch(resourceNum) {
                    case 1:
                        out.append("    ")
                                .append(((StartingCard) inCard).getFrontResources().getFirst().toChar())
                                .append("    ");
                        break;
                    case 2:
                        out.append("   ")
                                .append(((StartingCard) inCard).getFrontResources().getFirst().toChar())
                                .append(" ")
                                .append(((StartingCard) inCard).getFrontResources().getLast().toChar())
                                .append("   ");
                        break;
                    case 3:
                        out.append("  ")
                                .append(((StartingCard) inCard).getFrontResources().getFirst().toChar())
                                .append(" ")
                                .append(((StartingCard) inCard).getFrontResources().get(1).toChar())
                                .append(" ")
                                .append(((StartingCard) inCard).getFrontResources().getLast().toChar())
                                .append("  ");
                        break;
                    default:
                        throw new RuntimeException("Invalid number of resources");
                }
            }
        } else {
            throw new RuntimeException("Card type not supported");
        }


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
        Card ncard = Game.getCardByID(86);
        ncard.setFrontSideUp(true);
        PrintCards print = new PrintCards(ncard);
        System.out.print(print);
    }

}


