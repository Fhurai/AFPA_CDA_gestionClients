package entities;

/**
 * Exception sur les entités du project.
 */
public final class SocieteEntityException extends Exception {

    public SocieteEntityException(String message) {
        super(message);
    }

    public SocieteEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
