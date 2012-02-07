package nu.mrpi.wordfeudapi.exception;

/**
 * @author Pierre Ingmansson
 */
public class WordFeudException extends RuntimeException {
    public WordFeudException() {
    }

    public WordFeudException(final String message) {
        super(message);
    }

    public WordFeudException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WordFeudException(final Throwable cause) {
        super(cause);
    }
}
