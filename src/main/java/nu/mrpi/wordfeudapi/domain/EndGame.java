package nu.mrpi.wordfeudapi.domain;

/**
 * @author Pierre Ingmansson
 */
public enum EndGame {
    NotOver(0),
    NormalEnding(1),
    Resign(2),
    ResignByTimout(3),
    OverUnknown(Byte.MAX_VALUE);

    private final byte apiByteRepresentation;

    EndGame(final int apiByteRepresentation) {
        this.apiByteRepresentation = (byte) apiByteRepresentation;
    }

    public int getApiByteRepresentation() {
        return apiByteRepresentation;
    }

    public static EndGame fromByte(final byte number) {
        for (final EndGame endGame : values()) {
            if (endGame.apiByteRepresentation == number) {
                return endGame;
            }
        }
        throw new IllegalArgumentException("EndGame for value " + number + " not found!");
    }
}
