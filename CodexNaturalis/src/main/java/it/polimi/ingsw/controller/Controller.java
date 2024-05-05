package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.threads.GameThread;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.helpers.exceptions.model.ExistingUsernameException;
import it.polimi.ingsw.helpers.exceptions.model.TooManyPlayersException;
import it.polimi.ingsw.helpers.exceptions.model.UnrecognisedCardException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.awt.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Controller class.
 * It gets messages from the GameThread and calls and changes the model.
 * @author Alessio Guarisco
 * */
public class Controller {
    private final GameThread thread;
    private final BlockingQueue<ThreadMessage> messageQueue;
    private Game game;

    /**
     * Constructor for the Controller class.
     * @param thread the GameThread when the Game is instantiated.
     * @param messageQ the BlockingQueue that will contain the messages.
     * @param maxPlayers the maximum number of players that can join a game.
     * */
    public Controller(GameThread thread, BlockingQueue<ThreadMessage> messageQ, Integer maxPlayers) {
        this.thread = thread;
        this.messageQueue = messageQ;
        this.game = new Game(maxPlayers);
    }

    /**
     * Controller Method to create a new Game.
     * @param username the username of the player that creates the game.
     * @param playerNum the number of players that will join the game.
     * @param gameId the unique identifier of the game.
     * @param messageId the unique identifier of the message.
     * */
    public void createGame(String username, Integer playerNum, UUID gameId, UUID messageId) {
        game = new Game(playerNum);
        messageQueue.add(ThreadMessage.okResponse(username, messageId));
    }

