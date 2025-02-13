package DAO.filesystem;

import DAO.SocieteDatabaseException;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Classe de la base de données de la connexion Filesystem.
 */
public class FilesystemDatabase {

    /**
     * Tableau des noms de table.
     */
    final private static String[] tables = new String[]{"clients", "prospects", "contrats"};
    /**
     * Liste des fichiers table.
     */
    private ArrayList<FilesystemTable> db;

    /**
     * Constructor.
     *
     * @throws SocieteDatabaseException Exception à la création des fichiers
     *                                  table.
     */
    public FilesystemDatabase() throws SocieteDatabaseException {
        // Liste des fichiers table initialisés.
        ArrayList<FilesystemTable> fst = new ArrayList<>();

        for (String table : tables) {
            // Pour chaque nom de table.

            // Ajout de la table à la base de données.
            fst.add(new FilesystemTable(table));
        }

        // Valorisation de la liste des tables de la base de données.
        this.setDb(fst);
    }

    /**
     * Setter Liste des fichiers table.
     *
     * @param db Nouvelle liste des fichiers table.
     */
    public void setDb(ArrayList<FilesystemTable> db) {
        this.db = db;
    }

    /**
     * Retourne la table correspondante au nom donné.
     *
     * @param tableName Nom de la table recherchée.
     * @return La table recherchée.
     * @throws SocieteDatabaseException Exception si aucune table correspond
     *                                  au nom donné.
     */
    public FilesystemTable getFilesystemTable(String tableName) throws SocieteDatabaseException {
        Optional<FilesystemTable> fst =
                this.db.stream().filter(t -> t.getTableName().equals(tableName)).findFirst();

        if (fst.isPresent()) {
            // Table trouvée, elle est retournée.
            return fst.get();
        }

        // Aucune table trouvée, lancement d'une exception explicite à
        // l'utilisateur.
        throw new SocieteDatabaseException("Aucune table n'existe avec ce " +
                "nom");
    }
}
