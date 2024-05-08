package it.polimi.ingsw.view.TUI.components.printables;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;
import java.util.Map;

public abstract class CardBack implements Iterable<String> {
    public static final int width = 15;
    public static final int height = 5;
    public static final String animalsBack = """
            ┏━━━━━━━━━━━━━┓
            ┃   |\\.-./|   ┃
            ┃   |^._.^|   ┃
            ┃    \\_Y_/    ┃
            ┗━━━━━━━━━━━━━┛""";
    public static final String plantsBack = """
            ┏━━━━━━━━━━━━━┓
            ┃      _      ┃
            ┃     (ඞ)     ┃
            ┃     \\|/     ┃
            ┗━━━━━━━━━━━━━┛""";
    public static final String insectsBack = """
            ┏━━━━━━━━━━━━━┓
            ┃     \\./     ┃
            ┃   (O·|·O)   ┃
            ┃    (o|o)    ┃
            ┗━━━━━━━━━━━━━┛""";
    public static final String fungiBack = """
            ┏━━━━━━━━━━━━━┓
            ┃    .-·-.    ┃
            ┃   (._._.)   ┃
            ┃     |_|     ┃
            ┗━━━━━━━━━━━━━┛""";

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
