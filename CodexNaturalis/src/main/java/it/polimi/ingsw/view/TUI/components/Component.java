package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.enums.Resource;

public interface Component {
    default String[] toStringArray() {
        return this.toString().split("\n");
    }
    default String[] toStringArrayColor() {
        String s = this.toString();
        for(Resource r: Resource.values()) {
            s = s.replaceAll(Character.toString(r.toChar()), r.toColorBlock());
        }
        System.out.println(s);
        return s.split("\n");
    }
}
