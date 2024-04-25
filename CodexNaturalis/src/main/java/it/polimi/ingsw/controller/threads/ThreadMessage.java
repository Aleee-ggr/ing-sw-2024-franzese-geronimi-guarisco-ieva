package it.polimi.ingsw.controller.threads;

import it.polimi.ingsw.model.board.Coordinates;

import java.util.UUID;

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
}
