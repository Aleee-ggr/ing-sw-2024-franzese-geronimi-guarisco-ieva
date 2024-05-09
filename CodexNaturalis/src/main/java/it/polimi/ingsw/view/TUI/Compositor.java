package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.TUI.components.*;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;

public class Compositor {
    private static final int screenWidth = 166;
    private static final int screenHeight = 39;
    private static final ClientData clientData = Client.getData();

    private final MiniBoard[] miniBoard = new MiniBoard[clientData.getPlayerNum()-1];
    private final Chat chat = new Chat();
    private ResourceView resources = new ResourceView(clientData.getUsername());
    private HandView hand = new HandView();
    //private DeckView deck = new DeckView();
    private final Board board = new Board();
    private ScoreBoard scoreBoard = new ScoreBoard();
    private final Prompt prompt = new Prompt(clientData.getUsername());

    private final ObjectiveView objectiveView = createObjectiveView();

    @Override
    public String toString() {
        return updateView();
    }

    public Compositor(String[] players) {
        for(int i = 0; i < miniBoard.length; i++){
            miniBoard[i] = new MiniBoard(players[i]);
        }
    }

    public String updateView(){
        StringBuilder out = new StringBuilder();

        for (MiniBoard miniBoard : miniBoard) {
            miniBoard.setBoard(clientData.getPlayerBoard(miniBoard.getUsername()));
        }

        for(int y = 0; y < MiniBoard.boardHeight; y++){
            for (MiniBoard miniBoard : miniBoard) {
                out.append(miniBoard.toStringArray()[y])
                        .append('┃');
            }
            out.append(chat.toStringArray()[y])
                    .append('┃');
            out.append(resources.toStringArray()[y]);
            out.append('\n');
        }


        switch(clientData.getPlayerNum()-1){
            case 1:
                out.append("━".repeat(MiniBoard.boardWidth));
                break;
            case 2:
                out.append("━".repeat((MiniBoard.boardWidth-1)/2))
                        .append('┻')
                        .append("━".repeat((MiniBoard.boardWidth-1)/2));
                break;
            case 3:
                out.append("━".repeat((MiniBoard.boardWidth-2)/3))
                        .append('┻')
                        .append("━".repeat((MiniBoard.boardWidth-2)/3))
                        .append('╋')
                        .append("━".repeat((MiniBoard.boardWidth-2)/3));
                break;
            default:
                throw new RuntimeException("Invalid player number.");
        }
        out.append('┻')
                .append("━".repeat(Chat.chatWidth))
                .append('┻');
        out.append("━".repeat(ResourceView.width));
        out.append('\n');


        int y;
        for(y = 0; y < HandView.panelHeight; y++){
            out.append(hand.toStringArray()[y])
                    .append("┃")
                    .append(board.toStringArray()[y])
                    .append("\n");
        }


        for(; y < HandView.panelHeight + ScoreBoard.scoreHeight; y++) {
            out.append(scoreBoard.toStringArray()[y - HandView.panelHeight])
                    .append("┃")
                    .append(board.toStringArray()[y])
                    .append("\n");
        }

        //TODO: finish when board is completed


        return out.toString();
    }



    private static ObjectiveView createObjectiveView(){
        ObjectiveCard personal = new ObjectiveCard(Game.getObjectiveByID(clientData.getPersonalObjective()));
        ObjectiveCard[] global = new ObjectiveCard[GameConsts.globalObjectives];
        for (int i = 0; i < GameConsts.globalObjectives; i++) {
            global[i] = new ObjectiveCard(Game.getObjectiveByID(clientData.getGlobalObjectives()[i]));
        }
        return new ObjectiveView(personal, global);
    }

}
