package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a type of message for communication between the server and games.
 * It contains different methods for creating various types of messages as needed.
 *
 * @param status The status of the message (REQUEST, OK, ERROR, INIT).
 * @param player The username of the player making the request.
 * @param type The type of the message.
 * @param args Additional arguments for the message.
 * @param messageUUID A unique identifier for the message.
 */
public record ThreadMessage(Status status, String player, String type, String[] args, UUID messageUUID) {

    //ThreadMessage with generic response

    /**
     * Creates a new message to request the creation of a game.
     * @param username The username of the player making the request.
     * @param playerNum The number of players for the game.
     * @param gameId The UUID of the game.
     * @return A new ThreadMessage with the specified parameters.
     */
    public static ThreadMessage create(String username, Integer playerNum, UUID gameId){
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "create",
                new String[] {
                        playerNum.toString(),
                        gameId.toString()
                },
                UUID.randomUUID()
        );
    }

    /**
     * Creates a new message to request joining a game.
     * @param username The username of the player making the request.
     * @return A new ThreadMessage to join a game.
     */
    public static ThreadMessage join(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "join",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a new message to request placing a card.
     * @param username The username of the player making the request.
     * @param coordinates The coordinates where the card will be placed.
     * @param cardId The ID of the card to be placed.
     * @return A new ThreadMessage to place a card.
     */
    public static ThreadMessage placeCard(String username, Coordinates coordinates, Integer cardId) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "place",
                new String[] {
                        coordinates.x().toString(),
                        coordinates.y().toString(),
                        cardId.toString()
                },
                UUID.randomUUID()
        );
    }



    //ThreadMessage with specific response

    /**
     * Creates a new message to wait for updates from the Client.
     * @param username The username of the player making the request.
     * @return A new ThreadMessage to wait for updates.
     */
    public static ThreadMessage update(String username){
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "update",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a new message to request drawing a card.
     * @param username The username of the player making the request.
     * @param position The position of the card to be drawn.
     * @return A new ThreadMessage to draw a card.
     */
    public static ThreadMessage draw(String username, Integer position) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "draw",
                new String[] {
                        position.toString()
                },
                UUID.randomUUID()
        );
    }

    /**
     * Creates a new message to request choosing a personal objective.
     * @param username The username of the player making the request.
     * @param objectiveId The ID of the objective chosen.
     * @return A new ThreadMessage to choose a personal objective.
     */
    public static ThreadMessage choosePersonalObjective (String username, Integer objectiveId) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "choosePersonalObjective",
                new String[]{
                        objectiveId.toString()
                },
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting the score map.
     * @param username The username of the player.
     * @return A ThreadMessage requesting the score map.
     */
    public static ThreadMessage getScoreMap(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getScoreMap",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting the hand.
     * @param username The username of the player.
     * @return A ThreadMessage requesting the hand.
     */
    public static ThreadMessage getHand(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getHand",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting the common objectives.
     * @param username The username of the player.
     * @return A ThreadMessage requesting the common objectives.
     */
    public static ThreadMessage getCommonObjectives(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getStartingObjectives",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting the starting objectives.
     * @param username The username of the player.
     * @return A ThreadMessage requesting the starting objectives.
     */
    public static ThreadMessage getStartingObjectives(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getStartingObjectives",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting player resources.
     * @param username The username of the player.
     * @param usernameRequiredData The username of the required data.
     * @return A ThreadMessage requesting the player's resources.
     */
    public static ThreadMessage getPlayerResources(String username, String usernameRequiredData) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getPlayerResources",
                new String[] {
                        usernameRequiredData
                },
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting visible cards.
     * @param username The username of the player.
     * @return A ThreadMessage requesting visible cards.
     */
    public static ThreadMessage getVisibleCards(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getVisibleCards",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting back side decks.
     * @param username The username of the player.
     * @return A ThreadMessage requesting back side decks.
     */
    public static ThreadMessage getBackSideDecks(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getBackSideDecks",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting valid placements.
     * @param username The username of the player.
     * @return A ThreadMessage requesting valid placements.
     */
    public static ThreadMessage getValidPlacements(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getValidPlacements",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting the board.
     * @param username The username of the player.
     * @param usernameRequiredData The username of the required data.
     * @return A ThreadMessage requesting the board.
     */
    public static ThreadMessage getBoard(String username, String usernameRequiredData) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getBoard",
                new String[] {
                        usernameRequiredData
                },
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting hand color.
     * @param username The username of the player.
     * @param usernameRequiredData The username of the required data.
     * @return A ThreadMessage requesting hand color.
     */
    public static ThreadMessage getHandColor(String username, String usernameRequiredData) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getHandColor",
                new String[] {
                    usernameRequiredData
                },
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting an ArrayList of order of cards.
     * @param username The username of the player.
     * @param usernameRequiredData The username of the required data.
     * @return A ThreadMessage requesting order of cards.
     */
    public static ThreadMessage getPlacingOrder(String username, String usernameRequiredData) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getPlacingOrder",
                new String[] {
                        usernameRequiredData
                },
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting the players.
     * used to get the turn order for players.
     * @param username The username of the player.
     * @return A ThreadMessage requesting the players.
     */
    public static ThreadMessage getPlayers(String username){
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getPlayers",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting to draw the starting card.
     * @param username The username of the player.
     * @return A ThreadMessage requesting the starting card.
     */
    public static ThreadMessage getStartingCard(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getStartingCard",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage placeStartingCard(String username, String bool){
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "placeStartingCard",
                new String[]{
                        bool
                },
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting the game state.
     * @param username The username of the player.
     * @return A ThreadMessage requesting the game state.
     */
    public static ThreadMessage getGameState(String username){
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getGameState",
                null,
                UUID.randomUUID()
        );
    }

    /**
     * Creates a ThreadMessage for requesting the available games.
     * @param username The username of the player.
     * @return A ThreadMessage requesting the available games.
     */
    public static ThreadMessage getAvailableGames(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getAvailableGames",
                null,
                UUID.randomUUID()
        );
    }


    //generic Responses

    /**
     * Creates a ThreadMessage for a generic error response.
     * @param username The username of the player.
     * @param messageUUID The UUID of the message.
     * @param cause the cause of the error
     * @return A ThreadMessage for a generic error response.
     */
    public static ThreadMessage genericError(String username, UUID messageUUID, String cause) {
        return new ThreadMessage(
                Status.ERROR,
                username,
                "genericError",
                new String[] {cause},
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for an OK response.
     * @param username The username of the player.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for an OK response.
     */
    public static ThreadMessage okResponse(String username, UUID messageUUID) {
        return new ThreadMessage(
                Status.OK,
                username,
                "ok",
                null,
                messageUUID
        );
    }


    //specific Responses

    /**
     * Creates a ThreadMessage for an update response.
     * @param username The username of the player.
     * @param playerTurn A boolean indicating whether it is the player's turn.
     * @param messageUUID The UUID of the message.
     */
    public static ThreadMessage updateResponse(String username, boolean playerTurn, UUID messageUUID) {
        return new ThreadMessage(
                Status.OK,
                username,
                "updateResponse",
                new String[] {
                        String.valueOf(playerTurn)
                },
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a draw response.
     * @param username The username of the player.
     * @param cardId The ID of the drawn card.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a draw response.
     */
    public static ThreadMessage drawResponse(String username, Integer cardId, UUID messageUUID) {
        return new ThreadMessage(
                Status.OK,
                username,
                "drawResponse",
                new String[] {
                        cardId.toString()
                },
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a personal objective choice response.
     * @param username The username of the player.
     * @param correct A boolean indicating whether the choice was correct.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a personal objective choice response.
     */
    public static ThreadMessage choosePersonalObjectiveResponse (String username, boolean correct, UUID messageUUID) {
        return new ThreadMessage(
                Status.OK,
                username,
                "choosePersonalObjectiveResponse",
                new String[]{
                        String.valueOf(correct)
                },
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a starting card response.
     * @param username The username of the player.
     * @param cardId The ID of the starting card.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a starting card response.
     */
    public static ThreadMessage getStartingCardResponse(String username, Integer cardId, UUID messageUUID) {
        return new ThreadMessage(
                Status.OK,
                username,
                "getStartingCardResponse",
                new String[]{
                        cardId.toString()
                },
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a score map response.
     * @param username The username of the player.
     * @param scoreMap The score map as a ConcurrentHashMap.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a score map response.
     */
    public static ThreadMessage getScoreMapResponse(String username, ConcurrentHashMap<String, Integer> scoreMap, UUID messageUUID) {
        String[] args = new String[scoreMap.size()];
        int index = 0;

        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            args[index] = entry.getKey() + ":";
            if (entry.getValue() == null) {
                args[index] += "0";
            } else {
                args[index] += entry.getValue().toString();
            }
            index++;
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getScoreMapResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a hand response.
     * @param username The username of the player.
     * @param handIds An ArrayList of hand IDs.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a hand response.
     */
    public static ThreadMessage getHandResponse(String username, ArrayList<Integer> handIds, UUID messageUUID) {
        String[] args = new String[handIds.size()];

        for (int i = 0; i < handIds.size(); i++) {
            args[i] = handIds.get(i).toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getHandResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a common objectives response.
     * @param username The username of the player.
     * @param startingObjectivesIds An ArrayList of starting objective IDs.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a common objectives response.
     */
    public static ThreadMessage getCommonObjectivesResponse(String username, ArrayList<Integer> startingObjectivesIds, UUID messageUUID) {
        String[] args = new String[startingObjectivesIds.size()];

        for (int i = 0; i < startingObjectivesIds.size(); i++) {
            args[i] = startingObjectivesIds.get(i).toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getCommonObjectivesResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a starting objectives response.
     * @param username The username of the player.
     * @param startingObjectivesIds An ArrayList of starting objective IDs.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a starting objectives response.
     */
    public static ThreadMessage getStartingObjectivesResponse(String username, ArrayList<Integer> startingObjectivesIds, UUID messageUUID) {
        String[] args = new String[startingObjectivesIds.size()];

        for (int i = 0; i < startingObjectivesIds.size(); i++) {
            args[i] = startingObjectivesIds.get(i).toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getStartingObjectivesResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a player resources response.
     * @param username The username of the player.
     * @param playerResources A ConcurrentHashMap of player resources.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a player resources response.
     */
    public static ThreadMessage getPlayerResourcesResponse(String username, ConcurrentHashMap<Resource, Integer> playerResources, UUID messageUUID) {
        String[] args = new String[playerResources.size()];
        int index = 0;

        for (Resource r : playerResources.keySet()) {
            args[index] = r.toString() + ":" + playerResources.get(r).toString();
            index++;
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getPlayerResourcesResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a visible card response.
     * @param username The username of the player.
     * @param cardId An ArrayList of card IDs.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a visible card response.
     */
    public static ThreadMessage getVisibleCardsResponse(String username, ArrayList<Integer> cardId, UUID messageUUID) {
        String[] args = new String[cardId.size()];

        for (int i = 0; i < cardId.size(); i++) {
            args[i] = cardId.get(i).toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getVisibleCardResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a back side decks response.
     * @param username The username of the player.
     * @param backSideCardsIds An ArrayList of back side deck card IDs.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a back side decks response.
     */
    public static ThreadMessage getBackSideDecksResponse(String username, ArrayList<Integer> backSideCardsIds, UUID messageUUID) {
        String[] args = new String[backSideCardsIds.size()];

        for (int i = 0; i < backSideCardsIds.size(); i++) {
            args[i] = backSideCardsIds.get(i).toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getBackSideDecksResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a valid placements response.
     * @param username The username of the player.
     * @param validPlacements A set of valid placements represented by coordinates.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a valid placements response.
     */
    public static ThreadMessage getValidPlacementsResponse(String username, Set<Coordinates> validPlacements, UUID messageUUID) {
        String[] args = new String[validPlacements.size()];

        int index = 0;
        for (Coordinates coordinates : validPlacements) {
            args[index] = coordinates.x() + "," + coordinates.y();
            index++;
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getValidPlacementsResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a board response.
     * @param username The username of the player.
     * @param board A HashMap representing the game board, with Coordinates as keys and integers as values.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a board response.
     */
    public static ThreadMessage getBoardResponse(String username, HashMap<Coordinates, Integer> board, UUID messageUUID) {
        String[] args = new String[board.size()];
        int index = 0;

        for (Map.Entry<Coordinates, Integer> entry : board.entrySet()) {
            args[index] = entry.getKey().x().toString() + "," + entry.getKey().y().toString() + "," + entry.getValue().toString();
            index++;
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getBoardResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a hand color response.
     * @param username The username of the player.
     * @param handColors An ArrayList of hand colors.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a hand color response.
     */
    public static ThreadMessage getHandColorResponse(String username, ArrayList<Resource> handColors, UUID messageUUID) {
        String[] args = new String[handColors.size()];

        for (int i = 0; i < handColors.size(); i++) {
            args[i] = handColors.get(i).toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getHandColorResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a last Placing card order response.
     * @param username The username of the player.
     * @param order A Deque of card IDs.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a last placed cards order response.
     */
    public static ThreadMessage getPlacingOrderResponse(String username, Deque<Integer> order, UUID messageUUID) {
        String[] args = new String[order.size()];
        int index = 0;

        for (Integer cardId : order) {
            args[index] = cardId.toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getPlacingOrderResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a players response.
     * @param username The username of the player.
     * @param players An array of player usernames.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a players response.
     */
    public static ThreadMessage getPlayersResponse(String username, ArrayList<String> players, UUID messageUUID) {
        String[] args = new String[players.size()];
        int index = 0;

        for (String player : players) {
            args[index] = player;
            index++;
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getPlayersResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for a getAvailableGames response.
     * @param username The username of the player.
     * @param availableGames An ArrayList of available game UUIDs.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a getAvailableGames response.
     */
    public static ThreadMessage getAvailableGamesResponse(String username, ArrayList<UUID> availableGames, UUID messageUUID) {
        String[] args = new String[availableGames.size()];

        for (int i = 0; i < availableGames.size(); i++) {
            args[i] = availableGames.get(i).toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getAvailableGamesResponse",
                args,
                messageUUID
        );
    }

    /**
     * Creates a ThreadMessage for the game state response.
     * @param username The username of the player.
     * @param gameState The game state.
     * @param messageUUID The UUID of the message.
     * @return A ThreadMessage for a gameState response.
     * */
    public static ThreadMessage getGameStateResponse(String username, String gameState, UUID messageUUID){
        String[] args = new String[1];
        args[0] = gameState;
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getGameStateResponse",
                args,
                messageUUID
        );
    }
}