    /**
     * Controller Method to join a Game.
     * @param username the username of the player that joins the game.
     * @param messageId the unique identifier of the message.
     * */
    public void join(String username, UUID messageId) {
        try {
            game.addPlayer(username);
            messageQueue.add(ThreadMessage.okResponse(username, messageId));
        } catch (TooManyPlayersException e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, "Too Many Players"));
        } catch (ExistingUsernameException e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, "Username already exists"));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to place a card.
     * @param username the username of the player that places the card.
     * @param coordinates the coordinates where to place the card.
     * @param cardId the unique identifier of the card.
     * @param messageId the unique identifier of the message.
     * */
    public void placeCard(String username, Coordinates coordinates, Integer cardId, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            user.getPlayerBoard().placeCard(Game.getCardByID(cardId), coordinates);
        } catch (IndexOutOfBoundsException e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, "Invalid card placing index"));
        } catch (UnrecognisedCardException e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, "Unrecognised card"));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
        messageQueue.add(ThreadMessage.okResponse(username, messageId));
    }

    /**
     * Controller Method to draw a card.
     * @param username the username of the player that draws the card.
     * @param index the index of where to draw the card.
    4 for the standard deck, 5 for the gold deck, 0-3 for the visible cards.
     * @param messageId the unique identifier of the message.
     * */
    public void draw(String username, Integer index, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            ColoredCard card;
            if (index == 4) {
                card = user.drawDecks(false);
            } else if (index == 5) {
                card = user.drawDecks(true);
            } else {
                card = user.drawVisible(index);
            }
            Integer cardId = card.getId();
            messageQueue.add(ThreadMessage.drawResponse(username, cardId, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to choose a personal objective.
     * @param username the username of the player that chooses the objective.
     * @param objId the unique identifier of the chosen objective.
     * @param messageId the unique identifier of the message.
     * */
    public void choosePersonalObjective(String username, Integer objId, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            user.choosePersonalObjective(objId);
            messageQueue.add(ThreadMessage.okResponse(username, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the score map.
     * @param username the username of the player that requests the score map.
     * @param messageId the unique identifier of the message.
     * */
    public void getScoreMap(String username, UUID messageId) {
        try {
            ConcurrentHashMap<Player, Integer> scoreBoard = game.getGameBoard().getScore();
            ConcurrentHashMap<String, Integer> scoreBoardString = new ConcurrentHashMap<>();
            for (Player player : scoreBoard.keySet()) {
                scoreBoardString.put(player.getUsername(), scoreBoard.get(player));
            }
            messageQueue.add(ThreadMessage.getScoreMapResponse(username, scoreBoardString, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the hand of the player.
     * @param username the username of the player that requests the hand.
     * @param messageId the unique identifier of the message.
     * */
    public void getHand(String username, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));

            ColoredCard[] hand = user.getHand();
            ArrayList<Integer> handIds = new ArrayList<>();

            for (ColoredCard card : hand) {
                handIds.add(card.getId());
            }

            messageQueue.add(ThreadMessage.getHandResponse(username, handIds, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the common objectives.
     * @param username the username of the player that requests the common objectives.
     * @param messageId the unique identifier of the message.
     * */
    public void getCommonObjectives(String username, UUID messageId) {
        try {
            Objective[] objectives = game.getGameBoard().getGlobalObjectives();
            ArrayList<Integer> objectiveIds = new ArrayList<>();

            for (Objective objective : objectives) {
                objectiveIds.add(objective.getId());
            }

            messageQueue.add(ThreadMessage.getCommonObjectivesResponse(username, objectiveIds, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the starting objectives to chose from.
     * @param username the username of the player that gets the objectives.
     * @param messageId the unique identifier of the message.
     * */
    public void getStartingObjectives(String username, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            user.setStartingObjectives();
            ArrayList<Objective> objectives = user.getStartingObjectives();

            ArrayList<Integer> objectiveIds = new ArrayList<>();
            for (Objective objective : objectives) {
                objectiveIds.add(objective.getId());
            }

            messageQueue.add(ThreadMessage.getStartingObjectivesResponse(username, objectiveIds, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the player resources.
     * @param username the username of the player that requests the resources.
     * @param usernameRequiredData the username of the player whose resources to get.
     * @param messageId the unique identifier of the message.
     * */
    public void getPlayerResources(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData));
            messageQueue.add(ThreadMessage.getPlayerResourcesResponse(username, user.getResources(), messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the visible cards.
     * @param username the username of the player that requests the visible cards.
     * @param messageId the unique identifier of the message.
     * */
    public void getVisibleCards(String username, UUID messageId) {
        try {
            ColoredCard[] visibleCards = (ColoredCard[]) game.getGameBoard().getVisibleCards();
            ArrayList<Integer> cardIds = new ArrayList<>();

            for (ColoredCard card : visibleCards) {
                cardIds.add(card.getId());
            }

            messageQueue.add(ThreadMessage.getVisibleCardsResponse(username, cardIds, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    //TODO: return an Arraylist of String of Resources, not cardIds
    /**
     * Controller Method to get the back side resource of decks.
     * @param username the username of the player that requests the back resource of decks.
     * @param messageId the unique identifier of the message.
     * */
    public void getBackSideDecks(String username, UUID messageId) {
        try {
            ArrayList<Integer> cardIds = new ArrayList<>();
            cardIds.add(game.getGameBoard().getGoldDeck().peekFirstCard().getId());
            cardIds.add(game.getGameBoard().getStdDeck().peekFirstCard().getId());
            messageQueue.add(ThreadMessage.getBackSideDecksResponse(username, cardIds, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the valid placement coordinates of cards in the board.
     * @param username the username of the player that requests the coordinates.
     * @param messageId the unique identifier of the message.
     * */
    public void getValidPlacements(String username, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            Set<Coordinates> validPlacements = user.getPlayerBoard().getValidPlacements();
            messageQueue.add(ThreadMessage.getValidPlacementsResponse(username, validPlacements, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the map board of a player.
     * @param username the username of the player that requests the board.
     * @param usernameRequiredData the username of the player whose board to get.
     * @param messageId the unique identifier of the message.
     * */
    public void getBoard(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData));
            HashMap<Coordinates, Card> board = user.getPlayerBoard().getBoard();
            HashMap<Coordinates, Integer> boardIds = new HashMap<>();
            for (Coordinates coordinate : board.keySet()) {
                boardIds.put(coordinate, board.get(coordinate).getId());
            }
            messageQueue.add(ThreadMessage.getBoardResponse(username, boardIds, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the resources of cards in the hand of a Player.
     * @param username the username of the player that requests the resources.
     * @param usernameRequiredData the username of the player whose hand to get.
     * @param messageId the unique identifier of the message.
     * */
    public void getHandColor(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData));
            ArrayList<Resource> handColors = new ArrayList<>();
            for (ColoredCard c : user.getHand()) {
                handColors.add(c.getBackResource());
            }
            messageQueue.add(ThreadMessage.getHandColorResponse(username, handColors, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

    /**
     * Controller Method to get the Deque of the unique identifiers of the last placed Cards of the player.
     * @param username the username of the player that requests the last placed cards.
     * @param messageId the unique identifier of the message.
     * */
    public void getLastPlacedCards(String username, UUID messageId){
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username));
            Deque<Integer> cardIds = new ArrayDeque<>();
            for (Card card : user.getPlayerBoard().getLastPlacedCards()) {
                cardIds.add(card.getId());
            }
            messageQueue.add(ThreadMessage.getLastPlacedCardsResponse(username, cardIds, messageId));
        } catch (Exception e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, e.getMessage()));
        }
    }

}
