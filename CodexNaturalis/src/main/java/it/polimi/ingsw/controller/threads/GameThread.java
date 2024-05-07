package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * GameThread Class, used to manage the thread-side logic for the controller.
 * Instantiate a new thread for each game to work simultaneously with the same server.
 * @author Daniele Ieva
 * @author ALessio Guarisco
 * */
public class GameThread extends Thread {
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final Integer maxPlayers;
    private final Controller controller;
    private String currentPlayer;
    private GameState gameState = GameState.LOBBY;
    private final Map<String, Boolean> turnMap;

    public GameThread(BlockingQueue<ThreadMessage> messageQueue, Map<String, Boolean> turnMap, Integer maxPlayers) {
        this.messageQueue = messageQueue;
        this.maxPlayers = maxPlayers;
        this.controller = new Controller(this, messageQueue, maxPlayers);
        this.turnMap = turnMap;
    }



    @Override
    public void run() {
        gameLoop();
    }

    public void gameLoop(){
        ThreadMessage msg;
        while(gameState != GameState.STOP){
            switch (gameState){
                case LOBBY:
                    gameLobby();
                    break;
                case SETUP:
                    setup();
                    break;
                case MAIN:
                    mainGame();
                    break;
                case ENDGAME:
                    endGame();
                    break;
            }
        }
        while (controller.getGame().getPlayers().size() == controller.getGame().getMaxPlayers()){
            for(Player player : controller.getGame().getPlayers()){
                playerTurn(player.getUsername());
            }
        }
    }

