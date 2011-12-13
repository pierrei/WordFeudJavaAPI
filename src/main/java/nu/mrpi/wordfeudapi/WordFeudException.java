package nu.mrpi.wordfeudapi;

/**
 * @author Pierre Ingmansson (pierre@ingmansson.se)
 */
public class WordFeudException extends RuntimeException {
    public WordFeudException() {
    }

    public WordFeudException(String message) {
        super(message);
    }

    public WordFeudException(String message, Throwable cause) {
        super(message, cause);
    }

    public WordFeudException(Throwable cause) {
        super(cause);
    }
}
