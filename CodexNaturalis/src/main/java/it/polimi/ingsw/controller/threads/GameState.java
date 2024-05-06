package it.polimi.ingsw.controller.threads;

import com.google.common.collect.ImmutableSet;

public enum GameState {
    LOBBY,
    SETUP,
    MAIN,
    ENDGAME,
    STOP;
    public static final ImmutableSet<String> lobby = ImmutableSet.of("join");
    public static final ImmutableSet<String> setup = ImmutableSet.of("join", "getHand", "getStartingObjectives", "getCommonObjectives", "getStartingCard");
}
