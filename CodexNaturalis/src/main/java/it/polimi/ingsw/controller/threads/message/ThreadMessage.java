package it.polimi.ingsw.controller.threads.message;

import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.model.board.Coordinates;

public record ThreadMessage(Status status, String player, String type, String[] args) {
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
}
