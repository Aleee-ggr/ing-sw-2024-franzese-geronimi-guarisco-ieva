package it.polimi.ingsw;

import it.polimi.ingsw.model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class GameConsts {

    public static HashMap<String, Resource> resourceMap = new HashMap<>() {{
        put("MUSHROOM", Resource.MUSHROOM);
        put("LEAF", Resource.LEAF);
        put("BUTTERFLY", Resource.BUTTERFLY);
        put("WOLF", Resource.WOLF);
        put("QUILL", Resource.QUILL);
        put("INK", Resource.INK);
        put("SCROLL", Resource.SCROLL);
        put("NONE", Resource.NONE);
    }};

    public static List<String> requirementsList = new ArrayList<>() {{
        add("MUSHROOM");
        add("LEAF");
        add("BUTTERFLY");
        add("WOLF");
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
