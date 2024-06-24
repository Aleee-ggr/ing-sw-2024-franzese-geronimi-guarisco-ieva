package it.polimi.ingsw.controller;

/**
 * Enumeration representing different states a player can be in during the game.
 * These states dictate the actions players can perform and the notifications they receive.
 */
public enum WaitState {
    UPDATE, TURN_UPDATE, TURN, WAIT, SETUP_TURN, ENDGAME
}
