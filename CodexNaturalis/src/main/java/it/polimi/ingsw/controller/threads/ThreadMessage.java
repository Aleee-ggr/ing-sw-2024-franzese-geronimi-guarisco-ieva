package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ThreadMessage Record is used as type of messages to communicate from and to the server and the games.
 * It has different ThreadMessages for each message needed.
 * @author Daniele Ieva
 * @author Alessio Guarisco
 * @author Gloria Geronimi
 * @author Samuele Franzese
 * */
public record ThreadMessage(Status status, String player, String type, String[] args, UUID messageUUID) {
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

    public static ThreadMessage join(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "join",
                null,
                UUID.randomUUID()
        );
    }

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

    public static ThreadMessage getUsername(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getUsername",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getScoreMap(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getScoreMap",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getHand(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getHand",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getPlayerBoards(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getPlayerBoard",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getCommonObjectives(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getStartingObjectives",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getStartingObjectives(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getStartingObjectives",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getPlayerResources(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getPlayerResources",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getVisibleCards(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getVisibleCard",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getBackSideDecks(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getBackSideDecks",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getValidPlacements(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getValidPlacements",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getBoard(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getBoard",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getHandColor(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getHandColor",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage getLastPlacedCards(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getLastPlacedCards",
                null,
                UUID.randomUUID()
        );
    }

    public static ThreadMessage genericError(String username, UUID messageUUID) {
        return new ThreadMessage(
                Status.ERROR,
                username,
                "genericError",
                null,
                messageUUID
        );
    }

    public static ThreadMessage okResponse(String username, UUID messageUUID) {
        return new ThreadMessage(
                Status.OK,
                username,
                "ok",
                null,
                messageUUID
        );
    }

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

    public static ThreadMessage getUsernameResponse(String username, String usernameResponse, UUID messageUUID) {
        return new ThreadMessage(
                Status.OK,
                username,
                "getUsernameResponse",
                new String[]{
                        usernameResponse
                },
                messageUUID
        );
    }

    public static ThreadMessage getScoreMapResponse(String username, ConcurrentHashMap<String, Integer> scoreMap, UUID messageUUID) {
        String[] args = new String[scoreMap.size()];
        int index = 0;

        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            args[index] = entry.getKey() + ":" + entry.getValue().toString();
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

    public static ThreadMessage getValidPlacementsResponse(String username, Set<Coordinates> validPlacements, UUID messageUUID) {
        String[] args = new String[validPlacements.size()];

        int index = 0;
        for (Coordinates coordinates : validPlacements) {
            args[index] = coordinates.x() + "," + coordinates.y();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getValidPlacementsResponse",
                args,
                messageUUID
        );
    }

    public static ThreadMessage getLastPlacedCardsResponse(String username, Deque<Integer> lastPlacedCards, UUID messageUUID) {
        String[] args = new String[lastPlacedCards.size()];
        int index = 0;

        for (Integer cardId : lastPlacedCards) {
            args[index] = cardId.toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getLastPlacedCardsResponse",
                args,
                messageUUID
        );
    }

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

    public static ThreadMessage getVisibleCardResponse(String username, ArrayList<Integer> cardId, UUID messageUUID) {
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

    public static ThreadMessage getPlayerResourcesResponse(String username, ConcurrentHashMap<Resource, Integer> playerResources, UUID messageUUID) {
        String[] args = new String[playerResources.size()];
        int index = 0;

        for (Map.Entry<Resource, Integer> entry : playerResources.entrySet()) {
            args[index] = entry.getKey().toString() + ":" + entry.getValue().toString();
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
}
