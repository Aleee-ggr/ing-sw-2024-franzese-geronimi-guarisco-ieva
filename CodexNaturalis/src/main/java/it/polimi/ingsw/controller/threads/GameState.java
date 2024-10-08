package it.polimi.ingsw.controller.threads;

import com.google.common.collect.ImmutableSet;

/**
 * Enumerates the possible states of the game and the methods that can be called in each state.
 */
public enum GameState {
    LOBBY,
    SETUP,
    MAIN,
    ENDGAME,
    STANDBY,
    STOP;

    public static final ImmutableSet<String> lobby =
            ImmutableSet.of("join",
                    "getGameState",
                    "getPlayers");

    public static final ImmutableSet<String> setup =
            ImmutableSet.of("join",
                    "getHand",
                    "getPlayers",
                    "placeStartingCard",
                    "getPlacingOrder",
                    "getStartingObjectives",
                    "getCommonObjectives",
                    "getStartingCard",
                    "choosePersonalObjective",
                    "choosePlayerColor",
                    "getGameState");

    public static final ImmutableSet<String> main =
            ImmutableSet.of("join",
                    "draw",
                    "place",
                    "getPlayers",
                    "getCommonObjectives",
                    "getPlacingOrder",
                    "getHand",
                    "getScoreMap",
                    "getPlayerResources",
                    "getVisibleCard",
                    "getBackSideDecks",
                    "getValidPlacements",
                    "getBoard",
                    "getHandColor",
                    "getHandType",
                    "getLastPlacedCards",
                    "update",
                    "getGameState");

    public static final ImmutableSet<String> standby =
            ImmutableSet.of("join",
                    "getPlayers",
                    "getCommonObjectives",
                    "getPlacingOrder",
                    "getHand",
                    "getScoreMap",
                    "getPlayerResources",
                    "getVisibleCard",
                    "getBackSideDecks",
                    "getValidPlacements",
                    "getBoard",
                    "getHandColor",
                    "getHandType",
                    "getLastPlacedCards",
                    "update",
                    "getGameState");

    public static final ImmutableSet<String> stop = ImmutableSet.of("update");
}
