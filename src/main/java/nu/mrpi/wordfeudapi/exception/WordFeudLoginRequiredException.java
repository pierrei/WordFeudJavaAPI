package nu.mrpi.wordfeudapi.exception;

/**
 * @author Pierre Ingmansson
 */
public class WordFeudLoginRequiredException extends WordFeudException {
    public WordFeudLoginRequiredException() {
    }

    public WordFeudLoginRequiredException(String message) {
        super(message);
    }

    public WordFeudLoginRequiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public WordFeudLoginRequiredException(Throwable cause) {
        super(cause);
    }
}
