package it.polimi.ingsw.model.board;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedBoard {
    public SharedBoard(DeckArea deckArea) {
        this.deckArea = deckArea;
    }

    public Map<Player, Integer> getScore() {
        return new ConcurrentHashMap<>(scoreMap); // Copy the map instead of returning the object itself
    }

    public Objective[] getGlobalObjectives() {
        return Arrays.copyOf(objectives, GameConsts.globalObjectives);
    }

    public DeckArea getDeckArea() {
        return this.deckArea;
    }

    private final Objective[] objectives = new Objective[GameConsts.globalObjectives];
    private final ConcurrentHashMap<Player, Integer> scoreMap = new ConcurrentHashMap<>();
    private final DeckArea deckArea;
}