    public void gameLobby(){ //to move in main loop in ClientApp
        ThreadMessage msg = getMessage();
        if(GameState.lobby.contains(msg.type())){
            respond(msg);
        } else {
            messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "Invalid message for context."));
        }
        if (controller.getGame().getPlayers().size() == maxPlayers){
            gameState = GameState.SETUP;
            controller.getGame().setGameState(GameState.SETUP);
        }
    }

    public void setup() {
        for(String currentPlayer : controller.getGame().getPlayers().stream().map(Player::getUsername).toList()){
            boolean objChosen = false;
            boolean startChosen = false;
            this.currentPlayer = currentPlayer;

            turnMap.put(currentPlayer, true);

            while(!objChosen || !startChosen){
                ThreadMessage msg = getMessage();
                if(GameState.setup.contains(msg.type()) && msg.player().equals(currentPlayer)){
                    respond(msg);
                } else if(!msg.player().equals(currentPlayer)){
                    messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "not player's turn."));
                } else {
                    messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "Invalid message for context."));
                }

                if(msg.type().equals("choosePersonalObjective")){ //TODO: needs to check responses!
                    objChosen = true;
                } else if(msg.type().equals("chooseStartingCard")){
                    startChosen = true;
                }
            }
            turnMap.put(currentPlayer, false);
        }
        gameState = GameState.MAIN;
        controller.getGame().setGameState(GameState.MAIN);
    }

    public void mainGame(){
        for(Player player : controller.getGame().getPlayers()){
            this.currentPlayer = player.getUsername();
            turnMap.put(player.getUsername(), true);
            if(playerTurn(player.getUsername()) || controller.getGame().getGameBoard().areCardsOver()){
                gameState = GameState.ENDGAME;
                controller.getGame().setGameState(GameState.ENDGAME);
            }
            turnMap.put(player.getUsername(), false);
        }
    }

    public boolean playerTurn(String playerName){ //TODO: check is player can't place a card
        boolean draw = false;
        boolean place = false;
        while (!place){
            ThreadMessage msg = getMessage();
            if(GameState.getters.contains(msg.type()) || msg.type().equals("place") && msg.player().equals(currentPlayer)){
                respond(msg);
            } else if(!msg.player().equals(currentPlayer)){
                messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "not player's turn."));
            } else {
                messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "Invalid message for context."));
            }
            if(msg.type().equals("place")){
                place = true;
            }
        }

        while (!draw && !controller.getGame().getGameBoard().areCardsOver()){
            ThreadMessage msg = getMessage();
            if(msg.type().equals("draw") && msg.player().equals(currentPlayer)){
                respond(msg);
            } else if(!msg.player().equals(currentPlayer)){
                messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "not player's turn."));
            } else {
                messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "Invalid message for context."));
            }
            if(msg.type().equals("draw")){
                draw = true;
            }
        }

        Player player = controller.getGame().getPlayers().stream().filter(p -> p.getUsername().equals(playerName)).toList().getFirst();
        return (player.getScore()>= GameConsts.endingScore);
    }

    public void endGame() {
        for(String currentPlayer : controller.getGame().getPlayers().stream().map(Player::getUsername).toList()){
            this.currentPlayer = currentPlayer;
            turnMap.put(currentPlayer, true);
            playerTurn(currentPlayer);
            turnMap.put(currentPlayer, false);
        }

        for(Player player : controller.getGame().getPlayers()) {
            for(Objective obj : controller.getGame().getGameBoard().getGlobalObjectives()){
                controller.getGame().getGameBoard().updateScore(player, obj.getPoints(player));
            }
            controller.getGame().getGameBoard().updateScore(player, player.getHiddenObjective().getPoints(player));
            turnMap.put(player.getUsername(), true);
        }

        for(int i = 0; i < controller.getGame().getNumPlayers(); i++) {
            ThreadMessage msg = getMessage();
            if (msg.type().equals("getScoreMap")) {
                respond(msg);
            } else {
                i --;
                messageQueue.add(ThreadMessage.genericError(msg.player(), msg.messageUUID(), "Invalid message for context."));
            }
        }
        gameState = GameState.STOP;
        controller.getGame().setGameState(GameState.STOP);
    }

    private ThreadMessage getMessage() {
        ThreadMessage msg;
        do {
            msg = messageQueue.peek();
        } while (msg == null || msg.status() != Status.REQUEST);

        return messageQueue.remove();
    }

    private ThreadMessage respond(ThreadMessage msg) {
        switch (msg.type()) {
            case "create":
                controller.createGame(
                        msg.player(),
                        Integer.valueOf(msg.args()[0]),
                        UUID.fromString(msg.args()[1]),
                        msg.messageUUID()
                );
                break;
            case "join":
                controller.join(msg.player(),
                        msg.messageUUID()
                );
                break;
            case "place":
                controller.placeCard(
                        msg.player(),
                        new Coordinates(Integer.valueOf(msg.args()[0]),Integer.valueOf(msg.args()[1])),
                        Integer.valueOf(msg.args()[2]),
                        msg.messageUUID()
                );
                break;
            case "update":
                controller.update(
                        msg.player(),
                        currentPlayer.equals(msg.player()),
                        msg.messageUUID()
                );
                break;
            case "draw":
                controller.draw(
                        msg.player(),
                        Arrays.stream(msg.args()).mapToInt(Integer::parseInt).findFirst().orElse(-1),
                        msg.messageUUID()
                );
                break;
            case "choosePersonalObjective":
                controller.choosePersonalObjective(
                        msg.player(),
                        Integer.valueOf(msg.args()[0]),
                        msg.messageUUID()
                );
                break;
            case "getScoreMap":
                controller.getScoreMap(
                        msg.player(),
                        msg.messageUUID()
                );
                break;
            case "getHand":
                controller.getHand(
                        msg.player(),
                        msg.messageUUID());
                break;
            case "getCommonObjectives":
                controller.getCommonObjectives(
                        msg.player(),
                        msg.messageUUID());
                break;
            case "getStartingObjectives":
                controller.getStartingObjectives(
                        msg.player(),
                        msg.messageUUID());
                break;
            case "getPlayerResources":
                controller.getPlayerResources(
                        msg.player(),
                        msg.args()[0],
                        msg.messageUUID());
                break;
            case "getVisibleCards":
                controller.getVisibleCards(
                        msg.player(),
                        msg.messageUUID());
                break;
            case "getBackSideDecks":
                controller.getBackSideDecks(
                        msg.player(),
                        msg.messageUUID());
                break;
            case "getValidPlacements":
                controller.getValidPlacements(
                        msg.player(),
                        msg.messageUUID());
                break;
            case "getBoard":
                controller.getBoard(
                        msg.player(),
                        msg.args()[0],
                        msg.messageUUID());
                break;
            case "getHandColor":
                controller.getHandColor(
                        msg.player(),
                        msg.args()[0],
                        msg.messageUUID());
                break;
            case "getLastPlacedCards":
                controller.getLastPlacedCards(
                        msg.player(),
                        msg.messageUUID());
                break;
            case "getStartingCard":
                controller.getStartingCards(
                        msg.player(),
                        msg.messageUUID()
                );
                break;
            case "getPlayers":
                controller.getPlayers(
                        msg.player(),
                        msg.messageUUID()
                );
                break;
            case "getGameState":
                controller.getGameState(
                        msg.player(),
                        msg.messageUUID()
                );
                break;
            case "kill":
                gameState = GameState.STOP;
                break;
            default:
                messageQueue.add(
                    new ThreadMessage(Status.ERROR, msg.player(), "unknown", null, msg.messageUUID())
                );
        }
        return msg;
    }
}
