package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson
 */
public enum BoardType {
    Normal("normal"),
    Random("random");

    private final String apiStringRepresentation;

    BoardType(final String apiStringRepresentation) {
        this.apiStringRepresentation = apiStringRepresentation;
    }

    public String getApiStringRepresentation() {
        return apiStringRepresentation;
    }

    public static BoardType fromString(final String string) {
        for (final BoardType boardType : values()) {
            if (boardType.toString().equalsIgnoreCase(string)) {
                return boardType;
            }
        }
        throw new IllegalArgumentException("Board type matching \"" + string + "\" not found!");
    }
}
