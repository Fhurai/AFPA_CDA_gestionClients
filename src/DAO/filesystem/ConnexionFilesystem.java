package DAO.filesystem;

import DAO.SocieteDatabaseException;
import logs.LogManager;

import java.util.logging.Level;

/**
 * Classe pour la connexion Filesystem.
 */
public class ConnexionFilesystem {

    /**
     * Tableau de fichiers.
     */
    private static FilesystemDatabase db = null;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            /**
             * Méthode à lancer lors de la fermeture de l'application.
             */
            public void run() {
                if (db != null) {
                    LogManager.logs.log(Level.INFO, "Database fermée");
                    db = null;
                }
            }
        });
    }

    /**
     * Constructor
     */
    public ConnexionFilesystem() {
    }

    /**
     * Méthode qui renvoie une instance de la connexion.
     *
     * @return ArrayList<File> Liste des fichiers table.
     * @throws SocieteDatabaseException Exception lors de la création des
     *                                  fichiers table.
     */
    public static FilesystemDatabase getInstance() throws SocieteDatabaseException {

        if (db == null) {
            // Si la base de donnée
            db = new FilesystemDatabase();
        }

        // Retourne la base de données des fichiers table.
        return db;
    }

    /**
     * Méthode de fermeture de la base de données.
     */
    public static void close() {
        db = null;
    }
}
