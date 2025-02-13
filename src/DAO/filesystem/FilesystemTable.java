package DAO.filesystem;

import DAO.SocieteDatabaseException;
import logs.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Classe Table Filesystem.
 */
public class FilesystemTable {

    /**
     * Nom de la table.
     */
    private String tableName;
    /**
     * Compteur d'incrémentation.
     */
    private int autoIncrement;
    /**
     * Liste des enregistrements de la table.
     */
    private ArrayList<String[]> records;

    /**
     * Constructeur nécessitant le nom de la table.
     *
     * @param tableName Nom de la table.
     * @throws SocieteDatabaseException Exception de chargement de la table.
     */
    public FilesystemTable(String tableName) throws SocieteDatabaseException {
        // Initialisation du nom de la table.
        this.setTableName(tableName);

        // Initialisation de la liste des enregistrements.
        this.setRecords(new ArrayList<>());

        // Chargement de la table.
        this.loadTable();
    }

    /**
     * Méthode de chargement de la table.
     *
     * @throws SocieteDatabaseException Exception lors du chargement ou de la
     *                                  création de la base de données
     */
    private void loadTable() throws SocieteDatabaseException {
        // Initialisation du fichier table.
        File f = new File("save/" + tableName + ".csv");

        if (f.exists()) {
            // Chargement du fichier dans l'objet table.

            try {
                // Initialisation du scanner de fichier.
                Scanner sc = new Scanner(f);

                while (sc.hasNextLine()) {
                    // Tant que le fichier a une ligne à lire.

                    // Récupération de la ligne depuis le fichier.
                    String line = sc.nextLine();

                    if (!line.contains(";")) {
                        // Ligne n'est pas une ligne d'enregistrement, c'est
                        // la valeur du compteur qui est valorisé.
                        this.setAutoIncrement(Integer.parseInt(line));
                    } else {
                        // Ligne est une ligne d'enregistrement, ajout de
                        // celle-ci dans la ligne des enregistrements.
                        this.addRecord(line.split(";"));
                    }
                }

            } catch (FileNotFoundException e) {
                // Exception attrapée, log de l'exception et lancement d'une
                // nouvelle exception plus lisible pour l'utilisateur.
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Erreur lors du chargement" +
                        " de la base de données.");
            }
        } else {
            // Création du fichier correspondant à l'objet table.

            try {
                //Tentative de création du fichier table.

                if (f.createNewFile()) {
                    // Le fichier a bien été créé.

                    // Log de la création.
                    LogManager.logs.log(Level.INFO, "Table créée pour la fichier " + tableName + ".csv");

                    // Écriture du compteur d'identifiant à 1.
                    FileWriter fw = new FileWriter(f);
                    fw.write("1\n");

                    // Fermeture du fichier.
                    fw.close();

                    // Initialisation des variables par défaut.
                    this.setAutoIncrement(1);
                    this.setRecords(new ArrayList<>());
                }
            } catch (IOException e) {
                // Exception attrapée, log de l'exception et lancement d'une
                // exception plus lisible pour l'utilisateur.
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Erreur lors de la" +
                        " création de la base de données.");
            }
        }
    }

    /**
     * Méthode de fermeture de la table.
     *
     * @throws SocieteDatabaseException Exception lors de la sauvegarde ou de
     *                                  la fermeture de la table.
     */
    public void closeTable() throws SocieteDatabaseException {
        // Initialisation du fichier table.
        String path = "save/" + tableName + ".csv";
        File f = new File(path);

        if (f.exists()) {
            // Fichier table existe.


            try {
                // Écrivain de la table est initialisé.
                FileWriter fw = new FileWriter(path);

                // Écriture de la valeur du compteur d'incrémentation.
                fw.write(this.getAutoIncrement() + "\n");

                for (String[] record : this.getRecords()) {
                    // Parcours des enregistrements qui sont écrits un à un
                    // dans le fichier.
                    fw.write(String.join(";", record) + "\n");
                }

                // Fermeture du fichier.
                fw.close();
            } catch (IOException e) {
                // Exception attrapée, log de l'exception et lancement d'une
                // exception plus lisible pour l'utilisateur.
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Erreur d'intégrité de la " +
                        "base de données.");
            }
        } else {
            // Fichier non existant.

            // Exception attrapée, log de l'exception et lancement d'une
            // exception plus lisible pour l'utilisateur.
            LogManager.logs.log(Level.SEVERE, "Fichier table manquant");
            throw new SocieteDatabaseException("Fichier de sauvegarde " +
                    "manquant pour les " + this.getTableName());
        }

    }

    /**
     * Getter Nom de la table.
     *
     * @return Nom de la table.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Setter Nom de la table.
     *
     * @param tableName Nouveau nom de la table.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Getter compteur d'incrémentation.
     *
     * @return Compteur d'incrémentation.
     */
    public int getAutoIncrement() {
        return autoIncrement;
    }

    /**
     * Setter compteur d'incrémentation.
     *
     * @param autoIncrement Nouvelle valeur compteur d'incrémentation.
     */
    public void setAutoIncrement(int autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * Incrémente la valeur du compteur d'incrémentation de 1.
     */
    public void increaseAutoIncrement() {
        this.setAutoIncrement(this.getAutoIncrement() + 1);
    }

    /**
     * Getter Enregistrements.
     *
     * @return Enregistrements.
     */
    public ArrayList<String[]> getRecords() {
        return records;
    }

    /**
     * Setter Enregistrements.
     *
     * @param records Nouveaux enregistrements.
     */
    public void setRecords(ArrayList<String[]> records) {
        this.records = records;
    }

    /**
     * Ajout un enregistrement à la liste d'enregistrement.
     *
     * @param record Enregistrement à ajouter.
     */
    public void addRecord(String[] record) {
        this.records.add(record);
    }

    /**
     * Récupère l'enregistrement ayant l'identifiant donné.
     *
     * @param id Identifiant de l'enregistrement à retrouver.
     * @return Enregistrement recherché.
     * @throws SocieteDatabaseException Aucun enregistrement trouvé avec
     *                                  l'identifiant donné.
     */
    public String[] get(int id) throws SocieteDatabaseException {
        // Création d'un optional d'enregistrement à partir de l'identifiant.
        Optional<String[]> option = this.getRecords().stream().filter(r -> Integer.parseInt(r[0])
                == id).findFirst();

        if (option.isPresent()) {
            // Si l'enregistrement existe dans l'optional, il est récupéré et
            // retourné.
            return option.get();
        } else {
            // L'enregistrement n'existe pas dans l'optional, lancement d'une
            // exception pour notifier l'utilisateur.
            throw new SocieteDatabaseException("Aucun enregistrement avec cet" +
                    " identifiant");
        }
    }

    /**
     * Récupère l'enregistrement ayant le nom donné.
     *
     * @param nom Nom de l'enregistrement à retrouver.
     * @return Enregistrement recherché.
     * @throws SocieteDatabaseException Aucun enregistrement trouvé avec le
     *                                  nom donné.
     */
    public String[] find(String nom) throws SocieteDatabaseException {
        // Création d'un optional d'enregistrement à partir du nom.
        Optional<String[]> option =
                this.getRecords().stream().filter(r -> nom.equalsIgnoreCase(r[1])).findFirst();

        if (option.isPresent()) {
            // Si l'enregistrement existe dans l'optional, il est récupéré et
            // retourné.
            return option.get();
        } else {
            // L'enregistrement n'existe pas dans l'optional, lancement d'une
            // exception pour notifier l'utilisateur.
            throw new SocieteDatabaseException("Aucun enregistrement avec ce" +
                    " nom");
        }
    }

    /**
     * Supprime d'un enregistrement ayant l'identifiant donné.
     *
     * @param id Identifiant de l'enregistrement à retrouver.
     * @return Vrai si un élément a été supprimé.
     * @throws SocieteDatabaseException Exception lors de la suppression.
     */
    public boolean delete(int id) throws SocieteDatabaseException {
        return this.getRecords().removeIf(r -> Integer.parseInt(r[0]) == id);
    }

    /**
     * Crée un enregistrement dans la table.
     *
     * @param record L'enregistrement à sauvegarder.
     * @return Vrai si l'enregistrement a été créé.
     */
    public boolean create(String[] record) {
        // Valorisation de l'identifiant de l'enregistrement avec la valeur
        // du compteur d'incrémentation.
        record[0] = String.valueOf(this.getAutoIncrement());

        // Incrémentation du compteur d'incrémentation.
        this.increaseAutoIncrement();

        // Retourne l'indication si l'enregistrement a bien été ajouté aux
        // enregistrements de la table.
        return this.getRecords().add(record);
    }

    /**
     * Met à jour un enregistrement dans la table.
     *
     * @param record L'enregistrement à mettre à jour.
     * @return Indication si la table a été mise à jour.
     */
    public boolean update(String[] record) {
        boolean selected = false;

        for (String[] r : this.getRecords()) {
            if (Objects.equals(r[0], record[0])) {
                selected = true;
                System.arraycopy(record, 0, r, 0, r.length);
            }
        }
        return selected;
    }
}
