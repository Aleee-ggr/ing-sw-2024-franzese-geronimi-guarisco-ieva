package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.view.TUI.components.printables.Alphabet;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.TUI.components.printables.Alphabet.alphabet;

public class EndGameTUI {
    private final List<Pair<String, Integer>> scoreboard;

    public EndGameTUI(Map<String, Integer> scoreboard) {
        this.scoreboard = scoreboard.entrySet().stream().sorted((e1, e2) -> e2.getValue() - e1.getValue()).map(e -> Pair.of(e.getKey(), e.getValue())).toList();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        byte position = 1;
        for (Pair<String, Integer> p : scoreboard) {
            String strScore = String.valueOf(p.second());
            for (int i = 0; i < Alphabet.letterHeight; i++) {
                // Print placing position
                out.append(alphabet.get((char) ('0' + position)).split("\n")[i]);
                out.append(Alphabet.space());
                out.append(alphabet.get('.').split("\n")[i]);
                out.append(Alphabet.space());
                out.append(Alphabet.space());
                // Print username
                for (char c : p.first().toCharArray()) {
                    out.append(Alphabet.getLetter(c).split("\n")[i]);
                    out.append(Alphabet.space());
                }

                // Print semicolon
                out.append(Alphabet.space());
                out.append(Alphabet.getLetter(':').split("\n")[i]);
                out.append(Alphabet.space());
                out.append(Alphabet.space());
                for (char c : strScore.toCharArray()) {
                    out.append(Alphabet.getLetter(c).split("\n")[i]);
                    out.append(Alphabet.space());
                }
                out.append("\n");

            }

            position++;
        }

        return out.toString();
    }
}
