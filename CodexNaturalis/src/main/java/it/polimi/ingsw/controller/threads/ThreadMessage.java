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
