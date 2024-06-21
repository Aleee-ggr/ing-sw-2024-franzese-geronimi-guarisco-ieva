package it.polimi.ingsw.model.enums;

/**
 * Represents the resources that can be found on a card.
 * These resources include FUNGI, PLANT, ANIMAL, INSECT, INKWELL, QUILL, and MANUSCRIPT.
 * The NONE value is used when no specific resource is assigned.
 * The NONCOVERABLE value is used to represent a non-coverable corner.
 *
 */
public enum Resource {
    FUNGI, PLANT, ANIMAL, INSECT, INKWELL, QUILL, MANUSCRIPT, NONE, NONCOVERABLE;

    /**
     * Converts a string to a corresponding Resource enum value.
     * @param string The string representation of the resource (case insensitive).
     * @return The corresponding Resource enum value.
     */
    public static Resource fromString(String string) {
        return Resource.valueOf(string.toUpperCase());
    }

    /**
     * Converts the resource to a character representation.
     * @return The character representation of the resource.
     */
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
        };
    }

    /**
     * Converts the resource to a centered character representation.
     * @return The centered character representation of the resource.
     */
    public char toCharCenter() {
        return switch (this) {
            case FUNGI -> 'Ѓ';
            case PLANT -> 'Ῥ';
            case ANIMAL -> 'Ἁ';
            case INSECT -> 'Ῐ';
            default -> ' ';
        };
    }

    /**
     * Converts the resource to a colored block representation.
     * @return The colored block representation of the resource.
     */
    public String toColorBlock() {
        return toColorChar('█');
    }

    /**
     * Converts the resource to a colored character representation.
     * @param character The character to be colored.
     * @return The colored character representation of the resource.
     */
    public String toColorChar(Character character) {
        return switch (this) {
            case FUNGI -> "\u001b[1;31m%c\u001b[0m".formatted(character);
            case PLANT -> "\u001b[1;32m%c\u001b[0m".formatted(character);
            case ANIMAL -> "\u001b[1;34m%c\u001b[0m".formatted(character);
            case INSECT -> "\u001b[1;35m%c\u001b[0m".formatted(character);
            case INKWELL -> "\u001b[1;30m%c\u001b[0m".formatted(character);
            case QUILL -> "\u001b[1;37m%c\u001b[0m".formatted(character);
            case MANUSCRIPT -> "\u001b[1;33m%c\u001b[0m".formatted(character);
            case NONCOVERABLE -> "╳";
            default -> " ";
        };
    }

    /**
     * Converts the resource to a centered colored character representation.
     * @return The centered colored character representation of the resource.
     */
    public String toColorCenter() {
        return switch (this) {
            case FUNGI, PLANT, ANIMAL, INSECT -> toColorChar('◆');
            default -> " ";
        };
    }
}
