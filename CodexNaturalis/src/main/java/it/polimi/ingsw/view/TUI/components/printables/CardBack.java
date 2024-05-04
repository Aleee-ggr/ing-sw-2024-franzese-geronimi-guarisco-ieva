package it.polimi.ingsw.view.TUI.components.printables;

import java.util.Arrays;
import java.util.Iterator;

public abstract class CardBack implements Iterable<String> {
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
}
