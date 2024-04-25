package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.player.Player;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * Main Controller class.
 * It gets messages from the GameThread and calls and changes the model.
 * @author Alessio Guarisco
 * */
public class Controller {
    private final GameThread thread;
    private final BlockingQueue<ThreadMessage> messageQueue;
    private Game game;

    public Controller(GameThread thread, BlockingQueue<ThreadMessage> messageQ){
        this.thread = thread;
        this.messageQueue = messageQ;
    }

    //TODO: differentiate error responses
    public void createGame(String username, Integer playerNum, UUID gameId, UUID messageId){
        game = new Game(gameId);
        messageQueue.add(ThreadMessage.okResponse(username, messageId));
    }

    public void join(String username, UUID messageId){
        try{
            game.addPlayer(username);
        }catch (Exception e){
            messageQueue.add(ThreadMessage.genericError(username, messageId));
        }
    }

    public void draw(String username, Integer index, UUID messageId){
        try{
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            ColoredCard card;
            if(index == 4) {
                card = user.drawDecks(false);
            } else if(index == 5) {
                card = user.drawDecks(true);
            } else {
                card = user.drawVisible(index);
            }
            Integer cardId = card.getId();
            messageQueue.add(ThreadMessage.drawResponse(username, cardId, messageId));
        } catch (Exception e){
            messageQueue.add(ThreadMessage.genericError(username, messageId));
        }
    }

    public void placeCard(String username, Coordinates coordinates, Integer cardId, UUID messageId){
        try{
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            user.getPlayerBoard().placeCard(Game.getCardByID(cardId), coordinates);
        }catch (Exception e){
            messageQueue.add(ThreadMessage.genericError(username, messageId));
        }
        messageQueue.add(ThreadMessage.okResponse(username, messageId));
    }

}
