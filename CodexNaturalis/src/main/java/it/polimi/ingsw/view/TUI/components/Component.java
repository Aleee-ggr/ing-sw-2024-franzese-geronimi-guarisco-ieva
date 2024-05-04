package it.polimi.ingsw.view.TUI.components;

public interface Component {
    default String[] toStringArray() {
        return this.toString().split("\n");
    }
}
