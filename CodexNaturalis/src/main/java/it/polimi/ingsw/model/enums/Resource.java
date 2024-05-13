package it.polimi.ingsw.model.enums;

/**
 * Represents the resources that can be found on a card.
 * These resources include FUNGI, PLANT, ANIMAL, INSECT, INKWELL, QUILL, and MANUSCRIPT.
 * The NONE value is used when no specific resource is assigned.
 * The NONCOVERABLE value is used to represent a non-coverable corner.
 * @author Gloria Geronimi
 */
public enum Resource {
    FUNGI, PLANT, ANIMAL, INSECT, INKWELL, QUILL, MANUSCRIPT, NONE, NONCOVERABLE;

    public char toChar() {
        return switch (this) {
            case FUNGI -> 'F';
            case PLANT -> 'P';
            case ANIMAL -> 'A';
            case INSECT -> 'I';
            case INKWELL -> 'K';
            case QUILL -> 'Q';
            case MANUSCRIPT -> 'M';
            default -> ' ';
        };
    }

    public static Resource fromString(String string) {
        return Resource.valueOf(string.toUpperCase());
    }

    public static Resource fromChar(char c) {
        return switch (c) {
            case 'F' -> FUNGI;
            case 'P' -> PLANT;
            case 'A' -> ANIMAL;
            case 'I' -> INSECT;
            case 'K' -> INKWELL;
            case 'Q' -> QUILL;
            case 'M' -> MANUSCRIPT;
            default -> NONE;
        };
    }

    public String toColorBlock() {
        return switch (this) {
            case FUNGI -> "\u001b[1;31m█\u001b[0m";
            case PLANT -> "\u001b[1;32m█\u001b[0m";
            case ANIMAL -> "\u001b[1;36m█\u001b[0m";
            case INSECT -> "\u001b[1;35m█\u001b[0m";
            case INKWELL -> "\u001b[1;30m█\u001b[0m";
            case QUILL -> "\u001b[1;37m█\u001b[0m";
            case MANUSCRIPT -> "\u001b[1;33m█\u001b[0m";
            default -> " ";
        };
    }
}
