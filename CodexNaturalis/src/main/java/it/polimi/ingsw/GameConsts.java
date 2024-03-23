package it.polimi.ingsw;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;

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
    public static final int visibleCards = 4;
    public static final int globalObjectives = 2;
    public static final int numCorners = 4;
    public static final int maxNumRequirements = 5;
    public static final int firstHandDim = 3;
    public static final int fistHandStdNum = 2;
}
