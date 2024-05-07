package it.polimi.ingsw.controller.threads;

import com.google.common.collect.ImmutableSet;

public enum GameState {
    LOBBY,
    SETUP,
    MAIN,
    ENDGAME,
    STOP;
    public static final ImmutableSet<String> lobby = ImmutableSet.of("join");
    public static final ImmutableSet<String> setup = ImmutableSet.of("join",
                                                                    "getHand",
                                                                    "getPlayers",
                                                                    "placeStartingCard",
                                                                    "getStartingObjectives",
                                                                    "getCommonObjectives",
                                                                    "getStartingCard",
                                                                    "choosePersonalObjectiveResponse");
    public static final ImmutableSet<String> main = ImmutableSet.of("join",
                                                                    "draw",
                                                                    "place",
                                                                    "getPlayers",
                                                                    "getCommonObjectives",
                                                                    "getHand",
                                                                    "getScoreMap",
                                                                    "getPlayerResources",
                                                                    "getVisibleCards",
                                                                    "getBackSideDecks",
                                                                    "getValidPlacements",
                                                                    "getBoard",
                                                                    "getHandColor",
                                                                    "getLastPlacedCards",
                                                                    "update");
    public static final ImmutableSet<String> endgame = main;
    public static final ImmutableSet<String> stop = ImmutableSet.of("update");
    public static final ImmutableSet<String> getters = ImmutableSet.of("getHand", //TODO: check if this is all
                                                                        "getStartingObjectives",
                                                                        "getPlayers",
                                                                        "getCommonObjectives",
                                                                        "getStartingCard",
                                                                        "getScoreMap",
                                                                        "getPlayerResources",
                                                                        "getVisibleCards",
                                                                        "getBackSideDecks",
                                                                        "getValidPlacements",
                                                                        "getBoard",
                                                                        "getHandColor",
                                                                        "getLastPlacedCards");
}
