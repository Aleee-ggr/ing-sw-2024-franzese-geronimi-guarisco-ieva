package it.polimi.ingsw.controller;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.helpers.exceptions.model.TooManyPlayersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ColoredCard;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Controller class.
 * It gets messages from the GameThread and calls and changes the model.
 */
public class Controller {
    private final BlockingQueue<ThreadMessage> messageQueue;
    private Game game;

    /**
     * Constructor for the Controller class.
     *
     * @param messageQ   the BlockingQueue that will contain the messages.
     * @param maxPlayers the maximum number of players that can join a game.
     */
    public Controller(BlockingQueue<ThreadMessage> messageQ, Integer maxPlayers) {
        this.messageQueue = messageQ;
        this.game = new Game(maxPlayers);
    }

    /**
     * Getter for the Game.
     *
     * @return the Game object.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Controller Method to create a new Game.
     *
     * @param username  the username of the player that creates the game.
     * @param playerNum the number of players that will join the game.
     * @param gameId    the unique identifier of the game.
     * @param messageId the unique identifier of the message.
     */
    public void createGame(String username, Integer playerNum, UUID gameId, UUID messageId) {
        game = new Game(playerNum);
        messageQueue.add(ThreadMessage.okResponse(username, messageId));
    }

    /**
     * Controller Method to join a Game.
     *
     * @param username  the username of the player that joins the game.
     * @param messageId the unique identifier of the message.
     */
    public void join(String username, UUID messageId) {
        try {
            game.addPlayer(username);
            messageQueue.add(ThreadMessage.okResponse(username, messageId));
        } catch (TooManyPlayersException e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, "Too Many Players"));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to randomize the order of the players.
     * Used to randomize the turn order of the players in the game.
     */
    public void randomizePlayers() {
        game.randomizePlayers();
    }


    /**
     * Updates the message queue with a thread message representing an update response.
     *
     * @param username   the username associated with the update
     * @param playerTurn indicates if it's the player's turn
     * @param messageId  the unique identifier of the message
     */
    public void update(String username, boolean playerTurn, UUID messageId) {
        messageQueue.add(ThreadMessage.updateResponse(username, playerTurn, messageId));
    }

    /**
     * Controller Method to place a card.
     *
     * @param username    the username of the player that places the card.
     * @param coordinates the coordinates where to place the card.
     * @param cardId      the unique identifier of the card.
     * @param messageId   the unique identifier of the message.
     */
    public boolean placeCard(String username, Coordinates coordinates, Integer cardId, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];
            ColoredCard card = (ColoredCard) Game.getCardByID(cardId);
            if (cardId < 0) {
                card.setFrontSideUp(false);
            }

