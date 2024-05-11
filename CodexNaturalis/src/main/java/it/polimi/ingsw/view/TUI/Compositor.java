package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.client.PlayerData;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientInterface;
import it.polimi.ingsw.view.TUI.components.*;
import it.polimi.ingsw.view.TUI.components.printables.ObjectiveCard;
import it.polimi.ingsw.view.TUI.controller.View;

import java.util.HashMap;
import java.util.Map;

public class Compositor {
    private static final int screenWidth = 166;
    private static final int screenHeight = 39;

    private final ClientInterface client;
    private final MiniBoard[] miniBoard;
    private final Chat chat = new Chat();
    private final ResourceView resources;
    private final HandView hand;
    private final ScoreBoard scoreBoard;
    private final Prompt prompt;
    private Map<View, Component> mainComponent = new HashMap<>();
    private View view = View.BOARD;

    @Override
    public String toString() {
        System.out.println(view);
        return updateView();
    }

    public Compositor(ClientInterface client) {
        this.client = client;
        miniBoard = new MiniBoard[client.getPlayerNum()-1];
        for(int i = 0, y = 0; i < miniBoard.length; i++, y++){
            if(client.getPlayers().get(y).equals(client.getUsername())){
                i--;
                continue;
            }
            miniBoard[i] = new MiniBoard(client.getPlayers().get(y), client);
        }

        this.resources = new ResourceView(client.getUsername());
        this.hand = new HandView(client);
        this.scoreBoard = new ScoreBoard(client);
        this.prompt = new Prompt(client.getUsername());

        mainComponent.put(View.BOARD,  new Board(client));
        mainComponent.put(View.OBJECTIVES, createObjectiveView(client));
        mainComponent.put(View.DECK, new DeckView(client));
    }

    public String updateView(){
        StringBuilder out = new StringBuilder();

        for (MiniBoard miniBoard : miniBoard) {
            Map<Coordinates, Integer> convertedBoard = convertBoard(((Client)client).getOpponentData().get(miniBoard.getUsername()).getBoard());
            miniBoard.setBoard(convertedBoard);
        }
        resources.setResourceCount(((Client)client).getPlayerData().getResources());

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


        switch(client.getPlayerNum()-1){
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
                    .append(mainComponent.get(view).toStringArray()[y])
                    .append("\n");
        }

        for(int of_y = 0; of_y < ScoreBoard.scoreHeight; of_y++) {
            out.append(scoreBoard.toStringArray()[of_y])
                    .append("┃")
                    .append(mainComponent.get(view).toStringArray()[y + of_y])
                    .append("\n");
        }

        //TODO: finish when board is completed

        out.append(prompt.toString());
        return out.toString();
    }

    private ObjectiveView createObjectiveView(ClientInterface client){
        PlayerData clientData = client.getPlayerData();
        ObjectiveCard personal = new ObjectiveCard(clientData.getPersonalObjective());
        ObjectiveCard[] global = new ObjectiveCard[GameConsts.globalObjectives];
        for (int i = 0; i < GameConsts.globalObjectives; i++) {
            global[i] = new ObjectiveCard(clientData.getGlobalObjectives().get(i));
        }
        return new ObjectiveView(personal, global);
    }

    private Map<Coordinates, Integer> convertBoard(Map<Coordinates, Card> board){
        Map<Coordinates, Integer> convertedBoard = new HashMap<>();
        for (Map.Entry<Coordinates, Card> entry : board.entrySet()) {
            convertedBoard.put(entry.getKey(), entry.getValue().getId());
        }
        return convertedBoard;
    }

    public Board getBoard() {
        return (Board) mainComponent.get(View.BOARD);
    }

    public void switchView(View view) {
        this.view = view;
    }
}
