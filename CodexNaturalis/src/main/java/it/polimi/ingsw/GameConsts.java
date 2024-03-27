package it.polimi.ingsw;

import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains constants used throughout the game.
 */
public abstract class GameConsts {
    public static final HashMap<String, Resource> resourceMap = new HashMap<>() {{
        put("FUNGI", Resource.FUNGI);
        put("PLANT", Resource.PLANT);
        put("INSECT", Resource.INSECT);
        put("ANIMAL", Resource.ANIMAL);
        put("QUILL", Resource.QUILL);
        put("INKWELL", Resource.INKWELL);
        put("MANUSCRIPT", Resource.MANUSCRIPT);
        put("NONE", Resource.NONE);
    }};
    public static final List<String> requirementsList = new ArrayList<>() {{
        add("FUNGI");
        add("PLANT");
        add("INSECT");
        add("ANIMAL");
    }};
    public static final String cardJsonPath = "src/main/resources/cards.json";
    public static final int visibleCards = 4;
    public static final int globalObjectives = 2;
    public static final int numCorners = 4;
    public static final int maxNumRequirements = 5;
    public static final int firstHandDim = 3;
    public static final int fistHandStdNum = 2;
    public static final int maxPlayersNum = 4;
}
