package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.helpers.exceptions.model.ExistingUsernameException;
import it.polimi.ingsw.helpers.exceptions.model.TooManyPlayersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
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
        this.game = new Game();
    }

    //TODO: differentiate error responses
    public void createGame(String username, Integer playerNum, UUID gameId, UUID messageId){
        game = new Game();
        messageQueue.add(ThreadMessage.okResponse(username, messageId));
    }

    public void join(String username, UUID messageId){
        try{
            game.addPlayer(username);
            messageQueue.add(ThreadMessage.okResponse(username, messageId));
        }catch (TooManyPlayersException e){
            messageQueue.add(ThreadMessage.genericError(username, messageId, "Too Many Players"));
        } catch (ExistingUsernameException e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, "Username already exists"));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
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
 //LAB31
    public void choosePersonalObjective(String username, Integer objId, UUID messageId){
        try{
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            user.choosePersonalObjective(objId);
            messageQueue.add(ThreadMessage.okResponse(username, messageId));
        } catch(Exception e){
            messageQueue.add(ThreadMessage.genericError(username, messageId));
        }
    }

    public void getStartingObjectives(String username, UUID messageId){
        try{
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            user.setStartingObjectives();
            ArrayList<Objective> objectives = user.getStartingObjectives();

            ArrayList<Integer> objectiveIds = new ArrayList<>();
            for(Objective objective : objectives) {
                objectiveIds.add(objective.getId());
            }
            
            messageQueue.add(ThreadMessage.getStartingObjectivesResponse(username, objectiveIds, messageId));
        } catch(Exception e){
            messageQueue.add(ThreadMessage.genericError(username, messageId));
        }
    }

}
