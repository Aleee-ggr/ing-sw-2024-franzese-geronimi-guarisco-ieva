package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ThreadMessage Record is used as type of messages to communicate from and to the server and the games.
 * It has different ThreadMessages for each message needed.
 * @author Daniele Ieva
 * @author Alessio Guarisco
 * */
public record ThreadMessage(Status status, String player, String type, String[] args) {
    public static ThreadMessage create(String username, Integer playerNum, UUID gameId){
        return new ThreadMessage(
            Status.REQUEST,
            username,
            "create",
            new String[] {
                    playerNum.toString(),
                    gameId.toString()
            }
        );
    }

    public static ThreadMessage join(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "join",
                null
        );
    }

    public static ThreadMessage draw(String username, Integer position) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "draw",
                new String[] {
                        position.toString()
                }
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
                }
        );
    }

    public static ThreadMessage getUsername(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getUsername",
                null
        );
    }

    public static ThreadMessage getScoreMap(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getScoreMap",
                null
        );
    }

    public static ThreadMessage getHand(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getHand",
                null
        );
    }

    public static ThreadMessage getPlayerBoards(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getPlayerBoard",
                null
        );
    }

    public static ThreadMessage getCommonObjectives(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getStartingObjectives",
                null
        );
    }

    public static ThreadMessage getPlayerResources(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getPlayerResources",
                null
        );
    }

    public static ThreadMessage getVisibleCards(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getVisibleCard",
                null
        );
    }

    public static ThreadMessage getBackSideDecks(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getBackSideDecks",
                null
        );
    }

    public static ThreadMessage getValidPlacements(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getValidPlacements",
                null
        );
    }

    public static ThreadMessage getPlayersBoards(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "getPlayersBoards",
                null
        );
    }

    public static ThreadMessage choosePersonalObjective(String username) {
        return new ThreadMessage(
                Status.REQUEST,
                username,
                "choosePersonalObjective",
                null
        );
    }

    public static ThreadMessage genericError(String username) {
        return new ThreadMessage(
            Status.ERROR,
            username,
            "genericError",
            null
            );
    }

    public static ThreadMessage okResponse(String username) {
        return new ThreadMessage(
                Status.OK,
                username,
                "ok",
                null
            );
    }

    public static ThreadMessage drawResponse(String username, Integer cardId) {
        return new ThreadMessage(
                Status.OK,
                username,
                "drawResponse",
                new String[] {
                        cardId.toString()
                }
        );
    }

    public static ThreadMessage getUsernameResponse(String username, String usernameResponse) {
        return new ThreadMessage(
                Status.OK,
                username,
                "getUsernameResponse",
                new String[]{
                        usernameResponse
                }
        );
    }

    public static ThreadMessage getScoreMapResponse(String username, ConcurrentHashMap<String, Integer> scoreMap) {
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
                args
        );
    }

    public static ThreadMessage getValidPlacementsResponse(String username, Set<Coordinates> validPlacements) {
        String[] args = new String[validPlacements.size()];

        int index = 0;
        for (Coordinates coordinates : validPlacements) {
            args[index] = coordinates.x() + "," + coordinates.y();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getValidPlacementsResponse",
                args
        );
    }

    public static ThreadMessage getHandResponse(String username, ArrayList<Integer> handIds) {
        String[] args = new String[handIds.size()];

        for (int i = 0; i < handIds.size(); i++) {
            args[i] = handIds.get(i).toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getHandResponse",
                args
        );
    }

    public static ThreadMessage getVisibleCardResponse(String username, Integer[] cardId) {
        String[] args = new String[cardId.length];

        for (int i = 0; i < cardId.length; i++) {
            args[i] = cardId[i].toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getVisibleCardResponse",
                args
        );
    }

    public static ThreadMessage getCommonObjectivesResponse(String username, Integer[] startingObjectivesIds) {
        String[] args = new String[startingObjectivesIds.length];

        for (int i = 0; i < startingObjectivesIds.length; i++) {
            args[i] = startingObjectivesIds[i].toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getCommonObjectivesResponse",
                args
        );
    }

    public static ThreadMessage getPlayerResourcesResponse(String username, ConcurrentHashMap<Resource, Integer> playerResources) {
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
                args
        );
    }

    public static ThreadMessage getBackSideDecksResponse(String username, Integer[] backSideCardsIds) {
        String[] args = new String[backSideCardsIds.length];

        for (int i = 0; i < backSideCardsIds.length; i++) {
            args[i] = backSideCardsIds[i].toString();
        }

        return new ThreadMessage(
                Status.OK,
                username,
                "getBackSideDecksResponse",
                args
        );
    }
}
