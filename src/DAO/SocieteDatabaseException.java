package DAO;

/**
 * Exception sur la base de données du project.
 */
public final class SocieteDatabaseException extends Exception {

    /**
     * Constructor.
     *
     * @param message Message d'erreur concernant la base de données.
     */
    public SocieteDatabaseException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message Message d'erreur concernant la base de données.
     * @param cause La cause de l'exception.
     */
    public SocieteDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