            user.playCard(card, coordinates);
            messageQueue.add(ThreadMessage.okResponse(username, messageId));
            return true;
        } catch (Exception e) {
            logError(username, messageId, e);
        }
        return false;
    }

    /**
     * Controller Method to draw a card.
     *
     * @param username  the username of the player that draws the card.
     * @param index     the index of where to draw the card.
     *                  4 for the standard deck, 5 for the gold deck, 0-3 for the visible cards.
     * @param messageId the unique identifier of the message.
     */
    public boolean draw(String username, Integer index, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];
            ColoredCard card;
            if (index == 4) {
                card = user.drawDecks(true);
            } else if (index == 5) {
                card = user.drawDecks(false);
            } else {
                card = user.drawVisible(index);
            }
            Integer cardId = card.getId();
            messageQueue.add(ThreadMessage.drawResponse(username, cardId, messageId));
            return true;
        } catch (NullPointerException e) {
            messageQueue.add(ThreadMessage.genericError(username, messageId, "No card to draw"));

        } catch (Exception e) {
            logError(username, messageId, e);
        }
        return false;
    }

    /**
     * Retrieves the usernames of all players in the game and sends a response message.
     *
     * @param username  the username initiating the request
     * @param messageId the unique identifier of the message
     */
    public void getPlayers(String username, UUID messageId) {
        try {
            ArrayList<String> players = new ArrayList<>();
            for (Player player : game.getPlayers()) {
                players.add(player.getUsername());
            }
            messageQueue.add(ThreadMessage.getPlayersResponse(username, players, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to choose a personal objective.
     *
     * @param username  the username of the player that chooses the objective.
     * @param objId     the unique identifier of the chosen objective.
     * @param messageId the unique identifier of the message.
     */
    public void choosePersonalObjective(String username, Integer objId, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];
            user.choosePersonalObjective(objId);
            messageQueue.add(ThreadMessage.choosePersonalObjectiveResponse(username, true, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Allows a player to choose a color and updates the game accordingly.
     *
     * @param username    the username of the player choosing the color
     * @param playerColor the chosen color by the player
     * @param messageId   the unique identifier of the message
     */
    public void choosePlayerColor(String username, Color playerColor, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];
            user.choosePlayerColor(playerColor);
            game.updateAvailableColors(playerColor);
            messageQueue.add(ThreadMessage.choosePlayerColorResponse(username, true, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the score map.
     *
     * @param username  the username of the player that requests the score map.
     * @param messageId the unique identifier of the message.
     */
    public void getScoreMap(String username, UUID messageId) {
        try {
            ConcurrentHashMap<Player, Integer> scoreBoard = game.getGameBoard().getScore();
            ConcurrentHashMap<String, Integer> scoreBoardString = new ConcurrentHashMap<>();
            for (Player player : scoreBoard.keySet()) {
                scoreBoardString.put(player.getUsername(), scoreBoard.get(player));
            }
            messageQueue.add(ThreadMessage.getScoreMapResponse(username, scoreBoardString, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the hand of the player.
     *
     * @param username  the username of the player that requests the hand.
     * @param messageId the unique identifier of the message.
     */
    public void getHand(String username, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];

            ColoredCard[] hand = Arrays.stream(user.getHand()).filter(Objects::nonNull).toArray(ColoredCard[]::new);
            ArrayList<Integer> handIds = new ArrayList<>();

            for (ColoredCard card : hand) {
                handIds.add(card.getId());
            }

            messageQueue.add(ThreadMessage.getHandResponse(username, handIds, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the common objectives.
     *
     * @param username  the username of the player that requests the common objectives.
     * @param messageId the unique identifier of the message.
     */
    public void getCommonObjectives(String username, UUID messageId) {
        try {
            Objective[] objectives = game.getGameBoard().getGlobalObjectives();
            ArrayList<Integer> objectiveIds = new ArrayList<>();

            for (Objective objective : objectives) {
                objectiveIds.add(objective.getId());
            }

            messageQueue.add(ThreadMessage.getCommonObjectivesResponse(username, objectiveIds, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Add an error threadMessage in the queue
     *
     * @param username  sender username
     * @param messageId request id
     * @param e         error
     */
    private void logError(String username, UUID messageId, Exception e) {
        messageQueue.add(ThreadMessage.genericError(username, messageId, Arrays.toString(e.getStackTrace())));
    }

    /**
     * Controller Method to get the personal objective.
     *
     * @param username  the username of the player that requests the common objectives.
     * @param messageId the unique identifier of the message.
     */
    public void getPersonalObjective(String username, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];

            messageQueue.add(ThreadMessage.getPersonalObjectiveResponse(username, user.getHiddenObjective().getId(), messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the starting objectives to chose from.
     *
     * @param username  the username of the player that gets the objectives.
     * @param messageId the unique identifier of the message.
     */
    public void getStartingObjectives(String username, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];
            user.setStartingObjectives();
            ArrayList<Objective> objectives = user.getStartingObjectives();

            ArrayList<Integer> objectiveIds = new ArrayList<>();
            for (Objective objective : objectives) {
                objectiveIds.add(objective.getId());
            }

            messageQueue.add(ThreadMessage.getStartingObjectivesResponse(username, objectiveIds, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Retrieves and sends the available colors in the game to the requesting player.
     *
     * @param username  the username of the player requesting the available colors
     * @param messageId the unique identifier of the message
     */
    public void getAvailableColors(String username, UUID messageId) {
        messageQueue.add(ThreadMessage.getAvailableColorsResponse(username, game.getAvailableColors(), messageId));
    }

    /**
     * Retrieves and sends the color of a specified player to the requesting player.
     *
     * @param username             the username of the player requesting the color
     * @param usernameRequiredData the username of the player whose color is being requested
     * @param messageId            the unique identifier of the message
     */
    public void getPlayerColor(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData)).toArray()[0];
            messageQueue.add(ThreadMessage.getPlayerColorResponse(username, user.getPlayerColor(), messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the player resources.
     *
     * @param username             the username of the player that requests the resources.
     * @param usernameRequiredData the username of the player whose resources to get.
     * @param messageId            the unique identifier of the message.
     */
    public void getPlayerResources(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData)).toArray()[0];
            messageQueue.add(ThreadMessage.getPlayerResourcesResponse(username, user.getResources(), messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the visible cards.
     *
     * @param username  the username of the player that requests the visible cards.
     * @param messageId the unique identifier of the message.
     */
    public void getVisibleCards(String username, UUID messageId) {
        try {
            ColoredCard[] visibleCards = (ColoredCard[]) game.getGameBoard().getVisibleCards();
            for (ColoredCard card : visibleCards) {
                if (card != null) {
                    card.getId();
                }
            }
            ArrayList<Integer> cardIds = new ArrayList<>();

            for (ColoredCard card : visibleCards) {
                if (card != null) {
                    cardIds.add(card.getId());
                } else {
                    cardIds.add(null);
                }
            }

            messageQueue.add(ThreadMessage.getVisibleCardsResponse(username, cardIds, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the back side resource of decks.
     *
     * @param username  the username of the player that requests the back resource of decks.
     * @param messageId the unique identifier of the message.
     */
    public void getBackSideDecks(String username, UUID messageId) {
        try {
            ArrayList<Integer> cardIds = new ArrayList<>();
            ColoredCard goldDeck = game.getGameBoard().getGoldDeck().peekFirstCard();
            ColoredCard stdCard = game.getGameBoard().getStdDeck().peekFirstCard();
            if (goldDeck != null) {
                cardIds.add(goldDeck.getId());
            }
            if (stdCard != null) {
                cardIds.add(stdCard.getId());
            }
            System.out.println(cardIds);
            messageQueue.add(ThreadMessage.getBackSideDecksResponse(username, cardIds, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the valid placement coordinates of cards in the board.
     *
     * @param username  the username of the player that requests the coordinates.
     * @param messageId the unique identifier of the message.
     */
    public void getValidPlacements(String username, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];
            Set<Coordinates> validPlacements = user.getPlayerBoard().getValidPlacements();
            messageQueue.add(ThreadMessage.getValidPlacementsResponse(username, validPlacements, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the map board of a player.
     *
     * @param username             the username of the player that requests the board.
     * @param usernameRequiredData the username of the player whose board to get.
     * @param messageId            the unique identifier of the message.
     */
    public void getBoard(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData)).toArray()[0];
            HashMap<Coordinates, Card> board = user.getPlayerBoard().getBoard();
            HashMap<Coordinates, Integer> boardIds = new HashMap<>();
            for (Coordinates coordinate : board.keySet()) {
                if (board.get(coordinate).getId() == GameConsts.notFillableId) {
                    continue;
                }
                boardIds.put(coordinate, board.get(coordinate).getId());

            }
            messageQueue.add(ThreadMessage.getBoardResponse(username, boardIds, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the resources of cards in the hand of a Player.
     *
     * @param username             the username of the player that requests the resources.
     * @param usernameRequiredData the username of the player whose hand to get.
     * @param messageId            the unique identifier of the message.
     */
    public void getHandColor(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData)).toArray()[0];
            ArrayList<Resource> handColors = new ArrayList<>();
            for (ColoredCard c : user.getHand()) {
                if (c == null) {
                    continue;
                }
                handColors.add(c.getBackResource());
            }
            messageQueue.add(ThreadMessage.getHandColorResponse(username, handColors, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Retrieves and sends if the cards in the hand are gold or standard of a specified player to the requesting player.
     *
     * @param username             the username of the player requesting the hand types
     * @param usernameRequiredData the username of the player whose hand types are being requested
     * @param messageId            the unique identifier of the message
     */
    public void getHandType(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData)).toArray()[0];
            ArrayList<Boolean> isGold = new ArrayList<>();
            for (ColoredCard c : user.getHand()) {
                if (c == null) {
                    continue;
                }
                isGold.add(c instanceof GoldCard);
            }
            messageQueue.add(ThreadMessage.getHandTypeResponse(username, isGold, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the Deque of the unique identifiers of the last placed Cards of the player.
     *
     * @param username  the username of the player that requests the last placed cards.
     * @param messageId the unique identifier of the message.
     */
    public void getPlacingOrder(String username, String usernameRequiredData, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(usernameRequiredData)).toArray()[0];
            Deque<Integer> cardIds = new ArrayDeque<>();
            for (Card card : user.getPlayerBoard().getLastPlacedCards()) {
                cardIds.add(card.getId());
            }
            messageQueue.add(ThreadMessage.getPlacingOrderResponse(username, cardIds, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Retrieves and sends the starting card of a specified player to the requesting player.
     *
     * @param username  the username of the player requesting the starting card
     * @param messageId the unique identifier of the message
     */
    public void getStartingCards(String username, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];

            Card startingCard = user.drawStartingCard();

            messageQueue.add(ThreadMessage.getStartingCardResponse(username, startingCard.getId(), messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Sets the first card placement for a specified player and sends an OK response.
     *
     * @param username  the username of the player setting the first card placement
     * @param bool      the boolean value indicating the first card placement status
     * @param messageId the unique identifier of the message
     */
    public void placeStartingCard(String username, boolean bool, UUID messageId) {
        try {
            Player user = (Player) game.getPlayers().stream().filter(player -> player.getUsername().equals(username)).toArray()[0];

            user.setFirstCard(bool);

            messageQueue.add(ThreadMessage.okResponse(username, messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

    /**
     * Controller Method to get the state of the game.
     *
     * @param username  the username of the player that requests the state.
     * @param messageId the unique identifier of the message.
     */
    public void getGameState(String username, UUID messageId) {
        try {
            messageQueue.add(ThreadMessage.getGameStateResponse(username, game.getGameState().toString(), messageId));
        } catch (Exception e) {
            logError(username, messageId, e);
        }
    }

}
