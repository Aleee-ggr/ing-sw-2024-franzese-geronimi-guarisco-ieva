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

/**
 * The Compositor class is responsible for composing and managing various components of the TUI
 * It handles the display of multiple views, including the board, objectives, deck, and player resources.
 */
public class Compositor {
    public static final int screenWidth = 166;
    public static final int screenHeight = 39;

    private final ClientInterface client;
    private final MiniBoard[] miniBoard;
    private final Chat chat;
    private final ResourceView resources;
    private final HandView hand;
    private final ScoreBoard scoreBoard;
    private final Prompt prompt;
    private final TopBar topBar = new TopBar();
    private final Map<View, Component> mainComponent = new HashMap<>();
    private View view = View.BOARD;
    private String viewPlayer;

    /**
     * Constructs a new Compositor instance, initializing all components based on the client interface.
     *
     * @param client The client interface used to interact with the server and obtain game data.
     */
    public Compositor(ClientInterface client) {
        this.client = client;
        this.chat = new Chat(client);
        this.viewPlayer = client.getUsername();
        miniBoard = new MiniBoard[client.getPlayerNum() - 1];
        for (int i = 0, y = 0; i < miniBoard.length; i++, y++) {
            if (client.getPlayers().get(y).equals(client.getUsername())) {
                i--;
                continue;
            }
            miniBoard[i] = new MiniBoard(client.getPlayers().get(y), client);
        }

        this.resources = new ResourceView(client.getUsername());

        this.hand = new HandView(client);
        this.scoreBoard = new ScoreBoard(client);
        this.prompt = new Prompt(client.getUsername(), client.getPlayers().getFirst());

        mainComponent.put(View.BOARD, new Board(client));
        mainComponent.put(View.OBJECTIVES, createObjectiveView(client));
        mainComponent.put(View.DECK, new DeckView(client));
    }

    @Override
    public String toString() {
        return updateView();
    }

    /**
     * Sets the player whose view is currently being displayed.
     *
     * @param player The username of the player to set as the current view player.
     */
    public void setViewPlayer(String player) {
        this.viewPlayer = player;
        resources.setUsername(player);
        hand.setCurrentPlayer(player);
        Board board = (Board) mainComponent.get(View.BOARD);
        board.setCurrentPlayer(player);

    }

    /**
     * Updates the current view by composing the various components into a single string representation.
     *
     * @return A string representing the current view with all components.
     */
    public String updateView() {
        StringBuilder out = new StringBuilder();
        out.append(topBar).append("\n");
        resources.setResourceCount(client.getOpponentData().get(viewPlayer).getResources());

        for (MiniBoard miniBoard : miniBoard) {
            Map<Coordinates, Integer> convertedBoard = convertBoard(((Client) client).getOpponentData().get(miniBoard.getUsername()).getBoard());
            miniBoard.setBoard(convertedBoard);
        }

        for (int y = 0; y < MiniBoard.boardHeight; y++) {
            out.append(resources.toStringArrayColor()[y]).append('┃');
            for (MiniBoard miniBoard : miniBoard) {
                out.append(miniBoard.toStringArrayColor()[y]).append('┃');
            }
            out.append(chat.toStringArray()[y]).append('┃');
            out.append('\n');
        }

        out.append("━".repeat(ResourceView.width)).append('╋');

        switch (client.getPlayerNum() - 1) {
            case 1:
                out.append("━".repeat((MiniBoard.boardWidth)));
                break;
            case 2:
                out.append("━".repeat((MiniBoard.boardWidth))).append('┻').append("━".repeat((MiniBoard.boardWidth)));
                break;
            case 3:
                out.append("━".repeat((MiniBoard.boardWidth))).append('┻').append("━".repeat((MiniBoard.boardWidth))).append('┻').append("━".repeat((MiniBoard.boardWidth)));
                break;
            default:
                throw new RuntimeException("Invalid player number.");
        }
        out.append('┻').append("━".repeat(chat.chatWidth)).append("┛");
        out.append('\n');


        int y;
        for (y = 0; y < HandView.panelHeight; y++) {
            out.append(hand.toStringArrayColor()[y]).append("┃").append(mainComponent.get(view).toStringArrayColor()[y]).append("\n");
        }

        for (int of_y = 0; of_y < ScoreBoard.scoreHeight; of_y++) {
            out.append(scoreBoard.toStringArray()[of_y]).append("┃").append(mainComponent.get(view).toStringArrayColor()[y + of_y]).append("\n");
        }

        out.append(prompt.toString());
        return out.toString();
    }

    /**
     * Sets the message to be displayed in the top bar.
     *
     * @param message The message to set in the top bar.
     */
    public void setTopBar(String message) {
        topBar.setMessage(message);
    }

    /**
     * Creates an ObjectiveView based on the client's data, including personal and global objectives.
     *
     * @param client The client interface used to obtain player data.
     * @return The created ObjectiveView instance.
     */
    private ObjectiveView createObjectiveView(ClientInterface client) {
        PlayerData clientData = client.getPlayerData();
        ObjectiveCard personal = new ObjectiveCard(clientData.getPersonalObjective());
        ObjectiveCard[] global = new ObjectiveCard[GameConsts.globalObjectives];
        for (int i = 0; i < GameConsts.globalObjectives; i++) {
            global[i] = new ObjectiveCard(clientData.getGlobalObjectives().get(i));
        }
        return new ObjectiveView(personal, global);
    }

    /**
     * Converts a board represented by a map of Coordinates to Card into a map of Coordinates to Integer.
     *
     * @param board The original board represented as a map of Coordinates and Card.
     * @return The converted board represented as a map of Coordinates and Integer.
     */
    private Map<Coordinates, Integer> convertBoard(Map<Coordinates, Card> board) {
        Map<Coordinates, Integer> convertedBoard = new HashMap<>();
        for (Map.Entry<Coordinates, Card> entry : board.entrySet()) {
            convertedBoard.put(entry.getKey(), entry.getValue().getId());
        }
        return convertedBoard;
    }

    /**
     * Gets the main Board component.
     *
     * @return The main Board component.
     */
    public Board getBoard() {
        return (Board) mainComponent.get(View.BOARD);
    }

    /**
     * Switches the current view to the specified view.
     *
     * @param view The view to switch to.
     */
    public void switchView(View view) {
        this.view = view;
    }
}
