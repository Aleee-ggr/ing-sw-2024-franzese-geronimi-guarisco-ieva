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
            case FUNGI -> 'ଚ';
            case PLANT -> '♣';
            case ANIMAL -> '♞';
            case INSECT -> 'Ӝ';
            case INKWELL -> '⚱';
            case QUILL -> '✎';
            case MANUSCRIPT -> '✉';
            case NONE -> '⎕';
            case NONCOVERABLE -> '╳';
            default -> ' ';
        };
    }

    public char toCharCenter(){
        return switch (this){
            case FUNGI -> 'Ѓ';
            case PLANT -> 'Ῥ';
            case ANIMAL -> 'Ἁ';
            case INSECT -> 'Ῐ';
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
            case 'N' -> NONCOVERABLE;
            default -> NONE;
        };
    }

    public String toColorBlock() {
        return toColorChar('█');
    }


    public String toColorChar(Character character){
        return switch (this) {
            case FUNGI -> "\u001b[1;31m%c\u001b[0m".formatted(character);
            case PLANT -> "\u001b[1;32m%c\u001b[0m".formatted(character);
            case ANIMAL -> "\u001b[1;36m%c\u001b[0m".formatted(character);
            case INSECT -> "\u001b[1;35m%c\u001b[0m".formatted(character);
            case INKWELL -> "\u001b[1;30m%c\u001b[0m".formatted(character);
            case QUILL -> "\u001b[1;37m%c\u001b[0m".formatted(character);
            case MANUSCRIPT -> "\u001b[1;33m%c\u001b[0m".formatted(character);
            case NONCOVERABLE -> "╳";
            default -> " ";
        };
    }

    public String toColorCenter(){
        return switch (this) {
            case FUNGI, PLANT, ANIMAL, INSECT ->  toColorChar('◆');
            default -> " ";
        };
    }
}
