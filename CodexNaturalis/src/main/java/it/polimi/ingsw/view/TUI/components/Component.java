package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.enums.Resource;

/**
 * Represents a generic interface for TUI components.
 */
public interface Component {
    /**
     * Converts the string representation of the component into an array of strings,
     * splitting by newline characters.
     *
     * @return An array of strings representing the component.
     */
    default String[] toStringArray() {
        return this.toString().split("\n");
    }

    /**
     * Converts the string representation of the component into an array of strings with color-enhanced content,
     * replacing specific characters with colored representations.
     *
     * @return An array of strings with color-enhanced content representing the component.
     */
    default String[] toStringArrayColor() {
        String s = this.toString();
        for(Resource r: Resource.values()) {
            s = s.replaceAll(Character.toString(r.toCharCenter()), r.toColorCenter());
            s = s.replaceAll(Character.toString(r.toChar()), r.toColorBlock());
        }
        return s.split("\n");
    }
}
