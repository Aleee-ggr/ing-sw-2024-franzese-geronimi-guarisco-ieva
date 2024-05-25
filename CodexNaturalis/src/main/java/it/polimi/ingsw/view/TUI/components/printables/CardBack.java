package it.polimi.ingsw.view.TUI.components.printables;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;
import java.util.Map;

public abstract class CardBack implements Iterable<String> {
    public static final int width = 15;
    public static final int height = 5;

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";


    public static final String animalsBack =
            "┏━━━━━━━━━━━━━┓\n" +
            "┃"+ ANSI_BLUE + "   |\\.-./|   " + ANSI_RESET + "┃\n" +
            "┃"+ ANSI_BLUE + "   |^._.^|   " + ANSI_RESET + "┃\n" +
            "┃"+ ANSI_BLUE + "    \\_Y_/    " + ANSI_RESET + "┃\n" +
            "┗━━━━━━━━━━━━━┛";

    public static final String plantsBack =
            "┏━━━━━━━━━━━━━┓\n" +
            "┃" + ANSI_GREEN + "      _      " + ANSI_RESET + "┃\n" +
            "┃" + ANSI_GREEN + "     (ඞ)     " + ANSI_RESET + "┃\n" +
            "┃" + ANSI_GREEN + "     \\|/     " + ANSI_RESET + "┃\n" +
            "┗━━━━━━━━━━━━━┛";

    public static final String insectsBack =
            "┏━━━━━━━━━━━━━┓\n" +
            "┃" + ANSI_PURPLE + "     \\./     " + ANSI_RESET + "┃\n" +
            "┃" + ANSI_PURPLE + "   (O·|·O)   " + ANSI_RESET + "┃\n" +
            "┃" + ANSI_PURPLE + "    (o|o)    " + ANSI_RESET + "┃\n" +
            "┗━━━━━━━━━━━━━┛";

    public static final String fungiBack =
            "┏━━━━━━━━━━━━━┓\n" +
            "┃" + ANSI_RED + "    .-·-.    " + ANSI_RESET + "┃\n" +
            "┃" + ANSI_RED + "   (._._.)   " + ANSI_RESET + "┃\n" +
            "┃" + ANSI_RED + "     |_|     " + ANSI_RESET + "┃\n" +
            "┗━━━━━━━━━━━━━┛";

    public static final String[] values = {
            animalsBack,
            plantsBack,
            insectsBack,
            fungiBack
    };

    public static final Map<Resource, String> resources = new HashMap<>() {{
        put(Resource.FUNGI, fungiBack);
        put(Resource.PLANT, plantsBack);
        put(Resource.ANIMAL, animalsBack);
        put(Resource.INSECT, insectsBack);
    }};
}
